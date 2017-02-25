package service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import model.PhotoInfoRestModel;
import model.PhotoInfoSO;

/**
 * 数据库服务类
 *
 * @author Su
 *
 */
public class DBService
{

	private final Logger log = Logger.getLogger(DBService.class);

	private final String tableName = "photoinfo";

	/**
	 * 插入图片信息
	 *
	 * @param photoList
	 *            图片列表
	 */
	public void insertPhotoInfo(List<PhotoInfoRestModel> photoList)
	{
		final String insertHead = "insert into " + tableName + " (name) values ";
		final StringBuilder sql = new StringBuilder(insertHead);
		for (final PhotoInfoRestModel photoInfo : photoList)
		{
			sql.append("(").append('"').append(photoInfo.getName()).append('"').append("),");
		}
		DBDao.getInstance().executeSql(sql.substring(0, sql.length() - 1).toString());

	}

	/**
	 * 查询图片信息
	 * 
	 * @param so
	 *            图片搜索条件
	 * @return 图片信息列表
	 */
	public List<PhotoInfoRestModel> queryPhotoInfo(PhotoInfoSO so)
	{
		final List<PhotoInfoRestModel> photoInfoList = new ArrayList<PhotoInfoRestModel>(20);
		final StringBuilder strBuilder = new StringBuilder("SELECT * FROM ");
		strBuilder.append(tableName);
		if (so.getName() != null)
		{
			strBuilder.append(" WHERE name = ").append("'").append(so.getName()).append("'");
		}
		strBuilder.append(" ORDER BY ").append(so.getOrderBy());
		if (so.isOrderRule())
		{
			strBuilder.append(" ASC ");
		}
		else
		{
			strBuilder.append(" DESC ");
		}

		strBuilder.append(" LIMIT ").append(so.getPageBegin()).append(",").append(so.getPageLength());

		final ResultSet result = DBDao.getInstance().executeQuerySql(strBuilder.toString());

		try
		{
			while (result.next())
			{
				final PhotoInfoRestModel photoInfo = new PhotoInfoRestModel();
				photoInfo.setName(result.getString(2));
				photoInfo.setDate(result.getString(3));
				photoInfoList.add(photoInfo);
			}
		}
		catch (final SQLException e)
		{
			log.error(e);
		}
		finally
		{
			try
			{
				if (result != null)
				{
					result.close();
				}
			}
			catch (final SQLException e)
			{
				log.error(e);
			}
		}
		return photoInfoList;
	}

	public void cleanAllPhotoInfo()
	{
		final String sql = "DELETE   FROM " + tableName;
		DBDao.getInstance().executeSql(sql);
	}
}
