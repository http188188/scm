package com.sina.scm.data.dao;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.sina.scm.data.DO.NodeDef;

public class NodeDefDAOImpl {

	private static final String namespace = "nodeDef";
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger
			.getLogger(NodeDefDAOImpl.class.getName());

	static {
		try {
			Reader reader = Resources
					.getResourceAsReader("com/sina/scm/data/dao/SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			LOGGER.error("NodeDefDAOImpl db error", e);
		}
	}
	
	public static Integer insertNodeDef(NodeDef nodeDef) {

		Integer id = null;

		try {
			id = (Integer) sqlMapClient.insert(namespace + ".insert",nodeDef);
		} catch (SQLException e) {
			LOGGER.error("insertNodeDef db error", e);
		}
		return id;
	}
	
	public static NodeDef getNodeDefById(Integer node_def_id){
		
		try {
			return (NodeDef) sqlMapClient.queryForObject(namespace
					+ ".getNodeDefById", node_def_id);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getNodeDefById from database error", e);
		}
		return null;
		
	}
	
}
