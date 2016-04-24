package com.sina.scm.data.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertiesUtils {
	private static final String fileName = "/scm_plat_data.conf";
	private static Properties properties;
	static {
		InputStream in = PropertiesUtils.class.getResourceAsStream(fileName);
		if (in != null) {
			try {
				properties = new Properties();
				properties.load(in);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				properties = null;
			}
		}
	}
	/**
	 * ���ߺ����ȡָ�������ļ��е�ָ��������ֵ������ļ�������/key�����ڣ��򷵻�null 
	 * @param key
	 * @return
	 */
	public static String getConfValue(String key){
		if (properties == null) {
			return "";
		} else {
			String value = properties.getProperty(key);
			if (value != null) {
				return value;
			} else {
				return "";
			}
		}
	}
}
