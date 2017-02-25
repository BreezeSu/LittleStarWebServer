
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import common.Constant;
import model.PhotoInfoRestModel;
import model.PhotoInfoSO;
import service.DBService;
import service.PhotoInfoCollector;
import service.ResponseManager;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	DBService dbService = new DBService();

	List<PhotoInfoRestModel> photoList;

	private final Logger log = Logger.getLogger(HelloWorld.class);

	@Override
	public void init() throws ServletException
	{
		super.init();

		dbService.cleanAllPhotoInfo();
		dbService.insertPhotoInfo(PhotoInfoCollector.getInstance().collectPhotoInfo());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// final ServletContext context = getServletContext();// 得到背景对象
		final Map<String, String[]> paraMap = request.getParameterMap();
		querySwitch(paraMap, response);

	}

	private void querySwitch(Map<String, String[]> paraMap, HttpServletResponse response)
	{
		final String[] queryType = paraMap.get("queryType");
		if (queryTypeValidator(queryType))
		{
			switch (queryType[0])
			{
				case "info":
					getPhotoInfo(paraMap, response);
					break;
				case "pic":
					getPhotoPic(paraMap, response);
					break;
				case "snap":
					getSnapView(paraMap, response);
					break;
			}
		}
	}

	private boolean queryTypeValidator(String[] queryType)
	{
		if (queryType == null || queryType.length != 1)
		{
			return false;
		}
		else if ("pic".equals(queryType[0]) || "info".equals(queryType[0]) || "snap".equals(queryType[0]))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private void getPhotoInfo(Map<String, String[]> paraMap, HttpServletResponse response)
	{
		final PhotoInfoSO so = new PhotoInfoSO();
		final String[] name = paraMap.get("name");
		final String[] pageBegin = paraMap.get("pageBegin");
		final String[] pageLength = paraMap.get("pageLength");
		final String[] orderBy = paraMap.get("orderBy");
		if (name != null && name.length == 1)
		{
			so.setName(name[0]);
		}
		if (pageBegin != null && pageBegin.length == 1)
		{
			so.setPageBegin(pageBegin[0]);
		}
		if (pageLength != null && pageLength.length == 1)
		{
			so.setPageLength(pageLength[0]);
		}
		if (orderBy != null && orderBy.length == 1)
		{
			so.setOrderBy(orderBy[0]);
		}

		photoList = dbService.queryPhotoInfo(so);
		ResponseManager.getInstance().getJsonInfo(photoList, response);
	}

	private void getPhotoPic(Map<String, String[]> paraMap, HttpServletResponse response)
	{

		final String[] name = paraMap.get("name");
		if (name != null && name.length == 1)
		{
			final String photoUrl = Constant.PHOTO_DEFAULT_PATH + "\\" + name[0];
			try
			{
				ResponseManager.getInstance().getImageInfo(photoUrl, response);
			}
			catch (final IOException e)
			{
				log.error(e);
			}
		}

	}

	private void getSnapView(Map<String, String[]> paraMap, HttpServletResponse response)
	{
		final String[] name = paraMap.get("name");
		if (name != null && name.length == 1)
		{
			final String photoUrl = Constant.PHOTO_DEFAULT_PATH + "\\" + name[0];
			ResponseManager.getInstance().getSnapView(photoUrl, response);
		}
	}
}
