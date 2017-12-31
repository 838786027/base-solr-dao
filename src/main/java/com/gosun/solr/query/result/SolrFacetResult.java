package com.gosun.solr.query.result;

/**
 * 构面查询
 * 代表一个构面
 * @author caixiaopeng
 * 
 */
public class SolrFacetResult {
	/**
	 * 构面元素的值
	 */
	private String value;
	
	/**
	 * 构面元素的数量
	 */
	private long count;

	/*------------------------
	 * getter/setter
	 *-----------------------*/
	public String getValue() {
		return value;
	}
	public SolrFacetResult setValue(String value) {
		this.value = value;
		return this;
	}
	public long getCount() {
		return count;
	}
	public SolrFacetResult setCount(long count) {
		this.count = count;
		return this;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SolrFacetResult [value=").append(value).append(", count=").append(count).append("]");
		return builder.toString();
	}
}
