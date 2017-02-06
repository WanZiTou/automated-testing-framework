package com.sand.qa.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * DB Class
 * 
 * @author nihuaiqing
 */
public class DBUtil {

	private Connection ct;

	private DBConnection conn;

	private DBType type;

	private String[] columns;
	
	public DBUtil() {}
	
	public DBUtil(String key) {
		try {
			type = DBType.valueOf(System.getProperty("DB." + key + ".type",
					"MYSQL").toUpperCase());
		} catch (Exception e) {
			type = DBType.MYSQL;
		}
		conn = new DBConnection(type,
				System.getProperty("DB." + key + ".host"),
				System.getProperty("DB." + key + ".username"),
				System.getProperty("DB." + key + ".password"),
				System.getProperty("DB." + key + ".database"),
				System.getProperty("DB." + key + ".port", ""));

	}
	
	
    /**
     *  @Description 该方法主要使用场景：<br>
     *  <b>1. 在测试业务中有对数据为分库分表存储时，使用此方法创建不同数据库链接</b>
     *  @author fengwei5
     *  @param key:<b>属性文件配数据的关键字，如M 代表mysql<b>   ; <br>datebase:<b>属性文件中配置数据库地址的变量字段  如：DB.M0.database = popfin0 ; M0 用database参数化 </b> 
     */
	public void setConnectionInfo(String key,String datebase){
		try {
			type = DBType.valueOf(System.getProperty("DB." + key + ".type",
					" ").toUpperCase());
			System.out.println("type"+type);
		} catch (Exception e) {
			type = DBType.MYSQL;
		}
	

		 conn = new DBConnection(type,
				System.getProperty("DB." + key + ".host"),
				System.getProperty("DB." + key + ".username"),
				System.getProperty("DB." + key + ".password"),
				System.getProperty("DB." + datebase + ".database"),
				System.getProperty("DB." + key + ".port", ""));
		
	}
	
	/**
	 * 判断数据库连接是否有效
	 * 
	 * @throws SQLException
	 */
	public boolean isConnectValid() throws SQLException {
		if (!ct.isClosed()) {
			return ct.isValid(0);
		}
		return false;
	}

	/**
	 * 数据库连接
	 * @return 
	 */
	public void connection() {
		
		try {
			Class.forName(conn.getJDBCDriver()).newInstance();
			ct = DriverManager.getConnection(conn.getJDBCString(),
					conn.getDbusername(), conn.getDbpasswd());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭数据库连接
	 */
	public void close() {
		try {
			ct.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean isConnectClosed() throws SQLException
	{
		return ct.isClosed();
	}

	/**
	 * 根据sql语句查询的第一行第一列信息
	 * 
	 * @param sql
	 *            SQL语句
	 * @return
	 */
	public Object selectForFirstColumn(String sql) {
		return selectForColumn(sql, 1, 1);
	}

	/**
	 * 根据sql查询行号为row列名为columnName的数据信息
	 * 
	 * @param sql
	 *            SQL查询语句
	 * @param row
	 *            行号
	 * @param columnName
	 *            列名
	 * @return
	 */
	public Object selectForColumn(String sql, int row, String columnName) {
		Object[] rowdata = selectForRow(sql, row);
		int index = getColumnIndexByName(columnName);
		if (index > 0 && index <= rowdata.length)
			return rowdata[index - 1];
		return new Object();
	}

	/**
	 * 根据sql查询行号为row列号为colum的数据信息
	 * 
	 * @param sql
	 *            SQL查询语句
	 * @param row
	 *            行号
	 * @param column
	 *            列号
	 * @return
	 */
	public Object selectForColumn(String sql, int row, int column) {
		Object[] rowdata = selectForRow(sql, row);
		if (column > 0 && column <= rowdata.length)
			return rowdata[column - 1];
		return new Object();
	}

	/**
	 * 根据sql查询列号为colum的所有信息
	 * 
	 * @param sql
	 *            SQL语句
	 * @param column
	 *            列号
	 * @return
	 */
	public Object[] selectForColumn(String sql, int column) {
		Object[][] result = selectForResult(sql);
		if (result.length > 0 && column > 0 && column <= result[0].length) {
			Object[] o = new Object[result.length];
			for (int i = 0; i < result.length; i++) {
				o[i] = result[i][column - 1];
			}
			return o;
		}
		return new Object[] {};
	}

	/**
	 * 根据sql语句查询列名为columnName的所有信息
	 * 
	 * @param sql
	 *            SQL语句
	 * @param columnName
	 *            列名
	 * @return
	 */
	public Object[] selectForColumn(String sql, String columnName) {
		Object[][] result = selectForResult(sql);
		int index = getColumnIndexByName(columnName);
		if (result.length > 0 && index > 0 && index <= result[0].length) {
			Object[] o = new Object[result.length];
			for (int i = 0; i < result.length; i++) {
				o[i] = result[i][index - 1];
			}
			return o;
		}
		return new Object[] {};
	}

	/**
	 * 根据sql查询出的第一行数据信息
	 * 
	 * @param sql
	 *            SQL语句
	 * @return
	 */
	public Object[] selectForFristRow(String sql) {
		return selectForRow(sql, 1);
	}

	/**
	 * 根据sql语句查询索引号为index的对象信息，也就是第index行信息
	 * 
	 * @param sql
	 *            SQL语句
	 * @param index
	 *            索引号
	 * @return
	 */
	public Object[] selectForRow(String sql, int index) {
		Object[][] resultset = selectForResult(sql);
		if (index > 0 && index <= resultset.length) {
			return resultset[index - 1];
		}
		return new Object[] {};
	}

	/**
	 * 根据sql语句返回所有查询的数据信息
	 * 
	 * @param sql
	 *            SQL语句
	 * @return
	 */
	public Object[][] selectForResult(String sql) {
	
		try {
			Statement cs = ct.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = cs.executeQuery(sql);
			getColumns(rs.getMetaData());
			int columnsize = columns.length;
			rs.last();
			int rowsize = rs.getRow();
			rs.beforeFirst();
			if (rs.next()) {
				Object[][] o = new Object[rowsize][columnsize];
				for (int r = 1; r <= rowsize; r++, rs.next()) {
					for (int c = 1; c <= columnsize; c++) {
						o[r - 1][c - 1] = rs.getObject(c);
					}
				}
				rs.close();
				return o;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Object[][] {};
	}

	/**
	 * 执行单条sql语句
	 * 
	 * @param sql
	 *            SQL语句
	 * @return 返回值为0代表未查询到任何条目，返回非零值表示返回的行数
	 */
	public int execute(String sql) {
		try {
			ct.setAutoCommit(false);
			Statement cs = ct.createStatement();
			int i = cs.executeUpdate(sql);
			ct.commit();
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 执行多条sql语句
	 * 
	 * @param sqls
	 *            SQL语句集，参数为String数组
	 */
	public void execute(String[] sqls) {
		try {
			ct.setAutoCommit(false);
			Statement cs = ct.createStatement();
			for (String sql : sqls) {
				cs.executeUpdate(sql);
			}
			ct.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * sql语句查询结果显示
	 * 
	 * @param sql
	 *            SQL语句
	 */
	public void showSqlResult(String sql) {
		for (Object[] row : selectForResult(sql)) {
			StringBuilder rowline = new StringBuilder();
			for (Object column : row) {
				rowline.append(column + " ");
			}
			showColumnName();
			System.out.println(rowline.toString());
		}
	}

	/**
	 * 根据index提取sql查询到的数据内容
	 * 
	 * @param sql
	 *            SQL语句
	 * @param index
	 *            索引值
	 */
	public void showSqlRow(String sql, int index) {
		StringBuilder rowline = new StringBuilder();
		for (Object o : selectForRow(sql, index)) {
			rowline.append(o + " ");
		}
		showColumnName();
		System.out.println(rowline.toString());
	}

	
	private int getColumnIndexByName(String columnName) {
		for (int i = 0; i <columns.length; i++) {
			if (columns[i].equalsIgnoreCase(columnName)) {
				return i + 1;
			}
		}
		return 0;
	}

	private void showColumnName() {
		StringBuilder rowline = new StringBuilder();
		for (int i = 0; i < columns.length; i++) {
			rowline.append(columns[i] + " ");
		}
		System.out.println(rowline.toString());
	}

	private void getColumns(ResultSetMetaData metadata) {
		try {
			columns = new String[metadata.getColumnCount()];
			for (int i = 0; i < columns.length; i++) {
				columns[i] = metadata.getColumnName(i + 1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
