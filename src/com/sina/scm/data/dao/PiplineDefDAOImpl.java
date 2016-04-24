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
import com.sina.scm.data.DO.PiplineDef;

public class PiplineDefDAOImpl {

	private static final String namespace = "piplineDef";
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger
			.getLogger(PiplineDefDAOImpl.class.getName());

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
	
	public static Integer insertPipelineDef(PiplineDef piplineDef) {

		Integer id = null;

		try {
			id = (Integer) sqlMapClient.insert(namespace + ".insert",piplineDef);
		} catch (SQLException e) {
			LOGGER.error("insertPipelineDef db error", e);
		}
		return id;
	}

	// get all module_ids
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
	public static List<PiplineDef> getTemplateList() {

		try {
			return (List<PiplineDef>) sqlMapClient.queryForList(namespace
					+ ".getTemplateList");
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getTemplateList from database error", e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<PiplineDef> getPiplineListByModuleId(Integer moduleId) {

		try {
			return (List<PiplineDef>) sqlMapClient.queryForList(namespace
					+ ".getPiplineListByModuleId", moduleId);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getPiplineListByModuleId from database error", e);
		}
		return null;
	}
	
	public static PiplineDef getPiplineByDefId(Integer piplineDefId) {

		try {
			return (PiplineDef) sqlMapClient.queryForObject(namespace
					+ ".getPiplineByDefId", piplineDefId);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getPiplineByDefId from database error", e);
		}
		return null;
	}
	
	public static String getPiplineJsonByDefId(Integer piplineDefId) {

		try {
			return (String) sqlMapClient.queryForObject(namespace
					+ ".getPiplineJsonByDefId", piplineDefId);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getPiplineJsonByDefId from database error", e);
		}
		return null;
	}
	
	public static void updatePipelineJsonById(Integer piplineDefId,String pipelineJson){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("piplineDefId", piplineDefId);
		params.put("pipelineJson", pipelineJson);
		
		try {
			sqlMapClient.update(namespace + ".updatePipelineJsonById", params);	
			
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updatePipelineJsonById from database error",e);			
		}		
	}
	
	@SuppressWarnings("unchecked")
	public static List<PiplineDef> getAllReleaseList(Integer start,Integer end,String name) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		params.put("name", name);

		try {
			return (List<PiplineDef>) sqlMapClient.queryForList(namespace
					+ ".getAllReleaseList",params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getAllReleaseList from database error", e);
		}
		return null;
	}	
	
	@SuppressWarnings("unchecked")
  public static List<PiplineDef> getAllReleaseList5(Integer start,Integer end,String name,List<Integer> module_ids) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		params.put("name", name);
		params.put("module_ids", module_ids);

		try {
			return (List<PiplineDef>) sqlMapClient.queryForList(namespace
					+ ".getAllReleaseList5",params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getAllReleaseList from database error", e);
		}
		return null;
	}	
	
	public static Integer getAllReleaseCount(String name) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);

		try {
			return (Integer) sqlMapClient.queryForObject(namespace
					+ ".getAllReleaseCount",params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getAllReleaseCount from database error", e);
		}
		return null;
	}
	
	public static Integer getAllReleaseCount3(String name) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);

		try {
			return (Integer) sqlMapClient.queryForObject(namespace
					+ ".getAllReleaseCount3",params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getAllReleaseCount3 from database error", e);
		}
		return null;
	}
    public static Integer getAllReleaseCount2(String name,List<Integer> module_ids) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("module_ids", module_ids);

		try {
			return (Integer) sqlMapClient.queryForObject(namespace
					+ ".getAllReleaseCount2",params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getAllReleaseCount from database error", e);
		}
		return null;
	}
	
	public static PiplineDef getPiplineByDefId1(Integer piplineDefId,String name) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("piplineDefId", piplineDefId);

		try {
			return (PiplineDef) sqlMapClient.queryForObject(namespace
					+ ".getPiplineByDefId1", params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getPiplineByDefId1 from database error", e);
		}
		return null;
	}
	
     public static Integer getAllReleaseCount1(Integer piplineDefId,String name) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("piplineDefId", piplineDefId);

		try {
			return (Integer) sqlMapClient.queryForObject(namespace
					+ ".getAllReleaseCount1",params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getAllReleaseCount1 from database error", e);
		}
		return null;
	}
     public static String getPiplineNameByModuleId(Integer module_id) {

 		try {
 			return (String) sqlMapClient.queryForObject(namespace
 					+ ".getPiplineNameByModuleId", module_id);
 		} catch (SQLException e) {
 			e.printStackTrace();
 			LOGGER.info("getPiplineNameByModuleId from database error", e);
 		}
 		return null;
 	}

}
