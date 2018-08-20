package com.aws.codestar.projecttemplates.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataGovResult {

	private String title;
	private DataGovResource[] resources;
	@JsonProperty("private")
	private boolean isPrivate;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the resources
	 */
	public DataGovResource[] getResources() {
		return resources;
	}
	/**
	 * @param resources the resources to set
	 */
	public void setResources(DataGovResource[] resources) {
		this.resources = resources;
	}
	/**
	 * @return the isPrivate
	 */
	public boolean isPrivate() {
		return isPrivate;
	}
	/**
	 * @param isPrivate the isPrivate to set
	 */
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	
}
