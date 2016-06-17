package com.skl.cloud.service.user;

import java.util.List;
import java.util.Set;

import com.skl.cloud.common.entity.IdEntity;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.model.user.Permission;
import com.skl.cloud.model.user.Role;

/**
 * 用户管理公共类
 * @ClassName: UserPermissionService
 * @Description: TODO
 * <p>Creation Date: 2016年5月27日 and by Author: zhaonao </p>
 *
 * @author $Author: zhaonao $
 * @date $Date: 2016-06-07 17:39:56 +0800 (Tue, 07 Jun 2016) $
 * @version  $Revision: 9494 $
 */
public interface UserPermissionService<T extends IdEntity> extends UserService<T>{

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
	public void addUserRole(Long userId, Long roleId, Long cameraId) throws BusinessException;

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
	public void addRolePermission(Long roleId, Long permissionId) throws BusinessException;

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
	 * TODO(查询-通过用户编号查询角色列表)
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
	 * TODO(查询-通过角色编号查询用户列表)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param userId
	 * @return
	 * @throws BusinessException
	 * @return List<Role>
	 * @throws
	 *
	 */
	public List<AppUser> queryUserRole(Long roleId) throws BusinessException;

	/**
	 * 
	 * TODO(查询-通过角色编号查询权限列表)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param roleId
	 * @return
	 * @throws BusinessException
	 * @return List<Permission>
	 * @throws
	 *
	 */
	public List<Permission> queryPermissionRole(Long roleId) throws BusinessException;

	/**
	 * 
	 * TODO(查询-通过权限编号查询角色列表)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param roleId
	 * @return
	 * @throws BusinessException
	 * @return List<Permission>
	 * @throws
	 *
	 */
	public List<Role> queryRolePermission(Long permissionId) throws BusinessException;
	
	/**
	 * 
	 * TODO(根据用户id获取其角色名称集合)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param userId
	 * @return
	 * @throws BusinessException
	 * @return Set<String>
	 * @throws
	 *
	 */
	public Set<String> findRoleName(Long userId) throws BusinessException;

	/**
	 * 
	 * TODO(根据用户id获取其权限集合)
	 * <p>Creation Date: 2016年6月2日 and by Author: zhaonao </p>
	 * @param userId
	 * @return
	 * @throws BusinessException
	 * @return Set<String>
	 * @throws
	 *
	 */
	public Set<String> findPermission(Long userId) throws BusinessException;
	
	/**
	 * 
	 * TODO(分配角色带设备)
	 * <p>Creation Date: 2016年6月7日 and by Author: zhaonao </p>
	 * @param userId
	 * @param userKind
	 * @param roleType
	 * @param cameraId
	 * @throws BusinessException
	 * @return void
	 * @throws
	 *
	 */
	public void assignRole(Long userId, String userKind, Integer roleType, Long cameraId) throws BusinessException;
	
	/**
	 * 
	 * TODO(分配角色不带设备)
	 * <p>Creation Date: 2016年6月7日 and by Author: zhaonao </p>
	 * @param userId
	 * @param userKind
	 * @param roleType
	 * @param cameraId
	 * @throws BusinessException
	 * @return void
	 * @throws
	 *
	 */
	public void assignRole(Long userId, String userKind, Integer roleType) throws BusinessException;
}
