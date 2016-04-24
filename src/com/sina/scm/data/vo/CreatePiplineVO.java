package com.sina.scm.data.vo;

import java.util.List;

import com.sina.scm.data.DO.NodeCase;

public class CreatePiplineVO {
	
	//1:表示可以启动，0：没有权限，-1：不存在pipline
	public String whether_to_start;
	//2.包类型
	public String packageType;
	//3.pipline自定义ID
	public String piplineDefId;
	//4.节点定义
	public List<NodeCase> node;
	//5.上线类型(分号分隔)
	public String allReleaseType;
	//6.pipline定义表中的上线类型
	public String releaseType;	
	//7.pipline的状态
	public String pipline_status;
	//8.审批人
	public String qa_owner;
	public String urgent_release_approver;
	public String release_follower;
	//9.pipline的实例ID
	public Integer pipline_case_id;
	public String releaseWarUrl;
	public String rollBackWarUrl;
	public String comments;	
	//10.source中需要的version
	public String version;
	public String dev_version;	
	
	public String rollBackVersion;
	public String selection;
	
	public String getSelection() {
		return selection;
	}
	public void setSelection(String selection) {
		this.selection = selection;
	}
	public String getRollBackVersion() {
		return rollBackVersion;
	}
	public void setRollBackVersion(String rollBackVersion) {
		this.rollBackVersion = rollBackVersion;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDev_version() {
		return dev_version;
	}
	public void setDev_version(String dev_version) {
		this.dev_version = dev_version;
	}
	public String getReleaseWarUrl() {
		return releaseWarUrl;
	}
	public void setReleaseWarUrl(String releaseWarUrl) {
		this.releaseWarUrl = releaseWarUrl;
	}
	public String getRollBackWarUrl() {
		return rollBackWarUrl;
	}
	public void setRollBackWarUrl(String rollBackWarUrl) {
		this.rollBackWarUrl = rollBackWarUrl;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getPipline_case_id() {
		return pipline_case_id;
	}
	public void setPipline_case_id(Integer pipline_case_id) {
		this.pipline_case_id = pipline_case_id;
	}
	public String getQa_owner() {
		return qa_owner;
	}
	public void setQa_owner(String qa_owner) {
		this.qa_owner = qa_owner;
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
	public String getWhether_to_start() {
		return whether_to_start;
	}
	public void setWhether_to_start(String whether_to_start) {
		this.whether_to_start = whether_to_start;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public String getPiplineDefId() {
		return piplineDefId;
	}
	public void setPiplineDefId(String piplineDefId) {
		this.piplineDefId = piplineDefId;
	}
	public List<NodeCase> getNode() {
		return node;
	}
	public void setNode(List<NodeCase> node) {
		this.node = node;
	}
	public String getAllReleaseType() {
		return allReleaseType;
	}
	public void setAllReleaseType(String allReleaseType) {
		this.allReleaseType = allReleaseType;
	}
	public String getReleaseType() {
		return releaseType;
	}
	public void setReleaseType(String releaseType) {
		this.releaseType = releaseType;
	}
	public String getPipline_status() {
		return pipline_status;
	}
	public void setPipline_status(String pipline_status) {
		this.pipline_status = pipline_status;
	}	
	
}
