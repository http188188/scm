
package com.sina.scm.data.service;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sina.scm.data.DO.Module;
import com.sina.scm.data.DO.NodeCase;
import com.sina.scm.data.DO.NodeDef;
import com.sina.scm.data.DO.PiplineCase;
import com.sina.scm.data.DO.PiplineDef;
import com.sina.scm.data.DO.PiplineNodeDef;
import com.sina.scm.data.dao.GroupDAOImpl;
import com.sina.scm.data.dao.ModuleDAOImpl;
import com.sina.scm.data.dao.ModuleVersionDAOImpl;
import com.sina.scm.data.dao.NodeCaseDAOImpl;
import com.sina.scm.data.dao.NodeDefDAOImpl;
import com.sina.scm.data.dao.PiplineCaseDAOImpl;
import com.sina.scm.data.dao.PiplineDefDAOImpl;
import com.sina.scm.data.dao.RollBackDAOImpl;
import com.sina.scm.data.dao.UserDAOImpl;
import com.sina.scm.data.util.AuthorityUtill;
import com.sina.scm.data.util.DateUtils;
import com.sina.scm.data.util.HttpUtils;
import com.sina.scm.data.util.PiplineAndNodeStatus;
import com.sina.scm.data.util.PiplineUtils;
import com.sina.scm.data.vo.CreatePiplineVO;
import com.sina.scm.data.vo.DataVO;
import com.sina.scm.data.vo.DataVO2;
import com.sina.scm.data.vo.Group;
import com.sina.scm.data.vo.LogVO;
import com.sina.scm.data.vo.Menu;
import com.sina.scm.data.vo.ModuleNameAndIdsVO;
import com.sina.scm.data.vo.ModuleVersion;
import com.sina.scm.data.vo.PiplineCountVO;
import com.sina.scm.data.vo.PlateformResponse;
import com.sina.scm.data.vo.ResultVO;
import com.sina.scm.data.vo.RollBack;
import com.sina.scm.data.vo.TaskVO;
import com.sina.scm.data.vo.User;
import com.sina.scm.data.vo.poolVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 开发关于pipline的API
 * @author jintao3
 *
 */


@Path("scmPlatPiplineService")
public class ScmPlatPiplineService {
	
	private static final Logger LOGGER = Logger
			.getLogger(ScmPlatPiplineService.class.getName());
	
	@Path("/getProductLine")
	@GET
	@Produces("text/json")
	public String getProductLine(@QueryParam("callback") String callback) {		
		
		return callback+"("+JSONArray.fromObject(ModuleDAOImpl.getProductLine()).toString()+")";	
	}
	
	//统计模块pipline的失败率
	@Path("/getPiplineCountByModuleId")
	@GET
	@Produces("text/json")
	public String getPiplineCountByModuleId(@QueryParam("moduleId") Integer moduleId,
			@QueryParam("start_time") String start_time,
			@QueryParam("end_time") String end_time,
			@QueryParam("callback") String callback) {	
		double failcount,allcount,failpercent = 0;String percent = null;
		PiplineCountVO piplineCountVO = new PiplineCountVO();
		if(null != start_time && null != end_time){
			allcount = PiplineCaseDAOImpl.getAllPiplineByModuleIdAndTime(moduleId,start_time,end_time);
			failcount = PiplineCaseDAOImpl.getFailPiplineByModuleIdAndTime(moduleId,start_time,end_time);
		}else{
			allcount = PiplineCaseDAOImpl.getAllPiplineByModuleId(moduleId,start_time,end_time);
			failcount = PiplineCaseDAOImpl.getFailPiplineByModuleId(moduleId,start_time,end_time);
		}
		NumberFormat nt = NumberFormat.getPercentInstance();
		nt.setMinimumFractionDigits(2);
		if (allcount != 0.0){
			failpercent = failcount / allcount ;
			percent = nt.format(failpercent);
			piplineCountVO.setFail_percent(percent);
		}
		piplineCountVO.setAll_count(allcount);
		piplineCountVO.setFail_count(failcount);
		
		return callback+"("+JSONObject.fromObject(piplineCountVO).toString()+")";
			
	}
	

	@Path("/getAllModuleNameAndId")
	@GET
	@Produces("text/json")
	public String getAllModuleNameAndId(@QueryParam("callback") String callback) {		
		
		List<ModuleNameAndIdsVO> moduleNameAndId = new ArrayList<ModuleNameAndIdsVO>();
		//1.获取module_version表里面的所有module_id
		List<Integer> moduleIds  = PiplineDefDAOImpl.getAllModuleIds();
		if (CollectionUtils.isEmpty(moduleIds)) {
			LOGGER.info("get All module_ids is empty");
			return "getAllModuleNameAndId is fail";
		}
		for(Integer id : moduleIds){
			//2.根据module_id和status是online找出所有对应的module_name
			Module module =  ModuleDAOImpl.getModuleById(id);
			if(null != module){
				ModuleNameAndIdsVO moduleNameAndIds = new ModuleNameAndIdsVO();
				moduleNameAndIds.setModuleId(String.valueOf(id));
				moduleNameAndIds.setModuleName(module.getName());
				moduleNameAndId.add(moduleNameAndIds);
			}			
		}		
		
		return callback+"("+JSONArray.fromObject(moduleNameAndId).toString()+")";		
	}	
	
	@Path("/getPipelineListByModuleId")
	@GET
	@Produces("text/json")
	@Consumes("text/plain")
	public String getPipelineListByModuleId(@QueryParam("moduleId") Integer moduleId,@QueryParam("callback") String callback) {	
		
		//1.校验参数是否为空
		if(null == moduleId){
			LOGGER.info("the moduleId is empty!");
			return "the moduleId is empty";
		}
		//2.根据moduleId查找pipline自定义表
		List<PiplineDef> piplineList = PiplineDefDAOImpl.getPiplineListByModuleId(moduleId);
		if(CollectionUtils.isEmpty(piplineList)){
			LOGGER.info("the pipline list is empty by moduleId:"+moduleId);
			return "";
		}
		//3.生成需要的json
		return callback+"("+JSONArray.fromObject(piplineList).toString()+")";		
	}
	
	public String createPiplineByDefId(String user,Integer piplineDefId,String packageType) {

		// 1.校验user和piplineDefId，只要有一个空就返回
		if (StringUtils.isBlank(user) || null == piplineDefId) {
			LOGGER.info("check user or piplineDefId is empty");
			return "check user or piplineDefId is empty";
		}
		CreatePiplineVO createPiplineVO = new CreatePiplineVO();
		// 2.此用户是否有可以启动pipline的权限，默认都可以read
		String whetherToStart = PiplineUtils.whetherToStart(user, piplineDefId);
		createPiplineVO.setWhether_to_start(whetherToStart);
		createPiplineVO.setPackageType(packageType);
		createPiplineVO.setPiplineDefId(String.valueOf(piplineDefId));
		// 3.根据pipline自定义ID获取节点信息
		List<NodeCase> node = new ArrayList<NodeCase>();
		List<PiplineNodeDef> nodedef = PiplineUtils.getExcuteNodeByPiplineDefId(piplineDefId);
		if(CollectionUtils.isNotEmpty(nodedef)){
			for(PiplineNodeDef pndef : nodedef){
				Integer node_def_id = pndef.getNode_def_id();
				NodeDef ndef= NodeDefDAOImpl.getNodeDefById(node_def_id);
				NodeCase nd = new NodeCase();
				nd.setName(ndef.getName());
				nd.setType(ndef.getType());
				nd.setNode_def_id(node_def_id);
				nd.setStatus(PiplineAndNodeStatus.OPEN);		
				nd.setStart_next_node(pndef.getStart_next_node());
				nd.setTest_type(pndef.getTest_type());
				nd.setPreview_test_result_url("");
				node.add(nd);				
			}
			
		}		
		createPiplineVO.setNode(node);		
		createPiplineVO.setAllReleaseType(PiplineUtils.getAllReleaseType());
		createPiplineVO.setReleaseType(PiplineDefDAOImpl.getPiplineByDefId(piplineDefId).getDeploy_type1());
		createPiplineVO.setPipline_status(PiplineAndNodeStatus.OPEN);
		Module module = ModuleDAOImpl.getModuleById(PiplineDefDAOImpl.getPiplineByDefId(piplineDefId).getModule_id());
		createPiplineVO.setQa_owner(module.getQa_owner());
		createPiplineVO.setRelease_follower(module.getRelease_follower());
		createPiplineVO.setUrgent_release_approver(module.getUrgent_release_approver());
		//5.如果是源代码打包，需要计算出当前的版本和开发版本
		if(!PiplineAndNodeStatus.PACKAGE_WAR.equals(packageType)){
			createPiplineVO.setVersion(PiplineUtils.getVersionByPipelineDefId(module).get("version"));
			createPiplineVO.setDev_version(PiplineUtils.getVersionByPipelineDefId(module).get("dev_version"));
		}
		// 6.把解析好的json串需要存到对应的pipline定义表中
		String result =  JSONObject.fromObject(createPiplineVO).toString();
		PiplineDefDAOImpl.updatePipelineJsonById(piplineDefId, result);

		return result;
	}	
	
	@Path("/viewPiplineByDefId")
	@GET
	@Produces("text/json")
	@Consumes("text/plain")
	public String viewPiplineByDefId(@QueryParam("user") String user,
			@QueryParam("piplineDefId") Integer piplineDefId,
			@QueryParam("packageType") String packageType,
			@QueryParam("callback") String callback) {
		
		String result = "";
		//1.根据piplineDefId判断pl的实例表中状态是on-going的count
		int count = PiplineUtils.getCountByPiplineDefIdInPcase(piplineDefId).intValue();
		//如果个数是0，则表明是第一次创建原型
		if(count == 0){
			result = createPiplineByDefId(user,piplineDefId,packageType);
		}else if(count == 1){
			//如果个数是1，则表明已经有on-going and release的记录
			//CurrentModuleVersionVO currentModuleVersionVO = new CurrentModuleVersionVO();
			//String module_version = ModuleDAOImpl.getModuleById(PiplineDefDAOImpl.getPiplineByDefId(piplineDefId).getModule_id()).getCurrent_online_version();
			result = PiplineUtils.getPiplinecaseByPdefAndStatus(piplineDefId).getPipeline_case_json();		
			//currentModuleVersionVO.setModule_version(module_version);
			//currentModuleVersionVO.setPipeline_case_json(json);
			
		}else{
			//如果个数是3，则表明是异常，需要通知人员维护，排查问题	
			LOGGER.error("viewPiplineByDefId expection,and the piplineDefId is:"+piplineDefId
					+" getCountByPiplineDefIdInPcase is:"+count);
		}		
		
		LOGGER.info("viewPiplineByDefId result is:"+result);
		return callback+"("+result+")";
	}	
	
	@Path("/startPiplineByDefId")
	@GET
	@Produces("text/json")
	public String startPiplineByDefId(@QueryParam("callback") String callback,
			@QueryParam("packageType") String packageType,
			@QueryParam("piplineDefId") Integer piplineDefId,
			@QueryParam("releaseWarUrl") String releaseWarUrl,
			@QueryParam("rollBackWarUrl") String rollBackWarUrl,
			@QueryParam("comments") String comments,
			@QueryParam("operator") String operator,
			@QueryParam("releaseType") String releaseType,
			@QueryParam("rollBackVersion") String rollBackVersion,
			@QueryParam("release_package_url") String release_package_url,
			@QueryParam("frontMachine")  String frontMachine,
			@QueryParam("private_data") String private_data,
			@QueryParam("queueMachine") String queueMachine,
			@QueryParam("selection") String selection) {		
		//1.参数判断
		if(null == piplineDefId || StringUtils.isBlank(operator)){
			LOGGER.info("piplineDefId is empty!");
			return "-1 and piplineDefId";
		}
		// 3.创建pipline实例
		int count = PiplineUtils.getCountByPiplineDefIdInPcase(piplineDefId).intValue();
		if (count != 0) {
			LOGGER.info("the pipeline_case num isn't 0,can not insert by start,and the piplineDefId is:"+ piplineDefId);
			return "-1 and"+release_package_url;
		}
		Integer pipline_case_id = null;
		PiplineDef piplineDef = PiplineDefDAOImpl.getPiplineByDefId(piplineDefId);
		Module module = ModuleDAOImpl.getModuleById(piplineDef.getModule_id());
		//2.是否具有start权限
		if(!AuthorityUtill.hasStartAuthority(operator,module.getId())){
			LOGGER.info("no start authority");
			return "no start authority";
		}
		PiplineCase piplineCase = new PiplineCase();
		String version = rollBackVersion;
		String dev_version = "";
		if(null != piplineDef){
			piplineCase.setPipeline_def_id(piplineDefId);
			piplineCase.setModule_id(piplineDef.getModule_id());
			piplineCase.setStatus(PiplineAndNodeStatus.ON_GOING);			
			piplineCase.setRelease_note(comments);
			piplineCase.setOperator(operator);
			piplineCase.setStart_time(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
		    if(PiplineAndNodeStatus.PACKAGE_WAR.equals(packageType)){
		    	piplineCase.setRelease_package_url(releaseWarUrl);
		    	piplineCase.setRollback_package_url(rollBackWarUrl);
		    	piplineCase.setVersion(PiplineUtils.getModuleVersion(releaseWarUrl));
		    }
		    if(StringUtils.isBlank(version)){
		    	version = PiplineUtils.getVersionByPipelineDefId(module).get("version");
		    	dev_version = PiplineUtils.getVersionByPipelineDefId(module).get("dev_version"); 
		    	
		    }			
		    piplineCase.setVersion(version);
			piplineCase.setDeploy_type1(releaseType);
			pipline_case_id = PiplineCaseDAOImpl.insertPiplineCase(piplineCase);
			LOGGER.info("insert into pipline_case and the pipline_case_id is:"+pipline_case_id);			
		}
		//5.插入node实例,分析pipline自定义表中的json
		String pipline_json = piplineDef.getPipeline_json();
		if(StringUtils.isBlank(pipline_json)){
			LOGGER.info("pipline_json is empty!");
			return "-1 and pipline_json";
		}
		List<NodeCase> nodes = PiplineUtils.getAllNodeDefByPiplineDefId(pipline_json);
		if(CollectionUtils.isEmpty(nodes)){
			LOGGER.info("nodes is empty!");
			return "-1 and nodes";
		}
		for (NodeCase nd : nodes) {
			NodeDef nodeDef = NodeDefDAOImpl.getNodeDefById(nd.getNode_def_id());
			nd.setNode_def_id(nd.getNode_def_id());
			nd.setPipeline_case_id(pipline_case_id);
			nd.setJob_url(nodeDef.getJob_url());
			nd.setStatus(PiplineAndNodeStatus.OPEN);
			nd.setName(nodeDef.getName());
			nd.setType(nodeDef.getType());
			nd.setNotification_mail_list(PiplineUtils
					.getActionAndNotificationMailList(nd.getType(), module,operator).get("notification_mail_list"));
			
			Integer nodeCaseIid = NodeCaseDAOImpl.insertNodeCase(nd);
			String parameter = PiplineUtils.getParameterValueByNodeDefType(nd.getType(), piplineCase, nodeCaseIid, nodeDef,pipline_case_id,dev_version,piplineDef,rollBackVersion,release_package_url,frontMachine,private_data,queueMachine,selection);
			LOGGER.info("release_package_url is:"+release_package_url);
			nd.setParameter(parameter);
			nd.setId(nodeCaseIid);
			//更新node_case表
			NodeCaseDAOImpl.updateParameterById(parameter, nodeCaseIid);
		}		
		CreatePiplineVO createPiplineVO = new CreatePiplineVO();
		createPiplineVO.setSelection(selection);
		createPiplineVO.setQa_owner(module.getQa_owner());
		createPiplineVO.setRelease_follower(module.getRelease_follower());
		createPiplineVO.setUrgent_release_approver(module.getUrgent_release_approver());
		createPiplineVO.setNode(nodes);
		createPiplineVO.setPipline_status(PiplineAndNodeStatus.ON_GOING);
		createPiplineVO.setPipline_case_id(pipline_case_id);
		createPiplineVO.setPiplineDefId(String.valueOf(piplineDefId));
		createPiplineVO.setAllReleaseType(PiplineUtils.getAllReleaseType());
		createPiplineVO.setReleaseType(releaseType);
		createPiplineVO.setReleaseWarUrl(releaseWarUrl);
		createPiplineVO.setRollBackWarUrl(rollBackWarUrl);
		createPiplineVO.setComments(comments);
		createPiplineVO.setWhether_to_start("1");
		createPiplineVO.setRollBackVersion(version);
		if(!PiplineAndNodeStatus.PACKAGE_WAR.equals(packageType)){
			createPiplineVO.setVersion(version);
			createPiplineVO.setDev_version(dev_version);
		}
		//更新pipline_case的json串
		PiplineCaseDAOImpl.updatePiplineCaseJsonById(JSONObject.fromObject(createPiplineVO).toString(), pipline_case_id);
	    //更新module表里面的operator
		//ModuleVersion moduleVersion = new ModuleVersion();
		/*moduleVersion.setModule_id(module.getId());
		moduleVersion.setVersion(version);
		moduleVersion.setOperator(operator);
		ModuleVersionDAOImpl.insertModuleVersion(moduleVersion);*/
		//启动第一个node		
		PiplineUtils.triggerNode(nodes.get(0));
		
		return callback+"("+JSONObject.fromObject(createPiplineVO).toString()+")";
    }	
	
	@Path("/platfromCiNodePostCallback")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String platfromCiNodePostCallback(@QueryParam("result") String result,
			@QueryParam("pipelineCaseId") Integer pipeline_case_id,
			@QueryParam("nodeCaseId") Integer node_case_id,
			@QueryParam("testlog") String testlog,
			@QueryParam("callback") String callback) {
        		
		String pipLineCaseStatus = PiplineAndNodeStatus.ON_GOING; 
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(PiplineAndNodeStatus.SUCCESS);
		//1.校验不能为空
		LOGGER.info("platfromCiNodePostCallback api's parameter,result is:"
				+ result + " pipeline_case_id is:" + pipeline_case_id
				+ " node_case_id is:" + node_case_id);
		if (StringUtils.isBlank(result) || null == pipeline_case_id
				|| null == node_case_id) {
			resultVO.setResult(PiplineAndNodeStatus.FAIL);
			resultVO.setMessage("please check platfrom_ci_node_post_callback parameter");
			return callback+"("+JSONObject.fromObject(resultVO).toString()+")";
		}
		//2.根据pipeline_case_id获取对应的pipeline_case_json	
		PiplineCase piplineCase = PiplineCaseDAOImpl.getPipelineCaseById(pipeline_case_id);
		if(null == piplineCase){
			resultVO.setResult(PiplineAndNodeStatus.FAIL);
			resultVO.setMessage("the pipeline_case_id is:"+pipeline_case_id+" but the data is empty");
			return callback+"("+JSONObject.fromObject(resultVO).toString()+")";
		}
		//3.从json串里面获取当前node和nextNode(核心)
		CreatePiplineVO createPiplineVO = PiplineUtils.getPiplineCaseJson(piplineCase.getPipeline_case_json());
		List<NodeCase> nodeCases = createPiplineVO.getNode();
		NodeCase currentNodeCase = null;
		NodeCase nextNodeCase = null;
		boolean isEndPoint = false;
		String endTime = "";
		if(CollectionUtils.isNotEmpty(nodeCases)){
			for(int index=0;index<nodeCases.size();index++){
				if (node_case_id.intValue() == nodeCases.get(index).getId().intValue()) {
					if (!PiplineAndNodeStatus.ON_GOING.equals(piplineCase.getStatus())) {
						resultVO.setResult(PiplineAndNodeStatus.FAIL);
						resultVO.setMessage("the pipeline's status or the nodecase's status not on-going,please check");
						return callback+"("+JSONObject.fromObject(resultVO).toString()+")";
					}
					currentNodeCase = nodeCases.get(index);
					LOGGER.info("find the currentNodeCase and node_case_id is:"
							+ currentNodeCase.getId());
					endTime = DateUtils.formatDate(new Date(),
							"yyyy-MM-dd HH:mm:ss");
					nodeCases.get(index).setStatus(result);
					nodeCases.get(index).setEnd_time(endTime);
					if (nodeCases.size() - 1 != index) {
						nextNodeCase = nodeCases.get(index + 1);
						LOGGER.info("find the nextNodeCase and node_case_id is:"
								+ nextNodeCase.getId());
					} else {
						isEndPoint = true;
					}
				}				
			}			
		}  
		String type = PiplineDefDAOImpl.getPiplineByDefId(piplineCase.getPipeline_def_id()).getPipeline_type();
		Module module = ModuleDAOImpl.getModuleById(piplineCase.getModule_id());
		if(PiplineAndNodeStatus.SUCCESS.equals(result) && isEndPoint){
			pipLineCaseStatus = PiplineAndNodeStatus.SUCCESS;
		}
		if(PiplineAndNodeStatus.SUCCESS.equals(result) && isEndPoint && PiplineUtils.isInsertModulePiplineType(type)){
			//4.如果成功，并且这个节点是end-point，需要注意一下几点
		    //判断此pipline的类型，是上线还是回滚		 
		   if("rollback".equals(type)){
			   RollBack rollBack = new RollBack();
			   rollBack.setFrom_version(module.getCurrent_online_version());
			   rollBack.setTo_version(piplineCase.getVersion());
			   rollBack.setOperator(piplineCase.getOperator());
			   rollBack.setModule_id(piplineCase.getModule_id());
			   rollBack.setRollback_date(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
			   
			   RollBackDAOImpl.insertRollBack(rollBack);			   
		   }else{		   
		    //非回滚，即上线
			   ModuleVersion moduleVersion = new ModuleVersion();
				moduleVersion.setModule_id(piplineCase.getModule_id());
				moduleVersion.setVersion(piplineCase.getVersion());
				moduleVersion.setOperator(piplineCase.getOperator());
				moduleVersion.setRelease_date(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
			   if(0 == ModuleVersionDAOImpl.getVersionNum1(piplineCase.getVersion(), piplineCase.getModule_id().intValue()).intValue()){
				   ModuleVersionDAOImpl.insertModuleVersion(moduleVersion);
				   
			   }else{
				   ModuleVersionDAOImpl.updateModuleVersionByVersionAndModuleId(moduleVersion);				   
			   }		
			
				
			//更新标记位			
		    PiplineCaseDAOImpl.updateReleaseVersionByPipelineCaseId(piplineCase);
		   }
		    ModuleDAOImpl.updateModuleOnlineVersion(piplineCase.getVersion(),DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"),piplineCase.getModule_id());
		}else if(PiplineAndNodeStatus.FAIL.equals(result)){
			pipLineCaseStatus = PiplineAndNodeStatus.FAIL;
		}else if(PiplineAndNodeStatus.SKIP.equals(result)){
			pipLineCaseStatus = PiplineAndNodeStatus.SUCCESS;			
		}
		//更新pipeline_case表的属性，如下：
		piplineCase.setStatus(pipLineCaseStatus);	
		piplineCase.setEnd_time(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
		createPiplineVO.setNode(nodeCases);
		createPiplineVO.setPipline_status(pipLineCaseStatus);		
		piplineCase.setPipeline_case_json(JSONObject.fromObject(createPiplineVO).toString());
		currentNodeCase.setStatus(result);
		currentNodeCase.setEnd_time(endTime);
		NodeCaseDAOImpl.updateNodeCase(currentNodeCase);
		PiplineCaseDAOImpl.updatePipeLineCase(piplineCase);
		//7+a:给当前节点发邮件
		PiplineUtils.sendMail(NodeCaseDAOImpl.getNodeCaseById(node_case_id), PiplineDefDAOImpl.getPiplineByDefId(piplineCase.getPipeline_def_id()), 
				module, piplineCase, result, 
				PiplineUtils.getSendEmailUsers(NodeCaseDAOImpl.getNodeCaseById(node_case_id), piplineCase));	
		//7+b:更新测试日志
		if(StringUtils.isNotBlank(testlog)){
		updatePreviewAutotestResultUrlById(String.valueOf(node_case_id), testlog, String.valueOf(pipeline_case_id));		
		}
		//7.触发下一个node
		if (null != nextNodeCase) {
			if ((PiplineAndNodeStatus.SUCCESS.equals(result)
					&& ("auto").equals(currentNodeCase.getStart_next_node()) || 
					PiplineAndNodeStatus.PASS.equals(result)
					&& ("manual").equals(currentNodeCase.getStart_next_node()))) {
				PiplineUtils.triggerNode(nextNodeCase);
			}
		}
		return callback+"("+JSONObject.fromObject(resultVO).toString()+")";
	}
	
	@Path("/platfromCiNodePreCallback")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String platfromCiNodePreCallback(@QueryParam("pipelineCaseId") Integer pipeline_case_id,
			@QueryParam("nodeCaseId") Integer node_case_id,
			@QueryParam("buildUrl") String buildUrl,
			@QueryParam("logUrl") String logUrl,
			@QueryParam("callback") String callback) {
		//1.检查参数是否符合需求
		LOGGER.info("platfromCiNodePreCallback api's parameter,buildUrl is:"
				+ buildUrl + " pipeline_case_id is:" + pipeline_case_id
				+ " node_case_id is:" + node_case_id+"logUrl is:"+logUrl);
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(PiplineAndNodeStatus.SUCCESS);
		if (StringUtils.isBlank(buildUrl) || null == pipeline_case_id
				|| null == node_case_id || StringUtils.isBlank(logUrl) ) {
			resultVO.setResult(PiplineAndNodeStatus.FAIL);
			resultVO.setMessage("please check platfromCiNodePreCallback parameter");
			return callback+"("+JSONObject.fromObject(resultVO).toString()+")";
		}
		//2.根据pipeline_case_id获取对应的pipeline_case_json	
		PiplineCase piplineCase = PiplineCaseDAOImpl.getPipelineCaseById(pipeline_case_id);
		if(null == piplineCase){
			resultVO.setResult(PiplineAndNodeStatus.FAIL);
			resultVO.setMessage("the pipeline_case_id is:"+pipeline_case_id+" but the data is empty");
			return callback+"("+JSONObject.fromObject(resultVO).toString()+")";
		}
		CreatePiplineVO createPiplineVO = PiplineUtils.getPiplineCaseJson(piplineCase.getPipeline_case_json());
		List<NodeCase> nodeCases = createPiplineVO.getNode();
		NodeCase currentNodeCase = null;
		if(CollectionUtils.isNotEmpty(nodeCases)){
			for(int index=0;index<nodeCases.size();index++){
				if(node_case_id.intValue() == nodeCases.get(index).getId().intValue()){
					currentNodeCase = nodeCases.get(index);
					LOGGER.info("find the currentNodeCase and node_case_id is:"+currentNodeCase.getId());
					String startTime = DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss");
					nodeCases.get(index).setJob_url(buildUrl);
					nodeCases.get(index).setJob_log_url(logUrl);
					nodeCases.get(index).setStatus(PiplineAndNodeStatus.ON_GOING);
					nodeCases.get(index).setStart_time(startTime);
					currentNodeCase.setJob_url(buildUrl);
					currentNodeCase.setJob_log_url(logUrl);		
					currentNodeCase.setStatus(PiplineAndNodeStatus.ON_GOING);
					currentNodeCase.setStart_time(startTime);
				}				
			}			
		}
		createPiplineVO.setNode(nodeCases);
		NodeCaseDAOImpl.updateNodeCaseByID(currentNodeCase);
		PiplineCaseDAOImpl.updatePiplineCaseJsonById(JSONObject.fromObject(createPiplineVO).toString(),pipeline_case_id);
		return callback+"("+JSONObject.fromObject(resultVO).toString()+")";
	}
	
	
	
	
	
	@Path("/getPiplineCaseJsonById")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String getPiplineCaseJsonById(@QueryParam("pipelineCaseId") Integer pipeline_case_id
			,@QueryParam("callback") String callback) {
		
		//1.检查参数是否为空
		LOGGER.info("getPiplineCaseJsonById of parammeter pipelineCaseId is:"+pipeline_case_id);
		if(null == pipeline_case_id){
			LOGGER.info("getPiplineCaseJsonById pipelineCaseId is empty");
			return "";			
		}
		//检查是否真正在数据库里面存在这样一个实例
		PiplineCase piplineCase = PiplineCaseDAOImpl.getPipelineCaseById(pipeline_case_id);
		if(null != piplineCase){
			return callback+"("+piplineCase.getPipeline_case_json()+")";
		}		
		return "";
	}	
	
	@Path("/getStopPiplineCaseStatusById")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String getStopPiplineCaseStatusById(@QueryParam("pipelineCaseId") Integer pipeline_case_id
			,@QueryParam("callback") String callback) {
		
		//1.检查参数是否为空
		LOGGER.info("getStopPiplineCaseStatusById of parammeter pipelineCaseId is:"+pipeline_case_id);
		if(null == pipeline_case_id){
			LOGGER.info("getStopPiplineCaseStatusById pipelineCaseId is empty");
			return "";			
		}
		//检查是否真正在数据库里面存在这样一个实例
		PiplineCase piplineCase = PiplineCaseDAOImpl.getPipelineCaseById(pipeline_case_id);
		ModuleVersion moduleVersion = new ModuleVersion();
		moduleVersion.setModule_id(piplineCase.getModule_id());
		moduleVersion.setVersion(piplineCase.getVersion());
		ModuleVersionDAOImpl.insertModuleVersion(moduleVersion);
		//如果存在这样的实例，则更新Status为stop并且返回status.
		if(null != piplineCase){
			piplineCase.setStatus(PiplineAndNodeStatus.FAIL);
			//更新PipeLine
			PiplineCaseDAOImpl.updatePipeLineCase(piplineCase);
			CreatePiplineVO createPiplineVO = PiplineUtils.getPiplineCaseJson(piplineCase.getPipeline_case_json());
			List<NodeCase> nodeCases = createPiplineVO.getNode();
			NodeCase currentNodeCase = null;
			if(CollectionUtils.isNotEmpty(nodeCases)){
				for(int index=0;index<nodeCases.size();index++){
					if(PiplineAndNodeStatus.ON_GOING.equalsIgnoreCase(nodeCases.get(index).getStatus())){
						nodeCases.get(index).setStatus(PiplineAndNodeStatus.FAIL);						
						break;						
					}
				}
			}
			createPiplineVO.setNode(nodeCases);
			createPiplineVO.setPipline_status(PiplineAndNodeStatus.FAIL);
			NodeCaseDAOImpl.updateNodeCaseByID(currentNodeCase);
			PiplineCaseDAOImpl.updatePiplineCaseJsonById(JSONObject.fromObject(createPiplineVO).toString(),pipeline_case_id);
			return callback+"("+piplineCase.getStatus()+")";
		}		
		return "";
	}	
	
	@Path("/updateReleasePackageUrlByPipelineCaseId")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String updateReleasePackageUrlByPipelineCaseId(@QueryParam("pipelineCaseId") Integer pipeline_case_id,
			@QueryParam("release_package_url") String release_package_url) {

		// 1.检查参数是否为空
		String release_package="&release_package_url="+release_package_url;
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(PiplineAndNodeStatus.SUCCESS);
		LOGGER.info("updateReleasePackageUrlByPipelineCaseId of parammeter pipelineCaseId is:"+ pipeline_case_id);
		if (null == pipeline_case_id || StringUtils.isBlank(release_package_url)) {
			resultVO.setResult(PiplineAndNodeStatus.FAIL);
			resultVO.setMessage("getPiplineCaseJsonById pipelineCaseId is empty");
			return JSONObject.fromObject(resultVO).toString();
		}
		//2.更新数据库，中对应的pipelinecaseId记录
		PiplineCase piplineCase = new PiplineCase();
		piplineCase.setId(pipeline_case_id);
		piplineCase.setRelease_package_url(release_package_url);
		
		PiplineCaseDAOImpl.updateReleasePackageUrlByPipelineCaseId(piplineCase);	
		//3.更新node_case里面的对应的parameter键值对
		//找出pipeline_case_id对应的所有node_case_ids		
	    //每个node_case_id一一更新		
		List<NodeCase> node_cases = NodeCaseDAOImpl.getNodeCases(pipeline_case_id);
		for(NodeCase nd : node_cases){
			NodeCaseDAOImpl.updateParameterById(nd.getParameter()+release_package, nd.getId());
		}
		
		return JSONObject.fromObject(resultVO).toString();
	}
	
	@Path("/updatePreviewIplistById")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String updatePreviewIplistById(@QueryParam("pipelineCaseId") Integer pipeline_case_id,
			@QueryParam("preview_iplist") String preview_iplist) {
		
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(PiplineAndNodeStatus.SUCCESS);
		
		//1.检查参数
		if (null == pipeline_case_id || StringUtils.isBlank(preview_iplist)) {
			resultVO.setResult(PiplineAndNodeStatus.FAIL);
			resultVO.setMessage("updatePreviewIplistById pipelineCaseId is empty");
			return JSONObject.fromObject(resultVO).toString();
		}
		//2.更新数据库
		PiplineCaseDAOImpl.updatePreviewIplistById(pipeline_case_id, preview_iplist);
		//3.更新node_case里面的所有的parameter的值，为了jenkins的job可以调用
		List<NodeCase> node_cases = NodeCaseDAOImpl.getNodeCases(pipeline_case_id);
		for(NodeCase nd : node_cases){
			NodeCaseDAOImpl.updateParameterById(nd.getParameter()+"&preview_url="+preview_iplist, nd.getId());
		}		
		
		return JSONObject.fromObject(resultVO).toString();
	}
	
	@Path("/findPreviewIplistById")
	@GET
	@Produces("text/plain")	
	@Consumes("text/plain")
	public String updatePreviewIplistById(@QueryParam("pipelineCaseId") Integer pipeline_case_id) {
		
		return PiplineCaseDAOImpl.getPipelineCaseById(pipeline_case_id).getPreview_iplist();		
	}
	
	@Path("/updatePreviewAutotestResultUrlById")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String updatePreviewAutotestResultUrlById(@QueryParam("nodeCaseId") String node_case_id,
			@QueryParam("preview_test_result_url") String updatePreviewAutotestResultUrlById,@QueryParam("pipelineCaseId") String pipeline_case_id) {
		
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(PiplineAndNodeStatus.SUCCESS);
		
		//1.检查参数
		if (StringUtils.isBlank(node_case_id) || StringUtils.isBlank(updatePreviewAutotestResultUrlById)) {
			resultVO.setResult(PiplineAndNodeStatus.FAIL);
			resultVO.setMessage("updatePreviewAutotestResultUrlById node_case_id is empty");
			return JSONObject.fromObject(resultVO).toString();
		}
		//2.更新数据库
		NodeCaseDAOImpl.update_preview_test_result_urlById(updatePreviewAutotestResultUrlById, Integer.valueOf(node_case_id));
		//3.更新node_case里面的所有的parameter的值，为了jenkins的job可以调用		
		NodeCaseDAOImpl.updateParameterById(NodeCaseDAOImpl.getNodeCaseById(Integer.valueOf(node_case_id)).getParameter()+"&preview_autotest_result_url="+updatePreviewAutotestResultUrlById, Integer.valueOf(node_case_id));
		//4.更新json字符串
		String json = PiplineCaseDAOImpl.getPipelineCaseById(Integer.valueOf(pipeline_case_id)).getPipeline_case_json();
		CreatePiplineVO createPiplineVO = PiplineUtils.getPiplineCaseJson(json);
		List<NodeCase> nodeCases = createPiplineVO.getNode();
		for(NodeCase nd : nodeCases){
			if(Integer.valueOf(node_case_id).intValue() == nd.getId().intValue()){
				nd.setPreview_test_result_url(updatePreviewAutotestResultUrlById);
			}			
		}
		createPiplineVO.setNode(nodeCases);
		PiplineCaseDAOImpl.updatePiplineCaseJsonById(JSONObject.fromObject(createPiplineVO).toString(),Integer.valueOf(pipeline_case_id));
				
		return JSONObject.fromObject(resultVO).toString();
	}
	
	
	@Path("/platfromPackageNodePostCallback")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String platfromPackageNodePostCallback(@QueryParam("packageName") String name,
			@QueryParam("tagurl") String ci_version,@QueryParam("pipelineCaseId") Integer pipeline_case_id) {
		
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(PiplineAndNodeStatus.FAIL);
		
		//1.检查参数
		if (StringUtils.isBlank(ci_version) || StringUtils.isBlank(name) || null == pipeline_case_id) {
			resultVO.setMessage("platfromPackageNodePostCallback name or tagurl,pipeline_case_id is empty");
			return JSONObject.fromObject(resultVO).toString();
		}
		//2.往module_version表插入数据
		Module module = ModuleDAOImpl.getModuleByName(name);
		if(null !=module & null != module.getId()){
			
			ModuleVersion moduleVersion = new ModuleVersion();
			moduleVersion.setModule_id(module.getId());
			//moduleVersion.setVersion(tagurl.split("/")[tagurl.split("/").length-1]);
			moduleVersion.setVersion(ci_version);
			moduleVersion.setRelease_date(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
			//moduleVersion.setCheckout_command(tagurl);
			moduleVersion.setOperator(PiplineCaseDAOImpl.getPipelineCaseById(pipeline_case_id).getOperator());
			
			if(0 == ModuleVersionDAOImpl.getVersionNum1(ci_version, module.getId().intValue()).intValue()){
				   ModuleVersionDAOImpl.insertModuleVersion(moduleVersion);
				   
			   }else{
				   ModuleVersionDAOImpl.updateModuleVersionByVersionAndModuleId(moduleVersion);				   
			   }		
			
			//ModuleVersionDAOImpl.insertModuleVersion(moduleVersion);	
			
			ModuleDAOImpl.updateModuleOnlineVersion(moduleVersion.getVersion(),DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"),module.getId());
			
			resultVO.setResult(PiplineAndNodeStatus.SUCCESS);
			
		}
		
		return JSONObject.fromObject(resultVO).toString();
	}	
	
	@Path("/getMenuByName")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String getMenuByName(@QueryParam("name") String user
			,@QueryParam("callback") String callback) {
		
		List<Menu> menu = new ArrayList<Menu>();
		//1.找出user对应的group
		List<User> groups = UserDAOImpl.getAllGroupByName(user);
		if (CollectionUtils.isNotEmpty(groups)) {
			for (User us : groups) {
				// 2.这个人是否是scm_group组成员，如果是显示menu表里面所有的信息
				Group group = GroupDAOImpl.getGroupById(us.getGroup_id());
				if (null != group) {
					if ("scm_group".equals(GroupDAOImpl.getGroupById(us.getGroup_id()).getName())) {
						menu = UserDAOImpl.getSCMMenu();
						return callback + "("+ JSONArray.fromObject(menu).toString() + ")";
					} else {
						// 3.如果不是，只显示menu表里面的是1的信息
						menu = UserDAOImpl.getNoSCMMenu();
					}

				}

			}

		}
		return callback+"("+JSONArray.fromObject(UserDAOImpl.getNoSCMMenu()).toString()+")";		
	}
	
	@Path("/getAllUserBySCM")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String getAllUserBySCM(@QueryParam("callback") String callback) {		
		
		List<User> users = new ArrayList<User>();
		//1.找出scm_group的id
		Integer group_id = GroupDAOImpl.getGroupByNameAndModuleId(0, "scm_group").getId();
		//2.找出所有的相关组的人员信息
		users = UserDAOImpl.getUserByGroupId(group_id);
		
		return callback+"("+JSONArray.fromObject(users).toString()+")";
		
	}
	
	@Path("/getCodediffAndComments")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String getCodediffAndComments(@QueryParam("comments") String comments,
			@QueryParam("code_diff") String code_diff,
			@QueryParam("nodeCaseId") String node_case_id,
			@QueryParam("pipelineCaseId") String pipeline_case_id,
			@QueryParam("callback") String callback) {
		
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(PiplineAndNodeStatus.SUCCESS);
		
		String json = PiplineCaseDAOImpl.getPipelineCaseById(Integer.valueOf(pipeline_case_id)).getPipeline_case_json();
		CreatePiplineVO createPiplineVO = PiplineUtils.getPiplineCaseJson(json);
		List<NodeCase> nodeCases = createPiplineVO.getNode();
		for(NodeCase nd : nodeCases){
			if(Integer.valueOf(node_case_id).intValue() == nd.getId().intValue()){
				nd.setCode_diff(code_diff);
				nd.setComments(comments);
			}			
		}
		createPiplineVO.setNode(nodeCases);
		PiplineCaseDAOImpl.updatePiplineCaseJsonById(JSONObject.fromObject(createPiplineVO).toString(),Integer.valueOf(pipeline_case_id));
				
		return callback+"("+JSONObject.fromObject(resultVO).toString()+")";
	
	}
	
	@Path("/insertModuleVersionGitTag")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String insertModuleVersionGitTag(@QueryParam("module_id") Integer module_id,
			@QueryParam("operator") String operator,
			@QueryParam("ci_version") String ci_version,
			@QueryParam("callback") String callback) {
		
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(PiplineAndNodeStatus.SUCCESS);
		
		ModuleVersion moduleVersion = new ModuleVersion();
		moduleVersion.setOperator(operator);
		moduleVersion.setModule_id(module_id);
		moduleVersion.setVersion(ci_version);
		moduleVersion.setIs_tag("tag");
		moduleVersion.setRelease_date(DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
		
		ModuleVersionDAOImpl.insertModuleVersion(moduleVersion);
				
		return callback+"("+JSONObject.fromObject(resultVO).toString()+")";
	
	}
	
	@Path("/getLogUrlContent")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String platfromCiNodePreCallback(@QueryParam("logUrl") String logUrl,@QueryParam("callback") String callback) {
		
		//1.检查参数是否为空
		LogVO logVO = new LogVO();
		if(StringUtils.isBlank(logUrl)){
			LOGGER.info("platfromCiNodePreCallback's logUrl is empty");
			return callback+"("+JSONObject.fromObject(logVO).toString()+")";
		}
		//2.调用HTTP请求即logUrl
		try {
			logVO.setLog(HttpUtils.HttpPost(logUrl));
			return callback+"("+JSONObject.fromObject(logVO).toString()+")";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callback+"("+JSONObject.fromObject(logVO).toString()+")";
	}
	
	@Path("/getPlatformParame")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String getPlatformParame(@QueryParam("callback") String callback){
		String result = "";
		String url = "http://apo2.jpool.intra.sina.com.cn/api/openapi/scheduler/interface.php?param={\"action\":\"get_pools_by_identifier\",\"data\":{\"identifier\":\"\"},\"module\":\"shell\"}";
		try {
			result = HttpUtils.HttpGet2(url);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callback+"("+result+")";
	}
	@Path("/getSpecificServicePool")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String getSpecificServicePool(@QueryParam("moduleId") Integer  moduleId,
			@QueryParam("callback") String callback){
		String result = "";
		Module module = ModuleDAOImpl.getModuleById(moduleId);
		String moduleName = module.getName();
		String url = "http://apo2.jpool.intra.sina.com.cn/api/openapi/scheduler/interface.php?param={\"action\":\"get_pools_by_identifier\",\"data\":{\"identifier\":\""+moduleName+"\"},\"module\":\"shell\"}";
		LOGGER.info("getSpecificServicePool from "+moduleName+"and url is:"+url);
		try {
			result = HttpUtils.HttpGet2(url);	
			LOGGER.info("getSpecificServicePool from "+moduleName+"and result is:"+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callback+"("+result+")";
	}
	@Path("/setPlatformParame")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String setPlatformParame(@QueryParam("selection") String selection,
			@QueryParam("moduleId") Integer  moduleId,
			@QueryParam("callback") String callback){
		LOGGER.info("setPlatformParameBegin:");
		String result = "";
		Module module = ModuleDAOImpl.getModuleById(moduleId);
		String moduleName = module.getName();
		try {
			result = HttpUtils.HttpPost("http://apo2.jpool.intra.sina.com.cn/api/openapi/scheduler/interface.php", "{\"action\":\"set_pools_identifier\",\"data\":{\"identifier\":\""+moduleName+"\",\"pools\":\""+selection+"\"},\"module\":\"shell\"}");
			LOGGER.info("setPlatformParame from "+moduleName+"and result is:"+result);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callback+"("+result+")";
	}
	@Path("/getDetailTask")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String getDetailTask(@QueryParam("task_id") String task_id){
		LOGGER.info("getDetailTaskBegin:");
		String result = "";
		String result2 = "";
		try {
			while(true){
				Thread.sleep(1000*30);
				result = HttpUtils.HttpPost("http://apo2.jpool.intra.sina.com.cn/api/openapi/scheduler/interface.php", "{\"action\":\"get_task_details\",\"data\":{\"task_id\":\""+task_id+"\"},\"module\":\"shell\"}");
				LOGGER.info("task_id" + result);
				JSONObject jsonObject = JSONObject.fromObject(result);
				int code = jsonObject.getInt("code");
				if(0 != code){
					break;
				}
				LOGGER.info("code:" + code);
				JSONObject dataVO2 = (JSONObject) jsonObject.getJSONObject("data");
				LOGGER.info("dataVO2" + dataVO2.toString() );
				LOGGER.info("result2:" + result2);
				int status = dataVO2.getInt("status");
				LOGGER.info("status" + status);
				if(3 != status || 0 != status){
					result2 = dataVO2.toString();
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result2;
	}
	
	@Path("/getPreviewPool")
	@GET
	@Produces("text/json")	
	@Consumes("text/plain")
	public String getPreviewPool(@QueryParam("selection") String selection,
			@QueryParam("tag") String tag){
		LOGGER.info("getPreviewPool:"+selection);
		String result = "";
		String task_id = "";
		try {
			result = HttpUtils.HttpPost("http://apo2.jpool.intra.sina.com.cn/api/openapi/scheduler/interface.php", "{\"action\":\"preview_pool\",\"data\":{\"service_pool\":\""+selection+"\",\"tag\":\""+tag+"\"},\"module\":\"shell\"}");
			LOGGER.info("getPreviewPool" +result);
			JSONObject jsonObject = JSONObject.fromObject(result);
			int  code = (Integer) jsonObject.get("code");
			LOGGER.info(code);
			JSONObject dataVO = (JSONObject) jsonObject.getJSONObject("data");
			LOGGER.info(dataVO.get("task_id"));
			task_id = (String) dataVO.get("task_id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return task_id;
	}
}
