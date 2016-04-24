package com.sina.scm.data.vo;

public class StatisticsOnlineByDay {
    
	//每日上线次数
	public String onlineEveryDayNum;
    
	//每日更新时间
	public String onlineEveryDayUpdateTime;
    
	//每日回滚次数
	public String onlineEveryDayRollbackNum;
	
	//日期
	public String date;	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOnlineEveryDayNum() {
		return onlineEveryDayNum;
	}

	public void setOnlineEveryDayNum(String onlineEveryDayNum) {
		this.onlineEveryDayNum = onlineEveryDayNum;
	}

	public String getOnlineEveryDayUpdateTime() {
		return onlineEveryDayUpdateTime;
	}

	public void setOnlineEveryDayUpdateTime(String onlineEveryDayUpdateTime) {
		this.onlineEveryDayUpdateTime = onlineEveryDayUpdateTime;
	}

	public String getOnlineEveryDayRollbackNum() {
		return onlineEveryDayRollbackNum;
	}

	public void setOnlineEveryDayRollbackNum(String onlineEveryDayRollbackNum) {
		this.onlineEveryDayRollbackNum = onlineEveryDayRollbackNum;
	}

}
