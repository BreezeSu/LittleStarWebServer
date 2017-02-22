package model;

/**
 * 图片信息模型
 *
 * @author Su
 *
 */
public class PhotoInfoRestModel
{
	/**
	 * 照片名称
	 */
	private String name;

	/**
	 * 拍摄日期
	 */
	private String date;

	/**
	 * 文件大小
	 */
	private String size;

	/**
	 * 水平像素
	 */
	private String xResolution;

	/**
	 * 垂直像素
	 */
	private String yResolution;
	/**
	 * 相机型号
	 */
	private String cameraModel;

	public PhotoInfoRestModel()
	{

	}

	public PhotoInfoRestModel(String name)
	{
		setName(name);
	}

	public PhotoInfoRestModel(String name, String date, String size)
	{
		setName(name);
		setDate(date);
		setSize(size);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getSize()
	{
		return size;
	}

	public void setSize(String size)
	{
		this.size = size;
	}

	public String getxResolution()
	{
		return xResolution;
	}

	public void setxResolution(String xResolution)
	{
		this.xResolution = xResolution;
	}

	public String getyResolution()
	{
		return yResolution;
	}

	public void setyResolution(String yResolution)
	{
		this.yResolution = yResolution;
	}

	public String getCameraModel()
	{
		return cameraModel;
	}

	public void setCameraModel(String cameraModel)
	{
		this.cameraModel = cameraModel;
	}

}
