package com.sina.scm.data.service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.apache.log4j.Logger;

import com.sina.scm.data.DO.Module;
import com.sina.scm.data.DO.NodeDef;
import com.sina.scm.data.DO.PiplineCase;
import com.sina.scm.data.DO.PiplineDef;
import com.sina.scm.data.DO.Template;
import com.sina.scm.data.dao.GroupDAOImpl;
import com.sina.scm.data.dao.ModuleDAOImpl;
import com.sina.scm.data.dao.ModuleVersionDAOImpl;
import com.sina.scm.data.dao.PiplineCaseDAOImpl;
import com.sina.scm.data.dao.PiplineDefDAOImpl;
import com.sina.scm.data.dao.RollBackDAOImpl;
import com.sina.scm.data.dao.Scm_infoDAOImpl;
import com.sina.scm.data.dao.TemplateDAOImpl;
import com.sina.scm.data.dao.UserDAOImpl;
import com.sina.scm.data.util.AuthorityUtill;
import com.sina.scm.data.util.Pagination;
import com.sina.scm.data.util.PiplineAndNodeStatus;
import com.sina.scm.data.util.PiplineUtils;
import com.sina.scm.data.vo.Group;
import com.sina.scm.data.vo.GroupAndUserVO;
import com.sina.scm.data.vo.ModuleHistoryVO;
import com.sina.scm.data.vo.ModuleVersion;
import com.sina.scm.data.vo.ModuleVersionVO;
import com.sina.scm.data.vo.PipelineVO;
import com.sina.scm.data.vo.PiplineCaseAndType;
import com.sina.scm.data.vo.PiplineCountVO;
import com.sina.scm.data.vo.ReleaseHistoryVO;
import com.sina.scm.data.vo.ReleaseVO;
import com.sina.scm.data.vo.ResultVO;
import com.sina.scm.data.vo.RollBack;
import com.sina.scm.data.vo.Scm_info;
import com.sina.scm.data.vo.TemplateVO;
import com.sina.scm.data.vo.User;

/**
 * 开发关于ScmPlatReleaseService的API
 * @author jintao3
 *
 */

@Path("scmPlatReleaseService")
public class ScmPlatReleaseService {
	private static final Logger LOGGER = Logger
			.getLogger(ScmPlatReleaseService.class.getName());
	
   //private static final Logger LOGGER = Logger.getLogger(ScmPlatReleaseService.class.getName());

	@Path("/getReleaseHistory")
	@GET
	@Produces("text/json")
	public String getReleaseHistory(@QueryParam("callback") String callback,
			@QueryParam("pagecount") String pagecount,
			@QueryParam("pagenum") Integer pagenum,
			@QueryParam("moduleId") Integer moduleId,
			@QueryParam("piplineDefId") Integer piplineDefId) {
		
		ReleaseHistoryVO releaseVO = new ReleaseHistoryVO(); 
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(PiplineAndNodeStatus.SUCCESS);
		resultVO.setMessage("getReleaseHistory is success");
		//1.check parameter
		if(null == moduleId){
			resultVO.setMessage("getReleaseHistory is fail,please check the parameter moduleId");
			releaseVO.setResultVO(resultVO);			
			return callback+"("+JSONObject.fromObject(releaseVO).toString()+")";
		}
		//2.初始化分页器		
		Pagination pagination = null;
		if(StringUtils.isNotBlank(pagecount)){
			pagination = new Pagination(pagenum,Integer.valueOf(pagecount));
		}
		//3.根据piplineDefId的值，找到对应的pipeline的类型，分两种情况：
		//如果类型是rollback，历史记录只显示回滚的
		List<PiplineCaseAndType> listReleasHistory = new ArrayList<PiplineCaseAndType>();
		List<PiplineCase> pcs = new ArrayList<PiplineCase>();
		List<PiplineCase> pcs_not_rollback = new ArrayList<PiplineCase>();
		if("rollback".equals(PiplineDefDAOImpl.getPiplineByDefId(piplineDefId).getPipeline_type())){
			//只显示回滚的历史记录
			if(StringUtils.isBlank(pagecount)){
				pagination = new Pagination(pagenum,PiplineCaseDAOImpl.getAllPiplinecaseByPdef(piplineDefId).size());
				pagecount = String.valueOf(PiplineCaseDAOImpl.getAllPiplinecaseByPdef(piplineDefId).size());
			}
			pagination.setTotal(PiplineCaseDAOImpl.getAllPiplinecaseByPdef(piplineDefId).size());
			pcs = PiplineCaseDAOImpl.getAllPiplinecaseBypdfId(piplineDefId, pagination.getStart(), Integer.valueOf(pagecount));	
			releaseVO.setPipeline_type("rollback");
		}else{
			//非回滚类型的，除了回滚以外的全部显示
			List<PiplineCase> PiplineCases= PiplineCaseDAOImpl.getAllPiplinecaseByModuleId(moduleId);
			//过滤类型是回滚的，不显示
			if(CollectionUtils.isNotEmpty(PiplineCases)){
				for (PiplineCase pc : PiplineCases) {
					if (null != pc && null != pc.getPipeline_def_id()) {
						if(null != PiplineDefDAOImpl.getPiplineByDefId(pc.getPipeline_def_id())){
							String p_type = PiplineDefDAOImpl.getPiplineByDefId(pc.getPipeline_def_id()).getPipeline_type();
						
						
						if (!"rollback".equals(p_type)) {
							pcs_not_rollback.add(pc);
						}

					}}}
			}
			if(StringUtils.isBlank(pagecount)){
				pagination = new Pagination(pagenum,pcs_not_rollback.size());
			}
			pagination.setTotal(pcs_not_rollback.size());
			if(pagination.getEnd() >= pcs_not_rollback.size()){
				pcs = pcs_not_rollback.subList(pagination.getStart(),pcs_not_rollback.size());
			}else{
				pcs = pcs_not_rollback.subList(pagination.getStart(),pagination.getEnd());
			}
						
		}	
		if(CollectionUtils.isNotEmpty(pcs)){
			for(PiplineCase pc : pcs){
				if("rollback".equals(PiplineDefDAOImpl.getPiplineByDefId(piplineDefId).getPipeline_type())){
					RollBack rollBack = RollBackDAOImpl.getRollBackByToVersion(pc.getVersion());
					if(null != rollBack && StringUtils.isNotBlank(rollBack.getFrom_version())){
						pc.setFrom_version(rollBack.getFrom_version());
					}
				}						
				PiplineDef pipeLineDef = PiplineDefDAOImpl.getPiplineByDefId(pc.getPipeline_def_id());
				PiplineCaseAndType piplineCaseAndType = new PiplineCaseAndType();
				piplineCaseAndType.setPiplineCase(pc);
				piplineCaseAndType.setType(pipeLineDef.getName());
				if(null != pipeLineDef.getRollback_pipeline_def_id()){
					piplineCaseAndType.setRollback_pipeline_def_id(pipeLineDef.getRollback_pipeline_def_id());
				    piplineCaseAndType.setRollback_pipeline_name(PiplineDefDAOImpl.getPiplineByDefId(pipeLineDef.getRollback_pipeline_def_id()).getName());
				    piplineCaseAndType.setRollback_package_type((PiplineDefDAOImpl.getPiplineByDefId(pipeLineDef.getRollback_pipeline_def_id()).getPackage_type()));				
				}
				
				listReleasHistory.add(piplineCaseAndType);				
			}
		}
		//5.组成最终的json串是：
		releaseVO.setListReleasHistory(listReleasHistory);
		releaseVO.setResultVO(resultVO);
		releaseVO.setTotalCount(pagination.getTotal());
		releaseVO.setTotalPage(pagination.getTotalPage());	
		releaseVO.setModule_version(ModuleDAOImpl.getModuleById(moduleId).getCurrent_online_version());		
		releaseVO.setModule_name(ModuleDAOImpl.getModuleById(moduleId).getName());	
		
		return callback+"("+JSONObject.fromObject(releaseVO).toString()+")";
	}
	
	
	
	@Path("/getSpecificPiplineCase")
	@GET
	@Produces("text/json")
	public String getSpecificPiplineCase(@QueryParam("callback") String callback,
			@QueryParam("pipelineCaseId") Integer pipeline_case_id) {

		String p_json = "please check getSpecificPiplineCase!";
		if (null != pipeline_case_id) {

			p_json = PiplineCaseDAOImpl.getPipelineCaseById(pipeline_case_id)
					.getPipeline_case_json();

		}
		return callback+"("+p_json+")";
	}
	
	
	@Path("/getTemplateListBytemplate")
	@GET
	@Produces("text/json")
	public String getTemplateListBytemplate(@QueryParam("callback") String callback) {
		int pipeDefId; 
		List<TemplateVO> releaseList = new ArrayList<TemplateVO>();
		List<Template> temps = TemplateDAOImpl.getTemplateList();
		if (CollectionUtils.isNotEmpty(temps)) {
			for (Template temp : temps) {
				pipeDefId = temp.getPipeline_def_id();
                if( null !=PiplineDefDAOImpl.getPiplineByDefId(pipeDefId)){
                	TemplateVO templateVO = new TemplateVO();
                	//PiplineDef pdef = PiplineDefDAOImpl.getPiplineByDefId(pipeDefId);
                	templateVO.setTemplate_name(temp.getName());
                	templateVO.setTemplate_id(temp.getId());
                	
                 	releaseList.add(templateVO);
                }					
			}
		} 
		return callback+"("+JSONArray.fromObject(releaseList).toString()+")";
		
	}
	@Path("/getTemplateById")
	@GET
	@Produces("text/json")
	public String getTemplateById(@QueryParam("callback") String callback,
			@QueryParam("templateId") Integer templateId) {
		int pipeDefId; 
		TemplateVO templateVO = new TemplateVO();
		Template temp = TemplateDAOImpl.getTemplateById(templateId);
		if (null != temp) {
			
				pipeDefId = temp.getPipeline_def_id();
                if( null !=PiplineDefDAOImpl.getPiplineByDefId(pipeDefId)){
                	
                	PiplineDef pdef = PiplineDefDAOImpl.getPiplineByDefId(pipeDefId);
                	templateVO.setTemplate_name(temp.getName());
                	templateVO.setTemplate_id(temp.getId());
                	String pipline_json = pdef.getPipeline_json();
            		if(StringUtils.isBlank(pipline_json)){
            			LOGGER.info("pipline_json is empty!");
            			return "-1 and pipline_json";
            		}
            		List<NodeDef> nodes = PiplineUtils.getAllNodeDefByPiplineDefId2(pipline_json);
            		if(CollectionUtils.isEmpty(nodes)){
            			LOGGER.info("nodes is empty!");
            			return "-1 and nodes";
            		}
            		templateVO.setNode(nodes);
            		
               				
			}
		} 
		return callback+"("+JSONObject.fromObject(templateVO).toString()+")";
		
	}
	
	@Path("/getReleaseList")
	@GET
	@Produces("text/json")
	public String getReleaseList(@QueryParam("callback") String callback,
			@QueryParam("username") String username,
			@QueryParam("pagecount") String pagecount,
			@QueryParam("pagenum") Integer pagenum,
			@QueryParam("name") String name,
			@QueryParam("stage") String stage) {	
		
		if(StringUtils.isNotBlank(name)){
			name = name.trim();
		}
		Integer count = null;
		List<PiplineDef> releaseList = new ArrayList<PiplineDef>();
		Pagination pagination = null;
		if(StringUtils.isNotBlank(pagecount)){
			pagination = new Pagination(pagenum,Integer.valueOf(pagecount));
		}
		//1.根据username是否为空，判断是列出“我的发布”还是“全部发布”
		//2.只需要列出status为online的全部记录
		if ("running".equals(stage)) {
			count = PiplineCaseDAOImpl.getRunningPiplineCaseCount();
			if(StringUtils.isBlank(pagecount)){
				pagecount = String.valueOf(count);
				pagination = new Pagination(pagenum,count);
			}
			pagination.setTotal(count);

			List<Integer> ids = PiplineCaseDAOImpl.getRunningPiplineDefId(pagination.getStart(), Integer.valueOf(pagecount));
			if (CollectionUtils.isNotEmpty(ids)) {
				for (Integer id : ids) {			
                    if( 0 !=PiplineDefDAOImpl.getAllReleaseCount1(id,name)){
                    	releaseList.add(PiplineDefDAOImpl.getPiplineByDefId1(id,name));
                    }					
				}
			}
		} else {
			//****************************
			//1.非scm和op的人员
			if(!AuthorityUtill.hasStartAuthority1(username)){
				//2.得出用户相关联的module_id				
				List<User> groups = UserDAOImpl.getAllGroupByName(username);
				List<Integer> module_ids = new ArrayList<Integer>();
				if(CollectionUtils.isNotEmpty(groups)){
					for (User us : groups) {
						Group group = GroupDAOImpl.getGroupById(us.getGroup_id());
						module_ids.add(group.getModule_id());						
					}					
				//3.拼接sql语句
				count = PiplineDefDAOImpl.getAllReleaseCount2(name, module_ids);
				if(StringUtils.isBlank(pagecount)){
					pagination = new Pagination(pagenum,count);
					pagecount = String.valueOf(count);
				}
				pagination.setTotal(count);
				releaseList = PiplineDefDAOImpl.getAllReleaseList5(pagination.getStart(), Integer.valueOf(pagecount), name,module_ids);
				}
			}else{			
			//****************以下是old*************	
			count = PiplineDefDAOImpl.getAllReleaseCount(name);
			if(StringUtils.isBlank(pagecount)){
				pagination = new Pagination(pagenum,count);
				pagecount = String.valueOf(count);
			}
			pagination.setTotal(count);
			releaseList = PiplineDefDAOImpl.getAllReleaseList(pagination.getStart(), Integer.valueOf(pagecount), name);
		  }
		}
		if(CollectionUtils.isNotEmpty(releaseList)){
			
			for(PiplineDef pl : releaseList){
				if (null != pl) {
					String fail_percent = getPiplineCountByModuleId(pl.getModule_id(),null,null);
					Module module = ModuleDAOImpl.getModuleById(pl.getModule_id());
					if(null != module){
					pl.setFail_percent(fail_percent);
					pl.setModule_name(module.getName());
					pl.setModule_version(module.getCurrent_online_version());}}
			}
		}
		
		ReleaseVO releaseVO = new ReleaseVO();
		if(releaseList.size()>0){
			releaseVO.setTotalCount(count);
		    releaseVO.setTotalPage(pagination.getTotalPage());
		}else{
			releaseVO.setTotalCount(0);
		    releaseVO.setTotalPage(0);
		}
		
		releaseVO.setListRelease(releaseList);
		
		return callback+"("+JSONObject.fromObject(releaseVO).toString()+")";					
	}	
	
	public String getPiplineCountByModuleId(@QueryParam("moduleId") Integer moduleId,
			@QueryParam("start_time") String start_time,
			@QueryParam("end_time") String end_time
			) {	
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
		return percent;
			
	}
	
	
	
	@Path("/getAllModuleCurrentVersion")
	@GET
	@Produces("text/json")
	public String getAllModuleCurrentVersion(@QueryParam("callback") String callback,
			@QueryParam("pagecount") String pagecount,
			@QueryParam("pagenum") Integer pagenum,
			@QueryParam("name") String name,
			@QueryParam("isAll") String isAll,
			@QueryParam("product_id") String productId) {
		
		ModuleVersionVO moduleVersionVO = new ModuleVersionVO();
		List<ModuleHistoryVO> moduleHistoryVO = new ArrayList<ModuleHistoryVO>();
		//List<GroupAndUserVO> group_name = new ArrayList<GroupAndUserVO>();
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(PiplineAndNodeStatus.SUCCESS);
		resultVO.setMessage("getReleaseHistory is success");
		
		Pagination pagination = null;
		Integer moduleCount = null;
		List<Module> mds = new ArrayList<Module>();
		
		if("true".equalsIgnoreCase(isAll)){
			moduleCount = ModuleDAOImpl.getAllModuleCount(name);
		}else{
			moduleCount = ModuleDAOImpl.getModuleCount(name);
		}
		
		if(StringUtils.isNotBlank(pagecount)){
			pagination = new Pagination(pagenum,Integer.valueOf(pagecount));
		}else{
			pagination = new Pagination(pagenum,moduleCount);
			pagecount = String.valueOf(moduleCount);
		}
		
		if("true".equalsIgnoreCase(isAll)){
			mds  = ModuleDAOImpl.getIsAllModule(pagination.getStart(), Integer.valueOf(pagecount),name);
		}else{
			mds  = ModuleDAOImpl.getAllModule(pagination.getStart(), Integer.valueOf(pagecount),name);
		}
		//1.找出所有的module的信息
		pagination.setTotal(moduleCount);
		//List<Module> mds  = ModuleDAOImpl.getAllModule(pagination.getStart(), Integer.valueOf(pagecount),name);		
		for(Module md : mds){
			String current_version = md.getCurrent_online_version();
			ModuleHistoryVO moduleHistoryVO1 =new ModuleHistoryVO();
			moduleHistoryVO1.setId(md.getId());
			moduleHistoryVO1.setCurrent_online_version(current_version);
			moduleHistoryVO1.setName(md.getName());
			moduleHistoryVO1.setCode_type(md.getCode_type());
			moduleHistoryVO1.setModule_owner(md.getModule_owner());
			moduleHistoryVO1.setVersion_prefix(md.getVersion_prefix());
			moduleHistoryVO1.setQb_job_def_url(md.getQb_job_def_url() );
			
			//moduleHistoryVO1.setUpdate_version_time(md.getUpdate_version_time());
			//如果当前版本，在rollback表里，就取回滚表里面的操作人和时间
			RollBack rollback = RollBackDAOImpl.getRollBackByToVersion(current_version);
			if(null != rollback && StringUtils.isNotBlank(rollback.getTo_version())){
				moduleHistoryVO1.setUpdate_version_time(rollback.getRollback_date());
				moduleHistoryVO1.setOperator(rollback.getOperator());				
			}else{
				//反之，取module_version表的相关信息
				ModuleVersion mdv = ModuleVersionDAOImpl.getModuleVersionByToVersion(current_version,md.getId());
				if(null != mdv && StringUtils.isNotBlank(mdv.getVersion())){
					if(StringUtils.isBlank(mdv.getRelease_date())){
						moduleHistoryVO1.setUpdate_version_time(md.getUpdate_version_time());						
					}else{
						moduleHistoryVO1.setUpdate_version_time(mdv.getRelease_date());
					}
					moduleHistoryVO1.setOperator(mdv.getOperator());			
				}
			}	
			//根据moduleID找出自己所所包含的组
			List<Group> group= GroupDAOImpl.getGrooupByModuleId(md.getId());
			if(CollectionUtils.isNotEmpty(group)){	
				List<GroupAndUserVO> group_name = new ArrayList<GroupAndUserVO>();
				for(Group gp : group){
					GroupAndUserVO groupAndUserVO = new GroupAndUserVO();
					groupAndUserVO.setId(gp.getId());
					groupAndUserVO.setModule_id(gp.getModule_id());
					groupAndUserVO.setName(gp.getName());
					groupAndUserVO.setUser(UserDAOImpl.getUserByGroupId(gp.getId()));
					
					group_name.add(groupAndUserVO);					
				}
				moduleHistoryVO1.setGroup(group_name);	
			}
			moduleHistoryVO.add(moduleHistoryVO1);
		}
		
		moduleVersionVO.setResultVO(resultVO);		
		if (StringUtils.isBlank(productId)) {
			moduleVersionVO.setModuleHistoryVO(moduleHistoryVO);
			moduleVersionVO.setTotalCount(pagination.getTotal());
			moduleVersionVO.setTotalPage(pagination.getTotalPage());
		} else {
			List<ModuleHistoryVO> moduleHistoryVO_product = new ArrayList<ModuleHistoryVO>();
			for(ModuleHistoryVO mo : moduleHistoryVO){
				if(productId.equals(String.valueOf(ModuleDAOImpl.getModuleById(mo.getId()).getProduct_id()))){					
					moduleHistoryVO_product.add(mo);
				}				
			}
			moduleVersionVO.setModuleHistoryVO(moduleHistoryVO_product);
			if(moduleHistoryVO_product.size() == 0){
				moduleVersionVO.setTotalCount(0);
				moduleVersionVO.setTotalPage(0);				
			}else{
				pagination.setTotal(moduleHistoryVO_product.size());
				moduleVersionVO.setTotalCount(moduleHistoryVO_product.size());				
				moduleVersionVO.setTotalPage(pagination.getTotalPage());
			}					

		}
		
		return callback+"("+JSONObject.fromObject(moduleVersionVO).toString()+")";		
	}
	
	@Path("/insertUserToGroup")
	@GET
	@Produces("text/json")
	public String insertUserToGroup(@QueryParam("callback") String callback,
			@QueryParam("userName") String name,
			@QueryParam("groupId") Integer group_id) {
		
		User user =new User();
		user.setGroup_id(group_id);
		user.setName(name);
		
		return callback+"("+String.valueOf(UserDAOImpl.insertUserToGroup(user))+")";	
		
	}
	
	@Path("/deleteUserToGroup")
	@GET
	@Produces("text/json")
	public String deleteUserToGroup(@QueryParam("callback") String callback,
			@QueryParam("userName") String name,
			@QueryParam("groupId") Integer group_id) {
		
		return callback+"("+String.valueOf(UserDAOImpl.deleteUserToGroup(name, group_id))+")";		
	}	
	
	
	@Path("/updateModule")
	@GET
	@Produces("text/json")
	public String updateModule(@QueryParam("callback") String callback,
			@QueryParam("name") String name,
			@QueryParam("version_prefix") String version_prefix,
			@QueryParam("module_owner") String module_owner,@QueryParam("moduleId") Integer moduleId) {
		
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(PiplineAndNodeStatus.SUCCESS);
		resultVO.setMessage("updateModule is success");
		//1.check parameter
		if(null == moduleId || StringUtils.isBlank(name) || StringUtils.isBlank(version_prefix) || StringUtils.isBlank(module_owner)){
			resultVO.setMessage("updateModule is fail,please check the parameter moduleId,name and so on");
			return callback+"("+JSONObject.fromObject(resultVO).toString()+")";
		}
		//2.更新
		ModuleDAOImpl.updateModule(name, version_prefix, module_owner, moduleId);
		
		return callback+"("+JSONObject.fromObject(resultVO).toString()+")";	
	}
	
	@Path("/insertGroup")
	@GET
	@Produces("text/json")
	//添加成功返回json有问题
	public String insertGroup(@QueryParam("callback") String callback,
			@QueryParam("groupName") String name,
			@QueryParam("module_id") Integer module_id) {
		ResultVO resultVO = new ResultVO();
		if(null == GroupDAOImpl.getGroupByNameAndModuleId(module_id,name)){
			Group group =new Group();
			group.setName(name);
			group.setModule_id(module_id);
			String temp = String.valueOf(GroupDAOImpl.insertGroup(group));
			resultVO.setMessage(temp);	
		}else{
			resultVO.setMessage("group already exist");	
		}
		return callback+"("+JSONObject.fromObject(resultVO).toString()+")";
		
	}
	
	@Path("/deleteGroup")
	@GET
	@Produces("text/json")
	public String deleteGroup(@QueryParam("callback") String callback,
			@QueryParam("groupName") String name,
			@QueryParam("module_id") Integer module_id) {              
		if(null != GroupDAOImpl.getGroupByNameAndModuleId(module_id,name)){
			Group group = GroupDAOImpl.getGroupByNameAndModuleId(module_id,name);
			int group_id = group.getId();
			UserDAOImpl.deleteUserFromGroup(group_id);
			return callback+"("+String.valueOf(GroupDAOImpl.deleteGroup(name, module_id))+")";
			
		}
		
		return callback+"("+String.valueOf(0)+")";		
	}
	
	@Path("/getPipelineCount")
	@GET
	@Produces("text/json")
	public String getPipelineCount(@QueryParam("callback") String callback,
			@QueryParam("pipelineName") String name) {
		PipelineVO pipelineVO = new PipelineVO();
		int count = PiplineDefDAOImpl.getAllReleaseCount3(name);
		pipelineVO.setCount(count);
		return callback+"("+JSONObject.fromObject(pipelineVO)+")";		
	}	
	@Path("/getPipelineByRepo")
	@GET
	@Produces("text/json")
	public String getPipelineByRepo(@QueryParam("callback") String callback,
			@QueryParam("repo_url") String repo_url) {
		//从scm_info表中选出module_id
		Scm_info scm_info = Scm_infoDAOImpl.getModuleIdByCodeUrl(repo_url);
		PipelineVO pipelineVO = new PipelineVO();
		if(null != scm_info){
			int module_id = scm_info.getModule_id();
			//根据module_id从pipeline_def表中选出name
			String name = PiplineDefDAOImpl.getPiplineNameByModuleId(module_id);
			pipelineVO.setMessage("success");
			pipelineVO.setName(name);
			return callback+"("+JSONObject.fromObject(pipelineVO)+")";
		}else{
			pipelineVO.setMessage("error");
			return callback+"("+JSONObject.fromObject(pipelineVO)+")";
		}
	}
}
