package com.sand.qa.database;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

/**
 * Mongo Class
 * 
 * @author nihuaiqing
 */
public class MongoUtil {

	Mongo mongo;

	DB db;

	DBCollection coll;

	private int limit = 10;

	/**
	 * 配置限制返回的记录数量
	 * @param limit 表示配置得限制记录的数量
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 与Mongo数据库之间建立连接
	 * @param key key值
	 */
	public MongoUtil(String key) {
		try {
			mongo = new Mongo(System.getProperty("DB." + key + ".host"),
					Integer.valueOf(System.getProperty("DB." + key + ".port")));
			db = mongo.getDB(System.getProperty("DB." + key + ".database"));
			if (db.authenticate(System.getProperty("DB." + key + ".username"),
					System.getProperty("DB." + key + ".password").toCharArray())) {
				//System.out.println("Mongo Connection Success锛�);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 建立集合
	 * @param collName 集合名
	 */
	public void setCollection(String collName) {
		coll = db.getCollection(collName);
	}

	/**
	 * 根据sql语句获得查询结果
	 * @param sql Map字典
	 * @param limit 返回结果数量限制
	 * @return
	 */
	public String[] selectForResult(Map<String, Object> sql, int limit) {
		BasicDBObject searchQuery = new BasicDBObject();
		List<String> r = new ArrayList<String>();
		for (String key : sql.keySet()) {
			searchQuery.put(key, sql.get(key));
		}
		DBCursor cursor = coll.find(searchQuery).limit(limit);
		while (cursor.hasNext()) {
			r.add(cursor.next().toString());
		}
		String[] result = new String[r.size()];
		int i = 0;
		for (String s : r) {
			result[i++] = s;
		}
		return result;
	}

	/**
	 * 根据列名获取sql语句的查询制
	 * @param sql Map字典
	 * @param limit 返回的限制数值
	 * @param columnName 列名
	 * @return
	 */
	public String[] selectForResult(Map<String, Object> sql, int limit,
			String columnName) {
		BasicDBObject searchQuery = new BasicDBObject();
		List<String> r = new ArrayList<String>();
		for (String key : sql.keySet()) {
			searchQuery.put(key, sql.get(key));
		}
		DBCursor cursor = coll.find(searchQuery).limit(limit);
		while (cursor.hasNext()) {
			r.add(cursor.next().get(columnName).toString());
		}
		String[] result = new String[r.size()];
		int i = 0;
		for (String s : r) {
			result[i++] = s;
		}
		return result;
	}

	/**
	 * 根据sql返回查询值，此时的返回数量限制为默认值
	 * @param sql Map字典
	 * @return
	 */
	public String[] selectForResult(Map<String, Object> sql) {
		return selectForResult(sql, this.limit);
	}

	/**
	 * 根据sql语句返回查询值，此时的返回数量需自己指定
	 * @param sql SQL语句
	 * @param limit 指定的返回限制数量
	 * @return
	 */	
	public String[] selectForResult(String sql, int limit) {
		return selectForResult(parseSql(sql), limit);
	}

	/**
	 * 根据sql语句返回查询值，此时无返回数量限制
	 * @param sql 查询的SQL语句
	 * @return
	 */
	public String[] selectForResult(String sql) {
		return selectForResult(parseSql(sql));
	}

	/**
	 * 输入为字典格式的sql返回第一条查询结果
	 * @param sql Map字典格式
	 * @return 返回查询到的查询值，若失败返回""
	 */
	public String selectForRow(Map<String, Object> sql) {
		String[] result = selectForResult(sql, 1);
		if (result.length == 1) {
			return selectForResult(sql, 1)[0];
		} else {
			return "";
		}
	}

	/**
	 * 输入为字符串格式的sql返回第一条查询结果
	 * @param sql 字符串格式的sql
	 * @return 返回查询到的查询值，若失败返回""
	 */
	public String selectForRow(String sql) {
		return selectForRow(parseSql(sql));
	}

	/**
	 * 根据列名查询的返回结果
	 * @param sql Map字典格式的sql
	 * @param limit 返回数量限制
	 * @param columnName 列名
	 * @return
	 */
	public String[] selectForColumn(Map<String, Object> sql, int limit,
	 		String columnName) {
		return selectForResult(sql, limit, columnName);
	}

	/**
	 * 输入为string格式的sql查询的列名为columnName的结果
	 * @param sql  SQL的字符串格式
	 * @param limit 返回数量限制
	 * @param columnName 列名
	 * @return
	 */
	public String[] selectForColumn(String sql, int limit, String columnName) {
		return selectForResult(parseSql(sql), limit, columnName);
	}

	/**
	 * 输入为字典格式的sql查询的列名为columnName且仅返回1条结果
	 * @param sql Map字典
	 * @param columnName 列名
	 * @return
	 */
	public String selectForColumn(Map<String, Object> sql, String columnName) {
		String[] result = selectForResult(sql, 1, columnName);
		if (result.length == 1) {
			return result[0];
		}
		return "";
	}

	/**
	 * 输入为字符串格式的sql查询的列名为columnName且仅返回1条结果
	 * @param sql sql为字符串格式的语句
	 * @param columnName 列名
	 * @return
	 */
	public String selectForColumn(String sql, String columnName) {
		return selectForColumn(parseSql(sql), columnName);
	}

	private Map<String, Object> parseSql(String sql) {
		Map<String, Object> sqls = new HashMap<String, Object>();
		if (sql.startsWith("{") && sql.endsWith("}") && sql.length() > 2) {
			sql = sql.substring(1, sql.length() - 1);
			String[] keyvalues = sql.split(",");
			for (String k : keyvalues) {
				String[] columns = k.split(":");
				columns[0] = columns[0].substring(1, columns[0].length() - 1);
				if (columns[1].startsWith("'") && columns[1].endsWith("'")) {
					columns[1] = columns[1].substring(1,
							columns[1].length() - 1);
					sqls.put(columns[0], columns[1]);
				} else {
					sqls.put(columns[0], Long.parseLong(columns[1]));
				}
			}
		}
		return sqls;
	}
	
}

