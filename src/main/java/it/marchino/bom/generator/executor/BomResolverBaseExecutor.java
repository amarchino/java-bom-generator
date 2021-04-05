package it.marchino.bom.generator.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import it.marchino.bom.generator.exception.ExecutionException;
import it.marchino.bom.generator.model.Dependency;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BomResolverBaseExecutor<D extends Dependency> implements BomResolverExecutor<D> {

	protected List<D> dependencies = new ArrayList<>();
	protected ExecutorService executorService;
	
	protected void initExecutorService() {
		executorService = Executors.newFixedThreadPool(Math.max(1, Runtime.getRuntime().availableProcessors()));
	}
	
	protected void stopExecutorService() {
		executorService.shutdown();
		try {
			executorService.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			throw new ExecutionException("Interrupted while reading licenses", e);
		}
	}

}
