package com.sina.scm.data.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;

import com.sina.scm.data.dao.ModuleVersionDAOImpl;
import com.sina.scm.data.dao.RollBackDAOImpl;
import com.sina.scm.data.util.DateUtils;
import com.sina.scm.data.vo.StatisticsOnlineByDay;
import com.sina.scm.data.vo.StatisticsOnlineVO;


@Path("scmPlatDataDemoService")
public class ScmPlatDataDemoService {

	@Path("/statistics_online")
	@POST
	@Produces("text/json")
	public String statisticsOnlineAndRollback(@QueryParam("startTime") String startTime,
			@QueryParam("endTime") String endTime) {
		
		//1.check start time and end time
		if(StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)){
			
			return "start and end time can't empty!";			
		}
		//2.初始化需要展现的VO
		StatisticsOnlineVO statisticsOnlineVO = new StatisticsOnlineVO();
		//3.按照每天统计，上线发布次数，平均更新时间以及回滚系数
		statisticsOnlineByDay(startTime,endTime,statisticsOnlineVO);
		//4.时间段内模块的上线记录信息展示
		statisticsOnlineByModule(startTime,endTime,statisticsOnlineVO);
		//5.
		
		
		
		
		
		

		return "";
	}	

	private void statisticsOnlineByModule(String startTime, String endTime,
			StatisticsOnlineVO statisticsOnlineVO) {
		
		//1.endTime加1
		endTime = DateUtils.formatDate(DateUtils.getAfterDate((DateUtils.parseDate(endTime))),"yyyy-MM-dd");
		//2.获取时间段内，根据module_id进行group by以及每个count(module_id)
		
		
		
		
		
		
		
		
		
		
		
	}

	private void statisticsOnlineByDay(String startTime, String endTime,
			StatisticsOnlineVO statisticsOnlineVO) {

		Date[] days = DateUtils.getAllDate(DateUtils.parseDate(startTime),
				DateUtils.parseDate(endTime));
		Map<String, List<StatisticsOnlineByDay>> statisticsOnlineByDayMap = new HashMap<String, List<StatisticsOnlineByDay>>();
		List<StatisticsOnlineByDay> statisticsOnlineByDayList = new ArrayList<StatisticsOnlineByDay>();

		for (int day = 0; day < days.length; day++) {

			StatisticsOnlineByDay statisticsOnlineByDay = new StatisticsOnlineByDay();
			// 日期
			statisticsOnlineByDay.setDate(DateUtils.formatDate(days[day],
					"yyyy-MM-dd"));
			// 每日上线次数
			int releaseNum = ModuleVersionDAOImpl.getReleaseNumByDay(DateUtils
					.formatDate(days[day], "yyyy-MM-dd"));
			// 每日回滚次数
			int rollbackNum = RollBackDAOImpl.getRollBackNumByDay(DateUtils
					.formatDate(days[day], "yyyy-MM-dd"));
			// 生产环境平均更新时间
			int average = 24 * 60 / (releaseNum + rollbackNum);
			// 组装VO
			statisticsOnlineByDay.setOnlineEveryDayNum(String
					.valueOf(releaseNum));
			statisticsOnlineByDay.setOnlineEveryDayRollbackNum(String
					.valueOf(rollbackNum));
			statisticsOnlineByDay.setOnlineEveryDayUpdateTime(String
					.valueOf(average));

			statisticsOnlineByDayList.add(statisticsOnlineByDay);

		}

		statisticsOnlineByDayMap.put("general_statistics",
				statisticsOnlineByDayList);
		statisticsOnlineVO.setStatisticsOnlineByDay(statisticsOnlineByDayMap);

	}

}
