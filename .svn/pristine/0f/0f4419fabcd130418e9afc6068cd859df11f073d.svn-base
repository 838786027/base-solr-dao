package com.gosun.solr.query;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;

/**
 * SolrQuery.SortClause扩展类，扩展value方法
 * 
 * @author cxp
 */
public class ExtendedSortClause extends SolrQuery.SortClause {

	private static final long serialVersionUID = -3327536460921612735L;

	public ExtendedSortClause(String item, ORDER order) {
		super(item, order);
	}

	public ExtendedSortClause(String item, String order) {
		super(item, order);
	}

	/**
	 * 将field和order组合成字符串返回
	 * @return "field order"(eg."fieldName asc")
	 */
	public String value() {
		String value = "";
		if (StringUtils.isNotBlank(getItem())) {
			value = getItem();
			ORDER order = getOrder();
			if (order != null && StringUtils.isNotBlank(order.toString())) {
				value+=" "+order.toString();
			}
		}
		return value;
	}

}
