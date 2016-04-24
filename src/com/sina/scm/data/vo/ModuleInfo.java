package com.sina.scm.data.vo;

import java.io.Serializable;

public class ModuleInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String end_date;
	private String qb_job_url;
	private String beginDate;
	private String operate_type;
	private String version;
	private String push_list;
	private String operator;
	private String release_type;
	private String tag_path;	
	private String online_tag_url;
	private String release_tag_url;
	private String rsyncType;	
	
	public String getRsyncType() {
		return rsyncType;
	}
	public void setRsyncType(String rsyncType) {
		this.rsyncType = rsyncType;
	}
	public String getOnline_tag_url() {
		return online_tag_url;
	}
	public void setOnline_tag_url(String online_tag_url) {
		this.online_tag_url = online_tag_url;
	}
	public String getRelease_tag_url() {
		return release_tag_url;
	}
	public void setRelease_tag_url(String release_tag_url) {
		this.release_tag_url = release_tag_url;
	}
	public String getTag_path() {
		return tag_path;
	}
	public void setTag_path(String tag_path) {
		this.tag_path = tag_path;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getQb_job_url() {
		return qb_job_url;
	}
	public void setQb_job_url(String qb_job_url) {
		this.qb_job_url = qb_job_url;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getOperate_type() {
		return operate_type;
	}
	public void setOperate_type(String operate_type) {
		this.operate_type = operate_type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPush_list() {
		return push_list;
	}
	public void setPush_list(String push_list) {
		this.push_list = push_list;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getRelease_type() {
		return release_type;
	}
	public void setRelease_type(String release_type) {
		this.release_type = release_type;
	}
	
}
