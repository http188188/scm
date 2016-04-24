package com.sina.scm.data.util;

import com.sina.scm.data.DO.DeployTypePlatform;
import com.sina.scm.data.DO.Module;
import com.sina.scm.data.DO.NodeCase;
import com.sina.scm.data.DO.NodeDef;
import com.sina.scm.data.DO.PiplineCase;
import com.sina.scm.data.DO.PiplineDef;
import com.sina.scm.data.DO.PiplineNodeDef;
import com.sina.scm.data.dao.DeployTypePlatformDAOImpl;
import com.sina.scm.data.dao.GroupDAOImpl;
import com.sina.scm.data.dao.ModuleDAOImpl;
import com.sina.scm.data.dao.ModuleVersionDAOImpl;
import com.sina.scm.data.dao.NodeCaseDAOImpl;
import com.sina.scm.data.dao.NodeDefDAOImpl;
import com.sina.scm.data.dao.PiplineCaseDAOImpl;
import com.sina.scm.data.dao.PiplineDefDAOImpl;
import com.sina.scm.data.dao.PiplineNodeDefDAOImpl;
import com.sina.scm.data.dao.UserDAOImpl;
import com.sina.scm.data.vo.CreatePiplineVO;
import com.sina.scm.data.vo.Group;
import com.sina.scm.data.vo.User;

import java.util.ArrayList;
import java.util.Arrays; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class PiplineUtils {
	
	private static final Logger LOGGER = Logger
			.getLogger(PiplineUtils.class.getName());
	
	//检查用户是否可以启动pipline
	public static String whetherToStart(String user,Integer piplineDefId){
		
		//1.根据piplineDefId获取pipeline_owner
		PiplineDef pdef = PiplineDefDAOImpl.getPiplineByDefId(piplineDefId);
		if(null == pdef){
			return "-1";
		}
		//2.获取pipeline_owner
		String pipeline_owner = pdef.getPipeline_owner();
		List<String> owers = Arrays.asList(pipeline_owner.split(";"));
		if(owers.contains(user)){
			return "1";
		}		
		return "0";
	}
	
	//根据pipline自定义ID获取节点信息
	public static List<PiplineNodeDef> getExcuteNodeByPiplineDefId(Integer piplineDefId){
		
		List<PiplineNodeDef> nodes = new ArrayList<PiplineNodeDef>();
		//1.根据piplineDefId获取所有的节点信息
		List<PiplineNodeDef> excuteNode = PiplineNodeDefDAOImpl.getExcuteNodeByPiplineDefId(piplineDefId);
		if(CollectionUtils.isNotEmpty(excuteNode)){
			for(PiplineNodeDef ndef:excuteNode){				
				if("start-end-point".equals(ndef.getNode_pass())){
					//增加过滤条件，如果是单节点
					nodes.add(ndef);
					return nodes;
				}
				if("start_point".equals(ndef.getNode_pass())){
					//第一个节点
					nodes.add(ndef);
					//获取下一个节点
					int next_id = ndef.getNext_id_on_success();
					//递归
					getNextExcuteNode(next_id,nodes,excuteNode);
				}
				
			}
			
		}
		
		return nodes;		
	}    
	
	public static void getNextExcuteNode(int next_id, List<PiplineNodeDef> nodes,
			List<PiplineNodeDef> excuteNode) {

		for (PiplineNodeDef ndef : excuteNode) {
			if (next_id == ndef.getId()) {
				nodes.add(ndef);
				Integer nextId = ndef.getNext_id_on_success();
				if(null == nextId || "end_point".equals(ndef.getNode_pass())){
					return;
				}
				getNextExcuteNode(nextId, nodes, excuteNode);
			}

		}
	}	
	
	public static String getModuleVersion(String release_package_url) {

		String version = "";

		if (StringUtils.isBlank(release_package_url)) {
			LOGGER.info("release_package_url or packageType is empty,please check！");
			return version;
		}

		// 例如：
		// http://redmine.intra.weibo.com:8081/nexus/content/repositories/releases/com/weibo/api/darwin-web/1.25/darwin-web-1.25.war
		version = release_package_url.split("/")[release_package_url.split("/").length - 1]
				.split(".war")[0];

		return version;

	}
		
	public static String getAllReleaseType(){
		
		String allReleaseType = "";
		String result = "";
		
		List<DeployTypePlatform> releases = DeployTypePlatformDAOImpl.getAllReleaseType();
		if(CollectionUtils.isNotEmpty(releases)){
			for(DeployTypePlatform rel : releases){
				allReleaseType = allReleaseType+rel.getName()+";";
			}
			//去掉最后的分号
			result = allReleaseType.substring(0,allReleaseType.length()-1);
			
		}		
		
		return result;		
	}
	
	@SuppressWarnings("unchecked")
	public static List<NodeCase> getAllNodeDefByPiplineDefId(String pipline_json) {

		JSONArray jsonArray = JSONObject.fromObject(pipline_json).getJSONArray("node");
		
		return (List<NodeCase>)JSONArray.toCollection(jsonArray, NodeCase.class); 
		
	}	
	@SuppressWarnings("unchecked")
	public static List<NodeDef> getAllNodeDefByPiplineDefId2(String pipline_json) {

		JSONArray jsonArray = JSONObject.fromObject(pipline_json).getJSONArray("node");
		
		return (List<NodeDef>)JSONArray.toCollection(jsonArray, NodeDef.class); 
		
	}	
	
	public static CreatePiplineVO getPiplineCaseJson(String pipline_json) {
		
		Map<String,Object> classMap = new HashMap<String,Object>();
		classMap.put("node", NodeCase.class);
		return  (CreatePiplineVO)JSONObject.toBean(JSONObject.fromObject(pipline_json),CreatePiplineVO.class,classMap);

	}	
	
	public static String getParameterValueByNodeDefType(String type,PiplineCase piplineCase,Integer nodeCaseIid,NodeDef nodeDef,Integer pipline_case_id,String dev_version,PiplineDef piplineDef,String rollBackVersion,String release_package_url,String frontMachine,String private_data,String queueMachine,String selection){	
		
		StringBuffer parameterValue = new StringBuffer();		
		
		Module module = ModuleDAOImpl.getModuleById(piplineCase.getModule_id());		
		String module_name = module.getName();
		String version = "";
		if(StringUtils.isNotBlank(rollBackVersion)){
			version = rollBackVersion;
		}else{
			version = piplineCase.getVersion();
		}
		String release_package = "";
		if(StringUtils.isNotBlank(release_package_url)){
       	  
			release_package="&release_package_url="+release_package_url;
         }		
		String push_name = module.getPush_name();
		String pipeline_case_id = String.valueOf(pipline_case_id);
		String node_case_id = String.valueOf(nodeCaseIid);
		PiplineNodeDef piplineNodeDef = PiplineNodeDefDAOImpl.getNodeByNodeDefId(piplineCase.getPipeline_def_id(), nodeDef.getId());
		PiplineDef pipeline_def = PiplineDefDAOImpl.getPiplineByDefId(piplineNodeDef.getPipeline_def_id());
		String test_type = piplineNodeDef.getTest_type();
		if(StringUtils.isBlank(test_type)){
			test_type = "";
		}
		String autotest_url = piplineNodeDef.getAutotest_url();
		if(StringUtils.isBlank(autotest_url)){
			autotest_url = "";
		}
		String deploy_type = piplineCase.getDeploy_type1();
		if(StringUtils.isBlank(deploy_type)){
			deploy_type = "";
		}		
		//拼接parameter参数值,公共的参数列表
		parameterValue.append("module_name=").append(module_name)
				.append("&ci_version=").append(version)
				.append("&pipeline_case_id=").append(pipeline_case_id)
				.append("&node_case_id=").append(node_case_id)
				.append("&token=").append(nodeDef.getJob_token());
		//下面是按照每种类型
		if("deploy_preview_env".equals(type)){
			parameterValue.append("&push_name=").append(push_name)
			              .append("&package_type=").append(piplineDef.getPackage_type())
			              .append("&selection=").append(selection);
		}else if("test_preview_env".equals(type) || "test_openflow_preivew_env".equals(type)){
			parameterValue.append("&test_type=").append(test_type)
                          .append("&autotest_url=").append(autotest_url)
                          .append("&mail_list=").append(getActionAndNotificationMailList(type,module,"").get("action_mail_list"));
		}else if("openflow_preview_env".equals(type)){
			parameterValue.append("&push_name=").append(push_name);
		}else if("deploy_release_package".equals(type)){
			parameterValue.append("&push_name=").append(push_name)
                          .append("&deploy_type=").append(deploy_type)
			              .append("&package_type=").append(piplineDef.getPackage_type())
			              .append(release_package);
		}else if("test_online_regression".equals(type)){			
			parameterValue.append("&test_type=").append(test_type)
            .append("&autotest_url=").append(autotest_url)
            .append("&mail_list=").append(getActionAndNotificationMailList(type,module,"").get("action_mail_list"))
            .append("&push_name=").append(push_name);
		}else if("build".equals(type)){
			parameterValue.append("&ci_dev_version=").append(dev_version)
            .append("&mail_list=").append(getActionAndNotificationMailList(type,module,"").get("action_mail_list"))
            .append("&module_id=").append(module.getId())
            .append("&operator=").append(piplineCase.getOperator())
            .append("&pipeline_def_name=").append(pipeline_def.getName());
		}
		/*else if("prepare_release".equals(type)){
			parameterValue.append("&ci_dev_version=").append(dev_version)
            .append("&mail_list=").append(getActionAndNotificationMailList(type,module,"").get("action_mail_list"))
            .append("&url_online=").append(module.getUrl_online());
		}*/
		else if("release".equals(type)){
			parameterValue.append("&ci_dev_version=").append(dev_version)
            .append("&mail_list=").append(getActionAndNotificationMailList(type,module,"").get("action_mail_list"))
            .append("&url_online=").append(module.getUrl_online()).append("&frontMachine=").append(frontMachine)
            .append("&private_data=").append(private_data).append("&queueMachine=").append(queueMachine);           
		}
		else if("compare".equals(type)){
			parameterValue.append("&ci_name=").append(module.getName()+"_ci")
            .append("&current_version=").append(module.getCurrent_online_version());			
		}
		else if("prepare_release".equals(type)){
			parameterValue.append("&module_id=").append(module.getId())
            .append("&operator=").append(piplineCase.getOperator());
		}
        
		LOGGER.info("the nodeCaseIid is:"+nodeCaseIid+" and the parameter value is:"+ parameterValue.toString());
		return parameterValue.toString();		
	}
	
	public static Map<String,String> getActionAndNotificationMailList(String type,Module module,String operator){
		
		Map<String,String> kv = new HashMap<String,String>();
		String action_mail_list = "";
		String notification_mail_list = "";
		
		if("deploy_preview_env".equals(type)){
			notification_mail_list = operator;
			
		}else if("test_preview_env".equals(type)){
			action_mail_list = module.getQa_owner()+";"+module.getRelease_follower();
			notification_mail_list = module.getQa_owner()+";"+module.getRelease_follower()+";"+module.getOp_owner();
			
		}else if("openflow_preview_env".equals(type)){
			notification_mail_list = operator;
			
		}else if("test_openflow_preivew_env".equals(type)){
			action_mail_list = module.getRelease_follower();
			notification_mail_list = module.getQa_owner()+";"+module.getRelease_follower()+";"+module.getOp_owner();			
		}
		else if("deploy_release_package".equals(type)){
			notification_mail_list = operator;
			
		}else if("test_online_regression".equals(type)){
			action_mail_list = module.getQa_owner()+";"+module.getRelease_follower();
			notification_mail_list = module.getQa_owner()+";"+module.getRelease_follower()+";"+module.getOp_owner()
			+";"+module.getBu_owner()+";"+module.getDev_list();		
		}else if("build".equals(type)){
			//TODO:
		}
		
		kv.put("action_mail_list", action_mail_list);
		kv.put("notification_mail_list", notification_mail_list);
		
		return kv;
	}
	
	public static Integer getCountByPiplineDefIdInPcase(Integer piplineDefId) {

		return PiplineCaseDAOImpl.getCountByPiplineDefIdInPcase(piplineDefId);

	}
	
	public static PiplineCase getPiplinecaseByPdefAndStatus(Integer piplineDefId) {

		return PiplineCaseDAOImpl.getPiplinecaseByPdefAndStatus(piplineDefId);

	}
	
	public static String triggerNode(NodeCase nodeCase) {

		String startfirstNode = nodeCase.getJob_url().split("jenkins:")[1]+ "buildWithParameters" + "?" + NodeCaseDAOImpl.getNodeCaseById(nodeCase.getId()).getParameter();
		LOGGER.info("begin start the first Node,and the url is:"+ startfirstNode);
		try {
			HttpUtils.HttpGet(startfirstNode);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("http get create exception:",e);
			return "-1";
		}
		return "0";
	}
	
    public static Map<String,String> getVersionByPipelineDefId(Module module){
		
		Map<String,String> kv = new HashMap<String,String>();
		String version = "";
		String dev_version = "";
		if(StringUtils.isBlank(module.getVersion_prefix())){
			//需要报警，邮件通知
			return null;
		}
		String version_prefix = module.getVersion_prefix();
		String version_has = ModuleVersionDAOImpl.getLatestModuleVersionById(module.getId());
		if(StringUtils.isBlank(version_has)){
			version = version_prefix+".0";	
			dev_version = version_prefix+".1";			
		}else{
		    if(version_has.startsWith(version_prefix)){
			    int indexEnd = version_has.split("\\.").length;
		        int indexEndAndOne = Integer.valueOf(version_has.split("\\.")[indexEnd-1])+1;
			    version = version_prefix + "." +String.valueOf(indexEndAndOne);
			    dev_version = version_prefix + "." +String.valueOf(indexEndAndOne+1);			
		    }else{
			    version = version_prefix+".0";	
			    dev_version = version_prefix+".1";}
		    }
		
		kv.put("version", version);
		kv.put("dev_version", dev_version+"-SNAPSHOT");
		
		return kv;		
	}		
	
	
	/*public static Map<String,String> getVersionByPipelineDefId(Module module){
		
		Map<String,String> kv = new HashMap<String,String>();
		String version = "";
		String dev_version = "";
		if(StringUtils.isBlank(module.getVersion_prefix())){
			//需要报警，邮件通知
			return null;
		}
		String version_prefix = module.getVersion_prefix();
		//1.根据moduleId获取pipelineCase表里面最新的version
		Integer count = PiplineCaseDAOImpl.getAllPiplinecaseByModuleIdCount(module.getId());
		if(0 == count.intValue()){
			//a.如果没有pipelinecase
			//第一次版本某位加".0"
			version = version_prefix+".0";	
			dev_version = version_prefix+".1";
		}else{
			//b.找出此module对应的pipelinecase的最新的那一条记录
			PiplineCase piplineCase = PiplineCaseDAOImpl.getLastPiplinecaseByPdef(module.getId());		
		if(null == piplineCase){
			//第一次版本某位加".0"
			version = version_prefix+".0";	
			dev_version = version_prefix+".1";
		}else{
			//对比当前版本的是否和数据库里面的前缀一致，如果一致则加1，否则加".0"
			if(StringUtils.isNotBlank(piplineCase.getVersion()) && piplineCase.getVersion().startsWith(version_prefix)){
				int indexEnd = piplineCase.getVersion().split("\\.").length;
			    int indexEndAndOne = Integer.valueOf(piplineCase.getVersion().split("\\.")[indexEnd-1])+1;
				version = version_prefix + "." +String.valueOf(indexEndAndOne);
				dev_version = version_prefix + "." +String.valueOf(indexEndAndOne+1);
			}else{
				version = version_prefix+".0";
				dev_version = version_prefix+".1";
			}
			
		     }
		}
		
		kv.put("version", version);
		kv.put("dev_version", dev_version+"-SNAPSHOT");
		
		return kv;		
	}		*/
	
	public static boolean isInsertModulePiplineType(String type){
		
		List<String> pipline_type = new ArrayList<String>();
		pipline_type.add("normal_release");
		pipline_type.add("urgent_release");
		pipline_type.add("rollback");
		
		return pipline_type.contains(type);	
	}	
	
	public static void sendMail(NodeCase nodeCase, PiplineDef piplineDef, Module module,
			PiplineCase piplineCase,String result,String user) {
		
		String send_email_cmd_head = "";
		nodeCase.getNode_def_id();
		if("build".equalsIgnoreCase(NodeDefDAOImpl.getNodeDefById(nodeCase.getNode_def_id()).getType()) || "release".equalsIgnoreCase(NodeDefDAOImpl.getNodeDefById(nodeCase.getNode_def_id()).getType())){			
			send_email_cmd_head = "sh " + "-x "+ "/data0/apache-tomcat-6.0.43/shell/scm_build.sh ";			
		}
	
		String send_email_cmd = send_email_cmd_head + 
				module.getName() + " "+ 
				result + " " + 
				piplineCase.getVersion()+ " "+
				piplineCase.getOperator()+ " "+
				getTime(piplineCase.getEnd_time())+ " "+piplineCase.getRelease_package_url()+ " "+piplineCase.getId()+ " "+nodeCase.getId()+ " "+nodeCase.getJob_url()
				+ " "+user;
		LOGGER.info("send mail command is:"+send_email_cmd);
		String[] sendFailInfo = new String[] { "/bin/bash", "-c",send_email_cmd };
		SSHExecutor.runLocalCommand(sendFailInfo);
	}	
	
	public static String getSendEmailUsers(NodeCase nodeCase,PiplineCase piplineCase){
		
		StringBuffer bf = new StringBuffer();
		PiplineNodeDef pf = PiplineNodeDefDAOImpl.getNodeByNodeDefId(piplineCase.getPipeline_def_id(),nodeCase.getNode_def_id());
		String group_id = "";
		//a.计算group的所有ID
		if(null != pf){
			String group = pf.getNotification_mail();
			if(StringUtils.isNotBlank(group)){
				String[] groups = group.split(";");
				for(int i = 0 ; i<groups.length;i++){
					Group g = GroupDAOImpl.getGroupByNameAndModuleId(piplineCase.getModule_id(),groups[i]);
					Group g1 = GroupDAOImpl.getGroupByNameAndModuleId(0,groups[i]);
					if(null !=g && null !=g.getId()){
						group_id += g.getId()+";";						
					}
					if(null !=g1 && null !=g1.getId()){
						group_id += g1.getId()+";";						
					}					
				}
				
			}			
			LOGGER.info("all group id set is:"+group_id);
		}		
		//b.根据所有的groupId得出所有的user列表
		if(StringUtils.isNotBlank(group_id)){
			String[] gd = group_id.split(";");
			for(int i = 0 ; i<gd.length;i++){
				List<User> users = UserDAOImpl.getUserByGroupId(Integer.valueOf(gd[i]));
				if(CollectionUtils.isNotEmpty(users)){
					for(User us : users){
						bf.append(us.getMail()).append(",");
					}
					
				}				
			}	
			LOGGER.info("all user name set is:"+bf.toString());
		}	
		
		return bf.toString();		
	}
	
	public static String getTime(String endTime){
		
		return endTime.split(" ")[0]+"_"+endTime.split(" ")[1];		
		
	}
	
	public static void initPipelineAndNodeDef(Integer pipeline_def_id,List<NodeDef> nds){
		
		List<Integer> ids = new ArrayList<Integer>();
		PiplineNodeDef piplineNodeDef = new PiplineNodeDef();
		piplineNodeDef.setPipeline_def_id(pipeline_def_id);
		for(int index = 0 ; index<nds.size();index ++){
			piplineNodeDef.setNode_def_id(nds.get(index).getId());
			piplineNodeDef.setNotification_mail(nds.get(index).getAction_mail_list());
			piplineNodeDef.setTest_type(nds.get(index).getTest_type());
			piplineNodeDef.setNext_id_on_fail(0);
			//1.第一种情况-->单节点
			if(1 == nds.size()){
				piplineNodeDef.setNode_pass("start-end-point");
			}else if(1 < nds.size()){
				//2.第二种情况，节点数目大于1，第一个节点是start_point，最后一个节点是end_point，其他是progress_point
				if(0 == index){
					//a.是第一个节点
					piplineNodeDef.setNode_pass("start_point");
				}else if(nds.size()-1 == index){
					//b.是最后一个节点
					piplineNodeDef.setNode_pass("end_point");		
				}else{
					//c.中间节点
					piplineNodeDef.setNode_pass("progress_point");
				}	
				
			}
			Integer id = PiplineNodeDefDAOImpl.insert(piplineNodeDef);
			ids.add(id);
		}
		if(1<ids.size()){
			for(int i = 0 ; i<ids.size()-1;i++){
				//更新
				PiplineNodeDefDAOImpl.update(ids.get(i+1), ids.get(i));
			}
			
			
		}
		
	}
	
	public static void main(String[] args) {
		
		System.out.print(getTime("2015-05-14 17:52:13"));
		
	}
}
