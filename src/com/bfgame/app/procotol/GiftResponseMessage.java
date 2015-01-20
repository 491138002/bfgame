package com.bfgame.app.procotol;

import java.util.List;

import org.json.simple.BaseJson;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.bfgame.app.vo.Gift;
import com.google.gson.reflect.TypeToken;
/**
 * 礼包解析
 * @author admin
 *
 */
public class GiftResponseMessage extends ResponseMessage {

	private List<Gift> resultList;
	private Gift result;

	public List<Gift> getResultList() {
		return resultList;
	}

	public void setResultList(List<Gift> resultList) {
		this.resultList = resultList;
	}

	public Gift getResult() {
		return result;
	}

	public void setResult(Gift result) {
		this.result = result;
	}

	protected void parseBody(JSONObject obj) throws ParseException {
		if (obj.containsKey("data")) {
			String strRes = String.valueOf(obj.get("data"));
			String strFirst = strRes.substring(0, 1);
			if (strFirst.equals("[")) {
				if (obj.containsKey("data")) {
					String res = obj.get("data").toString();
					resultList = (List<Gift>) BaseJson.parser(
							new TypeToken<List<Gift>>() {
							}, res);
				}
			}else{
				String res = obj.get("data").toString();
				result = (Gift) BaseJson.parser(
						new TypeToken<Gift>() {
						}, res);
			}
		}
	}

}
