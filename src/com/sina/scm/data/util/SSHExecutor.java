package com.sina.scm.data.util;

import ch.ethz.ssh2.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 和ssh相关的工具类
 * @author jintao3
 *
 */

public class SSHExecutor {
	

	public static final int SSHEXECUTOR_SUCCESS = 0;
	public static final int SSHEXECUTOR_FAIL = 1;
	private static int connectTimeout = 120000;
	private static int connectEstablishTimeout = 2000;
	private static int execTimeout = 1000 * 60 * 60;
	private static final Logger LOGGER = Logger
			.getLogger(SSHExecutor.class.getName());
	
	/**
	 * Run SSH command.
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @param cmd
	 * @return exit status
	 * @throws IOException
	 */
	public static int runSSH(String host, String username, String password, String cmd) throws Exception {

		Connection conn = getOpenedConnection(host, username, password);
		Session sess = conn.openSession();
		sess.execCommand(cmd);

		InputStream stdout = new StreamGobbler(sess.getStdout());
		BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

		while (true) {
			// attention: do not comment this block, or you will hit
			// NullPointerException
			// when you are trying to read exit status
			String line = br.readLine();

			if (line == null)
				break;
		}
		sess.waitForCondition(ChannelCondition.EXIT_STATUS, execTimeout);
		stdout.close();
		br.close(); 
		Integer result = sess.getExitStatus();
		sess.close();
		conn.close();
		if (result == null) {
			return SSHEXECUTOR_SUCCESS;
		}
		return result;
	}
	
	public static void runSSH1(String host, String username, String password, String cmd) throws Exception {

		Connection conn = getOpenedConnection(host, username, password);
		Session sess = conn.openSession();
		sess.execCommand(cmd);
		sess.close();
		conn.close();
	}
	
   	/**
	 * return a opened Connection
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 */
	private static Connection getOpenedConnection(String host, String username, String password) throws Exception {

		Connection conn = new Connection(host);
		conn.connect(null, connectTimeout, connectEstablishTimeout);
		boolean isAuthenticated = conn.authenticateWithPassword(username, password);
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");
		return conn;
	}

	public static boolean loginSuccess(String host, String username, String password) {
		try {
			Connection conn = new Connection(host);
			conn.connect(null, connectTimeout, connectEstablishTimeout);
			boolean isAuthenticated = conn.authenticateWithPassword(username, password);
			conn.close();
			return isAuthenticated;
		} catch (Exception ex) {
			return false;
		}
	}

	public static int runLocalCommand(String[] command) {
		Process p = null;
		try {
			ProcessBuilder pb = new ProcessBuilder(command);
			pb.redirectErrorStream(true);
			LOGGER.info("redirectErrorStream end");
			p = pb.start();
			LOGGER.info("pb.start() end");
			readFromStream(p.getInputStream());
			LOGGER.info("readFromStream end");
			int exitValue = p.waitFor();
			LOGGER.info("exitValue is:"+exitValue);
			return exitValue;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				if (p != null) {
					p.getOutputStream().close();
					p.getInputStream().close();
					p.getErrorStream().close();
					p.destroy();
				}
			} catch (Exception ex) {
				
				ex.printStackTrace();
			}
		}
		return -1;
	}
	public static String readFromStream(InputStream inputStream) {
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(inputStream);
			br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String line = br.readLine();
			while (line != null) {
				if (!line.equals("")) {
					try {
						sb.append(line + "\n");
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				line = br.readLine();
			}
			LOGGER.info("readFromStream is:"+sb.toString());
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return "";
	}
	
	
	public static String runSSHWithOutput(String host, String username, String password, String cmd) throws Exception {

		Connection conn = getOpenedConnection(host, username, password);
		Session sess = conn.openSession();
		sess.execCommand(cmd);

		InputStream stdout = new StreamGobbler(sess.getStdout());
		BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
		StringBuffer sb = new StringBuffer();
		while (true) {
			// attention: do not comment this block, or you will hit
			// NullPointerException
			// when you are trying to read exit status
			String line = br.readLine();
			if (line != null) {
				sb.append(line);
			} else {
				break;
			}
		}
		sess.waitForCondition(ChannelCondition.EXIT_STATUS, execTimeout);
		br.close();
		Integer result = sess.getExitStatus();
		sess.close();
		conn.close();
		if (result == null || result == 0) {
			return sb.toString();
		}
		return "";
	}
}
