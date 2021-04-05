package it.marchino.bom.generator.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class JavascriptDependency implements Dependency {

	private String name;
	private String version;
	@Builder.Default private List<String> licenseList = new ArrayList<>();
	@Builder.Default private List<String> dependendencyList = new ArrayList<>();

	@Override
	public String getLicenses() {
		return licenseList.stream()
				.collect(Collectors.joining(","));
	}
	@Override
	public String getVendor() {
		// TODO?
		return "";
	}
	@Override
	public String getEndOfSupport() {
		// TODO?
		return "";
	}
	@Override
	public String getDependencies() {
		return dependendencyList.stream()
				.collect(Collectors.joining(","));
	}
	@Override
	public String getLanguage() {
		return Language.JAVASCRIPT.getDescription();
	}
	
	public URI getURI() {
		return URI.create(String.format("https://registry.npmjs.org/%s/%s", this.name, this.version));
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s", this.name, this.version);
	}
}
