package com.sina.scm.data.vo;

import com.sina.scm.data.DO.PiplineCase;

public class PiplineCaseAndType {
	
	public String type;
	
	public PiplineCase piplineCase;
	
    public Integer rollback_pipeline_def_id;
    
    public String rollback_package_type;
    
    public String rollback_pipeline_name;    
    
    

	public String getRollback_package_type() {
		return rollback_package_type;
	}

	public void setRollback_package_type(String rollback_package_type) {
		this.rollback_package_type = rollback_package_type;
	}

	public Integer getRollback_pipeline_def_id() {
		return rollback_pipeline_def_id;
	}

	public void setRollback_pipeline_def_id(Integer rollback_pipeline_def_id) {
		this.rollback_pipeline_def_id = rollback_pipeline_def_id;
	}

	public String getRollback_pipeline_name() {
		return rollback_pipeline_name;
	}

	public void setRollback_pipeline_name(String rollback_pipeline_name) {
		this.rollback_pipeline_name = rollback_pipeline_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PiplineCase getPiplineCase() {
		return piplineCase;
	}

	public void setPiplineCase(PiplineCase piplineCase) {
		this.piplineCase = piplineCase;
	}

}
