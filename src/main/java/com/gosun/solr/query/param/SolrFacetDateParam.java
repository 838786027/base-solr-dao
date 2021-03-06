package com.gosun.solr.query.param;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.FacetParams;

import com.gosun.solr.query.condition.SolrDateUtils;

/**
 * 日期构面参数
 * @author caixiaopeng
 *
 */
public class SolrFacetDateParam extends AbstractSolrFacetParam{
	private String facetDate; // Date类型字段构面
	private Date facetDateStart; // Date类型字段构面的起始时间
	private Date facetDateEnd; // Date类型字段构面的结束时间
	private String facetDateGap; // Date类型字段构面的间隔时间。eg.+1DAY
	private String facetDateOther; // 是否统计范围外的数据。[before | after | between | none | all]
	private String facetDateInclude; // 边界值如何处理。[lower | upper | edge | outer | all]
	
	@Override
	public void setSolrQueryParam(SolrQuery solrQuery) {
		super.setSolrQueryParam(solrQuery);
		if(StringUtils.isNotBlank(facetDate)){
			solrQuery.set(FacetParams.FACET_DATE, facetDate);
		}
		if(facetDateStart!=null){
			solrQuery.set(FacetParams.FACET_DATE_START, SolrDateUtils.getSolrDate(facetDateStart));
		}
		if(facetDateEnd!=null){
			solrQuery.set(FacetParams.FACET_DATE_END, SolrDateUtils.getSolrDate(facetDateEnd));
		}
		if(StringUtils.isNotBlank(facetDateGap)){
			solrQuery.set(FacetParams.FACET_DATE_GAP, facetDateGap);
		}
		if(StringUtils.isNotBlank(facetDateOther)){
			solrQuery.set(FacetParams.FACET_DATE_OTHER, facetDateOther);
		}
		if(StringUtils.isNotBlank(facetDateInclude)){
			solrQuery.set(FacetParams.FACET_DATE_INCLUDE, facetDateInclude);
		}
	}

	/*------------------------
	 * getter/setter
	 *-----------------------*/
	public String getFacetDate() {
		return facetDate;
	}

	public SolrFacetDateParam setFacetDate(String facetDate) {
		this.facetDate = facetDate;
		return this;
	}

	public Date getFacetDateStart() {
		return facetDateStart;
	}

	public SolrFacetDateParam setFacetDateStart(Date facetDateStart) {
		this.facetDateStart = facetDateStart;
		return this;
	}

	public Date getFacetDateEnd() {
		return facetDateEnd;
	}

	public SolrFacetDateParam setFacetDateEnd(Date facetDateEnd) {
		this.facetDateEnd = facetDateEnd;
		return this;
	}

	public String getFacetDateGap() {
		return facetDateGap;
	}

	public SolrFacetDateParam setFacetDateGap(String facetDateGap) {
		this.facetDateGap = facetDateGap;
		return this;
	}

	public String getFacetDateOther() {
		return facetDateOther;
	}

	public SolrFacetDateParam setFacetDateOther(String facetDateOther) {
		this.facetDateOther = facetDateOther;
		return this;
	}

	public String getFacetDateInclude() {
		return facetDateInclude;
	}

	public SolrFacetDateParam setFacetDateInclude(String facetDateInclude) {
		this.facetDateInclude = facetDateInclude;
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SolrFacetDateParam [");
		if (facetDate != null)
			builder.append("facetDate=").append(facetDate).append(", ");
		if (facetDateStart != null)
			builder.append("facetDateStart=").append(facetDateStart).append(", ");
		if (facetDateEnd != null)
			builder.append("facetDateEnd=").append(facetDateEnd).append(", ");
		if (facetDateGap != null)
			builder.append("facetDateGap=").append(facetDateGap).append(", ");
		if (facetDateOther != null)
			builder.append("facetDateOther=").append(facetDateOther).append(", ");
		if (facetDateInclude != null)
			builder.append("facetDateInclude=").append(facetDateInclude).append(", ");
		if (super.toString() != null)
			builder.append("toString()=").append(super.toString());
		builder.append("]");
		return builder.toString();
	}
}
