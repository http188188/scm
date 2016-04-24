package com.sina.scm.data.DO;

import java.io.Serializable;

public class PiplineCase implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer pipeline_def_id;
	private Integer module_id;
	private String status;
	private String release_package_url;
	private String rollback_package_url;
	private String release_note;
	private String operator;
	private String start_time;
	private String end_time;
	private String pipeline_case_json;
	private String deploy_type1;
	private String version;
	private String preview_iplist;	
	private String is_release_version;	
	private String rollBackVersion;	
	private String from_version;	
	private String preview_autotest_result_url;	
	
	public String getPreview_autotest_result_url() {
		return preview_autotest_result_url;
	}
	public void setPreview_autotest_result_url(String preview_autotest_result_url) {
		this.preview_autotest_result_url = preview_autotest_result_url;
	}
	public String getFrom_version() {
		return from_version;
	}
	public void setFrom_version(String from_version) {
		this.from_version = from_version;
	}
	public String getRollBackVersion() {
		return rollBackVersion;
	}
	public void setRollBackVersion(String rollBackVersion) {
		this.rollBackVersion = rollBackVersion;
	}
	public String getPreview_iplist() {
		return preview_iplist;
	}
	public String getIs_release_version() {
		return is_release_version;
	}
	public void setIs_release_version(String is_release_version) {
		this.is_release_version = is_release_version;
	}
	public void setPreview_iplist(String preview_iplist) {
		this.preview_iplist = preview_iplist;
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
	public Integer getModule_id() {
		return module_id;
	}
	public void setModule_id(Integer module_id) {
		this.module_id = module_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRelease_package_url() {
		return release_package_url;
	}
	public void setRelease_package_url(String release_package_url) {
		this.release_package_url = release_package_url;
	}
	public String getRollback_package_url() {
		return rollback_package_url;
	}
	public void setRollback_package_url(String rollback_package_url) {
		this.rollback_package_url = rollback_package_url;
	}
	public String getRelease_note() {
		return release_note;
	}
	public void setRelease_note(String release_note) {
		this.release_note = release_note;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getPipeline_case_json() {
		return pipeline_case_json;
	}
	public void setPipeline_case_json(String pipeline_case_json) {
		this.pipeline_case_json = pipeline_case_json;
	}
	public String getDeploy_type1() {
		return deploy_type1;
	}
	public void setDeploy_type1(String deploy_type1) {
		this.deploy_type1 = deploy_type1;
	}	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

}
