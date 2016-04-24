package com.sina.scm.data.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 上线发布记录按照起始和终止时间进行查询
 * 例如:(45周11月3日到11月9日)
 * @author jintao3
 *
 */
public class StatisticsOnlineVO implements Serializable {

	private static final long serialVersionUID = 1L;

	// 1.按照每天统计，上线发布次数，平均更新时间以及回滚系数
	public Map<String, List<StatisticsOnlineByDay>> statisticsOnlineByDay;

	// 2.时间段内模块的上线记录信息展示
	public Map<String, ArrayList<StatisticsOnlineByModule>> statisticsOnlineByModule;

	// 3.时间段内模块的回滚记录详细展示
	public Map<String, ArrayList<RollBack>> statisticsRollBackByModule;

	public Map<String, List<StatisticsOnlineByDay>> getStatisticsOnlineByDay() {
		return statisticsOnlineByDay;
	}

	public void setStatisticsOnlineByDay(
			Map<String, List<StatisticsOnlineByDay>> statisticsOnlineByDay) {
		this.statisticsOnlineByDay = statisticsOnlineByDay;
	}

	public Map<String, ArrayList<StatisticsOnlineByModule>> getStatisticsOnlineByModule() {
		return statisticsOnlineByModule;
	}

	public void setStatisticsOnlineByModule(
			Map<String, ArrayList<StatisticsOnlineByModule>> statisticsOnlineByModule) {
		this.statisticsOnlineByModule = statisticsOnlineByModule;
	}

	public Map<String, ArrayList<RollBack>> getStatisticsRollBackByModule() {
		return statisticsRollBackByModule;
	}

	public void setStatisticsRollBackByModule(
			Map<String, ArrayList<RollBack>> statisticsRollBackByModule) {
		this.statisticsRollBackByModule = statisticsRollBackByModule;
	}	

}