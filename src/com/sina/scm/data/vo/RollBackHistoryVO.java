package com.sina.scm.data.vo;

import java.util.List;


public class RollBackHistoryVO{

	public ResultVO resultVO;
	
	public String module_version;	
	
	public List<RollBackVO> pipelineCase;

	public ResultVO getResultVO() {
		return resultVO;
	}

	public void setResultVO(ResultVO resultVO) {
		this.resultVO = resultVO;
	}

	public String getModule_version() {
		return module_version;
	}

	public void setModule_version(String module_version) {
		this.module_version = module_version;
	}

	public List<RollBackVO> getPipelineCase() {
		return pipelineCase;
	}

	public void setPipelineCase(List<RollBackVO> pipelineCase) {
		this.pipelineCase = pipelineCase;
	}
	
}
