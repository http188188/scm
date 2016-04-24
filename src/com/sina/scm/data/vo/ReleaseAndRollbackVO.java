package com.sina.scm.data.vo;

import java.util.List;

public class ReleaseAndRollbackVO {
	
	public String year;
	public Integer total_release;
	public Integer total_rollback;
	public List<Integer> month_release;
	public List<Integer> month_rollback;
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Integer getTotal_release() {
		return total_release;
	}
	public void setTotal_release(Integer total_release) {
		this.total_release = total_release;
	}
	public Integer getTotal_rollback() {
		return total_rollback;
	}
	public void setTotal_rollback(Integer total_rollback) {
		this.total_rollback = total_rollback;
	}
	public List<Integer> getMonth_release() {
		return month_release;
	}
	public void setMonth_release(List<Integer> month_release) {
		this.month_release = month_release;
	}
	public List<Integer> getMonth_rollback() {
		return month_rollback;
	}
	public void setMonth_rollback(List<Integer> month_rollback) {
		this.month_rollback = month_rollback;
	}
	
}