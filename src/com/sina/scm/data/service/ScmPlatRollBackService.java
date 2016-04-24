package com.sina.scm.data.service;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sina.scm.data.DO.PiplineCase;
import com.sina.scm.data.dao.PiplineCaseDAOImpl;
import com.sina.scm.data.dao.PiplineDefDAOImpl;
import com.sina.scm.data.util.PiplineAndNodeStatus;
import com.sina.scm.data.vo.ResultVO;
import com.sina.scm.data.vo.RollBackHistoryVO;
import com.sina.scm.data.vo.RollBackVO;


/**
 * 开发关于ScmPlatReleaseService的API
 * @author jintao3
 *
 */

@Path("scmPlatRollBackService")
public class ScmPlatRollBackService {
	
	private static final Logger LOGGER = Logger
			.getLogger(ScmPlatRollBackService.class.getName());

	@Path("/getAllSucessHistoryVersionByModuleId")
	@GET
	@Produces("text/json")
	public String getAllSucessHistoryVersionByModuleId(@QueryParam("callback") String callback,
			@QueryParam("moduleId") Integer moduleId,
			@QueryParam("ci_version") String ci_version) {
		
		RollBackHistoryVO rollBackHistoryVO = new RollBackHistoryVO();
		List<RollBackVO> piplineCases = new ArrayList<RollBackVO>();
		ResultVO resultVO = new ResultVO();
		resultVO.setResult(PiplineAndNodeStatus.SUCCESS);
		resultVO.setMessage("getAllSucessHistoryVersionByModuleId is success");
		//1.检查参数
		if(null == moduleId){
			resultVO.setMessage("getAllSucessHistoryVersionByModuleId is fail,please check the parameter moduleId");
			rollBackHistoryVO.setResultVO(resultVO);
			return callback+"("+JSONObject.fromObject(rollBackHistoryVO).toString()+")";
		}
		//2.根据ci_version是否为空，分两种业务逻辑，如下：
	    //3.如果，ci_verison为空则：moduleId在pipelineCase表中找出，状态为成功的所有版本号，并且按照降序
		if(StringUtils.isBlank(ci_version)){
			List<PiplineCase> pipelineCases = PiplineCaseDAOImpl.getAllSucessHistoryVersionByModuleId(moduleId);
			for(PiplineCase pc : pipelineCases){
				//过滤掉回滚的version
				if(!"rollback".equals(PiplineDefDAOImpl.getPiplineByDefId(pc.getPipeline_def_id()).getPipeline_type())){
					
				RollBackVO rollBackVO = new RollBackVO();
				rollBackVO.setSvn_url(pc.getRelease_package_url());
				rollBackVO.setVersion(pc.getVersion());
				piplineCases.add(rollBackVO);
				
				}
			}
		}else{
			//4.如果，ci_version不为空，只需要查出ci_verison对应的svn_tag即可，是唯一一条记录
		    //piplineCases.add(PiplineCaseDAOImpl.getOneSucessHistoryVersionByModuleId(moduleId, ci_version));		
		}
		
		rollBackHistoryVO.setPipelineCase(piplineCases);
		return callback+"("+JSONObject.fromObject(rollBackHistoryVO).toString()+")";
		
	}		
	
}