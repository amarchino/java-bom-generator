package it.marchino.bom.generator.executor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationOutputHandler;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.apache.maven.shared.invoker.PrintStreamHandler;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.marchino.bom.generator.exception.ExecutionException;
import it.marchino.bom.generator.model.Configuration;
import it.marchino.bom.generator.model.JavaMavenDependency;
import it.marchino.bom.generator.util.HttpHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class JavaMavenBomResolverExecutor extends BomResolverBaseExecutor<JavaMavenDependency> {

	private static final Pattern OUTPUT_PATTERN = Pattern.compile("^\\[INFO\\] +(?<text>.*) *$");
	private static final Pattern DEPENDENCY_PATTERN = Pattern.compile("^(?<groupId>.*?):(?<artifactId>.*?):(?<packaging>.*?):(?<version>.*?):(?<scope>.*?) .*$");
	private final Configuration configuration;
	
	private File pomFile;
	private String mavenOutput;

	@Override
	public List<JavaMavenDependency> resolveBom() {
		pomFile = new File(configuration.getProjectPath(), "pom.xml");
		if(!pomFile.exists()) {
			log.info("pom.xml file not found. Not a Maven project: skip");
			return List.of();
		}
		initExecutorService();
		// Invoke maven
		mavenOutput = invokeMaven();
		// Parse output
		parseOutput();
		// Complete dependencies
		completeDependencies();
		stopExecutorService();
		return dependencies;
	}

	private String invokeMaven() {
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile(pomFile);
		request.setGoals(Arrays.asList("compile", "dependency:list"));
		request.setBatchMode(true);
		
		Invoker invoker = new DefaultInvoker();
		// Use own mvnw file
		invoker.setMavenHome(new File(""));
		invoker.setMavenExecutable(new File("mvnw").getAbsoluteFile());

		InvocationResult result;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8.toString())) {
			InvocationOutputHandler ioh = new PrintStreamHandler(ps, true);
			invoker.setOutputHandler(ioh);
			log.debug("Invoking Maven to pom.xml...");
			result = invoker.execute(request);
			if(result.getExitCode() != 0) {
				throw new ExecutionException("Maven execution exited with error: " + result.getExitCode(), result.getExecutionException());
			}
			log.debug("Maven invoked successfully");
			return baos.toString(StandardCharsets.UTF_8);
	    } catch (MavenInvocationException | IOException e) {
			throw new ExecutionException("Exception in Maven execution: " + e.getMessage(), e);
		}
	}

	private void parseOutput() {
		List<String> lines = mavenOutput.lines()
				.map(String::trim)
				.collect(Collectors.toList());
		boolean inDependency = false;
		for(String line : lines) {
			if(inDependency) {
				if("[INFO]".equalsIgnoreCase(line)) {
					inDependency = false;
				} else {
					Matcher matcher = OUTPUT_PATTERN.matcher(line);
					matcher.find();
					JavaMavenDependency dependency = toDependency(matcher.group("text"));
					if(dependency != null) {
						dependencies.add(dependency);
					}
				}
			} else if("[INFO] The following files have been resolved:".equalsIgnoreCase(line)) {
				inDependency = true;
			}
		}
	}
	
	private JavaMavenDependency toDependency(String str) {
		Matcher matcher = DEPENDENCY_PATTERN.matcher(str);
		if(!matcher.matches()) {
			return null;
		}
		log.debug("handling line " + str);
		matcher.group(0);
		return JavaMavenDependency.builder()
				.groupId(matcher.group("groupId"))
				.artifactId(matcher.group("artifactId"))
				.version(matcher.group("version"))
				.scope(matcher.group("scope"))
				.build();
	}
	
	private void completeDependencies() {
		dependencies.stream()
			.forEach(dep -> {
				executorService.execute(() -> {
					log.debug("Searching license for dependency " + dep + " in URI " + dep.getURI());
					Document document = HttpHelper.httpInvocationDocument(dep.getURI());
					Elements gridRows = document.select("#maincontent > .grid tr");
					for(Element row : gridRows) {
						Element title = row.child(0);
						if("LICENSE".equalsIgnoreCase(title.text())) {
							Element content = row.child(1).child(0);
							dep.getLicenseList().add(content.text());
						}
					}
				});
			});
	}
}
