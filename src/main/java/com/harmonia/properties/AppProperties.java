package com.harmonia.properties;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

public interface AppProperties {
	
	PropertiesLoader PROP_LOADER = new PropertiesLoader("application.properties");
	
	String RSCRIPT_LOCATION = Paths.get(PROP_LOADER.getProperty("rscriptLocation")).toAbsolutePath().toString();
	
	String RSCRIPT_OUTPUT_DIR = Paths.get(PROP_LOADER.getProperty("rscriptOutputDir")).toAbsolutePath().toString();

}
