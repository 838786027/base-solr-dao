package com.gosun.solr.query.param;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.GroupParams;

import com.gosun.solr.query.ExtendedSortClause;

/**
 * Solr分组查询参数实体
 * 
 * @author cxp
 *
 */
public class SolrGroupParam extends SolrCommonParam{
	/**
	 * 分组域列表
	 * note : Solr分组不支持多域组成一个分组。多域只能生成多个分组。
	 */
	private List<String> groupFields=new LinkedList<String>();
	/**
	 * 分组组内偏移量
	 * note : 负数为无效值
	 */
	private int groupOffset=-1;
	/**
	 * 分组组内结果集的数量的上限
	 * note : 负数为无效值
	 */
	private int groupLimit=-1;
	/**
	 * 分组组内结果集的排序方式
	 */
	private ExtendedSortClause groupSort;
	
	@Override
	public void setSolrQueryParam(SolrQuery solrQuery){
		super.setSolrQueryParam(solrQuery);
		solrQuery.setParam(GroupParams.GROUP, true);
		solrQuery.setParam(GroupParams.GROUP_FIELD, groupFields.toArray(new String[groupFields.size()]));
		if(groupOffset>=0){
			solrQuery.setParam(GroupParams.GROUP_OFFSET, ""+groupOffset);
		}
		if(groupLimit>=0){
			solrQuery.setParam(GroupParams.GROUP_LIMIT, ""+groupLimit);
		}
		//设置组内排序
		if(groupSort!=null&&StringUtils.isNotBlank(groupSort.value())){
			solrQuery.setParam(GroupParams.GROUP_SORT, groupSort.value());
		}
	}
	
	/*------------------------
	 * getter/setter
	 *-----------------------*/
	public SolrGroupParam addGroupField(String groupField) {
		this.groupFields.add(groupField);
		return this;
	}
	public int getGroupLimit() {
		return groupLimit;
	}
	public SolrGroupParam setGroupLimit(int groupLimit) {
		this.groupLimit = groupLimit;
		return this;
	}
	public ExtendedSortClause getGroupSort() {
		return groupSort;
	}
	public SolrGroupParam setGroupSort(ExtendedSortClause groupSort) {
		this.groupSort = groupSort;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SolrGroupParam [");
		if (groupFields != null)
			builder.append("groupFields=").append(groupFields).append(", ");
		builder.append("groupOffset=").append(groupOffset).append(", groupLimit=").append(groupLimit).append(", ");
		if (groupSort != null)
			builder.append("groupSort=").append(groupSort).append(", ");
		if (super.toString() != null)
			builder.append("toString()=").append(super.toString());
		builder.append("]");
		return builder.toString();
	}
}
