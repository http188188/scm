package com.sina.scm.data.dao;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.sina.scm.data.DO.ReleaseType;

public class ReleaseTypeDAOImpl {

	private static final String namespace = "releaseType";
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger
			.getLogger(ReleaseTypeDAOImpl.class.getName());

	static {
		try {
			Reader reader = Resources
					.getResourceAsReader("com/sina/scm/data/dao/SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			LOGGER.error("ReleaseTypeDAOImpl db error", e);
		}
	}
	
    @SuppressWarnings("unchecked")
	public static List<ReleaseType> getAllReleaseType(){
    	
    	List<ReleaseType> releaseType = new ArrayList<ReleaseType>();
    	
    	try {
    		releaseType = (List<ReleaseType>)sqlMapClient.queryForList(namespace + ".getAllReleaseType");
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getAllReleaseType from database error", e);
		}
    	
    	return releaseType;
    }
	
}
