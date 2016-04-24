package com.sina.scm.data.dao;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.sina.scm.data.vo.Scm_info;


public class Scm_infoDAOImpl{
	
	private static final String namespace = "scm_info";	
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger.getLogger(Scm_infoDAOImpl.class.getName());

	static {
		try {
			Reader reader = Resources
					.getResourceAsReader("com/sina/scm/data/dao/SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			LOGGER.error("Scm_infoDAOImpl db error", e);
		}
	}
	
	public static Integer insertScm_info(Scm_info scm_info) {

		Integer id = null;

		try { 
			id = (Integer) sqlMapClient.insert(namespace + ".insert",scm_info);
		} catch (SQLException e) {
			LOGGER.error("insertScm_info db error", e);
		}
		return id;
	}	
	
	public static void updateSCM(Scm_info scm_info) {

		try {
			sqlMapClient.update(namespace + ".updateSCM", scm_info);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("updateSCM from database error", e);
		}

	}
	
	//根据模块ID查询 tool_type(git还是svn)和code_url(git或svn地址)
	@SuppressWarnings("unchecked")
	public static List<Scm_info> getModuleTypeAndUrlById(Integer module_id){
		
		try {
			return (List<Scm_info>) sqlMapClient.queryForList(namespace+ ".getModuleTypeAndUrlById", module_id);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getModuleTypeAndModuleUrlByModuleId from database error", e);
		}

		return null;		
	}
	public static Scm_info getModuleIdByCodeUrl(String code_url){
		
		try {
			return (Scm_info) sqlMapClient.queryForObject(namespace+ ".getModuleIdByCodeUrl", code_url);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getModuleIdByCodeUrl from database error", e);
		}

		return null;		
	}
	
}
