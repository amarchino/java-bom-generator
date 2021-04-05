package it.marchino.bom.generator.executor;

import java.util.List;

import it.marchino.bom.generator.model.Dependency;

public interface BomResolverExecutor<D extends Dependency> {
	
	List<D> resolveBom();

}
