package com.bfgame.app.procotol;

import java.util.List;

import org.json.simple.BaseJson;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.bfgame.app.vo.SearchKey;
import com.google.gson.reflect.TypeToken;
/**
 * 搜索关键字解析
 * @author admin
 *
 */
public class SearchKeyResponseMessage extends ResponseMessage {

	private List<SearchKey> resultList;
	private SearchKey result;

	public List<SearchKey> getResultList() {
		return resultList;
	}

	public void setResultList(List<SearchKey> resultList) {
		this.resultList = resultList;
	}

	public SearchKey getResult() {
		return result;
	}

	public void setResult(SearchKey result) {
		this.result = result;
	}

	protected void parseBody(JSONObject obj) throws ParseException {
		if (obj.containsKey("data")) {
			String strRes = String.valueOf(obj.get("data"));
			String strFirst = strRes.substring(0, 1);
			if (strFirst.equals("[")) {
				if (obj.containsKey("data")) {
					String res = obj.get("data").toString();
					resultList = (List<SearchKey>) BaseJson.parser(
							new TypeToken<List<SearchKey>>() {
							}, res);
				}
			}else{
				String res = obj.get("data").toString();
				result = (SearchKey) BaseJson.parser(
						new TypeToken<SearchKey>() {
						}, res);
			}
		}
	}

}
