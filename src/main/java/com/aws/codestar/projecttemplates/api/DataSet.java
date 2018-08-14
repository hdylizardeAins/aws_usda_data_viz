package com.aws.codestar.projecttemplates.api;

import java.io.Serializable;

/**
 * Model that represents a file-based dataset
 *
 * @author ccruz-rodriguez
 *
 */
public class DataSet implements Serializable {

	/**
	 * Serialization Identifier
	 */
	private static final long serialVersionUID = 5703574685209628904L;

	private String fileName;

	private String displayName;

	/**
	 * Retrieves the name of the file
	 *
	 * @return the the name of the file
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the name of the file
	 *
	 * @param fileName the name to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Retrieves the display name of the dataset
	 * 
	 * @return the display name of the dataset
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name of the dataset
	 *
	 * @param displayName the display name to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
