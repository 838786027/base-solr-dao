package com.gosun.solr.dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.gosun.solr.DO.TestBean;
import com.gosun.solr.query.param.AbstractSolrFacetParam;
import com.gosun.solr.query.param.SolrCommonParam;
import com.gosun.solr.query.result.SolrFacetPivotResult;

public class BaseSolrDaoTestCase {

	private TestBeanSolrDao dao=new TestBeanSolrDao("application.properties");
	
	@Test
	public void testList() {
		SolrCommonParam param=new SolrCommonParam();
		// param.setCondition("id:BaseSolrDaoTestCase.testList");
		List<TestBean> result=dao.list(param);
		assertEquals(result.get(0).getStrValue(), "string");
	}
}
