package com.sina.scm.data.vo;

import java.util.List;

import com.sina.scm.data.DO.PiplineDef;

public class ReleaseVO{

	public Integer totalCount;
	
	public Integer totalPage;
	
	public List<PiplineDef> listRelease;		
	
	public ResultVO resultVO;	
	
	public ResultVO getResultVO() {
		return resultVO;
	}

	public void setResultVO(ResultVO resultVO) {
		this.resultVO = resultVO;
	}

	public List<PiplineDef> getListRelease() {
		return listRelease;
	}

	public void setListRelease(List<PiplineDef> listRelease) {
		this.listRelease = listRelease;
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

}
