package com.sina.scm.data.util;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.log4j.Logger;

public class HttpUtils {
	
	private static final Logger LOGGER = Logger
			.getLogger(HttpUtils.class.getName());
	
	private static final int timeoutInSec = 60;
	
	public static String HttpGet2(String url) throws Exception{
	//	String url = "http://apo2.jpool.intra.sina.com.cn/api/openapi/scheduler/interface.php?param={\"action\":\"get_pools_by_identifier\",\"data\":{\"identifier\":\"\"},\"module\":\"shell\"}";
		URI uri = parseUrI(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);
		HttpResponse httpResponse = httpClient.execute(httpGet);
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		while ((inputLine = reader.readLine()) != null) {
			response.append(inputLine);
		}
		reader.close();
		return response.toString();
		
	}
	public static URI parseUrI(String s) throws Exception {
	     URL u = new URL(s);
	     return new URI(
	            u.getProtocol(),
	            u.getAuthority(), 
	            u.getPath(), 
	            u.getQuery(),
	            u.getRef());
	}
	public static void HttpGet(String url) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, timeoutInSec * 1000); //设置请求超时
		httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, timeoutInSec * 1000); //设置连接超时
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpGet);
		HttpEntity resEntity = response.getEntity();
		if (resEntity == null) {
			LOGGER.info("response is null,and return");
			return;
		}
		InputStreamReader reader = new InputStreamReader(resEntity.getContent());
		char[] buff = new char[1024];
		int length = 0;
		while ((length = reader.read(buff)) != -1) {
			LOGGER.info(new String(buff, 0, length));
		}
		httpclient.getConnectionManager().shutdown(); // 关闭连接,释放资源
		reader.close();
	}
	
	public static InputStreamReader HttpGetToReader(String url) throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 5*timeoutInSec * 1000); 
		httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5*timeoutInSec * 1000); 
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpGet);
		HttpEntity resEntity = response.getEntity();
		if (resEntity == null) {
			LOGGER.info("response is null,and return");
			return null;
		}
		InputStreamReader reader = new InputStreamReader(resEntity.getContent(),"UTF-8");
		char[] buff = new char[1024];
		int length = 0;
		while ((length = reader.read(buff)) != -1) {
			LOGGER.info(new String(buff, 0, length));
		}
		httpclient.getConnectionManager().shutdown(); 
		reader.close();
	    return reader;
	}
	
	public static String HttpPost(String url, String xml) throws Exception {

		LOGGER.info(xml);
		LOGGER.info(url);
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, timeoutInSec * 1000);
		httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, timeoutInSec * 1000);
		HttpPost httppost = new HttpPost(url);
		StringEntity myEntity = new StringEntity(xml);
		httppost.addHeader("Content-Type", "application/xml");
		httppost.setEntity(myEntity);
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();
		if (resEntity == null) {
			LOGGER.info("response is null,and return");
			return "";
		}
		StringBuffer sb = new StringBuffer();
		InputStreamReader reader = new InputStreamReader(resEntity.getContent());
		char[] buff = new char[1024];
		int length = 0;
		while ((length = reader.read(buff)) != -1) {
			sb.append(new String(buff, 0, length));
		}
		httpclient.getConnectionManager().shutdown();
		reader.close();
		return sb.toString();
	}	
	
	 /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        LOGGER.info("sendPostBegin");
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            LOGGER.info(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            LOGGER.info(line);
        } catch (Exception e) {
        	LOGGER.info("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
	

	public static void HttpDelete(String url) throws Exception {

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, timeoutInSec * 1000);
		httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, timeoutInSec * 1000);
		HttpDelete httpDelete = new HttpDelete(url);

		HttpResponse response = httpclient.execute(httpDelete);
		HttpEntity resEntity = response.getEntity();
		if (resEntity == null) {
			LOGGER.info("response is null,and return");
			return;
		}
		InputStreamReader reader = new InputStreamReader(resEntity.getContent());
		char[] buff = new char[1024];
		int length = 0;
		while ((length = reader.read(buff)) != -1) {
			LOGGER.info(new String(buff, 0, length));
		}
		httpclient.getConnectionManager().shutdown();
		reader.close();
	}
	
	public static String readFile(String filePath, String charset) {
		String content = null;
		FileInputStream fin = null;
		BufferedReader br = null;
		try {
			StringBuffer sb = new StringBuffer();
			fin = new FileInputStream(filePath);
			br = new BufferedReader(new InputStreamReader(fin, charset));
			ByteBuffer bf = ByteBuffer.allocate(10240);
			CharBuffer cb = bf.asCharBuffer();
			while (br.read(cb) > -1) {
				cb.flip();
				sb.append(cb.toString());
			}
			content = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
					fin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}
	
	public static String HttpPost(String url, int timeout) throws Exception {
		LOGGER.info("setPlatformParame10");
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, timeout * 1000);
		httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, timeout * 1000);
		HttpPost httppost = new HttpPost(url);
		LOGGER.info("setPlatformParame11");
		HttpResponse response = httpclient.execute(httppost);
		LOGGER.info("setPlatformParame12");
		HttpEntity resEntity = response.getEntity();
		if (resEntity == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		InputStreamReader reader = new InputStreamReader(resEntity.getContent());
		char[] buff = new char[1024];
		int length = 0;
		while ((length = reader.read(buff)) != -1) {
			sb.append(new String(buff, 0, length));
		}
		httpclient.getConnectionManager().shutdown();
		reader.close();
		return sb.toString();
	}
	
	
	public static String HttpPost(String url) throws Exception {
		return HttpPost(url, timeoutInSec);
	}
	
	public static int HttpHead(String url) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, timeoutInSec * 1000);
			httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, timeoutInSec * 1000);
			HttpHead httpHead = new HttpHead(url);
			HttpResponse response = httpclient.execute(httpHead);
			int statusCode = response.getStatusLine().getStatusCode();
			return statusCode;
		} catch (Exception ex) {
			return -1;
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		System.out.print(HttpPost("http://platform.ci.intra.weibo.com/jenkins/createItem?name=jintao&mode=copy&from=platfrom_ci_openflow_preview_env"));
	}
}
