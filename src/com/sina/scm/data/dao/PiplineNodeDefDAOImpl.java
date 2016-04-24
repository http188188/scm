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
import com.sina.scm.data.DO.PiplineNodeDef;

public class PiplineNodeDefDAOImpl {

	private static final String namespace = "piplineNodeDef";
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger
			.getLogger(PiplineNodeDefDAOImpl.class.getName());

	static {
		try {
			Reader reader = Resources
					.getResourceAsReader("com/sina/scm/data/dao/SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			LOGGER.error("PiplineNodeDefDAOImpl db error", e);
		}
	}
	
	public static Integer insert(PiplineNodeDef piplineNodeDef) {

		Integer id = null;

		try {
			id = (Integer) sqlMapClient.insert(namespace + ".insert",piplineNodeDef);
		} catch (SQLException e) {
			LOGGER.error("insert db error", e);
		}
		return id;
	}
	
    @SuppressWarnings("unchecked")
	public static List<PiplineNodeDef> getExcuteNodeByPiplineDefId(Integer piplineDefId){
    	
    	List<PiplineNodeDef> piplineNodeDef = new ArrayList<PiplineNodeDef>();
    	
    	try {
    		piplineNodeDef = (List<PiplineNodeDef>)sqlMapClient.queryForList(namespace + ".getExcuteNodeByPiplineDefId",piplineDefId);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getExcuteNodeByPiplineDefId from database error", e);
		}
    	
    	return piplineNodeDef;
    }
    
   	public static PiplineNodeDef getNodeByNodeDefId(Integer pipeline_def_id,Integer node_def_id){
    	
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("node_def_id", node_def_id);
		params.put("pipeline_def_id", pipeline_def_id);
    	
       	
       	try {
       		return (PiplineNodeDef)sqlMapClient.queryForObject(namespace + ".getNodeByNodeDefId",params);
   		} catch (SQLException e) {
   			e.printStackTrace();
   			LOGGER.info("getNodeByNodeDefId from database error", e);
   		}
       	
       	return null;
       }
   	
   	public static void update(Integer next_id_on_success,Integer id) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("next_id_on_success", next_id_on_success);

		try {
			sqlMapClient.update(namespace + ".update", params);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("update from database error", e);
		}

	}
	
}
