package model;

public class PhotoInfoSO
{

	/**
	 * 图片名
	 */
	private String name;

	/**
	 * 分页查询：每页数量，默认20
	 */
	private String pageLength = "20";

	/**
	 * 分页查询：起始页
	 */
	private String pageBegin = "0";

	/**
	 * 按照什么排序
	 */
	private String orderBy = "name";

	/**
	 * 正向排序或逆向排序：True --- 正向;False --- 逆向
	 */
	private boolean orderRule;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPageLength()
	{
		return pageLength;
	}

	public void setPageLength(String pageLength)
	{
		this.pageLength = pageLength;
	}

	public String getPageBegin()
	{
		return pageBegin;
	}

	public void setPageBegin(String pageBegin)
	{
		this.pageBegin = pageBegin;
	}

	public String getOrderBy()
	{
		return orderBy;
	}

	public void setOrderBy(String orderBy)
	{
		this.orderBy = orderBy;
	}

	public boolean isOrderRule()
	{
		return orderRule;
	}

	public void setOrderRule(boolean orderRule)
	{
		this.orderRule = orderRule;
	}

}
