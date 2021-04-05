package it.marchino.bom.generator.model;

import java.io.IOException;
import java.util.Properties;

import it.marchino.bom.generator.exception.ExecutionException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Configuration {

	private final Properties properties;
	
	public static Configuration init() {
		Properties properties = new Properties();
		try {
			properties.load(Configuration.class.getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			throw new ExecutionException("Error in properties load", e);
		}
		return new Configuration(properties);
	}
	
	public String getProjectPath() {
		return properties.getProperty("projectPath", "/");
	}
	public String getProjectName() {
		return properties.getProperty("projectName", "prj");
	}
	
}
