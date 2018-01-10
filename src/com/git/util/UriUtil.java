package com.git.util;

import javax.servlet.http.HttpServletRequest;

public class UriUtil {
	
	public static String getControllerUri(HttpServletRequest request, String uri) {
		if(uri.contains("?")) {//有"?"，就截取"?"之前的部分
    		uri = uri.substring(request.getContextPath().length(), uri.indexOf("?"));
    	} else {//没有"?"，就截取全部
    		uri = uri.substring(request.getContextPath().length(), uri.length());
    	}
		return uri;
	}
}
