package com.skl.cloud.dao.user;

import java.util.List;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.Permission;
import com.skl.cloud.model.user.Role;

/**
 * 
 * @ClassName: UserPermissionMapper
 * @Description: TODO
 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
 *
 * @author $Author: zhaonao $
 * @date $Date: 2016-06-03 11:47:58 +0800 (Fri, 03 Jun 2016) $
 * @version  $Revision: 9341 $
 */
public interface UserPermissionMapper {

	/**
	 * 
	 * TODO(增加用户&角色)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param userId
	 * @param roleId
	 * @param cameraId
	 * @throws BusinessException
	 * @return void
	 * @throws
	 *
	 */
	public void insertUserRole(Long userId, Long roleId, Long cameraId) throws BusinessException;

	/**
	 * 
	 * TODO(增加角色&权限)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param roleId
	 * @param permissionId
	 * @throws BusinessException
	 * @return void
	 * @throws
	 *
	 */
	public void insertRolePermission(Long roleId, Long permissionId) throws BusinessException;

	/**
	 * 
	 * TODO(删除用户&角色)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param userId
	 * @param roleId
	 * @param cameraId
	 * @throws BusinessException
	 * @return void
	 * @throws
	 *
	 */
	public void deleteUserRole(Long userId, Long roleId, Long cameraId) throws BusinessException;

	/**
	 *  
	 * TODO(删除角色&权限)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param roleId
	 * @param permissionId
	 * @throws BusinessException
	 * @return void
	 * @throws
	 *
	 */
	public void deleteRolePermission(Long roleId, Long permissionId) throws BusinessException;

	/**
	 * 
	 * TODO(根据角色查询用户)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param roleId
	 * @return
	 * @throws BusinessException
	 * @return List<AppUser>
	 * @throws
	 *
	 */
	public List<AppUser> queryUserRole(Long roleId) throws BusinessException;

	/**
	 * 
	 * TODO(根据用户查询角色)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param userId
	 * @return
	 * @throws BusinessException
	 * @return List<Role>
	 * @throws
	 *
	 */
	public List<Role> queryRoleUser(Long userId) throws BusinessException;

	/**
	 * 
	 * TODO(根据权限查询关联的角色列表)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param permissionId
	 * @return
	 * @throws BusinessException
	 * @return List<Role>
	 * @throws
	 *
	 */
	public List<Role> queryRolePermission(Long permissionId) throws BusinessException;

	/**
	 * 
	 * TODO(根据角色查询关联的权限列表)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param roleId
	 * @return
	 * @throws BusinessException
	 * @return List<Permission>
	 * @throws
	 *
	 */
	public List<Permission> queryPermissionRole(Long roleId) throws BusinessException;
}
