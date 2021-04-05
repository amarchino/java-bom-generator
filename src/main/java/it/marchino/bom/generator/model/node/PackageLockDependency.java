package it.marchino.bom.generator.model.node;

import java.util.Map;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class PackageLockDependency {

	private String version;
	private String resolved;
	private String integrity;
	private Map<String, Object> dependencies;
	private Map<String, String> requires;
	
	private Boolean dev;
}
