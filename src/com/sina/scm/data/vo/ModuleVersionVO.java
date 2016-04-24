package com.sina.scm.data.vo;

import java.util.List;

public class ModuleVersionVO {

	public List<ModuleHistoryVO> moduleHistoryVO;
	
	public ResultVO resultVO;
	
	public Integer totalCount;
	
	public Integer totalPage;	
	
	public List<ModuleHistoryVO> getModuleHistoryVO() {
		return moduleHistoryVO;
	}

	public void setModuleHistoryVO(List<ModuleHistoryVO> moduleHistoryVO) {
		this.moduleHistoryVO = moduleHistoryVO;
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
}
