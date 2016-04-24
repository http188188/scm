package com.sina.scm.data.vo;

import java.util.List;


public class ReleaseHistoryVO{

	public Integer totalCount;
	
	public Integer totalPage;
	
	public List<PiplineCaseAndType> listReleasHistory;		
	
	public ResultVO resultVO;
	
	public String module_version;	
	
    public String module_name;    
    
    public String pipeline_type;    

	public String getPipeline_type() {
		return pipeline_type;
	}

	public void setPipeline_type(String pipeline_type) {
		this.pipeline_type = pipeline_type;
	}

	public String getModule_name() {
		return module_name;
	}

	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}

	public String getModule_version() {
		return module_version;
	}

	public void setModule_version(String module_version) {
		this.module_version = module_version;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public ResultVO getResultVO() {
		return resultVO;
	}

	public void setResultVO(ResultVO resultVO) {
		this.resultVO = resultVO;
	}

	public List<PiplineCaseAndType> getListReleasHistory() {
		return listReleasHistory;
	}

	public void setListReleasHistory(List<PiplineCaseAndType> listReleasHistory) {
		this.listReleasHistory = listReleasHistory;
	}
}
