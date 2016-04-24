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
import com.sina.scm.data.DO.PiplineDef;
import com.sina.scm.data.DO.Template;

public class TemplateDAOImpl {
	private static final String namespace = "template";
	private static SqlMapClient sqlMapClient = null;
	private static final Logger LOGGER = Logger
			.getLogger(TemplateDAOImpl.class.getName());

	static {
		try {
			Reader reader = Resources
					.getResourceAsReader("com/sina/scm/data/dao/SqlMapConfig.xml");
			sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close();
		} catch (IOException e) {
			LOGGER.error("TemplateDAOImpl db error", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Template> getTemplateList() {

		try {
			return (List<Template>) sqlMapClient.queryForList(namespace
					+ ".getTemplateList");
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getTemplateList from database error", e);
		}
		return null;
	}
	public static Template getTemplateById(Integer templateId) {

		try {
			return (Template) sqlMapClient.queryForObject(namespace
					+ ".getTemplateById", templateId);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.info("getTemplateById from database error", e);
		}
		return null;
	}
}
