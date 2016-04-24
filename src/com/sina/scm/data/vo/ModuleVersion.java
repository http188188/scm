package com.sina.scm.data.vo;

import java.io.Serializable;

public class ModuleVersion implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer module_id;
	private String qb_job_url;
	private String version;
	private String status;
	private String release_type;
	private String release_date;
	private String operator;
	private String checkout_command;
	private Integer pipeline_case_id;
	private String jenkins_job_url;
	private String code_tag;
	private String code_revision;
	private String release_req_num;
	private String release_note;
	private Integer code_change_lines;
	private String stable_version;	
	private String is_tag;	
	
	public String getIs_tag() {
		return is_tag;
	}
	public void setIs_tag(String is_tag) {
		this.is_tag = is_tag;
	}
	public Integer getPipeline_case_id() {
		return pipeline_case_id;
	}
	public void setPipeline_case_id(Integer pipeline_case_id) {
		this.pipeline_case_id = pipeline_case_id;
	}
	public String getJenkins_job_url() {
		return jenkins_job_url;
	}
	public void setJenkins_job_url(String jenkins_job_url) {
		this.jenkins_job_url = jenkins_job_url;
	}
	public String getCode_tag() {
		return code_tag;
	}
	public void setCode_tag(String code_tag) {
		this.code_tag = code_tag;
	}
	public String getCode_revision() {
		return code_revision;
	}
	public void setCode_revision(String code_revision) {
		this.code_revision = code_revision;
	}
	public String getRelease_req_num() {
		return release_req_num;
	}
	public void setRelease_req_num(String release_req_num) {
		this.release_req_num = release_req_num;
	}
	public String getRelease_note() {
		return release_note;
	}
	public void setRelease_note(String release_note) {
		this.release_note = release_note;
	}
	public Integer getCode_change_lines() {
		return code_change_lines;
	}
	public void setCode_change_lines(Integer code_change_lines) {
		this.code_change_lines = code_change_lines;
	}
	public String getStable_version() {
		return stable_version;
	}
	public void setStable_version(String stable_version) {
		this.stable_version = stable_version;
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
	public String getQb_job_url() {
		return qb_job_url;
	}
	public void setQb_job_url(String qb_job_url) {
		this.qb_job_url = qb_job_url;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRelease_type() {
		return release_type;
	}
	public void setRelease_type(String release_type) {
		this.release_type = release_type;
	}
	public String getRelease_date() {
		return release_date;
	}
	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getCheckout_command() {
		return checkout_command;
	}
	public void setCheckout_command(String checkout_command) {
		this.checkout_command = checkout_command;
	}	
}
