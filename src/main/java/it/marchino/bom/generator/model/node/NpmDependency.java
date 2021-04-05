package it.marchino.bom.generator.model.node;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class NpmDependency {

	private String name;
	private String version;
	// private String license;
	private Object license;
}
