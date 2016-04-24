package com.sina.scm.data.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeUtility;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;

public class SendMail extends Thread {
	private String content = "";
	private List<String> addressList = null;
	private String subject = "";
	private List<String> ccList;
	private File imgFile = null;
	private String attachmentPath = "";
	private String attachmentName = "";

	public SendMail(List<String> addressList, List<String> ccList, String subject, String content) {
		this.content = content;
		this.addressList = addressList;
		this.subject = subject;
		this.ccList = ccList;
	}

	public SendMail(List<String> addressList, List<String> ccList, String subject, String content, File imgFile, String attachmentPath, String attachmentName) {
		this.content = content;
		this.addressList = addressList;
		this.subject = subject;
		this.ccList = ccList;
		this.imgFile = imgFile;
		this.attachmentPath = attachmentPath;
		this.attachmentName = attachmentName;
	}

	public void run() {
		HtmlEmail mail = new HtmlEmail();
		mail.setCharset("GBK");
		try {
			String defaultToAddress = PropertiesUtils.getConfValue("default_to_address").trim();
			if (!defaultToAddress.endsWith("@staff.weibo.com")) {
				defaultToAddress = defaultToAddress + "@staff.weibo.com";
			}
			String smtp = PropertiesUtils.getConfValue("smtp_server").trim();
			String from = PropertiesUtils.getConfValue("from_address").trim();
			if (imgFile != null) {
				String cid = mail.embed(imgFile);
				content = content + "<br/>" + "<img src=\"cid:" + cid + "\">";
			}
			if (attachmentPath != null && !attachmentPath.equals("")) {
				EmailAttachment attachment = new EmailAttachment();
				attachment.setPath(attachmentPath);
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				if (attachmentName == null || attachmentName.equals("")) {
					attachmentName = attachmentPath.substring(attachmentPath.lastIndexOf(File.separator) + 1);
				}
				attachment.setDescription(attachmentName);
				attachment.setName(MimeUtility.encodeText(attachmentName));
				mail.attach(attachment);
			}
			mail.setSubject(subject);
			mail.setMsg(content);

			if (addressList == null)
				addressList = new ArrayList<String>();
			if (addressList != null && addressList.size() > 0) {
				for (int i = 0; i < addressList.size(); i++) {
					if (addressList.get(i) != null && !addressList.get(i).equals(""))
						mail.addTo((String) addressList.get(i));
				}
			} else {
				mail.addTo(defaultToAddress);

			}
			mail.setHostName(smtp);
			mail.setFrom(from);

			if (ccList != null && ccList.size() > 0) {
				for (int i = 0; i < ccList.size(); i++) {
					if (ccList.get(i) != null)
						mail.addCc((String) ccList.get(i));
				}
			}
			mail.send();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
