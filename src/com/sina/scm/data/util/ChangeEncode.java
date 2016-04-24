package com.sina.scm.data.util;

import java.io.UnsupportedEncodingException;

public class ChangeEncode {
	
	public static String changEncode(String code){
		
		String strDefaltEncode = "";
		try {
			strDefaltEncode = new String(code.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
	    return strDefaltEncode;
	}
}
