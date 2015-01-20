package com.bfgame.app.net.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * json管理工具
 * @author admin
 *
 */
public class JsonManager {
	/**
	 * json转对象
	 * @author sailor
	 *
	 */
	public static Object jsonToObject(String jsonStr, Object returnObject) {
		JSONObject jsonObject;

		try {
			jsonObject = new JSONObject(jsonStr);
			if (null != returnObject) {
				Field[] fields = returnObject.getClass().getDeclaredFields();
				for (Field f : fields) {
					if (jsonObject.has(f.getName())) {
						f.setAccessible(true);
						f.set(returnObject, jsonObject.get(f.getName()));
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return returnObject;
	}

	/**
	 * json转List
	 * @author chaike
	 *
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> jsonToList(String jsonStr, BaseList<T> baseList) {
		JSONObject jsonObject;
		List<T> list = new ArrayList<T>();
		Class<T> object = null;

		try {
			object = (Class<T>) Class.forName(baseList.getObjectType());
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		try {
			jsonObject = new JSONObject(jsonStr);
			JSONArray jsonArray = jsonObject.getJSONArray(object
					.getSimpleName());
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add((T) jsonToObject(jsonArray.get(i).toString(),
						object.newInstance()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		baseList.setList(list);
		return baseList.getList();
	}
}
