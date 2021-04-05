package it.marchino.bom.generator.writer;

import java.util.List;

import it.marchino.bom.generator.model.Configuration;
import it.marchino.bom.generator.model.Dependency;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ThirdPartyNoteWriter extends BaseWriter {

	@SuppressWarnings("unused")
	private final Configuration configuration;
	@Override
	public void writeDependencies(List<Dependency> dependencies) {
		// TODO Auto-generated method stub
		
	}

}
