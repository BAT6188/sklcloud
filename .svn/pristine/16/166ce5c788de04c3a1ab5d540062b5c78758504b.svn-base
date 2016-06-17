package com.skl.cloud.service.fw.impl;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.HttpMethod;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.util.DateUtils;
import com.skl.cloud.dao.fw.FWServiceMapper;
import com.skl.cloud.foundation.file.S3Factory;
import com.skl.cloud.foundation.remote.SKLRemoteException;
import com.skl.cloud.foundation.remote.annotation.Remote;
import com.skl.cloud.model.PlatformIpcfw;
import com.skl.cloud.remote.ipc.FWUpdateRemote;
import com.skl.cloud.remote.ipc.IPCameraRemote;
import com.skl.cloud.service.fw.FWService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;

/**
 * 关于fw的服务
 * @author weibin
 *
 */
@Service
public class FWServiceImpl implements FWService {
    private static final Logger log = Logger.getLogger(FWServiceImpl.class);
    @Remote
    private IPCameraRemote ipcameraRemote;

    @Autowired
    private FWServiceMapper fwMapper;

    @Remote
    private FWUpdateRemote fWUpdateRemote;

    /**
     * 远程获取ipc的版本信息
     * @param sn
     * @return
     * @throws SKLRemoteException
     */
    @Override
    @Transactional(readOnly = true)
    public String getRemoteIpcFWInfo(String sn) throws SKLRemoteException {
        return ipcameraRemote.getDeciceFWInfo(sn);
    }

    /**
     * 根据ipc类型获取最新的版本信息
     * @param model
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getCloudLatestFwVersion(String model) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        PlatformIpcfw fw = fwMapper.queryCloudLatestFwVersion(model);
        if (fw != null) {
            map.put("firmwareVersion", fw.getDecversion());
            map.put("firmwareReleasedDate", DateUtil.date2Str(fw.getVersiondate(), DateUtil.DATE_TIME_1_FULL_FORMAT));
        } else {
            map = null;
        }
        return map;
    }

    /**
     * 通过model和version获取版本信息
     * @param model
     * @param version
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public PlatformIpcfw getPlatformIpcfwInfo(String model, String version) {
        return fwMapper.queryPlatformIpcfwInfo(model, version);
    }

    /**
     * 远程请求ipc升级fw
     * @param sn
     * @param platformIpcfw
     * @throws SKLRemoteException
     */
    @Override
    @Transactional
    public String updateRemoteFw(String sn, PlatformIpcfw platformIpcfw) throws SKLRemoteException {
        String xml = "";
        if (platformIpcfw == null) {
            throw new BusinessException(50000002, "");
        } else {
            LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
            URL newUrl = S3Factory.getDefault().getPresignedUrl(StringUtil.convertToS3Key(platformIpcfw.getVerpath()), HttpMethod.GET);
            map.put("url", newUrl.toString());
            map.put("fwVersion", platformIpcfw.getDecversion());
            map.put("md5CheckSum", platformIpcfw.getMd5check());
            map.put("fwReleaseDate",
                    DateUtils.getISO8601(platformIpcfw.getVersiondate()));
            try {
                xml = XmlUtil.responseXml("firmwareDownload", map);
            } catch (Exception e) {
                log.error("给ipc的参数--" + xml, e);
                throw new BusinessException(50000027, "");
            }
        }
        return fWUpdateRemote.fwUpdate(sn, xml);
    }

    /**
    * 请求查看ipc更新fw的状态
    * @param sn
    * @return
    */
    @Override
    @Transactional(readOnly = true)
    public String getRemoteFwUpdateStatus(String sn) throws SKLRemoteException {
        String xml = fWUpdateRemote.queryUpdateFirmwareStatus(sn);
        return XmlUtil.mapToXmlRight("updateFirmwareStatus", XmlUtil.getElementMap(xml));
    }

}
