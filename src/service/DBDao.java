package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DB 业务
 *
 * @author Su
 *
 */
public class DBDao
{

	private static DBDao INSTANCE;

	/**
	 * DB URL
	 */
	private final String url = "jdbc:mysql://127.0.0.1/littlestar";

	/**
	 * DB Driver
	 */
	private final String name = "com.mysql.jdbc.Driver";

	/**
	 * 用户名
	 */
	private final String user = "root";

	/**
	 * 密码
	 */
	private final String password = "";

	/**
	 * DB连接
	 */
	private Connection dbConnection = null;

	private Statement stmt = null;

	private ResultSet result = null;

	private DBDao()
	{
		getStatement();
	}

	public static DBDao getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new DBDao();
		}
		return INSTANCE;
	}

	/**
	 * 获取DB Statement
	 *
	 * @return Statement
	 */
	private Statement getStatement()
	{
		try
		{
			Class.forName(name);
			dbConnection = DriverManager.getConnection(url, user, password);// 获取连接
			stmt = dbConnection.createStatement();
		}
		catch (final ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}

		return stmt;
	}

	/**
	 * 执行SQL语句
	 *
	 * @param sql
	 * @return
	 */
	public void executeSql(String sql)
	{

		try
		{
			stmt.execute(sql);
		}
		catch (final SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 执行SQL语句
	 *
	 * @param sql
	 * @return
	 */
	public ResultSet executeQuerySql(String sql)
	{

		try
		{
			result = stmt.executeQuery(sql);
		}
		catch (final SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 关闭DB连接
	 */
	public void close()
	{
		try
		{
			if (result != null)
			{
				result.close();
			}
			dbConnection.close();
			stmt.close();

		}
		catch (final SQLException e)
		{
			e.printStackTrace();
		}
	}
}
