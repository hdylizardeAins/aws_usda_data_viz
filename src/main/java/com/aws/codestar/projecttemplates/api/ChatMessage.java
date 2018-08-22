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
	
	private String graphData;
	
	private String caption;

	private String comment;
	
	private String dateTime;
	
	private String topic;
	
	private String id;
	
	//Used to indicate when the message has been persisted
	private boolean written;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @return the written
	 */
	public boolean getWritten() {
		return written;
	}

	/**
	 * @param written the written to set
	 */
	public void setWritten(boolean written) {
		this.written = written;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the graphData
	 */
	public String getGraphData() {
		return graphData;
	}

	/**
	 * @param graphData the graphData to set
	 */
	public void setGraphData(String graphData) {
		this.graphData = graphData;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

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
