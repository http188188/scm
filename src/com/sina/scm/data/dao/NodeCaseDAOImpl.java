package com.sina.scm.data.dao;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.sina.scm.data.DO.NodeCase;

public class NodeCaseDAOImpl
 {

	private static final String namespace = "nodeCase";
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger
			.getLogger(NodeCaseDAOImpl.class.getName());

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
	
	public static Integer insertNodeCase(NodeCase nodeCase) {

		Integer id = null;

		try {
			id = (Integer) sqlMapClient.insert(namespace + ".insert",nodeCase);
		} catch (SQLException e) {
			LOGGER.error("insertNodeCase db error", e);
		}
		return id;

	}
	
	public static void updateParameterById(String parameter,Integer id){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("parameter", parameter);
		params.put("id", id);
		
		try {
			sqlMapClient.update(namespace + ".updateParameterById", params);	
			
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updateParameterById from database error",e);			
		}		
		
	}
	
	public static void updateNodeCase(NodeCase nodeCase) {

		try {
			sqlMapClient.update(namespace + ".updateNodeCase", nodeCase);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updateNodeCase from database error", e);
		}

	}
	
	public static void updateNodeCaseByID(NodeCase nodeCase) {

		try {
			sqlMapClient.update(namespace + ".updateNodeCaseByID", nodeCase);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updateNodeCaseByID from database error", e);
		}

	}
	
	public static NodeCase getNodeCaseById(Integer nodeCaseId) {

		try {
			return (NodeCase) sqlMapClient.queryForObject(namespace
					+ ".getNodeCaseById", nodeCaseId);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getNodeCaseById from database error", e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<NodeCase> getNodeCases(Integer pipeline_case_id) {

		try {
			return (List<NodeCase>) sqlMapClient.queryForList(namespace
					+ ".getNodeCases", pipeline_case_id);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getNodeCases from database error", e);
		}
		return null;
	}	
	
  public static void update_preview_test_result_urlById(String preview_test_result_url,Integer id){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("preview_test_result_url", preview_test_result_url);
		params.put("id", id);
		
		try {
			sqlMapClient.update(namespace + ".update_preview_test_result_urlById", params);	
			
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("update_preview_test_result_urlById from database error",e);			
		}		
		
	}
	
}
