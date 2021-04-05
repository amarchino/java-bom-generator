package it.marchino.bom.generator.model;

import java.net.URI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class JavaMavenDependency extends JavaDependency {

	private String groupId;
	private String artifactId;
	private String scope;

	@Override
	public String getName() {
		return getArtifactId();
	}
	
	public URI getURI() {
		return URI.create(String.format("https://mvnrepository.com/artifact/%s/%s/%s", this.getGroupId(), this.getArtifactId(), this.getVersion()));
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s:%s", groupId, artifactId, getVersion());
	}
}
