package com.sina.scm.data.DO;

import java.io.Serializable;

public class PiplineDef implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String pipeline_type;
	private Integer module_id;
	private String status;
	private String pipeline_json;
	private String package_type;
	private String pipeline_owner;
	private String name;
	private String deploy_type1;	
	private String module_name;
	private String module_version;	
	private Integer rollback_pipeline_def_id;
	private String fail_percent;
	private String is_template;
	
	
	
	
	
	
	public String getIs_template() {
		return is_template;
	}
	public void setIs_template(String is_template) {
		this.is_template = is_template;
	}
	public String getFail_percent() {
		return fail_percent;
	}
	public void setFail_percent(String fail_percent) {
		this.fail_percent = fail_percent;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getRollback_pipeline_def_id() {
		return rollback_pipeline_def_id;
	}
	public void setRollback_pipeline_def_id(Integer rollback_pipeline_def_id) {
		this.rollback_pipeline_def_id = rollback_pipeline_def_id;
	}
	public String getModule_version() {
		return module_version;
	}
	public void setModule_version(String module_version) {
		this.module_version = module_version;
	}
	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}
	public String getDeploy_type1() {
		return deploy_type1;
	}
	public void setDeploy_type1(String deploy_type1) {
		this.deploy_type1 = deploy_type1;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPipeline_type() {
		return pipeline_type;
	}
	public void setPipeline_type(String pipeline_type) {
		this.pipeline_type = pipeline_type;
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
	public String getPipeline_json() {
		return pipeline_json;
	}
	public void setPipeline_json(String pipeline_json) {
		this.pipeline_json = pipeline_json;
	}
	public String getPackage_type() {
		return package_type;
	}
	public void setPackage_type(String package_type) {
		this.package_type = package_type;
	}
	public String getPipeline_owner() {
		return pipeline_owner;
	}
	public void setPipeline_owner(String pipeline_owner) {
		this.pipeline_owner = pipeline_owner;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	

}
