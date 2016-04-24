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
import com.sina.scm.data.vo.Menu;
import com.sina.scm.data.vo.User;

public class UserDAOImpl{
	
	private static final String namespace = "user";	
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class.getName());

	static {
		try {
			Reader reader = Resources
					.getResourceAsReader("com/sina/scm/data/dao/SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			LOGGER.error("UserDAOImpl db error", e);
		}
	}
	
	//根据module_Id查找自己所包含的组
	@SuppressWarnings("unchecked")
	public static List<User> getUserByGroupId(Integer group_id) {

		try {
			return (List<User>) sqlMapClient.queryForList(namespace+ ".getUserByGroupId", group_id);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getUserByGroupId from database error", e);
		}

		return null;

	}
	
	public static Integer insertUserToGroup(User user) {

		Integer id = null;

		try {
			id = (Integer) sqlMapClient.insert(namespace + ".insert",user);
		} catch (SQLException e) {
			LOGGER.error("insertUserToGroup db error", e);
		}
		return id;
	}
	
	public static Integer deleteUserToGroup(String name,Integer group_id) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("group_id", group_id);


		try {
			return (Integer) sqlMapClient.delete(namespace + ".deleteUserToGroup",params);
		} catch (SQLException e) {
			LOGGER.error("deleteUserToGroup db error", e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<User> getAllGroupByName(String name){
		
		try {
			return (List<User>) sqlMapClient.queryForList(namespace+ ".getAllGroupByName", name);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getAllGroupByName from database error", e);
		}

		return null;		
	}	
	
	@SuppressWarnings("unchecked")
   public static List<Menu> getSCMMenu(){
		
		try {
			return (List<Menu>) sqlMapClient.queryForList(namespace+ ".getSCMMenu");

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getSCMMenu from database error", e);
		}

		return null;		
	}	
	
	@SuppressWarnings("unchecked")
   public static List<Menu> getNoSCMMenu(){
		
		try {
			return (List<Menu>) sqlMapClient.queryForList(namespace+ ".getNoSCMMenu");

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getNoSCMMenu from database error", e);
		}

		return null;		
	}	
	
	public static Integer deleteUserFromGroup(Integer group_id) {
		try {
			return (Integer) sqlMapClient.delete(namespace + ".deleteUserFromGroup",group_id);
		} catch (SQLException e) {
			LOGGER.error("deleteUserFromGroup db error", e);
		}
		return null;
	}
	
}
