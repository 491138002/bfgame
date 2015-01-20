package com.bfgame.app.net.utils;

public class ErrorUtil {
	public static String errorJson(String resultCode,String message){
		return "{\"status\":\""+resultCode+"\",\"errno\":\""+resultCode+"\",\"error\":\""+message+"\"}";
	}
}
