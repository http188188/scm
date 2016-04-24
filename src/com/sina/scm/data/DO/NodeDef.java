package com.sina.scm.data.DO;

public class NodeDef {
	
	private Integer id;
	private String name;
	private String type;
	private String job_url;
	private String parameter;
	private String job_token;
	private String notification_mail_list;
	private String action_mail_list;
	private String start_next_node;		
	private String test_type;	
	
	public String getTest_type() {
		return test_type;
	}
	public void setTest_type(String test_type) {
		this.test_type = test_type;
	}
	public String getStart_next_node() {
		return start_next_node;
	}
	public void setStart_next_node(String start_next_node) {
		this.start_next_node = start_next_node;
	}
	public String getNotification_mail_list() {
		return notification_mail_list;
	}
	public void setNotification_mail_list(String notification_mail_list) {
		this.notification_mail_list = notification_mail_list;
	}
	public String getAction_mail_list() {
		return action_mail_list;
	}
	public void setAction_mail_list(String action_mail_list) {
		this.action_mail_list = action_mail_list;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getJob_token() {
		return job_token;
	}
	public void setJob_token(String job_token) {
		this.job_token = job_token;
	}
	
}
