package com.sina.scm.data.vo;

import java.util.List;

public class ModuleHistoryVO {
	
	private Integer id;	
	private String name;
	private String current_online_version;	
	private String update_version_time;	
	private String operator;
	private String module_owner;
	private String version_prefix;
	private String code_type;
	private String qb_job_def_url;
	private List<GroupAndUserVO> group;		
	
	public String getCode_type() {
		return code_type;
	}
	public void setCode_type(String code_type) {
		this.code_type = code_type;
	}
	public String getModule_owner() {
		return module_owner;
	}
	public void setModule_owner(String module_owner) {
		this.module_owner = module_owner;
	}
	public String getVersion_prefix() {
		return version_prefix;
	}
	public void setVersion_prefix(String version_prefix) {
		this.version_prefix = version_prefix;
	}
	public List<GroupAndUserVO> getGroup() {
		return group;
	}
	public void setGroup(List<GroupAndUserVO> group) {
		this.group = group;
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
	public String getCurrent_online_version() {
		return current_online_version;
	}
	public void setCurrent_online_version(String current_online_version) {
		this.current_online_version = current_online_version;
	}
	public String getUpdate_version_time() {
		return update_version_time;
	}
	public void setUpdate_version_time(String update_version_time) {
		this.update_version_time = update_version_time;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getQb_job_def_url() {
		return qb_job_def_url;
	}
	public void setQb_job_def_url(String qb_job_def_url) {
		this.qb_job_def_url = qb_job_def_url;
	}	

}
