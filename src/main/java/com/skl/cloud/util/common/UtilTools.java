package com.skl.cloud.util.common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class UtilTools {
    private static final Logger log = Logger.getLogger(UtilTools.class);

    /**
     * @author shaoxiong
     * @version
     * @instruction 获取本机的ip地址，在liux下获取的不是127.0.0.1
     */
    public static String getlocalIp() {
        Enumeration<?> allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                // System.out.println(netInterface.getName());
                Enumeration<?> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {

                        if (!ip.getHostAddress().equals("127.0.0.1")) {
                            // System.out.println("本机的IP = " +
                            // ip.getHostAddress());
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            log.info("getLocalhost erro:" + e);

        }
        return "";
    }

}
