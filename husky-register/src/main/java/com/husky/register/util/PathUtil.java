package com.husky.register.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by google on 16/7/5.
 */
public class PathUtil {

    private static final String SERVICE_PATH = "/husky/server";
    public static final String PATH_SEPARATOR = "/";
    public static final String PLACEHOLDER = "^";


    public static String getServicePath(String serviceName, String group) {
        String path = SERVICE_PATH + PATH_SEPARATOR + escapeServiceName(serviceName).trim();
        if (StringUtils.isNotBlank(group)) {
            path = path + PATH_SEPARATOR + group;
        }
        return path;
    }

    public static String escapeServiceName(String serviceName) {
        return serviceName.replace(PATH_SEPARATOR, PLACEHOLDER);
    }
}
