package com.sina.scm.data.vo;

public class StartPiplineVO {
	
	//release的WAR包地址
	public String releaseWarUrl;
	//回滚包地址
	public String rollBackWarUrl;
	//注释
	public String comments;
	//pipline的自定义Id
	public String piplineDefId;
	//包类型
	public String packageType;	
	//触发者
	public String operator;	
	//releaseType
	public String releaseType;	
	
	public String getReleaseType() {
		return releaseType;
	}
	public void setReleaseType(String releaseType) {
		this.releaseType = releaseType;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
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
	public String getPiplineDefId() {
		return piplineDefId;
	}
	public void setPiplineDefId(String piplineDefId) {
		this.piplineDefId = piplineDefId;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	
}
