package io.searchbox.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.searchbox.client.JestResult;

/**
 * ES sql查询结果
 * @author Thinkpad
 *
 */
public class SqlSearchResult extends JestResult{

	public SqlSearchResult(Gson gson) {
		super(gson);
	}
	
	public List<Map<String,Object>> getData(){
		JsonElement obj = this.jsonObject.get("columns");
		JsonElement rowsObj = this.jsonObject.get("rows");
		JsonArray titles = obj.getAsJsonArray();
		
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		for(JsonElement hitElement : rowsObj.getAsJsonArray()) {
			if(hitElement.isJsonArray()) {
				JsonArray values = hitElement.getAsJsonArray();
				Map<String,Object> dataMap = new HashMap<String,Object>();
				for(int i=0;i<values.size();i++) {
					JsonElement val = values.get(i);
					JsonElement title = titles.get(i);
					JsonObject titleobject = title.getAsJsonObject();
					String name = titleobject.get("name").getAsString();
					String type = titleobject.get("type").getAsString();
					dataMap.put(name, parseObject(val, type));
					
				}
				dataList.add(dataMap);
			}
		}
		return dataList;
	}
	
	/**
	 * 格式化数据
	 * @param val
	 * @param type
	 * @return
	 */
	private Object parseObject(JsonElement val,String type) {
		if("text".equals(type)) {
			return val.getAsString();
		}else if("date".equals(type)) {
			String d = val.getAsString();
			d = d.substring(0, 10);
			return d;
		}else if("float".equals(type)) {
			float f = val.getAsFloat();
			BigDecimal b = new BigDecimal(f);
			b.setScale(2, 4);
			return b.floatValue();
		}
		return val.getAsString();
	}

}
