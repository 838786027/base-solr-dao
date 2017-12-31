package com.gosun.solr.query.result;

import java.util.LinkedList;
import java.util.List;

/**
 * 深度构面结果集
 * @author caixiaopeng
 *
 */
public class SolrFacetPivotResult {
	/**
	 * 构面元素的值
	 */
	private Object value;
	
	/**
	 * 构面元素的数量
	 */
	private Long count;
	
	/**
	 * 下一层
	 */
	private List<SolrFacetPivotResult> nexts=new LinkedList<SolrFacetPivotResult>();
	
	public Object getValue() {
		return value;
	}
	public SolrFacetPivotResult setValue(Object value) {
		this.value = value;
		return this;
	}
	public Long getCount() {
		return count;
	}
	public SolrFacetPivotResult setCount(Long count) {
		this.count = count;
		return this;
	}
	public List<SolrFacetPivotResult> getNexts() {
		return nexts;
	}
	public SolrFacetPivotResult addNext(SolrFacetPivotResult facetPivotResult) {
		this.nexts.add(facetPivotResult);
		return this;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SolrFacetPivotResult [value=").append(value).append(", count=").append(count).append(", nexts=")
				.append(nexts).append("]");
		return builder.toString();
	}
}
