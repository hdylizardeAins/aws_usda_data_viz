package com.aws.codestar.projecttemplates.api;

import java.io.Serializable;

/**
 * Model that represents a message in the chat
 *
 * @author jfoley
 *
 */
public class ChatMessage implements Serializable {

	/**
	 * Serialization Identifier
	 */
	private static final long serialVersionUID = 5740689814640982673L;

	private String username;
	
	private String imageName;

	private String comment;
	
	private String dateTime;

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the dateTime
	 */
	public String getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	

}
