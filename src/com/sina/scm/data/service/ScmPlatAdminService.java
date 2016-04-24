package com.sina.scm.data.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sina.scm.data.DO.Module;
import com.sina.scm.data.DO.NodeDef;
import com.sina.scm.data.DO.PiplineDef;
import com.sina.scm.data.dao.ModuleDAOImpl;
import com.sina.scm.data.dao.NodeDefDAOImpl;
import com.sina.scm.data.dao.PiplineDefDAOImpl;
import com.sina.scm.data.dao.Scm_infoDAOImpl;
import com.sina.scm.data.util.PiplineUtils;
import com.sina.scm.data.vo.ResultVO;
import com.sina.scm.data.vo.Scm_info;

/**
 * 开发关于ScmPlatAdminService的API
 * @author jintao3
 *
 */

@Path("scmPlatAdminService")
public class ScmPlatAdminService {
	
   private static final Logger LOGGER = Logger.getLogger(ScmPlatAdminService.class.getName());

	@Path("/insertModuleInfo")
	@GET
	@Produces("text/json")
	public String updateModule(@QueryParam("callback") String callback,
			@QueryParam("name") String name,
			@QueryParam("code_type") String code_type,
			@QueryParam("module_owner") String module_owner,
			@QueryParam("push_list") String push_list,
			@QueryParam("url_online") String url_online,
			@QueryParam("qb_job_def_url") String qb_job_def_url,
			@QueryParam("tool_type") String tool_type,
			@QueryParam("code_url") String code_url,
			@QueryParam("tag_url") String tag_url,
			@QueryParam("version_prefix") String version_prefix) {
		
		ResultVO resultVO = new ResultVO();
		resultVO.setMessage("updateModule success!");
		resultVO.setResult("success");		
		//1.校验参数
		if (StringUtils.isBlank(name) ||
				StringUtils.isBlank(code_type) || 
				StringUtils.isBlank(module_owner) || 
				StringUtils.isBlank(push_list) || 
				StringUtils.isBlank(url_online) || StringUtils.isBlank(qb_job_def_url) || 
				StringUtils.isBlank(tool_type) || StringUtils.isBlank(code_url) || StringUtils.isBlank(tag_url) || StringUtils.isBlank(version_prefix)) {
			resultVO.setMessage("check updateModule's parameter");
			resultVO.setResult("fail");
			
			return callback+"("+JSONObject.fromObject(resultVO).toString()+")";	
		}		
		
		Module module = new Module();
		module.setName(name);
		module.setCode_type(code_type);
		module.setModule_owner(module_owner);
		module.setPush_list(push_list);
		module.setUrl_online(url_online);
		module.setQb_job_def_url(qb_job_def_url);
		module.setStatus("online");
		module.setVersion_prefix(version_prefix);
		
		Scm_info scm_info =new Scm_info();
		scm_info.setCode_url(code_url);
		scm_info.setTag_url(tag_url);
		scm_info.setTool_type(tool_type);
		//2.判断数据库中是否已经存在唯一字段，如果有的话就做更新，没有就直接插入
		Module md = ModuleDAOImpl.getModuleByName(name);
		if(null == md){
		//3.插入module表
		Integer module_id= ModuleDAOImpl.insertModule(module);
		if(null == module_id || 0 == module_id.intValue()){
			resultVO.setMessage("name duplicate or push_list,please check!");
			resultVO.setResult("fail");
			resultVO.setModule_id(module_id);
			return callback+"("+JSONObject.fromObject(resultVO).toString()+")";	
		}
		resultVO.setModule_id(module_id);		
		//4.插入scm_info表
		scm_info.setStatus("online");
		scm_info.setModule_id(module_id);
		
		Integer scm_id = Scm_infoDAOImpl.insertScm_info(scm_info);		
		//5.更新module表里面的scm_info_id	
		ModuleDAOImpl.updateModuleToScmId(module_id, scm_id);	
		}else{
			//更新所有字段
			ModuleDAOImpl.updateModule(module);
			scm_info.setModule_id(md.getId());
			Scm_infoDAOImpl.updateSCM(scm_info);	
			resultVO.setModule_id(md.getId());
		}	
				
		return callback+"("+JSONObject.fromObject(resultVO).toString()+")";		
	}
	
	@Path("/insertPiplineAndNodeDef")
	@GET
	@Produces("text/json")
	public String insertPiplineAndNodeDef(@QueryParam("callback") String callback,
			@QueryParam("pipeline_type") String pipeline_type,
			@QueryParam("module_id") Integer module_id,
			@QueryParam("package_type") String package_type,
			@QueryParam("pipeline_owner") String pipeline_owner,
			@QueryParam("pipelinename") String pipelineName,
			@QueryParam("node") String node){
		
		LOGGER.info("module_id is:"+module_id+" and the node is:"+node);
		
		ResultVO resultVO = new ResultVO();
		resultVO.setMessage("updateModccess!");
		resultVO.setResult("suule success");	
		//1.检查参数不能为空
		if (StringUtils.isBlank(pipelineName) ||
				StringUtils.isBlank(pipeline_type) || 
				StringUtils.isBlank(package_type) || 
				StringUtils.isBlank(pipeline_owner) || 
				null == module_id || StringUtils.isBlank(node)) {
			resultVO.setMessage("check insertPiplineAndNodeDef parameter");
			resultVO.setResult("fail");
			
			return callback+"("+JSONObject.fromObject(resultVO).toString()+")";		
		}	
		//2.先初始化pipelinedef表
		PiplineDef piplineDef = new PiplineDef();
		piplineDef.setName(pipelineName);
		piplineDef.setPipeline_type(pipeline_type);
		piplineDef.setPackage_type(package_type);
		piplineDef.setModule_id(module_id);
		piplineDef.setPipeline_owner(pipeline_owner);
		piplineDef.setStatus("online");
		Integer pipeline_def_id = PiplineDefDAOImpl.insertPipelineDef(piplineDef);
		//3.初始化node_def
		JSONArray nodes = JSONArray.fromObject(node);
		List<NodeDef> nds = new ArrayList<NodeDef>();
		for (int i = 0; i < nodes.size(); i++) {
			JSONObject nd = nodes.getJSONObject(i);
			//开始往数据库里面插入node_def
			NodeDef node_def = new NodeDef();
			node_def.setName(nd.getString("name"));
			node_def.setType(nd.getString("type"));
			node_def.setJob_token(nd.getString("job_url"));
			node_def.setJob_url("jenkins:http://platform.ci.intra.weibo.com/jenkins/job/"+nd.getString("job_url")+"/");
			Integer node_def_id = NodeDefDAOImpl.insertNodeDef(node_def);		
			node_def.setId(node_def_id);
			node_def.setAction_mail_list(nd.getString("notification_mail"));
			node_def.setStart_next_node(nd.getString("start_next_node"));
			node_def.setTest_type(nd.getString("test_type"));
			
			nds.add(node_def);
		 }
		//4.初始化pipeline_node_def表
		PiplineUtils.initPipelineAndNodeDef(pipeline_def_id, nds);
		//5.生成jenkins的job
		//build的类型需要修改构建命令，svn放打好包的地址以及哪个子模块需要打包(问开发)
		//6.新建svn存储空间		
		return callback+"("+JSONObject.fromObject(resultVO).toString()+")";	
	}		
	
	@Path("/getSpecificModuleAndUrl")
	@GET
	@Produces("text/json")
	//返回查询结果的json字符串
	public String getSpecificModuleAndUrl(@QueryParam("callback") String callback,
			@QueryParam("module_id") Integer module_id) {

		String m_json = "please check getSpecificModuleCase!";
		if (null != module_id) {

			List<Scm_info> sif = Scm_infoDAOImpl.getModuleTypeAndUrlById(module_id);
			JSONArray jsonArray = JSONArray.fromObject(sif);  
			m_json = jsonArray.toString();		

		}
		return callback+"("+m_json+")";
	}
	
	
}

