package it.marchino.bom.generator.executor;

import java.util.List;

import it.marchino.bom.generator.model.Configuration;
import it.marchino.bom.generator.model.JavaDependency;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class JavaIvyBomResolverExecutor implements BomResolverExecutor<JavaDependency> {

	@SuppressWarnings("unused")
	private final Configuration configuration;
	@Override
	public List<JavaDependency> resolveBom() {
		log.info("TODO");
		// TODO Auto-generated method stub
		return List.of();
	}

}
