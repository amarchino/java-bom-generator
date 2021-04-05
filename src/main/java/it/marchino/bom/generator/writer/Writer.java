package it.marchino.bom.generator.writer;

import java.util.List;

import it.marchino.bom.generator.model.Dependency;

public interface Writer {

	void writeDependencies(List<Dependency> dependencies);
}
