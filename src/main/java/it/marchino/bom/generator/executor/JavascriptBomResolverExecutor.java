package it.marchino.bom.generator.executor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.stream.JsonReader;

import it.marchino.bom.generator.exception.ExecutionException;
import it.marchino.bom.generator.model.Configuration;
import it.marchino.bom.generator.model.JavascriptDependency;
import it.marchino.bom.generator.model.node.NpmDependency;
import it.marchino.bom.generator.model.node.PackageLock;
import it.marchino.bom.generator.model.node.PackageLockDependency;
import it.marchino.bom.generator.util.HttpHelper;
import it.marchino.bom.generator.util.JsonHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class JavascriptBomResolverExecutor extends BomResolverBaseExecutor<JavascriptDependency> {

	private final Configuration configuration;
	private File packageLockFile;
	private PackageLock packageLock;
	
	@Override
	public List<JavascriptDependency> resolveBom() {
		File packageFile = new File(configuration.getProjectPath(), "package.json");
		if(!packageFile.exists()) {
			log.info("package.json file not found. Not a Node project: skip");
			return List.of();
		}
		packageLockFile = new File(configuration.getProjectPath(), "package-lock.json");
		if(!packageFile.exists()) {
			log.info("package.json found, but without package.json. Please compile the lock via \"npm install\" first");
			return List.of();
		}
		initExecutorService();
		
		// Read package-lock
		readPackageLock();
		// Complete dependencies
		completeDependencies();
		stopExecutorService();
		return dependencies;
	}
	private void readPackageLock() {
		try(FileReader fr = new FileReader(packageLockFile);
				JsonReader reader = new JsonReader(fr)) {
			packageLock = JsonHelper.GSON.fromJson(reader, PackageLock.class);
		} catch (IOException e) {
			throw new ExecutionException("Exception in package-lock parsing: " + e.getMessage(), e);
		}
		if(packageLock.getPackages() != null) {
			
		} else if (packageLock.getDependencies() != null) {
			parseDependencies();
		}
	}
	private void parseDependencies() {
		for(Entry<String, PackageLockDependency> entry : packageLock.getDependencies().entrySet()) {
			if(!Boolean.TRUE.equals(entry.getValue().getDev())) {
				dependencies.add(JavascriptDependency.builder()
						.name(entry.getKey())
						.version(entry.getValue().getVersion())
						.build());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void completeDependencies() {
		dependencies.stream()
		.forEach(dep -> {
			executorService.execute(() -> {
				log.debug("Searching license for dependency " + dep + " in URI " + dep.getURI());
				NpmDependency npmDep = HttpHelper.httpInvocationJson(dep.getURI(), NpmDependency.class);
				if(npmDep.getLicense() instanceof String) {
					dep.getLicenseList().add((String)npmDep.getLicense());
				} else {
					dep.getLicenseList().add(((Map<String, String>)npmDep.getLicense()).get("type"));
				}
			});
		});
	}

}
