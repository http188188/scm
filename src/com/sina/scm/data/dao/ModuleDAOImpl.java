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
import com.sina.scm.data.DO.Module;
import com.sina.scm.data.DO.ProductLine;

public class ModuleDAOImpl{
	
	private static final String namespace = "module";	
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger.getLogger(ModuleDAOImpl.class.getName());

	static {
		try {
			Reader reader = Resources
					.getResourceAsReader("com/sina/scm/data/dao/SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			LOGGER.error("ModuleDAOImpl db error", e);
		}
	}
	
	public static Integer insertModule(Module module) {

		Integer id = null;

		try {
			id = (Integer) sqlMapClient.insert(namespace + ".insert",module);
		} catch (SQLException e) {
			LOGGER.error("insertModule db error", e);
		}
		return id;
	}
	
	//pushName line
	public static int getPushNameNum(String pushName){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pushName", pushName);
		params.put("fenhao_pushName", ";"+pushName);
		params.put("fenhao_pushName_fenhao", ";"+pushName+";");
		params.put("pushName_fenhao", pushName+";");
		
		Integer pushNameNum = -1;
		
		try {
			pushNameNum = (Integer)sqlMapClient.queryForObject(namespace + ".getPushNameNum", params);	
			
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getPushNameNum from database error",e);			
		}
		
		return pushNameNum;		
	}	
	
	// get module id
	public static int getModuleId(String pushName) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pushName", pushName);
		params.put("fenhao_pushName", ";" + pushName);
		params.put("fenhao_pushName_fenhao", ";" + pushName + ";");
		params.put("pushName_fenhao", pushName + ";");

		Integer moduleId = -1;

		try {
			moduleId = (Integer) sqlMapClient.queryForObject(namespace
					+ ".getModuleId", params);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("moduleId from database error", e);
		}

		return moduleId;
	}
	
		public static String getModuleCurrentVersion(String pushName) {

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pushName", pushName);
			params.put("fenhao_pushName", ";" + pushName);
			params.put("fenhao_pushName_fenhao", ";" + pushName + ";");
			params.put("pushName_fenhao", pushName + ";");

			String currntVersion = "";

			try {
				currntVersion = (String) sqlMapClient.queryForObject(namespace
						+ ".getModuleCurrentVerison", params);

			} catch (SQLException e) {
				e.printStackTrace();
				LOGGER.info("moduleId from database error", e);
			}

			return currntVersion;
		}
	
	// get module id
	public static void updateModuleOnlineVersion(String current_online_version,String update_version_time,Integer id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("current_online_version", current_online_version);
		params.put("id", id);
		params.put("update_version_time", update_version_time);

		try {
			sqlMapClient.update(namespace + ".updateModuleOnlineVersion", params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updateModuleOnlineVersion from database error", e);
		}

	}
	
	//get list id
	@SuppressWarnings("unchecked")
	public static List<Integer> getIdbyVersionIsNull(){
		
		List<Integer> ids = new ArrayList<Integer>();		
		try {
			ids = (List<Integer>)sqlMapClient.queryForList(namespace + ".getIdbyVersionIsNull");
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getIdbyVersionIsNull from database error", e);
		}		
		
		return ids;		
	}
	
	public static Module getModuleById(Integer moduleId) {

		try {
			return (Module) sqlMapClient.queryForObject(namespace
					+ ".getModuleById", moduleId);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getModuleById from database error", e);
		}
		return null;
	}
	
	public static Integer getModuleCount(String name) {
		Integer count = null;
		try {
			count = (Integer) sqlMapClient.queryForObject(namespace+ ".getModuleCount",name);
		} catch (SQLException e) {
			LOGGER.error("getModuleCount db error", e);
		}

		return count;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Module> getAllModule(Integer start,Integer end,String name) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		params.put("name", name);
		
		try {
			return (List<Module>) sqlMapClient.queryForList(namespace
					+ ".getAllModule", params);
		} catch (SQLException e) {
			LOGGER.error("getAllModule db error", e);
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Module> getIdbyVersionIsNotNull(){
		
		try {
			return (List<Module>)sqlMapClient.queryForList(namespace + ".getIdbyVersionIsNotNull");
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getIdbyVersionIsNotNull from database error", e);
		}		
		
		return null;		
	}
	
	public static Module getModuleByName(String name) {

		try {
			return (Module) sqlMapClient.queryForObject(namespace
					+ ".getModuleByName", name);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getModuleByName from database error", e);
		}
		return null;
	}
	
	public static void updateModule(String name ,String version_prefix,String module_owner,Integer id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("id", id);
		params.put("version_prefix", version_prefix);
		params.put("module_owner", module_owner);

		try {
			sqlMapClient.update(namespace + ".updateModule", params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updateModule from database error", e);
		}

	}	
	
	public static void updateModuleToScmId(Integer id,Integer scm_info_id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("scm_info_id", scm_info_id);

		try {
			sqlMapClient.update(namespace + ".updateModuleToScmId", params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updateModuleToScmId from database error", e);
		}

	}	
	
	public static Integer getAllModuleCount(String name) {
		Integer count = null;
		try {
			count = (Integer) sqlMapClient.queryForObject(namespace+ ".getAllModuleCount",name);
		} catch (SQLException e) {
			LOGGER.error("getAllModuleCount db error", e);
		}

		return count;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Module> getIsAllModule(Integer start,Integer end,String name) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		params.put("name", name);
		
		try {
			return (List<Module>) sqlMapClient.queryForList(namespace
					+ ".getIsAllModule", params);
		} catch (SQLException e) {
			LOGGER.error("getIsAllModule db error", e);
		}

		return null;
	}
	
	public static void updateModule(Module module) {

		try {
			sqlMapClient.update(namespace + ".updateModule1", module);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updateModule from database error", e);
		}

	}
	
	@SuppressWarnings("unchecked")
	public static List<ProductLine> getProductLine(){
		
		try {
			return (List<ProductLine>)sqlMapClient.queryForList(namespace + ".getProductLine");
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getProductLine from database error", e);
		}		
		
		return null;		
	}
		
}
