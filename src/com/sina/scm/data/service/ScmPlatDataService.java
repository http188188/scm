package com.sina.scm.data.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sina.scm.data.dao.ModuleDAOImpl;
import com.sina.scm.data.dao.ModuleVersionDAOImpl;
import com.sina.scm.data.dao.RollBackDAOImpl;
import com.sina.scm.data.util.DateUtils;
import com.sina.scm.data.util.ScmDataServiceResult;
import com.sina.scm.data.util.SendMailUtils;
import com.sina.scm.data.vo.ModuleInfo;
import com.sina.scm.data.vo.ModuleVersion;
import com.sina.scm.data.vo.ReleaseAndRollbackVO;
import com.sina.scm.data.vo.RollBack;

@Path("scmPlatDataService")
public class ScmPlatDataService {

	private static final Logger LOGGER = Logger.getLogger(ScmPlatDataService.class.getName());
	private static final String FAIL_CODE = "moduleInfo is fail";
	private static final String SUCE_CODE = "moduleInfo is sucess";
	private static final String ONLINE = "release_yes";
	private static final String ROLLBACK = "rollback_yes";
	
	
	
	
	
	
	
	
	
	
	@Path("/releaseAndRollBackNumByModuleId")
	@GET
	@Produces("text/plain")
	public String releaseAndRollBackNumByModuleId(@QueryParam("callback") String callback,
			@QueryParam("moduleId") Integer moduleId) {
		
		ReleaseAndRollbackVO releaseAndRollbackVO = new ReleaseAndRollbackVO();
		//1.算出当前是哪一年
		Calendar a = Calendar.getInstance();
		String current_year = String.valueOf(a.get(Calendar.YEAR));
		//2.算出这一年的，每个月的上线和回滚次数
		releaseAndRollbackVO.setYear(current_year);
		releaseAndRollbackVO.setTotal_release(ModuleVersionDAOImpl
				.releaseAndRollBackNumBymoduleId(current_year,moduleId));
		releaseAndRollbackVO.setTotal_rollback(RollBackDAOImpl
				.releaseAndRollBackNumBymoduleId(current_year,moduleId));
		//3.每个月的统计
		 String month_num = "";
		 List<Integer> month_release = new ArrayList<Integer>();
		 List<Integer> month_rollback = new ArrayList<Integer>();
		for (int i = 1; i <= 12; i++) {
			if (i <= 9) {
				month_num = current_year+"-0" + i;
				
			} else {
				month_num = current_year+"-" + i;
			}

			month_release.add(ModuleVersionDAOImpl.releaseAndRollBackNumBymoduleId(month_num,moduleId));
			month_rollback.add(RollBackDAOImpl.releaseAndRollBackNumBymoduleId(month_num,moduleId));
		}
		
		releaseAndRollbackVO.setMonth_release(month_release);
		releaseAndRollbackVO.setMonth_rollback(month_rollback);
		
		return callback+"("+JSONObject.fromObject(releaseAndRollbackVO).toString()+")";				
		
	}	
	
	@Path("/releaseAndRollBackNum")
	@GET
	@Produces("text/plain")
	public String releaseAndRollBackNum(@QueryParam("callback") String callback) {

		List<ReleaseAndRollbackVO> releaseAndRollbackVOs = new ArrayList<ReleaseAndRollbackVO>();
		// 1.算出当前是哪一年
		Calendar a = Calendar.getInstance();
		String year1 = String.valueOf(a.get(Calendar.YEAR));
		String year2 = String.valueOf(a.get(Calendar.YEAR) - 1);

		String y = year1 + ";" + year2;

		String[] yy = y.split(";");
		for (int j = 0; j < yy.length; j++) {
			String current_year = yy[j];

			// 2.算出每一年的总的上线次数和回滚次数
			ReleaseAndRollbackVO current_year_num = new ReleaseAndRollbackVO();
			current_year_num.setYear(current_year);
			current_year_num.setTotal_release(ModuleVersionDAOImpl
					.releaseAndRollBackNum(current_year));
			current_year_num.setTotal_rollback(RollBackDAOImpl
					.releaseAndRollBackNum(current_year));
			String month_num = "";
			 List<Integer> month_release = new ArrayList<Integer>();
			 List<Integer> month_rollback = new ArrayList<Integer>();
			for (int i = 1; i <= 12; i++) {
				if (i <= 9) {
					month_num = current_year+"-0" + i;
					
				} else {
					month_num = current_year+"-" + i;
				}

				month_release.add(ModuleVersionDAOImpl.releaseAndRollBackNum(month_num));
				month_rollback.add(RollBackDAOImpl.releaseAndRollBackNum(month_num));

			}
			
			current_year_num.setMonth_release(month_release);
			current_year_num.setMonth_rollback(month_rollback);
			
			
			releaseAndRollbackVOs.add(current_year_num);
		}
		
		return callback+"("+JSONArray.fromObject(releaseAndRollbackVOs).toString()+")";	

	}	
	
	@Path("/moduleInfo")
	@POST
	@Produces("text/plain")
	public String getMachineApply(String moduleInfo) {

		// 1.check param
		if (StringUtils.isBlank(moduleInfo)) {
			LOGGER.info("moduleInfo is null!");
			return FAIL_CODE;
		}
		LOGGER.info("the moduleInfo json is:" + moduleInfo);
		// 2.String module change to json object
		JSONObject jsonObject = JSONObject.fromObject(moduleInfo);
		ModuleInfo module = (ModuleInfo) JSONObject.toBean(jsonObject,
				ModuleInfo.class);
		// 3.check pust_name number
		String push_name = module.getPush_list();
		if (StringUtils.isBlank(push_name)) {
			LOGGER.info("push_name is null!");
			return FAIL_CODE;
		}
		int push_name_num = ModuleDAOImpl.getPushNameNum(push_name);
		LOGGER.info("push_name num  is:" + push_name_num);
		if (1 != push_name_num) {
			// 4.not 1 need send email
			SendMailUtils.sendMail(module,push_name,String.valueOf(push_name_num),"pushNameNum");
			return FAIL_CODE;
		}
		// 5.is 1 continue other business to do		
		continueModuleVersionOrRollback(module,push_name);
		return SUCE_CODE;				
	}	
	
	//check module current_online_version
	@Path("/updateModuleCurrentOnlineVersion")
	@GET
	@Consumes("text/plain")
	public String updateModuleCurrentOnlineVersion() {
		
		StringBuffer bf = new StringBuffer();
		//1.获取current_online_version为null的id
		List<Integer> ids = ModuleDAOImpl.getIdbyVersionIsNull();
		if(CollectionUtils.isEmpty(ids)){
			return FAIL_CODE;			
		}
		for(int id:ids){
			//2.找出最近一次的版本进行更新module表
			String version = ModuleVersionDAOImpl.getLatestModuleVersionById(id);
			bf.append("id is:"+id+" and the latestversion is: "+version);
			bf.append("\r\n");
			if(StringUtils.isNotBlank(version)){
			//3.更新module表
			ModuleDAOImpl.updateModuleOnlineVersion(version,DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"),id);
			}								
		}
		return bf.toString();
	}
	
	//get the module currentVerison 
	@Path("/getModuleCurrentVerison")
	@GET
	@Produces("text/plain")
	public String getModuleCurrentVerison(@QueryParam("push_list") String push_list) {
		
		//1.check push_list
		String check_result = check_push_name(push_list);
		if (!ScmDataServiceResult.CHECK_PUSH_NAME_SUCESS.equals(check_result)){
			return check_result;
		}
		//2.get online_version
		String version = ModuleDAOImpl.getModuleCurrentVersion(push_list);
		if(StringUtils.isBlank(version)){
			
			return "module verison is empty,please check";
		}
		return version;		
	}
	
	//get the module currentVerison 
	@Path("/getModuleHistoryVerison")
	@GET
	@Produces("text/plain")
	public String getModuleHistoryVerison(@QueryParam("push_list") String push_list) {

		StringBuffer all_versions = new StringBuffer();
		// 1.check push_list
		String check_result = check_push_name(push_list);
		if (!ScmDataServiceResult.CHECK_PUSH_NAME_SUCESS.equals(check_result)) {
			return check_result;
		}
		// 2.get the module_id
		int module_id = ModuleDAOImpl.getModuleId(push_list);
		// 3.get all history online version by module_id
		List<String> versions = ModuleVersionDAOImpl
				.getHistoryVersionByModuleId(module_id);
		if (CollectionUtils.isNotEmpty(versions)) {

			for (String version : versions) {

				all_versions.append(version).append("\n");

			}

		}

		return all_versions.toString();

	}
	
	//check push_name
	private String check_push_name(String push_list) {

		// 1.check push_list
		if (StringUtils.isBlank(push_list)) {

			return "push list is empty";
		}
		// 2.get num by push_list
		int push_name_num = ModuleDAOImpl.getPushNameNum(push_list);
		if (1 != push_name_num) {

			return "the module version's num isn't 1";
		}
		// 3.check ok
		return ScmDataServiceResult.CHECK_PUSH_NAME_SUCESS;

	}
	//insert moduleVersion or rollback info
	private void continueModuleVersionOrRollback(ModuleInfo module,String push_name) {

		int module_id = ModuleDAOImpl.getModuleId(push_name);
		LOGGER.info("push_name is :"+push_name+"  and the module_id is: "+module_id);
		// 1.check release or rollback
		if (StringUtils.isNotBlank(module.getOperate_type())) {
			LOGGER.info("getOperate_type is:" + module.getOperate_type());
			if (ONLINE.equalsIgnoreCase(module.getOperate_type())) {
				// online business
				// a.insert moduleVerison
				ModuleVersion moduleVersion = new ModuleVersion();
				moduleVersion.setModule_id(module_id);
				moduleVersion.setQb_job_url(module.getQb_job_url());
				moduleVersion.setVersion(module.getVersion());
				moduleVersion.setStatus("success");
				moduleVersion.setRelease_type(module.getRelease_type());
				moduleVersion.setRelease_date(DateUtils.getJobBeginDate(module
						.getBeginDate()));
				moduleVersion.setOperator(module.getOperator());				
				String tag_path = module.getTag_path();
				String checkCmd = "";
				if(tag_path.endsWith(".git")){
					// git clone
					checkCmd = "git clone "+module.getTag_path()+"; git checkout "+module.getVersion();
					
				}else{
					//svn checkout
					checkCmd = "svn checkout " + module.getTag_path() + "/"
							+ module.getVersion();
				}				
				moduleVersion.setCheckout_command(checkCmd);
				// b.version unique
				if (0 == ModuleVersionDAOImpl
						.getVersionNum(module.getVersion())) {
					ModuleVersionDAOImpl.insertModuleVersion(moduleVersion);
					LOGGER.info("insertModuleVersion is succeess");
				}else{
					//c.has many version num sendmail to change
					SendMailUtils.sendMail(module,push_name,module.getVersion(),"versionNum");	
					//d.update module_version by version and module_id
					if("online".equalsIgnoreCase(module.getRsyncType())){
						ModuleVersionDAOImpl.updateModuleVersionByVersionAndModuleId(moduleVersion);
					}										
				}				
				// b.update module current_online_version
				if ("online".equalsIgnoreCase(module.getRsyncType())) {
					ModuleDAOImpl.updateModuleOnlineVersion(module.getVersion(),DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss") ,module_id);
					LOGGER.info("updateModuleOnlineVersion is succeess");
				}				

			} else if (ROLLBACK.equalsIgnoreCase(module.getOperate_type())) {
				// rollback business
				// a.insert table rollback
				RollBack rollback = new RollBack();
				rollback.setModule_id(module_id);
				rollback.setFrom_version(module.getOnline_tag_url());
				rollback.setTo_version(module.getRelease_tag_url());
				rollback.setQb_job_url(module.getQb_job_url());
				rollback.setOperator(module.getOperator());
				rollback.setRollback_date(DateUtils.getJobBeginDate(module.getBeginDate()));
				if (0 == RollBackDAOImpl.getQbJobUrlNum(module.getQb_job_url())) {
					RollBackDAOImpl.insertRollBack(rollback);
					LOGGER.info("insertRollBack is succeess");
					// b.update module current_online_version
					if("online".equalsIgnoreCase(module.getRsyncType())){
						ModuleDAOImpl.updateModuleOnlineVersion(module.getRelease_tag_url(),DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss") ,module_id);
						LOGGER.info("updateModuleOnlineVersion is succeess");
					}					
				}
			}
		}
	}			
	
	public static void main(String[] args) {
		
		Calendar a = Calendar.getInstance();
		String year1 = String.valueOf(a.get(Calendar.YEAR));
		String year2 = String.valueOf(a.get(Calendar.YEAR) - 1);
		
		System.out.print(year1+";"+year2);
		
	}
	
}