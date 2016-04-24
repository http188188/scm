package com.sina.scm.data.vo;

public class StatisticsOnlineByModule {
	
	//模块id
	public String moduleId;
	//scm_info_id
	public String scm_info_id;
	//模块名称
	public String moduleName;
	//模块负责人
	public String moduleOperator;
	//当前线上版本
	public String current_online_version;
	//本周上线次数
	public String moduleOnlineNumByWeek;
	//svn/git的地址
	public String code_url;
	//上线job的url
	public String qb_job_def_url;
	
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getScm_info_id() {
		return scm_info_id;
	}
	public void setScm_info_id(String scm_info_id) {
		this.scm_info_id = scm_info_id;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getModuleOperator() {
		return moduleOperator;
	}
	public void setModuleOperator(String moduleOperator) {
		this.moduleOperator = moduleOperator;
	}
	public String getCurrent_online_version() {
		return current_online_version;
	}
	public void setCurrent_online_version(String current_online_version) {
		this.current_online_version = current_online_version;
	}
	public String getModuleOnlineNumByWeek() {
		return moduleOnlineNumByWeek;
	}
	public void setModuleOnlineNumByWeek(String moduleOnlineNumByWeek) {
		this.moduleOnlineNumByWeek = moduleOnlineNumByWeek;
	}
	public String getCode_url() {
		return code_url;
	}
	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}
	public String getQb_job_def_url() {
		return qb_job_def_url;
	}
	public void setQb_job_def_url(String qb_job_def_url) {
		this.qb_job_def_url = qb_job_def_url;
	}

}
