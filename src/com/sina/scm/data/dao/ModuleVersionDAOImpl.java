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
import com.sina.scm.data.vo.ModuleVersion;

public class ModuleVersionDAOImpl{
	
	private static final String namespace = "moduleVersion";	
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger.getLogger(ModuleVersionDAOImpl.class.getName());

	static {
		try {
			Reader reader = Resources
					.getResourceAsReader("com/sina/scm/data/dao/SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			LOGGER.error("ModuleVersionDAOImpl db error", e);
		}
	}
	
	//insert table module_version 
	public static void insertModuleVersion(ModuleVersion moduleVersion) {

		try {
			sqlMapClient.insert(namespace + ".insert",moduleVersion);
		} catch (SQLException e) {
			LOGGER.error("ModuleVersionDAOImpl db error", e);
		}

	}
	
	//count version num
	public static int getVersionNum(String moduleVersion) {
		
		try {
			return (Integer)sqlMapClient.queryForObject(namespace + ".getVersionNum",moduleVersion);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("getVersionNum db error", e);
			return -1;
		}
		
	}
	
public static Integer getVersionNum1(String moduleVersion,Integer module_id) {
		
		Map<String, Object> params = new HashMap<String, Object>();
	    //where
		params.put("version", moduleVersion);
		params.put("module_id", module_id);
		
		
		try {
			return (Integer)sqlMapClient.queryForObject(namespace + ".getVersionNum1",params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("getVersionNum db error", e);
			return -1;
		}
		
	}
	
	//update module_version by version and module_id
	public static void updateModuleVersionByVersionAndModuleId(ModuleVersion moduleVersion){
		
		Map<String, Object> params = new HashMap<String, Object>();
	    //where
		params.put("version", moduleVersion.getVersion());
		params.put("module_id", moduleVersion.getModule_id());
		//set
		params.put("qb_job_url", moduleVersion.getQb_job_url());
		params.put("release_type", moduleVersion.getRelease_type());
		params.put("release_date", moduleVersion.getRelease_date());
		params.put("operator", moduleVersion.getOperator());
		//params.put("checkout_command", moduleVersion.getCheckout_command());
		
		try {
			sqlMapClient.update(namespace + ".updateModuleVersionByVersionAndModuleId", params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updateModuleVersionByVersionAndModuleId from database error", e);
		}		
		
	}
	
	//get latest module_version
	public static String getLatestModuleVersionById(Integer module_id) {

		String version = "";

		try {
			version = (String) sqlMapClient.queryForObject(namespace
					+ ".getVersionByModuleId", module_id);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getLatestModuleVersionById from database error", e);
		}

		return version;
	}	
	
	public static String getLatestModuleVersionById1(Integer module_id) {

		String version = "";

		try {
			version = (String) sqlMapClient.queryForObject(namespace
					+ ".getVersionByModuleId1", module_id);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getLatestModuleVersionById1 from database error", e);
		}

		return version;
	}
	
	// get latest module_version
	@SuppressWarnings("unchecked")
	public static List<String> getHistoryVersionByModuleId(Integer module_id) {

		List<String> version =new ArrayList<String>();

		try {
			version = (List<String>) sqlMapClient.queryForList(namespace
					+ ".getHistoryVersionByModuleId", module_id);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getLatestModuleVersionById from database error", e);
		}

		return version;
	}
	
	//get release num every day
	public static int getReleaseNumByDay(String release_date) {

		Integer releaseNumByDayNum = -1;

		try {
			releaseNumByDayNum = (Integer) sqlMapClient.queryForObject(
					namespace + ".getReleaseNumByDay", release_date);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getReleaseNumByDay from database error", e);
		}

		return releaseNumByDayNum;

	}
	
	//get all module_ids
	@SuppressWarnings("unchecked")
	public static List<Integer> getAllModuleIds() {
		List<Integer> ids = new ArrayList<Integer>();

		try {
			ids = (List<Integer>) sqlMapClient.queryForList(namespace
					+ ".getAllModuleIds");

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getAllModuleIds from database error", e);
		}

		return ids;

	}
	
	@SuppressWarnings("unchecked")
	public static List<Integer> getAllModuleId(Integer start,Integer end) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		
		try {
			return (List<Integer>) sqlMapClient.queryForList(namespace
					+ ".getAllModuleId", params);
		} catch (SQLException e) {
			LOGGER.error("getAllModuleId db error", e);
		}

		return null;
	}
	
	 public static ModuleVersion getModuleVersionByToVersion(String version,Integer module_id) {

		  Map<String, Object> params = new HashMap<String, Object>();
			params.put("version", version);
			params.put("module_id", module_id);
		try {
			return (ModuleVersion) sqlMapClient.queryForObject(namespace
					+ ".getModuleVersionByToVersion", params);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getModuleVersionByToVersion from database error", e);
		}

		return null;

	}
	 
	 public static Integer releaseAndRollBackNum(String year) {

			try {
				return (Integer) sqlMapClient.queryForObject(namespace
						+ ".releaseAndRollBackNum", year);

			} catch (SQLException e) {
				e.printStackTrace();
				LOGGER.info("releaseAndRollBackNum from database error", e);
			}

			return null;

		}	 
	 	 
	 public static Integer releaseAndRollBackNumBymoduleId(String release_date,Integer module_id) {
           
		    Map<String, Object> params = new HashMap<String, Object>();
			params.put("release_date", release_date);
			params.put("module_id", module_id);
		       
		 
			try {
				return (Integer) sqlMapClient.queryForObject(namespace
						+ ".releaseAndRollBackNumBymoduleId", params);

			} catch (SQLException e) {
				e.printStackTrace();
				LOGGER.info("releaseAndRollBackNumBymoduleId from database error", e);
			}

			return null;

		}
	 
}
