package com.gosun.solr.query.result;

import java.util.List;

/**
 * 分组查询
 * 代表一个分组
 * @author caixiaopeng
 *
 * @param <T> 实体类型
 */
public class SolrGroupResult<T> {
	/**
	 * 分组值
	 */
	private String value;
	/**
	 * 一个分组内匹配到的DO实体集合
	 */
	private List<T> beans;
	/**
	 * 一个分组内匹配到的总数
	 */
	private long amounts;
	
	/*------------------------
	 * getter/setter
	 *-----------------------*/
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<T> getBeans() {
		return beans;
	}
	public void setBeans(List<T> beans) {
		this.beans = beans;
	}
	public long getAmounts() {
		return amounts;
	}
	public void setAmounts(long amounts) {
		this.amounts = amounts;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SolrGroupResult [value=").append(value).append(", beans=").append(beans).append(", amounts=")
				.append(amounts).append("]");
		return builder.toString();
	}
}
