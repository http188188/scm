package com.sina.scm.data.DO;

public class NodeDefVO {
	
	private String name;
	private String type;
	private String job_url;
	private String notification_mail;
	private String start_next_node;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getJob_url() {
		return job_url;
	}
	public void setJob_url(String job_url) {
		this.job_url = job_url;
	}
	public String getNotification_mail() {
		return notification_mail;
	}
	public void setNotification_mail_list(String notification_mail) {
		this.notification_mail = notification_mail;
	}
	public String getStart_next_node() {
		return start_next_node;
	}
	public void setStart_next_node(String start_next_node) {
		this.start_next_node = start_next_node;
	}
	
}
