package io.glory.coremvc.util;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * HttpServlet utility class
 */
public class HttpServletUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpServletUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json().build();

    private HttpServletUtil() {
    }

    /**
     * Get HttpServletRequest
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * Get HttpServletResponse
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * Get request parameters
     *
     * @return request parameters as string
     */
    public static String getRequestParams() {
        StringBuilder requestParameters = new StringBuilder();
        getRequest().getParameterMap()
                .forEach((key, value) -> requestParameters.append(key)
                        .append("=")
                        .append(Arrays.toString(value))
                        .append(" "));

        return requestParameters.toString();
    }

    /**
     * Read request body of application/json, application/xml
     *
     * @return request body
     * @throws IOException when failed to parse request body
     */
    public static String getRequestBody() throws IOException {
        HttpServletRequest request = getRequest();

        boolean isMultipart = isContentTypeOf(request, MULTIPART_FORM_DATA_VALUE);
        if (isMultipart) {
            return request.getContentType() + " :: " + request.getContentLength() + " bytes";
        }

        try {
            return String.valueOf(OBJECT_MAPPER.readTree(request.getInputStream()));
        } catch (Exception e) {
            log.warn("# Failed to parse request body :: {}", e.getMessage());
            return request.getContentType() + " :: " + request.getContentLength() + " bytes";
        }
    }

    /**
     * Read request body with maximum length
     *
     * @param maxLength Maximum length of the request body,
     * @return request body with maximum length
     * @throws IOException when failed to read request body
     */
    public static String getRequestBody(int maxLength) throws IOException {
        InputStream inputStream = getRequest().getInputStream();
        StringBuilder requestBodyBuilder = new StringBuilder();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            if (maxLength != -1) {
                int lengthToAppend = Math.min(bytesRead, maxLength - requestBodyBuilder.length());
                requestBodyBuilder.append(new String(buffer, 0, lengthToAppend, StandardCharsets.UTF_8));
                if (requestBodyBuilder.length() >= maxLength) {
                    break; // Stop reading when maxLength is reached
                }
            } else {
                requestBodyBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            }
        }

        return requestBodyBuilder.toString();
    }

    /**
     * Read response body
     *
     * @return response body as string
     */
    public static String getResponseBody() {
        return getResponseBody(-1);
    }

    /**
     * Read response body with maximum length
     *
     * @param maxLength Maximum length of the response body,
     * @return response body with maximum length
     */
    public static String getResponseBody(int maxLength) {
        ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper)getResponse();
        StringBuilder payloadBuilder = new StringBuilder();
        if (wrapper != null) {
            wrapper.setCharacterEncoding(StandardCharsets.UTF_8.name());
            byte[] buf = wrapper.getContentAsByteArray();
            int length = maxLength == -1 ? buf.length : Math.min(buf.length, maxLength);
            if (length > 0) {
                payloadBuilder.append(new String(buf, 0, length, StandardCharsets.UTF_8));
            }
        }
        return payloadBuilder.toString();
    }

    /**
     * Check if the request content type is the same as the given content type
     *
     * @param request     HttpServletRequest
     * @param contentType content type
     * @return true if the request content type is the same as the given content type
     */
    private static boolean isContentTypeOf(HttpServletRequest request, String contentType) {
        return request.getContentType() != null && request.getContentType().startsWith(contentType);
    }

}
