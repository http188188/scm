package com.sina.scm.data.dao;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.sina.scm.data.vo.RollBack;

public class RollBackDAOImpl{
	
	private static final String namespace = "rollback";	
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger.getLogger(RollBackDAOImpl.class.getName());

	static {
		try {
			Reader reader = Resources
					.getResourceAsReader("com/sina/scm/data/dao/SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			LOGGER.error("RollBackDAOImpl db error", e);
		}
	}
	
	//insert table module_version 
	public static void insertRollBack(RollBack rollBack) {

		try {
			sqlMapClient.insert(namespace + ".insert",rollBack);
		} catch (SQLException e) {
			LOGGER.error("RollBackDAOImpl db error", e);
		}

	}
	
	//count QbJobUrlNum num
	public static int getQbJobUrlNum(String qb_job_url) {
			
			try {
				return (Integer)sqlMapClient.queryForObject(namespace + ".getQbJobUrlNum",qb_job_url);
			} catch (SQLException e) {
				e.printStackTrace();
				LOGGER.error("getQbJobUrlNum db error", e);
				return -1;
			}
			
		}
	
	public static int getRollBackNumByDay(String release_date) {

		Integer getRollBackNumByDay = -1;

		try {
			getRollBackNumByDay = (Integer) sqlMapClient.queryForObject(
					namespace + ".getRollBackNumByDay", release_date);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getRollBackNumByDay from database error", e);
		}

		return getRollBackNumByDay;

	}
	
	public static RollBack getRollBackByToVersion(String to_version) {

		try {
			return (RollBack) sqlMapClient.queryForObject(namespace
					+ ".getRollBackByToVersion", to_version);

		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getRollBackByToVersion from database error", e);
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
	 
	 public static Integer releaseAndRollBackNumBymoduleId(String rollback_date,Integer module_id) {
         
		    Map<String, Object> params = new HashMap<String, Object>();
			params.put("rollback_date", rollback_date);
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
