package com.sina.scm.data.vo;

import java.io.Serializable;

public class RollBack implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer module_id;
	private String from_version;
	private String to_version;	
	private String qb_job_url;	
	private String operator;
	private String jenkins_job_url;
	private String rollback_date;
	private String rollback_reason;
	private String rca;	
	
	public String getJenkins_job_url() {
		return jenkins_job_url;
	}
	public void setJenkins_job_url(String jenkins_job_url) {
		this.jenkins_job_url = jenkins_job_url;
	}
	public String getRollback_reason() {
		return rollback_reason;
	}
	public void setRollback_reason(String rollback_reason) {
		this.rollback_reason = rollback_reason;
	}
	public String getRca() {
		return rca;
	}
	public void setRca(String rca) {
		this.rca = rca;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getModule_id() {
		return module_id;
	}
	public void setModule_id(Integer module_id) {
		this.module_id = module_id;
	}
	public String getFrom_version() {
		return from_version;
	}
	public void setFrom_version(String from_version) {
		this.from_version = from_version;
	}
	public String getTo_version() {
		return to_version;
	}
	public void setTo_version(String to_version) {
		this.to_version = to_version;
	}
	public String getQb_job_url() {
		return qb_job_url;
	}
	public void setQb_job_url(String qb_job_url) {
		this.qb_job_url = qb_job_url;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getRollback_date() {
		return rollback_date;
	}
	public void setRollback_date(String rollback_date) {
		this.rollback_date = rollback_date;
	}	
}
