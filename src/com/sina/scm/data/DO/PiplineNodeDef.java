package com.sina.scm.data.DO;

import java.io.Serializable;

public class PiplineNodeDef implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer pipeline_def_id;
	private Integer node_def_id;
	private String node_pass;
	private Integer next_id_on_success;
	private Integer next_id_on_fail;
	private String mail_list;
	private Integer auto_test_pass_rate;
	private String test_type;
	private String node_test_rule;
	private String autotest_url;
	private String notification_mail;	
	private String start_next_node;	
	
	public String getStart_next_node() {
		return start_next_node;
	}
	public void setStart_next_node(String start_next_node) {
		this.start_next_node = start_next_node;
	}
	public String getNotification_mail() {
		return notification_mail;
	}
	public void setNotification_mail(String notification_mail) {
		this.notification_mail = notification_mail;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPipeline_def_id() {
		return pipeline_def_id;
	}
	public void setPipeline_def_id(Integer pipeline_def_id) {
		this.pipeline_def_id = pipeline_def_id;
	}
	public Integer getNode_def_id() {
		return node_def_id;
	}
	public void setNode_def_id(Integer node_def_id) {
		this.node_def_id = node_def_id;
	}
	public String getNode_pass() {
		return node_pass;
	}
	public void setNode_pass(String node_pass) {
		this.node_pass = node_pass;
	}
	public Integer getNext_id_on_success() {
		return next_id_on_success;
	}
	public void setNext_id_on_success(Integer next_id_on_success) {
		this.next_id_on_success = next_id_on_success;
	}
	public Integer getNext_id_on_fail() {
		return next_id_on_fail;
	}
	public void setNext_id_on_fail(Integer next_id_on_fail) {
		this.next_id_on_fail = next_id_on_fail;
	}
	public String getMail_list() {
		return mail_list;
	}
	public void setMail_list(String mail_list) {
		this.mail_list = mail_list;
	}
	public Integer getAuto_test_pass_rate() {
		return auto_test_pass_rate;
	}
	public void setAuto_test_pass_rate(Integer auto_test_pass_rate) {
		this.auto_test_pass_rate = auto_test_pass_rate;
	}
	public String getTest_type() {
		return test_type;
	}
	public void setTest_type(String test_type) {
		this.test_type = test_type;
	}
	public String getNode_test_rule() {
		return node_test_rule;
	}
	public void setNode_test_rule(String node_test_rule) {
		this.node_test_rule = node_test_rule;
	}
	public String getAutotest_url() {
		return autotest_url;
	}
	public void setAutotest_url(String autotest_url) {
		this.autotest_url = autotest_url;
	}

}
