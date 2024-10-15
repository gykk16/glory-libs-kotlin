package io.glory.coremvc.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Ip address utility class
 */
public class IpAddrUtil {

    private static final Logger log = LoggerFactory.getLogger(IpAddrUtil.class);

    private static final String SERVER_IP;
    private static final String SERVER_IP_LAST_OCTET;

    private static final String[] IP_HEADER_CANDIDATES = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"};

    static {
        String serverIp;
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
            log.warn("==> UnknownHostException");
            serverIp = "unknown";
        }
        SERVER_IP = serverIp.strip();
        SERVER_IP_LAST_OCTET = SERVER_IP.substring(serverIp.lastIndexOf(".") + 1);
    }

    private IpAddrUtil() {
    }

    /**
     * Get server ip address
     *
     * @return server ip address
     */
    public static String getServerIp() {
        return SERVER_IP;
    }

    /**
     * Get server ip last octet
     *
     * @return server ip last octet
     */
    public static String getServerIpLastOctet() {
        return SERVER_IP_LAST_OCTET;
    }

    /**
     * Get client ip address
     *
     * @return client ip address
     */
    public static String getClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return getClientIp(request);
    }

    /**
     * Get client ip address
     *
     * @param request http servlet request
     * @return client ip address
     */
    public static String getClientIp(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (StringUtils.hasText(ip) && !StringUtils.startsWithIgnoreCase(ip, "unknown")) {
                return ip.strip();
            }
        }
        return request.getRemoteAddr();
    }

}
