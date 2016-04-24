package com.sina.scm.data.util;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.sina.scm.data.dao.GroupDAOImpl;
import com.sina.scm.data.dao.UserDAOImpl;
import com.sina.scm.data.vo.Group;
import com.sina.scm.data.vo.User;

/**
 * 配置管理平台调用接口的权限控制
 * @author jintao3
 *
 */
public class AuthorityUtill {
	
	public static boolean hasStartAuthority(String user,Integer module_id){
		
		boolean hasStartAuthority = false;
		
		List<User> groups = UserDAOImpl.getAllGroupByName(user);
		if (CollectionUtils.isNotEmpty(groups)) {
			for (User us : groups) {
				Group group = GroupDAOImpl.getGroupById(us.getGroup_id());
				if (null != group) {
					if ("scm_group".equals(GroupDAOImpl.getGroupById(us.getGroup_id()).getName())
							|| "op_group".equals(GroupDAOImpl.getGroupById(us.getGroup_id()).getName())
							|| module_id.intValue()== group.getModule_id().intValue()) {
						
						hasStartAuthority = true;
					} 

				}

			}

		}
		
		return hasStartAuthority;
		
	}
	
public static boolean hasStartAuthority1(String user){
		
		boolean hasStartAuthority = false;
		
		List<User> groups = UserDAOImpl.getAllGroupByName(user);
		if (CollectionUtils.isNotEmpty(groups)) {
			for (User us : groups) {
				Group group = GroupDAOImpl.getGroupById(us.getGroup_id());
				if (null != group) {
					if ("scm_group".equals(GroupDAOImpl.getGroupById(us.getGroup_id()).getName())
							|| "op_group".equals(GroupDAOImpl.getGroupById(us.getGroup_id()).getName())) {
						
						hasStartAuthority = true;
					} 

				}

			}

		}
		
		return hasStartAuthority;
		
	}
}