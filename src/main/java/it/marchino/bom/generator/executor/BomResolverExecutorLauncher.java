package it.marchino.bom.generator.executor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.marchino.bom.generator.exception.ExecutionException;
import it.marchino.bom.generator.model.Configuration;
import it.marchino.bom.generator.model.Dependency;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BomResolverExecutorLauncher {
	
	private final Configuration configuration;
	
	public List<Dependency> launchBomGeneration() {
		File projectBaseFolder = new File(configuration.getProjectPath());
		if(!projectBaseFolder.exists()) {
			throw new ExecutionException("Project folder does not exist");
		}
		List<Dependency> result = new ArrayList<>();
		for(BomResolverExecutor<? extends Dependency> executor : getExecutors()) {
			List<? extends Dependency> dependencies = executor.resolveBom();
			result.addAll(dependencies);
		}
		
		return result;
	}
	
	private List<BomResolverExecutor<? extends Dependency>> getExecutors() {
		return List.of(
			new JavaIvyBomResolverExecutor(configuration),
			new JavaMavenBomResolverExecutor(configuration),
			new JavascriptBomResolverExecutor(configuration)
		);
	}
}
