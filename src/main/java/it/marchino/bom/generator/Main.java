package it.marchino.bom.generator;

import java.util.List;

import it.marchino.bom.generator.exception.ExecutionException;
import it.marchino.bom.generator.executor.BomResolverExecutorLauncher;
import it.marchino.bom.generator.model.Configuration;
import it.marchino.bom.generator.model.Dependency;
import it.marchino.bom.generator.writer.BomWriter;
import it.marchino.bom.generator.writer.ThirdPartyNoteWriter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {

	public static void main(String[] args) {
		try {
			new Main().execute();
		} catch(ExecutionException e) {
			log.error("Exception in BOM generation: " + e.getMessage(), e);
		}
	}
	
	private Configuration configuration;
	
	public void execute() {
		configuration = Configuration.init();
		
		BomResolverExecutorLauncher launcher = new BomResolverExecutorLauncher(configuration);
		List<Dependency> dependencies = launcher.launchBomGeneration();
		log.info(dependencies);
		
		BomWriter bomWriter = new BomWriter(configuration);
		bomWriter.writeDependencies(dependencies);
		
		ThirdPartyNoteWriter tpnWriter = new ThirdPartyNoteWriter(configuration);
		tpnWriter.writeDependencies(dependencies);
	}
	
}
