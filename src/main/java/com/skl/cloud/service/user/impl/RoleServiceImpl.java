package com.skl.cloud.service.user.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.dao.user.RoleMapper;
import com.skl.cloud.dao.user.UserPermissionMapper;
import com.skl.cloud.model.user.Role;
import com.skl.cloud.service.user.RoleService;

/**
 * 角色实现类
 * @ClassName: RoleServiceImpl
 * @Description: TODO
 * <p>Creation Date: 2016年6月1日 and by Author: zhaonao </p>
 *
 * @author $Author: zhaonao $
 * @date $Date: 2016-06-07 17:39:56 +0800 (Tue, 07 Jun 2016) $
 * @version  $Revision: 9494 $
 */
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private UserPermissionMapper userPermissionMapper;

	@Override
	@Transactional
	public void addRole(Role role) throws BusinessException {
		roleMapper.insertRole(role);
	}

	@Override
	@Transactional
	public void deleteRoleById(Long roleId) throws BusinessException {
		// 角色数据不存在时，需删除以下表信息
		// 1. 角色表信息
		roleMapper.deleteRole(roleId);

		// 2. 用户 & 角色表信息
		userPermissionMapper.deleteUserRole(null, roleId, null);

		// 3. 角色 & 权限表信息
		userPermissionMapper.deleteRolePermission(roleId, null);
	}

	@Override
	@Transactional
	public void deleteRolesByIds(List<Long> roleIds) throws BusinessException {
		for (Long roleId : roleIds) {
			this.deleteRoleById(roleId);
		}
	}

	@Override
	@Transactional
	public void updateRole(Role role) throws BusinessException {
		roleMapper.updateRole(role);
	}

	@Override
	@Transactional(readOnly = true)
	public Role queryRoleById(Long roleId) throws BusinessException {
		Role role = new Role();
		role.setId(roleId);
		return roleMapper.queryRole(role);
	}
}
