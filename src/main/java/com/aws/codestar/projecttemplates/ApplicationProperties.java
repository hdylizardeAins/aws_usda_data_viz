package com.aws.codestar.projecttemplates;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class ApplicationProperties {

	private String rScriptLocation;

	private String rScriptOutputDir;

	private String outputDir;
	
	private String chatFilePath;

	public String getRScriptLocation() {
		return rScriptLocation;
	}

	public void setRScriptLocation(String rScriptLocation) {
		this.rScriptLocation = rScriptLocation;
	}

	public String getRScriptOutputDir() {
		return rScriptOutputDir;
	}

	public void setRScriptOutputDir(String rScriptOutputDir) {
		this.rScriptOutputDir = rScriptOutputDir;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public String getChatFilePath() {
		return chatFilePath;
	}

	public void setChatFilePath(String chatFilePath) {
		this.chatFilePath = chatFilePath;
	}
}
