package com.sina.scm.data.DO;

import java.io.Serializable;

public class DeployTypePlatform implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String status;
	private String deploy_type;
	
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDeploy_type() {
		return deploy_type;
	}
	public void setDeploy_type(String deploy_type) {
		this.deploy_type = deploy_type;
	}
	
}
