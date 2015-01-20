package com.bfgame.app.net;

import java.io.Serializable;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/**
 * 目标：
 * 1、安全有序
 * 2、高效
 * 3、易用、易控制
 * 4、activity停止后停止该activity所用的线程。
 * 5、监测内存，当内存溢出的时候自动垃圾回收，清理资源 ，当程序退出之后终止线程池
 * @author zxy
 *
 */
public class BaseRequest  implements   Runnable, Serializable {
	//static HttpClient httpClient = null;
	HttpUriRequest request = null;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	protected ParseHandler handler = null;
	protected String url = null;
	/**
	 * default is 5 ,to set .
	 */
	protected int connectTimeout = 5000;
	/**
	 * default is 5 ,to set .
	 */
	protected int readTimeout = 20000;
//	protected RequestResultCallback requestCallback = null;
	
	
	@Override
	public void run() {
		
	}
	
	protected void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
	protected void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
	public HttpUriRequest getRequest() {
		return request;
	}
	
	
	protected boolean isCookieTimeOut(String json){
		boolean result = false;
		try{
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(json);
			int status = 0;
			if(obj.containsKey("status")){
				status = Integer.parseInt(obj.get("status").toString());
			}
			if(status == 1001){
				result = true;
//				if(obj.containsKey("error")){
//					BaseActivity.showToast(obj.get("error").toString());
//				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
}
