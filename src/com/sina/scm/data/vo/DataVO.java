package com.sina.scm.data.vo;


import java.util.ArrayList;

public class DataVO {
	private String task_id;
	private ArrayList<String> ips;
	
	
	public ArrayList<String> getIps() {
		return ips;
	}
	public void setIps(ArrayList<String> ips) {
		this.ips = ips;
	}
	public String getTask_id() {
		return task_id;
	}
	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}
	
	
}
