package com.gosun.solr.query.result;

import java.util.List;

/**
 * Solr基础查询的返回结果集
 * @author caixiaopeng
 *
 * @param <T> 实体类型
 */
public class SolrCommonResult<T> {
	/**
	 * 查询匹配的总数，常用于在分页查询中计算总页数
	 */
	private long amounts;
	/**
	 * 查询得到的DO实体集合
	 */
	private List<T> beans;
	
	/*------------------------
	 * getter/setter
	 *-----------------------*/
	public long getAmounts() {
		return amounts;
	}
	public void setAmounts(long amounts) {
		this.amounts = amounts;
	}
	public List<T> getBeans() {
		return beans;
	}
	public void setBeans(List<T> beans) {
		this.beans = beans;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SolrCommonResult [amounts=").append(amounts).append(", beans=").append(beans).append("]");
		return builder.toString();
	}
}
