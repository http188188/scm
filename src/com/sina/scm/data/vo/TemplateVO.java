package com.sina.scm.data.vo;

import java.util.List;


import com.sina.scm.data.DO.NodeDef;

public class TemplateVO {
	private Integer template_id;
	private String template_name;
	public List<NodeDef> node;
	public Integer getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(Integer template_id) {
		this.template_id = template_id;
	}
	public String getTemplate_name() {
		return template_name;
	}
	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}
	public List<NodeDef> getNode() {
		return node;
	}
	public void setNode(List<NodeDef> node) {
		this.node = node;
	}
	

}
