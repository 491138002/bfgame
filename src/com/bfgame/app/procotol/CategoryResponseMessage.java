package com.bfgame.app.procotol;

import java.util.List;

import org.json.simple.BaseJson;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.bfgame.app.vo.Category;
import com.google.gson.reflect.TypeToken;
/**
 * 分类解析
 * @author admin
 *
 */
public class CategoryResponseMessage extends ResponseMessage {

	private List<Category> resultList;
	private Category result;

	public List<Category> getResultList() {
		return resultList;
	}

	public void setResultList(List<Category> resultList) {
		this.resultList = resultList;
	}

	public Category getResult() {
		return result;
	}

	public void setResult(Category result) {
		this.result = result;
	}

	protected void parseBody(JSONObject obj) throws ParseException {
		if (obj.containsKey("data")) {
			String strRes = String.valueOf(obj.get("data"));
			String strFirst = strRes.substring(0, 1);
			if (strFirst.equals("[")) {
				if (obj.containsKey("data")) {
					String res = obj.get("data").toString();
					resultList = (List<Category>) BaseJson.parser(
							new TypeToken<List<Category>>() {
							}, res);
				}
			}else{
				String res = obj.get("data").toString();
				result = (Category) BaseJson.parser(
						new TypeToken<Category>() {
						}, res);
			}
		}
	}

}
