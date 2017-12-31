package com.gosun.solr.dao;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gosun.solr.dateconvert.DataConvertChain;
import com.gosun.solr.dateconvert.impl.DefaultValueDataConverter;
import com.gosun.solr.dateconvert.impl.TimeZoneDataConverter;
import com.gosun.solr.query.param.AbstractSolrFacetParam;
import com.gosun.solr.query.param.SolrGroupParam;
import com.gosun.solr.query.param.SolrCommonParam;
import com.gosun.solr.query.param.SolrFacetDateParam;
import com.gosun.solr.query.param.SolrFacetFieldParam;
import com.gosun.solr.query.param.SolrFacetQueryParam;
import com.gosun.solr.query.result.SolrCommonResult;
import com.gosun.solr.query.result.SolrFacetResult;
import com.gosun.solr.query.result.SolrFacetPivotResult;
import com.gosun.solr.query.result.SolrFacetsResult;
import com.gosun.solr.query.result.SolrGroupResult;
import com.gosun.solr.server.SolrServerFactory;
import com.gosun.util.ReflectUtils;

/**
 * Solr数据通道抽象基类
 * 
 * @author caixiaopeng
 */
public abstract class BaseSolrDao<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseSolrDao.class);
	protected SolrServer solrServer; // Solr操作工具
	protected Class<T> tClass; // 泛型Class
	protected String uniqueKey = "id"; // Solr唯一键
	protected DataConvertChain<T> dataConvertChain = new DataConvertChain<T>(); // 数据转换操作链

	/*----------------------
	 * <init>
	 *---------------------*/
	public BaseSolrDao(String configFileName) {
		// 通过配置文件加载SolrServer
		try {
			solrServer = SolrServerFactory.getSolrServerInstance(configFileName);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("获取SolrServer时出现异常", e);
		}
		init();
	}
	public BaseSolrDao(String solrCloudUrl, String solrCollection, String solrHostUrl) {
		try {
			solrServer = SolrServerFactory.getSolrServerInstance(solrCloudUrl, solrCollection, solrHostUrl);
		} catch (Exception e) {
			LOGGER.error("获取SolrServer时出现异常", e);
		}
		init();
	}
	private void init() {
		// 获取泛型Class
		if (tClass == null) {
			Type genType = getClass().getGenericSuperclass();
			Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
			tClass = (Class<T>) params[0];
		}
		// 添加数据转换器
		dataConvertChain.addConvert(new TimeZoneDataConverter<T>());
		dataConvertChain.addConvert(new DefaultValueDataConverter<T>());
	}

	/*----------------------
	 * 公有方法
	 *---------------------*/
	/**
	 * 基础查询
	 */
	public List<T> list(SolrCommonParam queryParam) {
		LOGGER.info("BaseSolrDao.list => "+queryParam);
		SolrQuery query = new SolrQuery();
		queryParam.setSolrQueryParam(query);
		List<T> result = new ArrayList<T>();
		long now=new Date().getTime();
		int qTime=-1;
		long solrNumFound=-1;
		try {
			QueryResponse response = solrServer.query(query, METHOD.POST);
			qTime=response.getQTime();
			result = response.getBeans(tClass);
			dataConvertChain.convert(result);
		} catch (SolrServerException e) {
			LOGGER.warn("Solr查询异常", e);
		}
		LOGGER.info("QTime="+(new Date().getTime()-now)+"ms , numFound="+result.size()+" , Solr QTime="+qTime+"ms");
		return result;
	}

	/**
	 * 查询总数
	 */
	public long count(SolrCommonParam queryParam) {
		queryParam.setRows(0);
		queryParam.setStart(0);
		LOGGER.info("BaseSolrDao.count => "+queryParam);
		SolrQuery query = new SolrQuery();
		queryParam.setSolrQueryParam(query);
		long result = 0;
		try {
			QueryResponse response = solrServer.query(query, METHOD.POST);
			result = response.getResults().getNumFound();
		} catch (SolrServerException e) {
			LOGGER.warn("Solr查询异常", e);
		}
		LOGGER.info("result=" + result);
		return result;
	}

	/**
	 * 基础查询，附带查询总量，以Result的形式返回
	 */
	public SolrCommonResult<T> listToQueryResult(SolrCommonParam queryParam) {
		LOGGER.info("BaseSolrDao.listToQueryResult => "+queryParam);
		SolrQuery query = new SolrQuery();
		queryParam.setSolrQueryParam(query);
		SolrCommonResult<T> result = new SolrCommonResult<T>();
		long now=new Date().getTime();
		int qTime=-1;
		try {
			QueryResponse response = solrServer.query(query, METHOD.POST);
			qTime=response.getQTime();
			List<T> list = response.getBeans(tClass);
			dataConvertChain.convert(list);
			result.setAmounts(response.getResults().getNumFound());
			result.setBeans(list);
		} catch (SolrServerException e) {
			LOGGER.warn("Solr查询异常", e);
		}
		LOGGER.info("QTime="+(new Date().getTime()-now)+"ms , numFound="+result.getBeans().size()+" , Solr QTime="+qTime+"ms");
		return result;
	}

	/**
	 * 通过uniqueKey获取对应的document
	 * 
	 * @param id not null
	 */
	public T getByUniqueKey(String id) {
		String condition = uniqueKey + ":" + id;
		SolrQuery query = new SolrQuery();
		query.setQuery(condition);
		query.setRows(1);
		LOGGER.info("BaseSolrDao.getByUniqueKey => "+condition);
		T result = null;
		long now=new Date().getTime();
		int qTime=-1;
		try {
			QueryResponse response = solrServer.query(query, METHOD.POST);
			qTime=response.getQTime();
			List<T> beans = response.getBeans(tClass);
			result = beans.get(0);
			dataConvertChain.convert(result);
		} catch (SolrServerException e) {
			LOGGER.warn("Solr查询异常", e);
		}
		LOGGER.debug("result=" + result);
		LOGGER.info("QTime="+(new Date().getTime()-now)+"ms , Solr QTime="+qTime+"ms");
		return result;
	}

	/**
	 * 多域构面查询
	 * Solr4.7.2没有提供多字段构面，对于输入多个构面字段时，会按着每个分组字段各自组成构面
	 */
	public List<SolrFacetsResult> listMultFieldFacet(SolrFacetFieldParam facetParam) {
		LOGGER.info("BaseSolrDao.listMultFacetField => "+facetParam);
		SolrQuery query = new SolrQuery();
		facetParam.setSolrQueryParam(query);
		List<SolrFacetsResult> result = new LinkedList<SolrFacetsResult>();
		long now=new Date().getTime();
		int qTime=-1;
		try {
			QueryResponse response = solrServer.query(query, METHOD.POST);
			qTime=response.getQTime();
			List<String> facetFieldList = facetParam.getFacetFields();
			for (String facetField : facetFieldList) {
				SolrFacetsResult facetsResult = new SolrFacetsResult().setFacetField(facetField);
				List<Count> counts = response.getFacetField(facetField).getValues();
				List<SolrFacetResult> facets = new LinkedList<>();
				for (Count count : counts) {
					facets.add(new SolrFacetResult().setValue(count.getName()).setCount(count.getCount()));
				}
				facetsResult.setFacets(facets);

				result.add(facetsResult);
			}
		} catch (SolrServerException e) {
			LOGGER.warn("Solr查询异常", e);
		}
		LOGGER.info("QTime="+(new Date().getTime()-now)+"ms , Solr QTime="+qTime+"ms");
		return result;
	}
	
	/**
	 * 单域构面查询
	 * 使用facetParam.getFacetFieldList().get(0)作为facet.field
	 */
	public List<SolrFacetResult> listFieldFacet(SolrFacetFieldParam facetParam) {
		LOGGER.info("BaseSolrDao.listFieldFacet => "+facetParam);
		SolrQuery query = new SolrQuery();
		facetParam.setSolrQueryParam(query);
		List<SolrFacetResult> result = new LinkedList<SolrFacetResult>();
		long now=new Date().getTime();
		int qTime=-1;
		try {
			QueryResponse response = solrServer.query(query, METHOD.POST);
			qTime=response.getQTime();
			List<String> facetFieldList = facetParam.getFacetFields();
			if (facetFieldList != null && facetFieldList.size() > 0) {
				List<Count> counts = response.getFacetField(facetFieldList.get(0)).getValues();
				for (Count count : counts) {
					result.add(new SolrFacetResult().setCount(count.getCount()).setValue(count.getName()));
				}
			}
		} catch (SolrServerException e) {
			LOGGER.warn("Solr查询异常", e);
		}
		LOGGER.info("QTime="+(new Date().getTime()-now)+"ms , numFound="+result.size()+" , Solr QTime="+qTime+"ms");
		return result;
	}

	/**
	 * 单域构面查询总数
	 */
	public int countFieldFacet(SolrFacetFieldParam facetParam) {
		facetParam.setFacetRows(-1)// 无限制
				.setFacetStart(0);// 从0开始
		LOGGER.info("BaseSolrDao.countFieldFacet => "+facetParam);
		SolrQuery query = new SolrQuery();
		facetParam.setSolrQueryParam(query);
		int result = -1;
		long now=new Date().getTime();
		int qTime=-1;
		try {
			QueryResponse response = solrServer.query(query, METHOD.POST);
			qTime=response.getQTime();
			List<String> facetFieldList = facetParam.getFacetFields();
			if (facetFieldList != null && facetFieldList.size() > 0) {
				result = response.getFacetField(facetFieldList.get(0)).getValueCount();
			}
		} catch (SolrServerException e) {
			LOGGER.warn("Solr查询异常", e);
		}
		LOGGER.debug("result=" + result);
		LOGGER.info("QTime="+(new Date().getTime()-now)+"ms , Solr QTime="+qTime+"ms");
		return result;
	}

	/**
	 * 日期构面查询
	 */
	public List<SolrFacetResult> listDateFacet(SolrFacetDateParam facetParam) {
		LOGGER.info("BaseSolrDao.listDateFacet => "+facetParam);
		SolrQuery query = new SolrQuery();
		facetParam.setSolrQueryParam(query);
		List<SolrFacetResult> result = new LinkedList<SolrFacetResult>();
		long now=new Date().getTime();
		int qTime=-1;
		try {
			QueryResponse response = solrServer.query(query, METHOD.POST);
			qTime=response.getQTime();
			String facetDate = facetParam.getFacetDate();
			if (StringUtils.isNotBlank(facetDate)) {
				List<Count> counts = response.getFacetDate(facetDate).getValues();
				for (Count count : counts) {
					result.add(new SolrFacetResult().setCount(count.getCount()).setValue(count.getName()));
				}
			}
		} catch (SolrServerException e) {
			LOGGER.warn("Solr查询异常", e);
		}
		LOGGER.debug("result=" + result);
		LOGGER.info("QTime="+(new Date().getTime()-now)+"ms , numFound="+result.size()+" , Solr QTime="+qTime+"ms");
		return result;
	}

	/**
	 * 自定义查询构面
	 */
	public List<SolrFacetResult> listQueryFacet(SolrFacetQueryParam facetParam) {
		LOGGER.info("BaseSolrDao.listQueryFacet => "+facetParam);
		SolrQuery query = new SolrQuery();
		facetParam.setSolrQueryParam(query);
		List<SolrFacetResult> result = new LinkedList<SolrFacetResult>();
		long now=new Date().getTime();
		int qTime=-1;
		try {
			QueryResponse response = solrServer.query(query, METHOD.POST);
			qTime=response.getQTime();
			Map<String, Integer> facetQueryResultMap = response.getFacetQuery();
			for (Map.Entry<String, Integer> facetQueryResult : facetQueryResultMap.entrySet()) {
				result.add(new SolrFacetResult().setCount(facetQueryResult.getValue())
						.setValue(facetQueryResult.getKey()));
			}
		} catch (SolrServerException e) {
			LOGGER.warn("Solr查询异常", e);
		}
		LOGGER.debug("result=" + result);
		LOGGER.info("QTime="+(new Date().getTime()-now)+"ms , numFound="+result.size()+" , Solr QTime="+qTime+"ms");
		return result;
	}

	/**
	 * 深度构面查询
	 * TODO 未实现
	 */
//	public List<SolrFacetPivotResult> listPivotFacet(SolrFacetPivotResult facetParam) {
//		assert facetParam != null;
//		LOGGER.info("BaseSolrDao.listPivotFacet");
//		LOGGER.info("参数：" + facetParam);
//		SolrQuery query = new SolrQuery();
//		facetParam.setSolrQueryParam(query);
//		List<SolrFacetPivotResult> result = new LinkedList<SolrFacetPivotResult>();
//		try {
//			QueryResponse response = solrServer.query(query, METHOD.POST);
//			NamedList<List<PivotField>> pivotsMap = response.getFacetPivot(); // 多个深度构面
//			for (Entry<String, List<PivotField>> pivots : pivotsMap) {
//				for (PivotField pivot : pivots.getValue()) {
//					SolrFacetPivotResult facetPivotQueryResult = getPivotResult(pivot);
//					result.add(facetPivotQueryResult);
//					System.out.println("asd");
//				}
//			}
//		} catch (SolrServerException e) {
//			LOGGER.warn("Solr查询异常", e);
//		}
//
//		LOGGER.info("返回值：" + result);
//		return result;
//	}

	/**
	 * 分组查询，以Result的形式返还
	 */
	public List<SolrGroupResult<T>> listGroup(SolrGroupParam groupParam) {
		LOGGER.info("BaseSolrDao.listGroup => "+groupParam);
		SolrQuery query = new SolrQuery();
		groupParam.setSolrQueryParam(query);
		List<SolrGroupResult<T>> result = new LinkedList<SolrGroupResult<T>>();
		long now=new Date().getTime();
		int qTime=-1;
		try {
			QueryResponse response = solrServer.query(query, METHOD.POST);
			qTime=response.getQTime();
			GroupResponse groupResponse = response.getGroupResponse();
			List<GroupCommand> groupCommandList = groupResponse.getValues();
			for (GroupCommand groupCommand : groupCommandList) {
				List<Group> groups = groupCommand.getValues();
				for (Group group : groups) {
					List<T> beanListOfGroup = new LinkedList<T>();
					String groupValue = group.getGroupValue();
					SolrDocumentList solrDocumentList = group.getResult();
					for (SolrDocument document : solrDocumentList) {
						try {
							T bean = (T) tClass.newInstance();
							// 解析注解，将solr document注入到bean中
							for (Field field : tClass.getDeclaredFields()) {
								org.apache.solr.client.solrj.beans.Field fieldOfSolr = field
										.getAnnotation(org.apache.solr.client.solrj.beans.Field.class);
								if (fieldOfSolr == null || StringUtils.isBlank(fieldOfSolr.value()))
									continue;
								String fieldNameOfSolr = fieldOfSolr.value();
								if ("#default".equals(fieldNameOfSolr)) {
									fieldNameOfSolr = field.getName();
								}
								Method setMethod = ReflectUtils.obtainSetter(tClass, field);
								Object value = document.get(fieldNameOfSolr);
								if (value != null)
									setMethod.invoke(bean, value);
							}
							beanListOfGroup.add(bean);
						} catch (NoSuchMethodException e) {
							LOGGER.warn("反射获取setter异常", e);
						} catch (SecurityException e) {
							LOGGER.warn("反射获取setter异常", e);
						} catch (IllegalArgumentException e) {
							LOGGER.warn("反射调用setter异常", e);
						} catch (InvocationTargetException e) {
							LOGGER.warn("反射调用setter异常", e);
						} catch (InstantiationException e) {
							LOGGER.warn("反射生成实体类" + tClass + "异常", e);
						} catch (IllegalAccessException e) {
							LOGGER.warn("反射生成实体类" + tClass + "异常", e);
						}
					}
					// 数据转换
					dataConvertChain.convert(beanListOfGroup);

					SolrGroupResult<T> groupResult = new SolrGroupResult<T>();
					groupResult.setAmounts(group.getResult().getNumFound());
					groupResult.setValue(groupValue);
					groupResult.setBeans(beanListOfGroup);
					result.add(groupResult);
				}
			}
		} catch (SolrServerException e) {
			LOGGER.warn("Solr查询异常", e);
		}
		int numFound=0;
		for(SolrGroupResult<T> group:result){
			numFound+=group.getBeans().size();
		}
		LOGGER.info("QTime="+(new Date().getTime()-now)+"ms , numFound="+numFound+" , Solr QTime="+qTime+"ms");
		return result;
	}

	/**
	 * 更新数据
	 */
	public boolean updateAll(List<T> tList) {
		dataConvertChain.reconvert(tList);

		Collection<SolrInputDocument> updateDocCollection = new LinkedList<SolrInputDocument>();
		for (T t : tList) {
			// 一个实体实例对应一个doc
			SolrInputDocument doc = new SolrInputDocument();
			try {
				for (Field field : tClass.getDeclaredFields()) {
					org.apache.solr.client.solrj.beans.Field fieldOfSolr = field
							.getAnnotation(org.apache.solr.client.solrj.beans.Field.class);
					if (fieldOfSolr == null || StringUtils.isBlank(fieldOfSolr.value())) {
						continue;
					}
					String fieldNameOfSolr = fieldOfSolr.value();
					if ("#default".equals(fieldNameOfSolr)) {
						fieldNameOfSolr = field.getName();
					}
					Method getMethod = ReflectUtils.obtainGetter(tClass, field);
					Object value = getMethod.invoke(t);
					if (value == null) {
						continue;
					}
					if (uniqueKey.equals(fieldNameOfSolr)) {
						// 主键标识
						doc.addField(uniqueKey, value);
					} else {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("set", value);
						doc.addField(fieldNameOfSolr, map);
					}
					if (doc.containsKey(uniqueKey)) {
						updateDocCollection.add(doc);
					}
				}
			} catch (SecurityException e) {
				LOGGER.warn("反射获取getter异常", e);
			} catch (NoSuchMethodException e) {
				LOGGER.warn("反射获取getter异常", e);
			} catch (IllegalArgumentException e) {
				LOGGER.warn("反射调用getter异常", e);
			} catch (IllegalAccessException e) {
				LOGGER.warn("反射调用getter异常", e);
			} catch (InvocationTargetException e) {
				LOGGER.warn("反射调用getter异常", e);
			}
		}

		// 更新doc
		try {
			if (updateDocCollection.size() != 0) {
				solrServer.add(updateDocCollection);
				UpdateResponse response = solrServer.commit();
				return response.getStatus() == 0 ? true : false;
			} else {
				return true;
			}
		} catch (SolrServerException e) {
			LOGGER.warn("SolrServer添加document异常", e);
		} catch (IOException e) {
			LOGGER.warn("SolrServer添加document异常", e);
		}

		return false;
	}

	/**
	 * 更新数据
	 */
	public boolean update(T t) {
		dataConvertChain.reconvert(t);
		List<T> tList = new LinkedList<T>();
		tList.add(t);
		return updateAll(tList);
	}

	/**
	 * 插入数据
	 */
	public boolean save(T t) {
		dataConvertChain.reconvert(t);
		try {
			UpdateResponse response = solrServer.addBean(t);
			solrServer.commit();
			return response.getStatus() == 0 ? true : false;
		} catch (IOException | SolrServerException e) {
			LOGGER.warn("SolrServer插入数据异常", e);
		}
		return false;
	}

	/**
	 * 插入数据
	 */
	public boolean saveAll(Collection<T> tCollection) {
		if (tCollection == null || tCollection.size() == 0) {
			return true;
		}
		dataConvertChain.reconvert(tCollection);
		try {
			UpdateResponse response = solrServer.addBeans(tCollection);
			solrServer.commit();
			return response.getStatus() == 0 ? true : false;
		} catch (IOException | SolrServerException e) {
			LOGGER.warn("SolrServer插入数据异常", e);
		}
		return false;
	}

	/**
	 * 通过uniqueKey删除数据
	 */
	public boolean remove(T t) {
		Field uniqueKeyField = null;
		for (Field field : tClass.getDeclaredFields()) {
			org.apache.solr.client.solrj.beans.Field fieldOfSolr = field
					.getAnnotation(org.apache.solr.client.solrj.beans.Field.class);
			if (fieldOfSolr == null || StringUtils.isBlank(fieldOfSolr.value())) {
				continue;
			}
			String fieldNameOfSolr = fieldOfSolr.value();
			if ("#default".equals(fieldNameOfSolr)) {
				fieldNameOfSolr = field.getName();
			}
			if (uniqueKey.equals(fieldNameOfSolr)) {
				// 主键标识
				uniqueKeyField = field;
				break;
			}
		}
		if (uniqueKeyField == null) {
			LOGGER.warn("获取uniquekey域异常，不存在对应的uniquekey域");
			return false;
		}
		Object id = null;
		try {
			Method getter = ReflectUtils.obtainGetter(tClass, uniqueKeyField);
			id = getter.invoke(t);
		} catch (NoSuchMethodException | SecurityException e) {
			LOGGER.warn("获取uniquekey域异常", e);
			return false;
		} catch (IllegalAccessException e) {
			LOGGER.warn("获取uniquekey值异常", e);
			return false;
		} catch (IllegalArgumentException e) {
			LOGGER.warn("获取uniquekey值异常", e);
			return false;
		} catch (InvocationTargetException e) {
			LOGGER.warn("获取uniquekey值异常", e);
			return false;
		}
		return removeByUniqueKey(id.toString());
	}

	/**
	 * 通过uniqueKey删除数据
	 */
	public boolean removeByUniqueKey(String id) {
		try {
			UpdateResponse response = solrServer.deleteById(id);
			solrServer.commit();
			return response.getStatus() == 0 ? true : false;
		} catch (SolrServerException | IOException e) {
			LOGGER.warn("SolrServer删除数据异常", e);
		}
		return false;
	}

	/**
	 * 通过uniqueKey删除数据
	 */
	public boolean removeByUniqueKey(List<String> ids) {
		try {
			UpdateResponse response = solrServer.deleteById(ids);
			solrServer.commit();
			return response.getStatus() == 0 ? true : false;
		} catch (SolrServerException | IOException e) {
			LOGGER.warn("SolrServer删除数据异常", e);
		}
		return false;
	}

	/**
	 * 通过条件查询，删除数据
	 */
	public boolean removeByCondition(String condition) {
		try {
			UpdateResponse response = solrServer.deleteByQuery(condition);
			solrServer.commit();
			return response.getStatus() == 0 ? true : false;
		} catch (SolrServerException | IOException e) {
			LOGGER.warn("SolrServer删除数据异常", e);
		}
		return false;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	/**
	 * 递归获取深度构面结果集
	 */
	private SolrFacetPivotResult getPivotResult(PivotField pivot) {
		if (pivot.getPivot() == null || pivot.getPivot().isEmpty()) {
			// 不存在下一层
			return new SolrFacetPivotResult().setValue(pivot.getValue()).setCount((long) pivot.getCount());
		}

		SolrFacetPivotResult result = new SolrFacetPivotResult();
		result.setValue(pivot.getValue()).setCount((long) pivot.getCount());
		// 获取下一层
		for (PivotField nextPivot : pivot.getPivot()) {
			SolrFacetPivotResult next = getPivotResult(nextPivot);
			result.addNext(next);
		}
		return result;
	}
}
