package com.bfgame.app.procotol;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.BaseJson;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.bfgame.app.vo.GameInfo;
import com.google.gson.reflect.TypeToken;
/**
 * 游戏解析
 * @author admin
 *
 */
public class GameResponseMessage extends ResponseMessage {

	private List<GameInfo> resultList;
	private GameInfo result;

	public List<GameInfo> getResultList() {
		if(resultList == null)
			resultList = new ArrayList<GameInfo>();
		return resultList;
	}

	public void setResultList(List<GameInfo> resultList) {
		this.resultList = resultList;
	}

	public GameInfo getResult() {
		return result;
	}

	public void setResult(GameInfo result) {
		this.result = result;
	}

	protected void parseBody(JSONObject obj) throws ParseException {
		if (obj.containsKey("data")) {
			String strRes = String.valueOf(obj.get("data"));
			String strFirst = strRes.substring(0, 1);
			if (strFirst.equals("[")) {
				if (obj.containsKey("data")) {
					String res = obj.get("data").toString();
					resultList = (List<GameInfo>) BaseJson.parser(
							new TypeToken<List<GameInfo>>() {
							}, res);
				}
			}else{
				String res = obj.get("data").toString();
				res = res.replace("\"gift\":[]", "\"gift\":{}");
				result = (GameInfo) BaseJson.parser(
						new TypeToken<GameInfo>() {
						}, res);
			}
		}
	}

}
