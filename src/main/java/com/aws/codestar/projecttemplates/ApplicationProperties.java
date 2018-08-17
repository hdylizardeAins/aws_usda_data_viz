package com.aws.codestar.projecttemplates;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class ApplicationProperties {

	private String rScriptLocation;

	private String rScriptOutputDir;

	private String outputDir;
	
	private String socialDir;
	
	private String inputDir;
	
	private String chatFileName;
	
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

	public String getChatFileName() {
		return chatFileName;
	}

	public void setChatFileName(String chatFilePath) {
		this.chatFileName = chatFilePath;
	}

	public String getSocialDir() {
		return socialDir;
	}

	public void setSocialDir(String socialDir) {
		this.socialDir = socialDir;
	}

	public String getInputDir() {
		return inputDir;
	}

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}
}
