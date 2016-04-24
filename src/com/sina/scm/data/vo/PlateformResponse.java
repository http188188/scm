package com.sina.scm.data.vo;

import java.util.ArrayList;


public class PlateformResponse {
	private Integer code;
	private String msg;
	private String log;
	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public DataVO data;
	

	

	
	

	public DataVO getData() {
		return data;
	}

	public void setData(DataVO data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
	
	

}
