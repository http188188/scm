package com.sina.scm.data.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.sina.scm.data.vo.ModuleInfo;

public class SendMailUtils {
	
	public static void sendMailWithCCToDefault(String mailTo, String title, String content) {
		String ccTo = PropertiesUtils.getConfValue("ccto").trim();
		sendMail(mailTo, ccTo, title, content, null, "", "");
	}
	
	public static void sendMailWithDefaultCC(String mailTo, String title, String content) {
		String ccTo = PropertiesUtils.getConfValue("ccto").trim();
		sendMail(mailTo, ccTo, title, content,  null, "", "");
	}
	
	public static void sendMailWithDefaultCCWithImgAndAttachment(String mailTo, String title, String content, File imgFile, String attachmentPath, String attachmentName) {
		String ccTo = PropertiesUtils.getConfValue("ccto").trim();
		sendMail(mailTo, ccTo, title, content, imgFile, attachmentPath, attachmentName);
	}
	
	public static void sendMailWithCCAndDefaultCC(String mailTo, String ccTo, String title, String content) {
		String defaultCCTo = PropertiesUtils.getConfValue("ccto").trim();
		ccTo = ccTo + ";" + defaultCCTo;
		sendMail(mailTo, ccTo, title, content, null, "", "");
	}
	
	public static void sendMail(String mailTo, String ccTo, String title, String content, File imgFile, String attachmentPath, String attachmentName) {
		List<String> mailToList = new ArrayList<String>();
		if(mailTo != null && !mailTo.equals("") && !mailTo.equals("null")) {
			StringTokenizer st = new StringTokenizer(mailTo, ";");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (token.equals("")) {
					continue;
				}
				if (!token.endsWith("@staff.weibo.com")) {
					token = token + "@staff.weibo.com";
				}
				mailToList.add(token);
			}
		}
		List<String> ccToList = new ArrayList<String>();
		if(ccTo != null && !ccTo.equals("")) {
			StringTokenizer st = new StringTokenizer(ccTo, ";");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (token.equals("")) {
					continue;
				}
				if (!token.endsWith("@staff.weibo.com")) {
					token = token + "@staff.weibo.com";
				}
				ccToList.add(token);
			}
		}
		SendMail sendMail = new SendMail(mailToList, ccToList, title, content, imgFile, attachmentPath, attachmentName);
		sendMail.start();
	}
	
	//send email
		public static void sendMail(ModuleInfo module, String push_name, String content,String alarmType) {
		
			String send_email_cmd_head = "";
			if("pushNameNum".equalsIgnoreCase(alarmType)){
				
				send_email_cmd_head = "sh " + "-x "+ "/data1/shell/sendMail.sh ";
				
			}else if("versionNum".equalsIgnoreCase(alarmType)){
				
				send_email_cmd_head = "sh " + "-x "+ "/data1/shell/sendVesion.sh ";
			}
		
			String send_email_cmd = send_email_cmd_head + push_name + " "+ String.valueOf(content) + " " + module.getQb_job_url();
		
			String[] sendFailInfo = new String[] { "/bin/bash", "-c",send_email_cmd };
			SSHExecutor.runLocalCommand(sendFailInfo);
		}	

}
