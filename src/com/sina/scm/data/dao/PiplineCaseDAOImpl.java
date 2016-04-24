package com.sina.scm.data.dao;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.sina.scm.data.DO.PiplineCase;

public class PiplineCaseDAOImpl {

	private static final String namespace = "piplineCase";
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger
			.getLogger(PiplineCaseDAOImpl.class.getName());

	static {
		try {
			Reader reader = Resources
					.getResourceAsReader("com/sina/scm/data/dao/SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			LOGGER.error("PiplineDefDAOImpl db error", e);
		}
	}
	
	public static Integer insertPiplineCase(PiplineCase piplineCase) {

		Integer id = null;

		try {
			id = (Integer) sqlMapClient.insert(namespace + ".insert",
					piplineCase);
		} catch (SQLException e) {
			LOGGER.error("insertPiplineCase db error", e);
		}
		return id;

	}
	
    public static void updatePiplineCaseJsonById(String pipeline_case_json,Integer id){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pipeline_case_json", pipeline_case_json);
		params.put("id", id);
		
		try {
			sqlMapClient.update(namespace + ".updatePiplineCaseJsonById", params);	
			
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updatePiplineCaseJsonById from database error",e);			
		}		
		
	}
    
	public static Integer getCountByPiplineDefIdInPcase(Integer pipeline_def_id) {

		try {
			return (Integer) sqlMapClient.queryForObject(namespace
					+ ".getCountByPiplineDefIdInPcase", pipeline_def_id);
		} catch (SQLException e) {
			LOGGER.error("getCountByPiplineDefIdInPcase db error", e);
		}

		return null;
	}

	public static Integer getFailPiplineByModuleId(Integer moduleId,String start_time,String end_time) {

		try {
			Map<String,Object> map=new HashMap<String,Object>();  
			map.put("module_id",moduleId);  
			map.put("start_time",start_time); 
			map.put("end_time",end_time);
			return (Integer) sqlMapClient.queryForObject(namespace
					+ ".getFailPiplineByModuleId", map);
		} catch (SQLException e) {
			LOGGER.error("getFailPiplineByModuleId db error", e);
		}

		return null;
	}
	public static Integer getFailPiplineByModuleIdAndTime(Integer moduleId,String start_time,String end_time) {

		try {
			Map<String,Object> map=new HashMap<String,Object>();  
			map.put("module_id",moduleId);  
			map.put("start_time",start_time); 
			map.put("end_time",end_time);
			return (Integer) sqlMapClient.queryForObject(namespace
					+ ".getFailPiplineByModuleIdAndTime", map);
		} catch (SQLException e) {
			LOGGER.error("getFailPiplineByModuleIdAndTime db error", e);
		}

		return null;
	}
	
	public static Integer getAllPiplineByModuleIdAndTime(Integer moduleId,String start_time,String end_time) {

		try {
			Map<String,Object> map=new HashMap<String,Object>();  
			map.put("module_id",moduleId);  
			map.put("start_time",start_time); 
			map.put("end_time",end_time);
			return (Integer) sqlMapClient.queryForObject(namespace
					+ ".getAllPiplineByModuleIdAndTime", map);
		} catch (SQLException e) {
			LOGGER.error("getAllPiplineByModuleIdAndTime db error", e);
		}

		return null;
	}
	public static Integer getAllPiplineByModuleId(Integer moduleId,String start_time,String end_time) {

		try {
			Map<String,Object> map=new HashMap<String,Object>();  
			map.put("module_id",moduleId);  
			map.put("start_time",start_time); 
			map.put("end_time",end_time);
			return (Integer) sqlMapClient.queryForObject(namespace
					+ ".getAllPiplineByModuleId", map);
		} catch (SQLException e) {
			LOGGER.error("getAllPiplineByModuleId db error", e);
		}

		return null;
	}
	
	public static PiplineCase getPiplineCaseByModuleId(Integer module_id) {

		try {
			return (PiplineCase) sqlMapClient.queryForObject(namespace
					+ ".getPiplineCaseByModuleId", module_id);
		} catch (SQLException e) {
			LOGGER.error("getPiplineCaseByModuleId db error", e);
		}

		return null;
	}
	
	public static PiplineCase getPiplinecaseByPdefAndStatus(Integer pipeline_def_id) {

		try {
			return (PiplineCase) sqlMapClient.queryForObject(namespace
					+ ".getPiplinecaseByPdefAndStatus", pipeline_def_id);
		} catch (SQLException e) {
			LOGGER.error("getPiplinecaseByPdefAndStatus db error", e);
		}

		return null;
	}
	
	public static PiplineCase getPipelineCaseById(Integer pipeline_case_id) {

		try {
			return (PiplineCase) sqlMapClient.queryForObject(namespace
					+ ".getPipelineCaseById", pipeline_case_id);
		} catch (SQLException e) {
			LOGGER.error("getPipelineCaseById db error", e);
		}

		return null;
	}
	
	
	
	public static void updatePipeLineCase(PiplineCase piplineCase) {

		try {
			sqlMapClient.update(namespace + ".updatePipeLineCase", piplineCase);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updatePipeLineCase from database error", e);
		}

	}
	
	public static PiplineCase getLastPiplinecaseByPdef(Integer module_id) {

		try {
			return (PiplineCase) sqlMapClient.queryForObject(namespace
					+ ".getLastPiplinecaseByPdef", module_id);
		} catch (SQLException e) {
			LOGGER.error("getLastPiplinecaseByPdef db error", e);
		}

		return null;
	}
	
	public static void updateReleasePackageUrlByPipelineCaseId(PiplineCase piplineCase) {

		try {
			sqlMapClient.update(namespace + ".updateReleasePackageUrlByPipelineCaseId", piplineCase);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updateReleasePackageUrlByPipelineCaseId from database error", e);
		}

	}
	
	public static Integer getRunningPiplineCaseCount() {
		Integer count = null;
		try {
			count = (Integer) sqlMapClient.queryForObject(namespace
					+ ".getRunningPiplineCaseCount");
		} catch (SQLException e) {
			LOGGER.error("getRunningPiplineCaseCount db error", e);
		}

		return count;
	}
	
	public static Integer getRunningPiplineCaseCountByName(String name) {
		Integer count = null;
		try {
			count = (Integer) sqlMapClient.queryForObject(namespace
					+ ".getRunningPiplineCaseCountByName",name);
		} catch (SQLException e) {
			LOGGER.error("getRunningPiplineCaseCountByName db error", e);
		}

		return count;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Integer> getRunningPiplineDefId(Integer start,Integer end) {
		
		List<Integer> ids = new ArrayList<Integer>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);

		try {
			ids = (List<Integer>) sqlMapClient.queryForList(namespace
					+ ".getRunningPiplineDefId",params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getRunningPiplineDefId from database error", e);
		}
		return ids;
	}	
	
	@SuppressWarnings("unchecked")
	public static List<PiplineCase> getAllPiplinecaseByModuleId(Integer module_id) {

		try {
			return (List<PiplineCase>) sqlMapClient.queryForList(namespace
					+ ".getAllPiplinecaseByModuleId", module_id);
		} catch (SQLException e) {
			LOGGER.error("getAllPiplinecaseByModuleId db error", e);
		}

		return null;
	}
	
	public static Integer getAllPiplinecaseByModuleIdCount(Integer module_id) {

		try {
			return (Integer) sqlMapClient.queryForObject(namespace
					+ ".getAllPiplinecaseByModuleIdCount", module_id);
		} catch (SQLException e) {
			LOGGER.error("getAllPiplinecaseByModuleIdCount db error", e);
		}

		return null;
	}
	
	public static void updatePreviewIplistById(Integer id,String preview_iplist){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("preview_iplist", preview_iplist);
		params.put("id", id);
		
		try {
			sqlMapClient.update(namespace + ".updatePreviewIplistById", params);	
			
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updatePreviewIplistById from database error",e);			
		}		
	}	
	
	@SuppressWarnings("unchecked")
	public static List<PiplineCase> getAllSucessHistoryVersionByModuleId(Integer module_id) {

		try {
			return (List<PiplineCase>) sqlMapClient.queryForList(namespace
					+ ".getAllSucessHistoryVersionByModuleId", module_id);
		} catch (SQLException e) {
			LOGGER.error("getAllSucessHistoryVersionByModuleId db error", e);
		}

		return null;
	}	
	
	public static PiplineCase getOneSucessHistoryVersionByModuleId(Integer module_id,String ci_version) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("module_id", module_id);
		params.put("version", ci_version);
		
		try {
			return (PiplineCase) sqlMapClient.queryForList(namespace
					+ ".getOneSucessHistoryVersionByModuleId", params);
		} catch (SQLException e) {
			LOGGER.error("getOneSucessHistoryVersionByModuleId db error", e);
		}

		return null;
	}	
	
	public static void updateReleaseVersionByPipelineCaseId(PiplineCase piplineCase) {

		try {
			sqlMapClient.update(namespace + ".updateReleaseVersionByPipelineCaseId", piplineCase);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updateReleaseVersionByPipelineCaseId from database error", e);
		}

	}
	
	@SuppressWarnings("unchecked")
	public static List<PiplineCase> getAllPiplinecaseByPdef(Integer pipeline_def_id) {

		try {
			return (List<PiplineCase>) sqlMapClient.queryForList(namespace
					+ ".getAllPiplinecaseByPdef", pipeline_def_id);
		} catch (SQLException e) {
			LOGGER.error("getAllPiplinecaseByPdef db error", e);
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<PiplineCase> getAllPiplinecaseBypdfId(Integer pipeline_def_id,Integer start,Integer end) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		params.put("pipeline_def_id", pipeline_def_id);
		
		try {
			return (List<PiplineCase>) sqlMapClient.queryForList(namespace
					+ ".getAllPiplinecaseBypdfId", params);
		} catch (SQLException e) {
			LOGGER.error("getAllPiplinecaseBypdfId db error", e);
		}

		return null;
	}
	
	public static Integer getRunningPiplineCaseCountByName() {
		Integer count = null;
		try {
			count = (Integer) sqlMapClient.queryForObject(namespace
					+ ".getRunningPiplineCaseCountByName");
		} catch (SQLException e) {
			LOGGER.error("getRunningPiplineCaseCountByName db error", e);
		}

		return count;
	}
	
   public static void updatePreviewAutotestResultUrlById(Integer id,String preview_autotest_result_url){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("preview_autotest_result_url", preview_autotest_result_url);
		params.put("id", id);
		
		try {
			sqlMapClient.update(namespace + ".updatePreviewAutotestResultUrlById", params);	
			
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updatePreviewAutotestResultUrlById from database error",e);			
		}		
	}	
	
	
}
