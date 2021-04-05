package it.marchino.bom.generator.model;

public interface Dependency {
	String getName();
	String getVersion();
	String getLicenses();
	String getVendor();
	String getEndOfSupport();
	String getDependencies();
	String getLanguage();
}
