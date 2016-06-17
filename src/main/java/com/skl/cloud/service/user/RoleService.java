package com.skl.cloud.service.user;

import java.util.List;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.user.Role;

/**
 * 用户管理-角色公共类
 * @ClassName: RoleService
 * @Description: TODO
 * <p>Creation Date: 2016年5月27日 and by Author: zhaonao </p>
 *
 * @author $Author: zhaonao $
 * @date $Date: 2016-06-07 17:39:56 +0800 (Tue, 07 Jun 2016) $
 * @version  $Revision: 9494 $
 */
public interface RoleService {

	/**
	 * 
	 * TODO(增加角色)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param role
	 * @throws BusinessException
	 * @return void
	 * @throws
	 *
	 */
	public void addRole(Role role) throws BusinessException;

	/**
	 * 
	 * TODO(删除角色)
	 * <p>Creation Date: 2016年6月7日 and by Author: zhaonao </p>
	 * @param roleId
	 * @throws BusinessException
	 * @return void
	 * @throws
	 *
	 */
	public void deleteRoleById(Long roleId) throws BusinessException;

	/**
	 * 
	 * TODO(批量删除角色)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param roleIds
	 * @throws BusinessException
	 * @return void
	 * @throws
	 *
	 */
	public void deleteRolesByIds(List<Long> roleIds) throws BusinessException;

	/**
	 * 
	 * TODO(更新角色)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param role
	 * @throws BusinessException
	 * @return void
	 * @throws
	 *
	 */
	public void updateRole(Role role) throws BusinessException;

	/**
	 * 
	 * TODO(查询角色)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param roleId
	 * @throws BusinessException
	 * @return Role
	 * @throws
	 *
	 */
	public Role queryRoleById(Long roleId) throws BusinessException;
}
