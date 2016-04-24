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
import com.sina.scm.data.DO.PiplineDef;
import com.sina.scm.data.vo.Group;

public class GroupDAOImpl{
	
	private static final String namespace = "group";	
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger.getLogger(GroupDAOImpl.class.getName());

	static {
		try {
			Reader reader = Resources
					.getResourceAsReader("com/sina/scm/data/dao/SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			LOGGER.error("GroupDAOImpl db error", e);
		}
	}
	
	//根据module_Id查找自己所包含的组
	@SuppressWarnings("unchecked")
	public static List<Group> getGrooupByModuleId(Integer module_id) {

		try {
			return (List<Group>) sqlMapClient.queryForList(namespace+ ".getGrooupByModuleId", module_id);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getGrooupByModuleId from database error", e);
		}

		return null;
	}
	
	public static Group getGroupByNameAndModuleId(Integer module_id,String name){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("module_id", module_id);
		params.put("name", name);
		
		try {
			return (Group) sqlMapClient.queryForObject(namespace+ ".getGroupByNameAndModuleId", params);
			
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getGroupByNameAndModuleId from database error",e);			
		}	
		
		return null;		
	}
	
    public static Group getGroupById(Integer id){
		
	   try {
			return (Group) sqlMapClient.queryForObject(namespace+ ".getGroupById", id);
			
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getGroupById from database error",e);			
		}	
		
		return null;		
	}
    
    public static Integer insertGroup(Group group) {

		Integer id = null;

		try {
			id = (Integer) sqlMapClient.insert(namespace + ".insert",group);
		} catch (SQLException e) {
			LOGGER.error("insertPipelineDef db error", e);
		}
		return id;
	}
    
    public static Integer deleteGroup(String name,Integer module_id) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("module_id", module_id);


		try {
			return (Integer) sqlMapClient.delete(namespace + ".deleteGroup",params);
		} catch (SQLException e) {
			LOGGER.error("deleteUserToGroup db error", e);
		}
		return null;
	}
}
