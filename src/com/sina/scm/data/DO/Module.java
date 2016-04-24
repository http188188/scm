package com.sina.scm.data.DO;

import java.io.Serializable;

public class Module implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer product_id;
	private String name;
	private String code_type;
	private String status;
	private String create_date;
	private String module_owner;
	private String qa_owner;
	private String op_owner;
	private String bu_owner;
	private String depends_on_id;
	private Integer scm_info_id;
	private String push_list;
	private String url_online;	
	private Integer pipeline_def_id;
	private String current_online_version;
	private String qb_job_def_url;
	private String dev_list;
	private String preview_url;
	private String push_name;
	private String urgent_release_approver;
	private String release_follower;
	private String version_prefix;
	private String preview_depends;
	private String update_version_time;	
	
	public String getUpdate_version_time() {
		return update_version_time;
	}
	public void setUpdate_version_time(String update_version_time) {
		this.update_version_time = update_version_time;
	}
	public String getVersion_prefix() {
		return version_prefix;
	}
	public void setVersion_prefix(String version_prefix) {
		this.version_prefix = version_prefix;
	}
	public String getPreview_depends() {
		return preview_depends;
	}
	public void setPreview_depends(String preview_depends) {
		this.preview_depends = preview_depends;
	}
	public String getPush_name() {
		return push_name;
	}
	public void setPush_name(String push_name) {
		this.push_name = push_name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode_type() {
		return code_type;
	}
	public void setCode_type(String code_type) {
		this.code_type = code_type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getModule_owner() {
		return module_owner;
	}
	public void setModule_owner(String module_owner) {
		this.module_owner = module_owner;
	}
	public String getQa_owner() {
		return qa_owner;
	}
	public void setQa_owner(String qa_owner) {
		this.qa_owner = qa_owner;
	}
	public String getOp_owner() {
		return op_owner;
	}
	public void setOp_owner(String op_owner) {
		this.op_owner = op_owner;
	}
	public String getBu_owner() {
		return bu_owner;
	}
	public void setBu_owner(String bu_owner) {
		this.bu_owner = bu_owner;
	}
	public String getDepends_on_id() {
		return depends_on_id;
	}
	public void setDepends_on_id(String depends_on_id) {
		this.depends_on_id = depends_on_id;
	}
	public Integer getScm_info_id() {
		return scm_info_id;
	}
	public void setScm_info_id(Integer scm_info_id) {
		this.scm_info_id = scm_info_id;
	}
	public String getPush_list() {
		return push_list;
	}
	public void setPush_list(String push_list) {
		this.push_list = push_list;
	}
	public String getUrl_online() {
		return url_online;
	}
	public void setUrl_online(String url_online) {
		this.url_online = url_online;
	}
	public Integer getPipeline_def_id() {
		return pipeline_def_id;
	}
	public void setPipeline_def_id(Integer pipeline_def_id) {
		this.pipeline_def_id = pipeline_def_id;
	}
	public String getCurrent_online_version() {
		return current_online_version;
	}
	public void setCurrent_online_version(String current_online_version) {
		this.current_online_version = current_online_version;
	}
	public String getQb_job_def_url() {
		return qb_job_def_url;
	}
	public void setQb_job_def_url(String qb_job_def_url) {
		this.qb_job_def_url = qb_job_def_url;
	}
	public String getDev_list() {
		return dev_list;
	}
	public void setDev_list(String dev_list) {
		this.dev_list = dev_list;
	}
	public String getPreview_url() {
		return preview_url;
	}
	public void setPreview_url(String preview_url) {
		this.preview_url = preview_url;
	}
	public String getUrgent_release_approver() {
		return urgent_release_approver;
	}
	public void setUrgent_release_approver(String urgent_release_approver) {
		this.urgent_release_approver = urgent_release_approver;
	}
	public String getRelease_follower() {
		return release_follower;
	}
	public void setRelease_follower(String release_follower) {
		this.release_follower = release_follower;
	}	

}
