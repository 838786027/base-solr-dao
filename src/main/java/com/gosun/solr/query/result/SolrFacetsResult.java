package com.gosun.solr.query.result;

import java.util.List;

/**
 * 构面结果
 * 代表同一域形成的构面总集
 * @author caixiaopeng
 *
 */
public class SolrFacetsResult {

	/**
	 * 用于构面的域
	 */
	private String facetField;
	
	private List<SolrFacetResult> facets;

	public String getFacetField() {
		return facetField;
	}
	public SolrFacetsResult setFacetField(String facetField) {
		this.facetField = facetField;
		return this;
	}
	public List<SolrFacetResult> getFacets() {
		return facets;
	}
	public void setFacets(List<SolrFacetResult> facets) {
		this.facets = facets;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SolrFacetsResult [facetField=").append(facetField).append(", facets=").append(facets)
				.append("]");
		return builder.toString();
	}
}
