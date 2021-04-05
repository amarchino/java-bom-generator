package it.marchino.bom.generator.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import it.marchino.bom.generator.exception.ExecutionException;
import it.marchino.bom.generator.model.Configuration;
import it.marchino.bom.generator.model.Dependency;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BomWriter extends BaseWriter {
	
	private static final String[] HEADERS = { "name*", "version*", "license*", "vendor/community", "end-of-support", "dependencies", "language" };

	private final Configuration configuration;
	@Override
	public void writeDependencies(List<Dependency> dependencies) {
		initOutputFolder("csv");
		try (FileWriter out = new FileWriter(new File(outputFolder, configuration.getProjectName() + ".csv"));
				CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {
			dependencies.forEach(dep -> print(dep, printer));
		} catch (IOException e) {
			throw new ExecutionException("Exception in BOM writing: " + e.getMessage(), e);
		}
	}
	
	private void print(Dependency dependency, CSVPrinter printer) {
		try {
			printer.printRecord(
				dependency.getName(),
				dependency.getVersion(),
				dependency.getLicenses(),
				dependency.getVendor(),
				dependency.getEndOfSupport(),
				dependency.getDependencies(),
				dependency.getLanguage()
			);
		} catch (IOException e) {
			throw new ExecutionException("Exception in BOM writing: " + e.getMessage(), e);
		}
	}

}
