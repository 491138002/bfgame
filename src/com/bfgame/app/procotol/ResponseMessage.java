package com.bfgame.app.procotol;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.bfgame.app.util.LogUtil;

/**
 * 解析基类
 * 
 * @author jzy
 * 
 */
public abstract class ResponseMessage implements MessageIF {
	private JSONParser parser;
	public int resultCode;
	public String message;
	public String more;
	public long expires;
	public static HashMap<String, String> RESULT_MESSAGE = new HashMap<String, String>();

	public ResponseMessage() {
		parser = new JSONParser();
	}

	private void parseHeader(JSONObject obj) throws Exception {
		if (obj.containsKey("code")) {
			resultCode = Integer.parseInt(obj.get("code").toString());
		} else {
			resultCode = 1;
		}
		if (obj.containsKey("msg")) {
			message = (String) obj.get("msg");
		} else {
			message = "数据解析异常，请重试";
		}
		if (obj.containsKey("expires")) {
			expires = Long.parseLong(obj.get("expires").toString());
		}
		if (obj.containsKey("more")) {
			more = obj.get("more").toString();
		}
	}

	public void parseResponse(String jsonStr) throws ParseException {
		try {
			parseResponse(jsonStr, true);
		} catch (ParseException e) {
			throw e;
		}
	}

	public void parseResponse(String jsonStr, boolean isShowError)
			throws ParseException {
		if (jsonStr == null || jsonStr.equals("")) {
			LogUtil.i("test", "result is null");
			return;
		}
		try {
			JSONObject obj = (JSONObject) parser.parse(jsonStr);
			parseHeader(obj);
			if (resultCode == 0 || resultCode == 1) {// 请求成功，有结果返回
				// todo 状态判断
				parseBody(obj);
			} else {
//				if(isShowError)
//					BaseActivity.showToast(message);
			}
		} catch (ParseException pe) {
			message = "数据解析异常，请重试";
			throw pe;
		} catch (Exception ex) {
			message = "数据解析异常，请重试";
			ex.printStackTrace();
		}
	}

//	public void parseResponse(String jsonStr, boolean is_status)
//			throws ParseException {
//		if (jsonStr == null || jsonStr.equals("")) {
//			LogUtil.i("test", "result is null");
//			return;
//		}
//		try {
//			JSONObject obj = (JSONObject) parser.parse(jsonStr);
//			parseHeader(obj);
//			if (is_status) {
//				parseBody(obj);
//			} else {
//				if (resultCode == 0) {// 请求成功，有结果返回
//					// todo 状态判断
//					parseBody(obj);
//				}
//			}
//		} catch (ParseException pe) {
//			message = "数据解析异常，请重试";
//			throw pe;
//		} catch (Exception ex) {
//			message = "数据解析异常，请重试";
//			ex.printStackTrace();
//		}
//	}

	/**
	 * 解析消息体
	 * 
	 * @param obj
	 */
	protected abstract void parseBody(JSONObject obj) throws ParseException;

	public int getResultCode() {
		return resultCode;
	}

	public String getMessage() {
		return message;
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

}
