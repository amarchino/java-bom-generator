package it.marchino.bom.generator.model.node;

import java.util.Map;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class PackageLock {

	private String name;
	private String version;
	private Integer lockfileVersion;
	private Map<String, PackageLockDependency> packages;
	private Map<String, PackageLockDependency> dependencies;
}
