package com.gosun.solr.query.param;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.FacetParams;

/**
 * 深度构面参数
 * @author caixiaopeng
 *
 */
public class SolrFacetPivotParam extends AbstractSolrFacetParam{
	/**
	 * 深度构面
	 * 1.域深度构面：field1,field2...
	 * note : 目前只支持多域深度构面。
	 */
	private String facetPiovt;
	/**
	 * 深度构面，最小值
	 * note : 小于0为无效值
	 */
	private int facetPivotMinCount=-1;
	
	@Override
	public void setSolrQueryParam(SolrQuery solrQuery) {
		super.setSolrQueryParam(solrQuery);
		if(StringUtils.isNotBlank(facetPiovt)){
			solrQuery.set(FacetParams.FACET_PIVOT, facetPiovt);
		}
		if(facetPivotMinCount>=0){
			solrQuery.set(FacetParams.FACET_PIVOT_MINCOUNT, facetPivotMinCount);
		}
	}
	
	/*------------------------
	 * getter/setter
	 *-----------------------*/
	public String getFacetPiovt() {
		return facetPiovt;
	}

	public SolrFacetPivotParam setFacetPiovt(String facetPiovt) {
		this.facetPiovt = facetPiovt;
		return this;
	}

	public int getFacetPivotMinCount() {
		return facetPivotMinCount;
	}

	public SolrFacetPivotParam setFacetPivotMinCount(int facetPivotMinCount) {
		this.facetPivotMinCount = facetPivotMinCount;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SolrFacetPivotParam [");
		if (facetPiovt != null)
			builder.append("facetPiovt=").append(facetPiovt).append(", ");
		builder.append("facetPivotMinCount=").append(facetPivotMinCount).append(", ");
		if (super.toString() != null)
			builder.append("toString()=").append(super.toString());
		builder.append("]");
		return builder.toString();
	}
}
