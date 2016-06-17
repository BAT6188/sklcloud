package sklcloud.service;

import java.util.Date;

import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skl.cloud.foundation.file.S3Factory;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.config.SystemConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-config/spring-*.xml" })
@ActiveProfiles("local")
public class IPCameraServiceTest {
    private static Logger logger = LoggerFactory.getLogger(IPCameraServiceTest.class);

    // 测试用的deveiceID 目前是16位长度
    private static final String TEST_DEVICEID = "ipcServiceTest01";

    // 测试用的ipc sn
    private static final String TEST_SN = "T000000000001";

    @Autowired
    private IPCameraService IPCService;

    @Autowired
	private S3Service s3Service;
    
    @Before
    public void testTheBefore(){
    	//SystemConfig.getInstance();
    }
    
    /**
     * 创建测试用的数据
    */
    //@Test
    public void testBefore() {
        IPCamera ipcamera = new IPCamera();
        //key data
        ipcamera.setDeviceId(TEST_DEVICEID);
        ipcamera.setSn(TEST_SN);
        //not null data
        ipcamera.setMac("0A:1B:3C:4D:5E:6F");
        ipcamera.setNickname("junit test");
        ipcamera.setKind("HPC05");
        ipcamera.setModel("1");
        ipcamera.getIpcSub().setPincode("T"+System.currentTimeMillis());
        ipcamera.getIpcSub().setVersion("0");
        ipcamera.setMakeDate(new Date());
        IPCService.createIPCamera(ipcamera);
        logger.info("--------before to creat data-------");
    }

    /**
     * 清掉测试用的数据
    */
    @Test
    public void testClean() {
        IPCamera ipcamera = IPCService.getIPCameraByDeviceId(TEST_DEVICEID);
        IPCService.deleteIPCamera(ipcamera.getId());
        logger.info("--------clean data-------");
    }

    /**
     * 更新IPC的信息
    */
    @Test
    public void testUpdateIPCamera() {
        IPCamera ipcamera = IPCService.getIPCameraByDeviceId(TEST_DEVICEID);
        ipcamera.setSpeakerVolume(75l);
        IPCService.updateIPCamera(ipcamera);
    }

    /**
     * 通过sn查询IPCamera的信息
     */
    @Test
    public void testGetIPCameraBySN() {
        IPCamera ipcamera = IPCService.getIPCameraBySN(TEST_SN);
        logger.info("IPC data : "+ipcamera);
        Assert.assertTrue(ipcamera.getId() > 0);
    }

    /**
     * 通过deveiceId查询IPCamera的信息
     */
    @Test
    public void testGetIPCameraByDeviceId() {
        IPCamera ipcamera = IPCService.getIPCameraByDeviceId(TEST_DEVICEID);
        logger.info("IPC data : "+ipcamera);
        Assert.assertTrue(ipcamera.getId() > 0);
    }
    
    @Test
    public void testS3Url(){
    	String uuid = "dafcc6a7-ab01-46b7-94a3-aa4ba77be81f";
    	String fileName = "picture_dafcc6a7-ab01-46b7-94a3-aa4ba77be81f.jpg";
    	String s3Key = StringUtil.convertToS3Key(s3Service.getUploadFileByUuid(uuid).getFilePath() + fileName);
		long fileSize = S3Factory.getDefault().getFile(s3Key).getContentLength();
		logger.info("fileSize data : "+fileSize);
    }
    
}