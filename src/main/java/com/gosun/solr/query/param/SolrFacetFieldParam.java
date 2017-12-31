package com.gosun.solr.query.param;

import java.util.LinkedList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;

/**
 * 域构面参数
 * @author caixiaopeng
 *
 */
public class SolrFacetFieldParam extends AbstractSolrFacetParam{
	/**
	 * 构面域列表 
	 * note : Solr构面不支持多域组成一个构面。多域只能生成多个构面。
	 */
	private List<String> facetFields = new LinkedList<String>();
	
	@Override
	public void setSolrQueryParam(SolrQuery solrQuery) {
		super.setSolrQueryParam(solrQuery);
		if (facetFields.size() > 0) {
			solrQuery.addFacetField(facetFields
					.toArray(new String[facetFields.size()]));
		}
	}
	
	/*------------------------
	 * getter/setter
	 *-----------------------*/
	public List<String> getFacetFields() {
		return facetFields;
	}
	public SolrFacetFieldParam addFacetField(String facetField){
		facetFields.add(facetField);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SolrFacetFieldParam [");
		if (facetFields != null)
			builder.append("facetFields=").append(facetFields).append(", ");
		if (super.toString() != null)
			builder.append("toString()=").append(super.toString());
		builder.append("]");
		return builder.toString();
	}
}
