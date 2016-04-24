package com.sina.scm.data.util;


import java.io.Serializable;

/**
 * 分页器
 * 
 *  
 */
public class Pagination implements Serializable {

	private static final long serialVersionUID = -7438949134440092123L;

	/** 默认每页行数 */
	public static final int DEFAULT_PAGE_SIZE = 20;

	/** 当前页 */
	private int curPage = 0;

	/** 每页数量大小 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/** 总数 */
	private int total = 0;

	/** 总页数 */
	private int totalPage = 0;


	/**
	 * 构造函数
	 * 
	 * @param page
	 *            请求的页码
	 */
	public Pagination(int page) {
		this(page, DEFAULT_PAGE_SIZE);
	}

	/**
	 * 构造函数
	 * 
	 * @param page
	 *            请求的页码
	 * @param pageSize
	 *            每页数量
	 */
	public Pagination(int page, int pageSize) {
		// 传入的页码如果小于1则默认从第一页开始
		this.curPage = page < 1 ? 1 : page;
		// 传入的每页数量如果小于1则取默认的每页数量
		this.pageSize = pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
	}



	public int getCurPage() {
		return curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotal() {
		return total;
	}

	public final void setTotal(int total) {
		this.total = total;
		totalPage = (total % pageSize == 0) ? (total / pageSize) : (total
				/ pageSize + 1);
	}

	public int getTotalPage() {
		return totalPage;
	}

	
	public int getStart() {
		return (curPage - 1) * pageSize;
	}

	public int getCount() {
		return pageSize;
	}
	
	public int getEnd() {
		return this.getStart() + this.getPageSize();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("curPage:").append(curPage).append(",pageSize:")
				.append(pageSize).append(",total:").append(total);
		return sb.toString();
	}

}
