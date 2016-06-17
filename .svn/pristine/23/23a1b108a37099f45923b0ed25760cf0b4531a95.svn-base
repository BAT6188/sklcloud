package com.skl.cloud.controller.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.model.AppCircle;
import com.skl.cloud.model.AppCircleMember;
import com.skl.cloud.model.AppDeviceShare;
import com.skl.cloud.model.OwnSharedDevices;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.model.user.AppUser;
import com.skl.cloud.service.AppMyCircleMgtService;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.constants.Constants;
import com.skl.cloud.util.validator.XmlElementValidator;

/**
 * @ClassName: AppMyGroupMgtController
 * @Description: 家庭组（圈子）管理-Controller层
 * @author zhaonao
 * @date 2015年09月01日
 *
 */
@Controller
@RequestMapping("/skl-cloud/app/Security/AAA/users/family")
public class AppMyCircleMgtController extends BaseController {
    private static final Logger logger = Logger.getLogger(AppMyCircleMgtController.class);

    @Autowired(required = true)
    private AppMyCircleMgtService appMyCircleMgtService;

    @Autowired(required = true)
    private AppUserService appUserService;

    @Autowired(required = true)
    private S3Service s3Service;
    
    @Autowired
    private IPCameraService ipcService;
    
    /**
     * <8.1 app用户创建新的家庭组>
     * 
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createCircle(HttpServletRequest req, HttpServletResponse resp) {
        // 返回信息(初始化）
        String sReturn = null;

        try {
            // 根据请求返回参数map
            Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);

            // 对参数进行获取，并同步进行isNull校验
            String circleName = XmlUtil.convertToString(paraMap.get("circleName"), false);
            String portraitId = XmlUtil.convertToString(paraMap.get("portraitId"), false);

            // 家庭组不支持自定义组头像
            if ("-1".equals(portraitId)) {
                throw new BusinessException("0x50020071");
            }

            AppCircle appCircle = new AppCircle();
            appCircle.setCircleName(circleName);
            appCircle.setPortraitId(portraitId);

			// 增加一个家庭组
			appMyCircleMgtService.addCircle(appCircle);

			AppUser appUser = appUserService.getUserById(getUserId());
			AppCircleMember appCircleMember = new AppCircleMember();
			appCircleMember.setCircleId(appCircle.getCircleId());
			appCircleMember.setMemberId(String.valueOf(appUser.getId()));
			appCircleMember.setMemberNickName(appUser.getName());
			appCircleMember.setMemberCircleRole(String.valueOf(Constants.MyCircleRole.admin));
			appCircleMember.setIsNew("true");
			
			// 增加当前用户为此家庭成员（默认权限admin）,包含设备的分享
			appMyCircleMgtService.addCircleMember(appCircleMember);

            // 返回结果组装（正确）
            Map<String, Object> backMap = new LinkedHashMap<String, Object>();
            backMap.put("circleId", appCircle.getCircleId());
            sReturn = XmlUtil.mapToXmlRight("appNewFamily", backMap);
        } catch (Exception e) {
            sReturn = getErrorXml("appNewFamily", e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * <8.2 app用户修改家庭组名称或图标>
     * 
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<String> changeCircle(HttpServletRequest req, HttpServletResponse resp) {
        String userId = String.valueOf(getUserId());
        String sReturn = null;

        try {
            Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);

            String circleId = XmlUtil.convertToString(paraMap.get("circleId"), false);
            String newCircleName = XmlUtil.convertToString(paraMap.get("newCircleName"), true);
            String portraitId = XmlUtil.convertToString(paraMap.get("portraitId"), true);

            // 校验：用户是管理员
            appMyCircleMgtService.isCircleAdmin(true, circleId, userId);

            // 校验通过，则执行app请求
            AppCircle appCircle = new AppCircle();
            appCircle.setCircleId(circleId);

            // newCircleName,portraitId为可选填字段，但不允许两个均为null或空
            int count = 0;
            if (newCircleName != null && !newCircleName.isEmpty()) {
                appCircle.setCircleName(newCircleName);
                count++;
            }
            if (portraitId != null && !portraitId.isEmpty()) {
                appCircle.setPortraitId(portraitId);
                count++;
            }

            if (count == 0) {
                throw new BusinessException("0x50000029");
            }

            appMyCircleMgtService.updateCircle(appCircle);
            sReturn = XmlUtil.mapToXmlRight();
        } catch (Exception e) {
            sReturn = getErrorXml(e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * <8.3 app用户添加家庭组成员>
     * 
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> addCircleMember(HttpServletRequest req, HttpServletResponse resp) {
        String userId = String.valueOf(getUserId());
        String sReturn = null;

        try {
            Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);

            String circleId = XmlUtil.convertToString(paraMap.get("circleId"), false);
            String email = XmlUtil.convertToString(paraMap.get("email"), false);

            // 校验: 当前用户是管理员
            appMyCircleMgtService.isCircleAdmin(true, circleId, userId);

            // 校验: 根据email查询待添加的用户信息(存在)
            AppUser appUser = appUserService.getAppUserByEmail(email);
            if (appUser == null) {
                throw new BusinessException("0x50020031");
            }

            // 校验: 待添加用户是不是此家庭组的成员
            appMyCircleMgtService.isCircleMember(false, circleId, String.valueOf(appUser.getId()));

            // 校验：待添加用户所加入的家庭组不能超过20
            long count = appMyCircleMgtService.getCountAtCircle(String.valueOf(appUser.getId()));
            if (count >= 20) {
                throw new BusinessException("0x50020070");
            }

            // 校验通过，则执行app请求
            AppCircleMember appCircleMember = new AppCircleMember();
            appCircleMember.setCircleId(circleId);
            appCircleMember.setMemberId(String.valueOf(appUser.getId()));
            appCircleMember.setMemberNickName(appUser.getName());
            appCircleMember.setMemberCircleRole(String.valueOf(Constants.MyCircleRole.user));
            appCircleMember.setIsNew("false");

			appMyCircleMgtService.addCircleMember(appCircleMember);

            Map<String, Object> backMap = new LinkedHashMap<String, Object>();
            backMap.put("circleId", circleId);
            backMap.put("nickName", appUser.getName());
            backMap.put("circleRole", Constants.MyCircleRole.user);
            backMap.put("userId", appUser.getId());

            HashMap<String, Object> portraitMap = new HashMap<String, Object>();
            String portraitId = appUser.getPortraintId();
            if ("-1".equals(portraitId)) {
                // 自定义头像
            	S3FileData file = s3Service.getUploadFileByUuid(appUser.getPortraintUuid());
                portraitMap.put("url", file.getFilePath() + (file.getFileName() == null ? "" : file.getFileName()));
            } else {
                // 缺省头像
                portraitMap.put("id", portraitId);
            }
            backMap.put("portrait", portraitMap);
            sReturn = XmlUtil.mapToXmlRight("addFamilyMember", backMap);
        } catch (Exception e) {
            sReturn = getErrorXml("addFamilyMember", e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * <8.4 app用户删除家庭组成员>
     * 
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "/deleteMember", method = RequestMethod.POST)
    public ResponseEntity<String> deleteCircleMember(HttpServletRequest req, HttpServletResponse resp) {
        String userId = String.valueOf(getUserId());
        String sReturn = null;

        try {
            Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);

            String circleId = XmlUtil.convertToString(paraMap.get("circleId"), false);
            String email = XmlUtil.convertToString(paraMap.get("email"), false);

            // 校验：app用户是管理员
            appMyCircleMgtService.isCircleAdmin(true, circleId, userId);

            // 校验：目标用户存在
            AppUser appUser = appUserService.getAppUserByEmail(email);
            if (appUser == null) {
                throw new BusinessException("0x50020031");
            }

            // 校验：目标用户是家庭组成员
            appMyCircleMgtService.isCircleMember(true, circleId, String.valueOf(appUser.getId()));

            // （排除）目标用户是管理员
            appMyCircleMgtService.isCircleAdmin(false, circleId, String.valueOf(appUser.getId()));

            // 校验通过，则执行app请求
            appMyCircleMgtService.deleteCircleMember(circleId, String.valueOf(appUser.getId()));
            sReturn = XmlUtil.mapToXmlRight();
        } catch (Exception e) {
            sReturn = getErrorXml(e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * <8.6 app用户查询所属家庭组>
     * 
     * @param req
     * @param resp
     * @param ID
     * @return
     */
    @RequestMapping(value = "/queryCircle/{UID}", method = RequestMethod.GET)
    public ResponseEntity<String> queryCircleListByMember(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable String UID) {
        String userId = String.valueOf(getUserId());
        String sReturn = null;

        try {
            // 校验：url参数ID的合法性
            if (!userId.equals(UID)) {
                throw new BusinessException("0x50020029");
            }

            List<Map<String, Object>> reCircleList = appMyCircleMgtService.getCircleListByMberId(userId);

            // 组装返回结果
            Map<String, Object> backMap = new LinkedHashMap<String, Object>();
            List<Object> circleList = new ArrayList<Object>();

            if (reCircleList != null && reCircleList.size() > 0) {
                for (Map<String, Object> reCircleMap : reCircleList) {
                    Map<String, Object> circleMaps = new LinkedHashMap<String, Object>();
                    Map<String, Object> circleMap = new LinkedHashMap<String, Object>();
                    circleMap.put("circleName", reCircleMap.get("circleName"));
                    circleMap.put("circleId", reCircleMap.get("circleId"));
                    circleMap.put("portraitId", reCircleMap.get("criclePortraitId"));
					// 看用户是否新加入该家庭组，如果为新加入，返回“true”
					if (StringUtils.equals(reCircleMap.get("memberIsNew").toString(), "false")) {
						circleMap.put("isNew", "true");
					}else {
						circleMap.put("isNew", "false");
					}
                    circleMaps.put("circle", circleMap);
                    circleList.add(circleMaps);
                }
            }
            backMap.put("circleList", circleList);
            sReturn = XmlUtil.mapToXmlRight("queryCircleList", backMap);
        } catch (Exception e) {
            sReturn = getErrorXml("queryCircleList", e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * <8.5 app用户变更成员在家庭组权限>
     * 
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "/modifyRole", method = RequestMethod.POST)
    public ResponseEntity<String> modifyCircleMemRole(HttpServletRequest req, HttpServletResponse resp) {
        String userId = String.valueOf(getUserId());
        String sReturn = null;

        try {
            Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);

            String circleId = XmlUtil.convertToString(paraMap.get("circleId"), false);
            String email = XmlUtil.convertToString(paraMap.get("email"), false);
            String circleRole = XmlUtil.convertToString(paraMap.get("circleRole"), false);

            // 校验：当前用户是家庭组管理员
            appMyCircleMgtService.isCircleAdmin(true, circleId, userId);

            // 校验: 根据email查询待变更的用户信息(存在)
            AppUser appUser = appUserService.getAppUserByEmail(email);
            if (appUser == null) {
                throw new BusinessException("0x50020031");
            }

            // （排除）待变更成员是用户自身，且是唯一家庭组管理员，且待变更权限是user
            if (userId.equals(String.valueOf(appUser.getId()))) {
                if (appMyCircleMgtService.isCircleAdminOnly(circleId, userId)) {
                    if (Constants.MyCircleRole.user.toString().equals(circleRole)) {
                        throw new BusinessException("0x50020045");
                    }
                }
            } else {
                // 校验: 待变更权限成员是家庭组成员
                appMyCircleMgtService.isCircleMember(true, circleId, String.valueOf(appUser.getId()));
            }

            // 校验通过，则执行App请求
            appMyCircleMgtService.updateCircleMemberRole(circleId, String.valueOf(appUser.getId()), circleRole);
            sReturn = XmlUtil.mapToXmlRight();
        } catch (Exception e) {
            sReturn = getErrorXml(e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * <8.7 app用户查询指定家庭组中成员信息>
     * 
     * @param req
     * @param resp
     * @param ID
     * @return
     */
    @RequestMapping(value = "/queryMember/{CID}", method = RequestMethod.GET)
    public ResponseEntity<String> queryCircleMemberListById(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable String CID) {
        String userId = String.valueOf(getUserId());
        String sReturn = null;

        try {
            // 校验：家庭组id合法性
            if (!StringUtil.pattern(XmlElementValidator.checkParaMap.get("circleId"), CID)) {
                throw new BusinessException("0x50000033");
            }

            // 校验：app用户是此家庭组成员
            appMyCircleMgtService.isCircleMember(true, CID, userId);

            // 校验通过，则执行app请求
            List<Map<String, Object>> reMemberList = appMyCircleMgtService.getCircleMemberListByCId(CID);

            Map<String, Object> backMap = new LinkedHashMap<String, Object>();
            List<Object> memberList = new ArrayList<Object>();
            if (reMemberList != null && reMemberList.size() > 0) {
                // 组装信息
                for (Map<String, Object> reMemberMap : reMemberList) {
                    Map<String, Object> memberMaps = new LinkedHashMap<String, Object>();
                    Map<String, Object> memberMap = new LinkedHashMap<String, Object>();
                    memberMap.put("email", reMemberMap.get("email"));
                    memberMap.put("circleName", reMemberMap.get("circleName"));
                    memberMap.put("circleRole", reMemberMap.get("circleRole"));
                    memberMap.put("userId", reMemberMap.get("userId"));
                    memberMap.put("nickName", reMemberMap.get("nickName"));

                    // 获取用户头像相关信息
                    Map<String, Object> portraitMap = new HashMap<String, Object>();
                    if (reMemberMap.get("portraitId") != null
                            && !String.valueOf(reMemberMap.get("portraitId")).isEmpty()) {
                        portraitMap.put("id", reMemberMap.get("portraitId"));
                        if (StringUtils.equals(String.valueOf(reMemberMap.get("portraitId")), "-1")) {
                            if (StringUtils.isBlank(String.valueOf(reMemberMap.get("portraitUuid")))) {
                                throw new BusinessException("0x50000002");
                            }
                            // 查数据库获取头像url
                            S3FileData fileData = s3Service.getUploadFileByUuid(String.valueOf(reMemberMap.get("portraitUuid")));
                            if (fileData == null) {
                                throw new BusinessException("0x50000002");
                            }
                            //如果自定义头像没有上传或上传失败了,就返回空
                            if (StringUtils.isBlank(fileData.getFilePath())
                                    || StringUtils.isBlank(fileData.getFileName())) {
                            	portraitMap.put("url", "");
                            }else{
                            	portraitMap.put("url", fileData.getFilePath() + fileData.getFileName());
                            }
                        }
                    } else {
						logger.warn("userId of: " + reMemberMap.get("userId") + " the user_ portrait_id is null");
                        throw new BusinessException("0x50000002");
                    }
                    memberMap.put("portrait", portraitMap);

                    // 获取用户名下关联的所有设备型号
                    List<Map<String, Object>> deviceModelsMap = new ArrayList<Map<String, Object>>();
                    deviceModelsMap = queryMemberDeviceModel(String.valueOf(reMemberMap.get("userId")));
                    if (deviceModelsMap != null) {
                        memberMap.put("deviceModels", deviceModelsMap);
                    } else {
                        memberMap.put("deviceModels", "");
                    }

                    memberMaps.put("circleMember", memberMap);
                    memberList.add(memberMaps);
                }
            }
            backMap.put("circleMemberList", memberList);
            sReturn = XmlUtil.mapToXmlRight("queryCircleMemberList", backMap);
        } catch (Exception e) {
            sReturn = getErrorXml("queryCircleMemberList",e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * <8.11 app用户查询家庭组中指定成员名下设备>
     * 
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "/memberDevice", method = RequestMethod.POST)
    public ResponseEntity<String> queryCircleMemberDevices(HttpServletRequest req, HttpServletResponse resp) {
        String userId = "";
        String sReturn = null;

        try {
            Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
            userId = paraMap.get("userId").toString();
            String circleId = XmlUtil.convertToString(paraMap.get("circleId"), false);
            String isIPC = XmlUtil.convertToString(paraMap.get("isIPC"), false);

            // 校验：app用户是此家庭组成员
            appMyCircleMgtService.isCircleMember(true, circleId, userId);

            // 校验：目标用户存在
            AppUser appUser = appUserService.getUserById(Long.valueOf(userId));
            if (appUser == null) {
                throw new BusinessException("0x50020031");
            }

            // 校验：目标用户是此家庭组成员
            appMyCircleMgtService.isCircleMember(true, circleId, String.valueOf(appUser.getId()));

            // 校验通过，则执行app请求
            // 数据组装
            String memberName = appMyCircleMgtService.getCircleMemberByCMId(circleId, String.valueOf(appUser.getId()))
                    .getMemberNickName();

            Map<String, Object> ownerMap = new LinkedHashMap<String, Object>();
            ownerMap.put("memberName", memberName);
            ownerMap.put("userId", appUser.getId());
            ownerMap.put("nickName", appUser.getName());
            ownerMap.put("email", appUser.getEmail());
            HashMap<String, Object> portraitMap = new HashMap<String, Object>();
            if (Integer.valueOf(appUser.getPortraintId()) <= -1) {
                portraitMap.put("url", s3Service.getUploadFileByUuid(appUser.getPortraintUuid()).getFilePath());
            } else {
                portraitMap.put("id", appUser.getPortraintId());
            }
            ownerMap.put("portrait", portraitMap);

            // 获取用户设备列表
            List<Map<String, Object>> reDeviceList = appMyCircleMgtService.getUserDevices(circleId,
                    String.valueOf(appUser.getId()), isIPC);

            Map<String, Object> backMap = new LinkedHashMap<String, Object>();
            List<Object> deviceList = new ArrayList<Object>();
            if (reDeviceList != null && reDeviceList.size() > 0) {
                // 组装信息
                for (Map<String, Object> reDeviceMap : reDeviceList) {
                    Map<String, Object> deviceMaps = new LinkedHashMap<String, Object>();
                    Map<String, Object> deviceMap = new LinkedHashMap<String, Object>();
                    deviceMap.put("SN", reDeviceMap.get("SN"));
                    deviceMap.put("deviceModel", reDeviceMap.get("deviceModel"));
                    deviceMap.put("deviceKind", reDeviceMap.get("deviceKind"));
                    deviceMap.put("deviceName", reDeviceMap.get("deviceName"));
                    deviceMap.put("onLineStatus", reDeviceMap.get("onLineStatus"));

                    deviceMaps.put("circleMemberDevice", deviceMap);
                    deviceList.add(deviceMaps);
                }
            }
            backMap.put("deviceOwner", ownerMap);
            backMap.put("circleMemberDeviceList", deviceList);
            sReturn = XmlUtil.mapToXmlRight("queryCircleMemberDeviceList", backMap);
        } catch (Exception e) {
            sReturn = getErrorXml("queryCircleMemberDeviceList", e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * <8.13 app用户查询所在家庭组中能看到的所有成员及设备信息列表>
     * 
     * @param req
     * @param resp
     * @return
     */
    @RequestMapping(value = "/allMember", method = RequestMethod.GET)
    public ResponseEntity<String> queryCircleListAllDevices(HttpServletRequest req, HttpServletResponse resp) {
        String userId = String.valueOf(getUserId());
        String sReturn = null;

        try {
            List<String> circleIdList = new ArrayList<String>();

            List<Map<String, Object>> circleList = appMyCircleMgtService.getCircleListByMberId(userId);
            for (Map<String, Object> circleObj : circleList) {
                circleIdList.add(String.valueOf(circleObj.get("circleId")));
            }

            Map<String, Object> backMap = new LinkedHashMap<String, Object>();
            List<Object> memberInfoList = new ArrayList<Object>();
            if (circleIdList.size() > 0) {
                List<Map<String, Object>> reList = appMyCircleMgtService.getShareDevicesByCIds(circleIdList);
                if (reList != null && reList.size() > 0) {
                    String userIdTemp = "";

                    Map<String, Object> memberMaps = new LinkedHashMap<String, Object>();
                    Map<String, Object> memberMap = new LinkedHashMap<String, Object>();
                    List<Object> deviceList = null;

                    for (Map<String, Object> reMap : reList) {
                    	// 分享权限过滤
						if ( (!userId.equals(String.valueOf(reMap.get("userId"))))
						  && ("false".equals(String.valueOf(reMap.get("notificationEnable")))) ) {
							continue;
						}
                    	
                        // 组装设备信息
                        Map<String, Object> deviceMaps = new LinkedHashMap<String, Object>();
                        Map<String, Object> deviceMap = new LinkedHashMap<String, Object>();
                        deviceMap.put("SN", reMap.get("SN"));
                        deviceMap.put("deviceModel", reMap.get("deviceModel"));
                        deviceMap.put("deviceKind", reMap.get("deviceKind"));
                        deviceMap.put("deviceName", reMap.get("deviceName"));
						deviceMap.put("onLineStatus", reMap.get("onLineStatus"));
						// 添加温度湿度
						Map<String, Object> sensorsMap = new LinkedHashMap<String, Object>();
						sensorsMap.put("temperature", reMap.get("temperature"));
						sensorsMap.put("humidity", reMap.get("humidity"));
						deviceMap.put("sensors", sensorsMap);

						deviceMaps.put("deviceInfo", deviceMap);

                        // 同一个
                        if (userIdTemp.equals(String.valueOf(reMap.get("userId")))) {
                            deviceList.add(deviceMaps);
                        } else {
                            userIdTemp = String.valueOf(reMap.get("userId"));

                            memberMaps = new LinkedHashMap<String, Object>();
                            memberMap = new LinkedHashMap<String, Object>();

                            // 组装用户信息
                            memberMap.put("userId", reMap.get("userId"));
                            memberMap.put("email", reMap.get("email"));
                            memberMap.put("nickName", reMap.get("nickName"));

                            Map<String, Object> portraitMap = new HashMap<String, Object>();

                            String portraitId = String.valueOf(reMap.get("portraitId"));
                            if ("-1".equals(portraitId)) {
                                // 自定义头像
                            	S3FileData file = s3Service.getUploadFileByUuid(String.valueOf(reMap.get("portraitUuid")));
                                portraitMap.put("url",file.getFilePath() + (file.getFileName() == null ? "" : file.getFileName()));
                            } else {
                                // 缺省头像
                                portraitMap.put("id", portraitId);
                            }
                            memberMap.put("portrait", portraitMap);

                            // 设备信息初始化
                            deviceList = new ArrayList<Object>();

                            deviceList.add(deviceMaps);
                            memberMap.put("deviceInfoList", deviceList);
                            memberMap.put("deviceInfoList", deviceList);
                            memberMaps.put("memberInfo", memberMap);
                            memberInfoList.add(memberMaps);
                        }
                    }
                }
            }
            backMap.put("memberInfoList", memberInfoList);
            sReturn = XmlUtil.mapToXmlRight("queryFamilyAllDevices", backMap);
        } catch (Exception e) {
            sReturn = getErrorXml("queryFamilyAllDevices", e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * <8.15 app用户设置将自己的设备分享给所在的家庭组>
     * 
     * @param req
     * @param resp
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/{SN}/shareDevice", method = RequestMethod.POST)
    public ResponseEntity<String> setShareOwnDevices(HttpServletRequest req, HttpServletResponse resp,
            @PathVariable String SN) {
        String sReturn = null;
        long deviceId = 0;
        try {
            IPCamera ipcamera = ipcService.getIPCameraBySN(SN);
            if (ipcamera != null) {
                deviceId = ipcamera.getId();
            } else {
                throw new BusinessException("0x50000047");
            }
            
            Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
            for (Object circleObj : (List) paraMap.get("circleList")) {
                Map<String, Object> circleMap = (Map<String, Object>) ((Map<String, Object>) circleObj).get("circleInfo");
        	    String circleId = XmlUtil.convertToString(circleMap.get("circleId"), false);
                String enable = XmlUtil.convertToString(circleMap.get("enable"), false);
            	
                AppDeviceShare appDeviceShare = new AppDeviceShare();
                appDeviceShare.setDeviceId(String.valueOf(deviceId));
                appDeviceShare.setCircleId(circleId);
                appDeviceShare.setStreamEnable(enable);
                appDeviceShare.setPlaybackEnable(enable);
                appDeviceShare.setNotificationEnable(enable);
                appDeviceShare.setMemberId(String.valueOf(getUserId()));
                appMyCircleMgtService.updateShareOwnDevice(appDeviceShare);
            }
            sReturn = XmlUtil.mapToXmlRight();
        } catch (Exception e) {
            sReturn = getErrorXml(e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * 
     * <8.10 APP用户同意/拒绝加入家庭组(或者退出家庭组)>
     * 
     * @Title: joinInOrQuitCircle
     * @param @param request
     * @param @return (参数说明)
     * @return ResponseEntity<String> (返回值说明)
     * @throws (异常说明)
     * @author wangming
     * @date 2016年1月4日
     */
    @RequestMapping(value = "/quit", method = RequestMethod.POST)
    public ResponseEntity<String> joinInOrQuitCircle(HttpServletRequest request) {
        String sReturn = XmlUtil.mapToXmlRight();
        StringBuffer xml=new StringBuffer("<ResponseStatus version=\"1.0\" xmlns=\"urn:skylight\">");
        

        // 从digest验证中获得userId
        String digestId = String.valueOf(getUserId());
        int userId = Integer.valueOf(digestId);
        try {
            Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(request);
            String confirm = (String) paraMap.get("confirm");
            String circleId = (String) paraMap.get("circleId");

            // 做容错处理
            if (confirm == null || circleId == null) {
                throw new BusinessException("0x50000029");
            }

            HashMap<String, String> queryMap = new HashMap<String, String>();
            queryMap.put("circleId", circleId);
            queryMap.put("userId", digestId);
            AppCircleMember appCircleMember = appMyCircleMgtService.getCircleMemberByCMId(circleId, digestId);
            if(appCircleMember==null){
            	throw new BusinessException("0x50020081");
            }
            String isNew = appCircleMember.getIsNew();
            if (isNew == null || isNew.trim().equals("")) {
                logger.warn("********circleId为" + circleId + ",userId为" + userId + "时，在数据库查到的is_New字段为null********");
                throw new BusinessException("0x50000002");
            }

            if (confirm.equals("true")) { // APP用户同意加入家庭组
                // APP用户同意加入家庭组,同时分享/被分享设备
                appMyCircleMgtService.joinInCircle(queryMap);
            } else if (confirm.equals("false")) {
            	// App用户拒绝加入或者退出家庭组
                if (isNew.equals("false")) { 
                	// App用户要拒绝加入家庭组
                    appMyCircleMgtService.deleteCircleMember(circleId, digestId);
                }else if (isNew.equals("true")) { 
                	// App用户要退出家庭组
					if (appMyCircleMgtService.isCircleAdminOnly(circleId, digestId)) {
						// 解散家庭组
						appMyCircleMgtService.deleteCircleById(circleId);
					} else {
						// 直接退出家庭组
						appMyCircleMgtService.deleteCircleMember(circleId, digestId);
					}
                }
            } else {
                throw new BusinessException("0x50000033");
            }
        } catch (Exception e) {
            sReturn = getErrorXml(e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * <8.12 APP用户查询指定家庭组中能看到的所有成员及设备信息列表>
     * 
     * @param circleId
     * @return
     */
    @RequestMapping(value = "/allMember/{circleId}", method = RequestMethod.GET)
    public ResponseEntity<String> queryCircleAllDevices(@PathVariable String circleId) {
    	String userId = String.valueOf(getUserId());
        String sReturn = null;

        try {
        	// 校验: 当前用户是否在此家庭组中
            appMyCircleMgtService.isCircleAdmin(true, circleId, userId);
            List<String> circleIdList = new ArrayList<String>();
            circleIdList.add(circleId);

            Map<String, Object> backMap = new LinkedHashMap<String, Object>();
            List<Object> memberInfoList = new ArrayList<Object>();
            if (circleIdList.size() > 0) {
                List<Map<String, Object>> reList = appMyCircleMgtService.getShareDevicesByCIds(circleIdList);
                if (reList != null && reList.size() > 0) {
                    String userIdTemp = "";

                    Map<String, Object> memberMaps = new LinkedHashMap<String, Object>();
                    Map<String, Object> memberMap = new LinkedHashMap<String, Object>();
                    List<Object> deviceList = null;

                    for (Map<String, Object> reMap : reList) {
                    	// 分享权限过滤
						if ( (!userId.equals(String.valueOf(reMap.get("userId"))))
						  && ("false".equals(String.valueOf(reMap.get("notificationEnable")))) ) {
							continue;
						}
                    	
                        // 组装设备信息
                        Map<String, Object> deviceMaps = new LinkedHashMap<String, Object>();
                        Map<String, Object> deviceMap = new LinkedHashMap<String, Object>();
                        deviceMap.put("SN", reMap.get("SN"));
                        deviceMap.put("deviceModel", reMap.get("deviceModel"));
                        deviceMap.put("deviceKind", reMap.get("deviceKind"));
                        deviceMap.put("deviceName", reMap.get("deviceName"));
						deviceMap.put("onLineStatus", reMap.get("onLineStatus"));
						// 添加温度湿度
						Map<String, Object> sensorsMap = new LinkedHashMap<String, Object>();
						sensorsMap.put("temperature", reMap.get("temperature"));
						sensorsMap.put("humidity", reMap.get("humidity"));
						deviceMap.put("sensors", sensorsMap);

						deviceMaps.put("deviceInfo", deviceMap);

                        // 同一个
                        if (userIdTemp.equals(String.valueOf(reMap.get("userId")))) {
                            deviceList.add(deviceMaps);
                        } else {
                            userIdTemp = String.valueOf(reMap.get("userId"));

                            memberMaps = new LinkedHashMap<String, Object>();
                            memberMap = new LinkedHashMap<String, Object>();

                            // 组装用户信息
                            memberMap.put("userId", reMap.get("userId"));
                            memberMap.put("email", reMap.get("email"));
                            memberMap.put("nickName", reMap.get("nickName"));

                            Map<String, Object> portraitMap = new HashMap<String, Object>();

                            String portraitId = String.valueOf(reMap.get("portraitId"));
                            if ("-1".equals(portraitId)) {
                                // 自定义头像
                            	S3FileData file = s3Service.getUploadFileByUuid(String.valueOf(reMap.get("portraitUuid")));
                                portraitMap.put("url", file.getFilePath() + (file.getFileName() == null ? "" : file.getFileName()));
                            } else {
                                // 缺省头像
                                portraitMap.put("id", portraitId);
                            }
                            memberMap.put("portrait", portraitMap);

                            // 设备信息初始化
                            deviceList = new ArrayList<Object>();

                            deviceList.add(deviceMaps);
                            memberMap.put("deviceInfoList", deviceList);
                            memberMap.put("deviceInfoList", deviceList);
                            memberMaps.put("memberInfo", memberMap);
                            memberInfoList.add(memberMaps);
                        }
                    }
                }
            }
            backMap.put("memberInfoList", memberInfoList);
            sReturn = XmlUtil.mapToXmlRight("queryFamilyAllDevices", backMap);
        } catch (Exception e) {
            sReturn = getErrorXml("queryFamilyAllDevices", e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * 查询用户名下关联的所有设备型号
     * 
     * @param userId
     * @return
     */
    private List<Map<String, Object>> queryMemberDeviceModel(String userId) {

        List<IPCamera> ipcList = appUserService.getUserBindDeviceList(Long.valueOf(userId));

        List<Map<String, Object>> deviceModels = new ArrayList<Map<String, Object>>();
        // 将用户所有的device的不同的model型号收集
        if (ipcList != null) {
            for (IPCamera ipCamera : ipcList) {
                if (ipCamera.getModel() != null) {
                    if (deviceModels.size() == 0) {
                        Map<String, Object> deviceModel = new HashMap<String, Object>();
                        deviceModel.put("model", ipCamera.getModel());
                        deviceModels.add(deviceModel);
                    } else {
                        // 判断列表是否已含有此model型号，不含有才加入列表deviceModels
                        boolean isExist = false;
                        for (Map<String, Object> dm : deviceModels) {
                            if (StringUtils.equals(dm.get("model").toString(), ipCamera.getModel())) {
                                isExist = true;
                            }
                        }
                        if (!isExist) {
                            Map<String, Object> deviceModel = new HashMap<String, Object>();
                            deviceModel.put("model", ipCamera.getModel());
                            deviceModels.add(deviceModel);
                        }
                    }

                } else {
                    // 查询DB 的设备 model信息为null的话，抛出异常
                    logger.info("********cameraId为" + ipCamera.getDeviceId()
                            + "时，在数据库表t_t_platform_ipcamera 查到的 camera_model 字段为null********");
                    throw new BusinessException("0x50000002");
                }
            }
        }
        return deviceModels;
    }

    /**
     * <8.9 APP用户查询所有家庭组及组中成员信息>
     * 
     * @param req
     * @param resp
     * @param ID
     * @return
     */
    @RequestMapping(value = "/queryCircleMember", method = RequestMethod.GET)
    public ResponseEntity<String> queryCircleAndMemberList(HttpServletRequest req, HttpServletResponse resp) {
        String userId = String.valueOf(getUserId());
        String sReturn = null;

        try {
            Map<String, Object> backMap = new LinkedHashMap<String, Object>();
            List<Object> circleList = new ArrayList<Object>();
            // 查询用户所属的所有家庭组
            List<Map<String, Object>> reCircleList = appMyCircleMgtService.getCircleListByMberId(userId);
            if (reCircleList == null) {
                backMap.put("circleList", "");
            } else {
                for (Map<String, Object> reCircleMap : reCircleList) {
                    // 获取每个家庭组的信息
                    Map<String, Object> circleMaps = new LinkedHashMap<String, Object>();
                    Map<String, Object> circleMap = new LinkedHashMap<String, Object>();
                    circleMap.put("circleName", reCircleMap.get("circleName"));
                    circleMap.put("circleId", reCircleMap.get("circleId"));
                    circleMap.put("portraitId", reCircleMap.get("portraitId"));
                    // 只返回查询用户已加入的家庭组的信息
                    if (reCircleMap.get("memberIsNew").equals("true")) {
                        // 查询出每个家庭组的所有成员，忽略没确认加入的成员信息（在mapper的XML文件有限制条件）
                        List<Map<String, Object>> reMemberList = appMyCircleMgtService
                                .getCircleMemberListByCId(reCircleMap.get("circleId").toString());
                        List<Object> memberList = new ArrayList<Object>();
                        if (reMemberList != null && reMemberList.size() > 0) {
                            // 组装信息
                            for (Map<String, Object> reMemberMap : reMemberList) {
                                Map<String, Object> memberMaps = new LinkedHashMap<String, Object>();
                                Map<String, Object> memberMap = new LinkedHashMap<String, Object>();
                                memberMap.put("email", reMemberMap.get("email"));
                                memberMap.put("circleRole", reMemberMap.get("circleRole"));
                                memberMap.put("userId", reMemberMap.get("userId"));
                                memberMap.put("nickName", reMemberMap.get("nickName"));

                                // 获取用户头像相关信息
                                Map<String, Object> portraitMap = new HashMap<String, Object>();
                                if (reMemberMap.get("portraitId") != null
                                        && !String.valueOf(reMemberMap.get("portraitId")).isEmpty()) {
                                    portraitMap.put("id", reMemberMap.get("portraitId"));
                                    if (StringUtils.equals(String.valueOf(reMemberMap.get("portraitId")), "-1")) {
                                    	 S3FileData platformFile = s3Service.getUploadFileByUuid(String.valueOf(reMemberMap.get("portraitUuid")));
                                    	 if(platformFile != null && StringUtils.isNotBlank(platformFile.getFilePath())
 												&& StringUtils.isNotBlank(platformFile.getFileName())){
                                    		 portraitMap.put("url", platformFile.getFilePath() + platformFile.getFileName());
                                    	 }else{
                                    		 portraitMap.put("url", ""); 
                                    	 }
                                    }
                                } else {
                                    throw new BusinessException("0x50000002");
                                }
                                memberMap.put("portrait", portraitMap);

                                // 获取用户名下关联的所有设备型号
                                List<Map<String, Object>> deviceModelsMap = new ArrayList<Map<String, Object>>();
                                deviceModelsMap = queryMemberDeviceModel(String.valueOf(reMemberMap.get("userId")));
                                if (deviceModelsMap != null) {
                                    memberMap.put("deviceModels", deviceModelsMap);
                                } else {
                                    memberMap.put("deviceModels", "");
                                }

                                memberMaps.put("circleMember", memberMap);
                                memberList.add(memberMaps);
                            }
                        }
                        circleMap.put("circleMemberList", memberList);
                        circleMaps.put("circle", circleMap);
                        circleList.add(circleMaps);
                    }
                }
                backMap.put("circleList", circleList);
            }
            sReturn = XmlUtil.mapToXmlRight("queryCircleMemberList", backMap);
        } catch (Exception e) {
            sReturn = getErrorXml("queryCircleMemberList", e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * <8.8 APP用户查询所有家庭组中成员信息>
     * 
     * @param req
     * @param resp
     * @param ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/queryMember", method = RequestMethod.GET)
    public ResponseEntity<String> queryCircleMemberList(HttpServletRequest req, HttpServletResponse resp) {
        String userId = String.valueOf(getUserId());
        String sReturn = null;

        try {
            Map<String, Object> backMap = new LinkedHashMap<String, Object>();
            // 查询用户所属的所有家庭组
            List<Map<String, Object>> reCircleList = appMyCircleMgtService.getCircleListByMberId(userId);
            List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
            if (reCircleList == null) {
                backMap.put("circleMemberList", "");
            } else {
                for (Map<String, Object> reCircleMap : reCircleList) {
                    // 只返回查询用户已加入的家庭组的信息
                    if (reCircleMap.get("memberIsNew").equals("true")) {
                        // 查询出每个家庭组的所有成员，忽略没确认加入的成员信息（在mapper的XML文件有限制条件）
                        List<Map<String, Object>> reMemberList = appMyCircleMgtService
                                .getCircleMemberListByCId(reCircleMap.get("circleId").toString());

                        if (reMemberList != null && reMemberList.size() > 0) {
                            // 组装信息
                            for (Map<String, Object> reMemberMap : reMemberList) {
                                Map<String, Object> memberMaps = new LinkedHashMap<String, Object>();
                                Map<String, Object> memberMap = new LinkedHashMap<String, Object>();
                                memberMap.put("email", reMemberMap.get("email"));
                                memberMap.put("userId", reMemberMap.get("userId"));
                                memberMap.put("nickName", reMemberMap.get("nickName"));

                                // 获取用户头像相关信息
                                Map<String, Object> portraitMap = new HashMap<String, Object>();
								if (reMemberMap.get("portraitId") != null
										&& !String.valueOf(reMemberMap.get("portraitId")).isEmpty()) {
									portraitMap.put("id", reMemberMap.get("portraitId"));
									if (StringUtils.equals(String.valueOf(reMemberMap.get("portraitId")), "-1")) {
										S3FileData platformFile = s3Service.getUploadFileByUuid(String.valueOf(reMemberMap.get("portraitUuid")));
										if (platformFile != null && StringUtils.isNotBlank(platformFile.getFilePath())
												&& StringUtils.isNotBlank(platformFile.getFileName())) {
											portraitMap.put("url",platformFile.getFilePath() + platformFile.getFileName());
										} else {
											portraitMap.put("url", "");
										}
									}
								} else {
									throw new BusinessException("0x50000002");
								}
                                memberMap.put("portrait", portraitMap);

                                // 获取用户名下关联的所有设备型号
                                List<Map<String, Object>> deviceModelsMap = new ArrayList<Map<String, Object>>();
                                deviceModelsMap = queryMemberDeviceModel(String.valueOf(reMemberMap.get("userId")));
                                if (deviceModelsMap != null) {
                                    memberMap.put("deviceModels", deviceModelsMap);
                                } else {
                                    memberMap.put("deviceModels", "");
                                }

                                // 判断memberList是否已含有此用户信息，不含有才加入列表memberList返回给app
                                if (memberList.size() == 0) {
                                    memberMaps.put("circleMember", memberMap);
                                    memberList.add(memberMaps);
                                } else {
                                    boolean isExist = false;
                                    for (Map<String, Object> mb : memberList) {
                                        if (StringUtils
                                                .equals(((Map<String, Object>) mb.get("circleMember")).get("userId")
                                                        .toString(), reMemberMap.get("userId").toString())) {
                                            isExist = true;
                                        }
                                    }
                                    if (!isExist) {
                                        memberMaps.put("circleMember", memberMap);
                                        memberList.add(memberMaps);
                                    }
                                }
                            }
                        }
                    }
                }
                backMap.put("circleMemberList", memberList);
            }
            sReturn = XmlUtil.mapToXmlRight("queryCircleMemberList", backMap);
        } catch (Exception e) {
            sReturn = getErrorXml("queryCircleMemberList", e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn, HttpStatus.OK);
    }

    /**
     * 
     * <8.14 用户查询自己的设备分享给所在的家庭组的信息>
     * 
     * @Title: queryOwnSharedDevicesInfo
     * @param @return (参数说明)
     * @return ResponseEntity<String> (返回值说明)
     * @throws (异常说明)
     * @author wangming
     * @date 2016年1月13日
     */
    @RequestMapping(value = "/{SN}/shareDevice", method = RequestMethod.GET)
    public ResponseEntity<String> queryOwnSharedDevicesInfo(@PathVariable String SN) {
        String sReturn = "";
        try {
            // 从digest验证中获得userId
        	long userId = getUserId();
            String digestId = String.valueOf(userId);
            

            // 容错处理
            if (userId <= 0L || SN.trim().equals("")) {
                if (userId <= 0L) {
                    throw new BusinessException("0x50010004");
                }
                if (SN.trim().equals("")) {
                    throw new BusinessException("0x50010003");
                }
            }

            // 先判断userId和SN是否绑定
            IPCamera ipcamera = appUserService.getUserBindDevice(userId, SN);
            if (ipcamera == null) {
                logger.info("********该useId：" + userId + "  没有绑定任何设备信息********");
            }

            List<Long> circleIdList = new ArrayList<Long>();
            // 判断该用户都加入了哪些家庭组
            circleIdList = appMyCircleMgtService.queryAllCircleIdByUserId(digestId);
            // 如果该用户没加入任何家庭组则返回
            if (circleIdList.isEmpty()) {
                throw new BusinessException("0x50000002");
            }

            // 查询该用户在家庭组中分享设备的所有信息
            List<OwnSharedDevices> oSDList = appMyCircleMgtService.queryOwnSharedDevicesInfo(digestId, ipcamera.getId().toString());
            StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.append("<shareOwnDevices version=\"1.0\" xmlns=\"urn:skylight\">");
            sb.append("<circleList>");
            if (ipcamera.getId() != null && !oSDList.isEmpty()) {
                Iterator<Long> iterator = circleIdList.iterator();
                while (iterator.hasNext()) {
                    long circleId = iterator.next();
                    for (OwnSharedDevices ownSharedDevice : oSDList) {
                        if (circleId != ownSharedDevice.getCircleId()) {
                            continue;
                        }
                        // 组装设备信息
                        sb.append("<circleInfo>");
                        sb.append("<circleId>");
                        sb.append(ownSharedDevice.getCircleId());
                        sb.append("</circleId>");
                        sb.append("<circleName>");
                        sb.append(ownSharedDevice.getCircleName());
                        sb.append("</circleName>");
                        sb.append("<enable>");
                        sb.append(ownSharedDevice.getNotification());
                        sb.append("</enable>");
                        sb.append("</circleInfo>");

                        // 技巧：先排除已分享的设备信息对应的circleId，保留该用户加入的但是并没有分享的家庭组Id
                        iterator.remove();
                    }
                }

                // 此处circleIdList只剩下该用户加入的但是并没有分享的家庭组Id
                for (Long circleId : circleIdList) {
                    AppCircle appCircle = appMyCircleMgtService.getCircleByCId(String.valueOf(circleId));
                    String circleName = appCircle.getCircleName();
                    sb.append("<circleInfo>");
                    sb.append("<circleId>");
                    sb.append(circleId + "");
                    sb.append("</circleId>");
                    sb.append("<circleName>");
                    sb.append(circleName);
                    sb.append("</circleName>");
                    sb.append("<enable>");
                    sb.append("false");
                    sb.append("</enable>");
                    sb.append("</circleInfo>");
                }
            } else {
                // 该用户加入了家庭组，但没分享任何设备信息的情况
                for (Long circleId : circleIdList) {
                    AppCircle appCircle = appMyCircleMgtService.getCircleByCId(String.valueOf(circleId));
                    String circleName = appCircle.getCircleName();
                    sb.append("<circleInfo>");
                    sb.append("<circleId>");
                    sb.append(circleId + "");
                    sb.append("</circleId>");
                    sb.append("<circleName>");
                    sb.append(circleName);
                    sb.append("</circleName>");
                    sb.append("<enable>");
                    sb.append("false");
                    sb.append("</enable>");
                    sb.append("</circleInfo>");
                }
            }
            sb.append("</circleList>");
            sb.append("<ResponseStatus>");
            sb.append("<statusCode>0</statusCode>");
            sb.append("<statusString>0</statusString>");
            sb.append("</ResponseStatus>");
            sb.append("</shareOwnDevices>");
            sReturn = sb.toString();
        } catch (Exception e) {
            sReturn = getErrorXml("shareOwnDevices", e, this.getClass().getName());
        }
        return new ResponseEntity<String>(sReturn.toString(), HttpStatus.OK);
    }
}
