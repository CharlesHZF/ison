package com.importsource.ison;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 实现一个真正轻量级的json转换器
 * 暂不支持线程安全。
 * 
 * @author Hezf
 *
 *
 *
 *         Data is in name/value pairs Data is separated by commas Curly braces
 *         hold objects Square brackets hold arrays
 */
public class Ison {
	public static final String COMMA = ",", LEFT_CURLY_BRACES = "{", RIGHT_CURLY_BRACES = "}",
			LEFT_SQUARE_BRACKETS = "[", RIGHT_SQUARE_BRACKETS = "]", COLON = ":";
	
	protected StringBuilder sb;

	public String toJson(List<Map<String, Object>> list, String rootName) {
		return toJson1(list, rootName);
	}

	
	public String toJson(List<Map<String, Object>> list) {
		return toJson1(list, "data");
	}


	private String toJson1(List<Map<String, Object>> list, String rootName) {
		// "employees":[ {"firstName":"Anna", "lastName":"Smith"},
		// {"firstName":"Peter", "lastName":"Jones"}]
		sb = new StringBuilder();
		appendLCB();
		sb.append("\"" + rootName + "\"");
		appendColon();
		appendLSB();
		append(list);
		appendRSB();
		appendRCB();
		return sb.toString();
	}

	

	private void append(List<Map<String, Object>> list) {
		for (int i = 0; i < list.size(); i++) {
			appendLCB();
			Map<String, Object> map = list.get(i);
			Set<String> keys = map.keySet();
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				Object value = map.get(key);

				appendKey(key);

				appendColon();

				if (value instanceof String) {
					appendValue(value);
				}
				if(value instanceof List){
					appendLSB();
					append((List<Map<String, Object>>)value);
					appendRSB();
				}

				if (iterator.hasNext()) {
					appendSeparator();
				}
			}
			appendRCB();
			if (i != list.size() - 1) {
				appendSeparator();
			}
		}
	}

	
	private void appendRSB() {
		sb.append(RIGHT_SQUARE_BRACKETS);
	}

	private void appendLSB() {
		sb.append(LEFT_SQUARE_BRACKETS);
	}
	
	private void appendColon() {
		sb.append(COLON);
	}

	private void appendLCB() {
		sb.append(LEFT_CURLY_BRACES);
	}

	private void appendRCB() {
		sb.append(RIGHT_CURLY_BRACES);
	}

	private void appendSeparator() {
		sb.append(COMMA);
	}

	private void appendValue(Object value) {
		sb.append("\"");
		sb.append(value);
		sb.append("\"");
	}

	private void appendKey(String key) {
		sb.append("\"");
		sb.append(key);
		sb.append("\"");
	}

	public static void main(String[] args) {
		
		List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("firstName", "Anna");
		map.put("lastName", "Smith");
		
		users.add(map);
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("firstName", "Peter");
		map1.put("lastName", "Jones");
		List<Map<String, Object>> users1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> map11 = new HashMap<String, Object>();
		map11.put("firstName", "Anna");
		map11.put("lastName", "Smith");
		users1.add(map11);
		
		map1.put("family", users1);
		users.add(map1);
		
		Ison ison = new Ison();
		System.out.println(ison.toJson(users, "employees"));
		System.out.println(ison.toJson(users));
	}
}