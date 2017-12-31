package com.gosun.solr.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gosun.util.ConfigUtils;

/**
 * SolrServer单例工厂
 * 
 * @author cxp
 */
public class SolrServerFactory {
	/**
	 * SolrServer单例缓存
	 */
	private static Map<String, SolrServer> serverMap = new HashMap<String, SolrServer>();
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SolrServerFactory.class);
	
	/**
	 * 配置文件相关字段
	 */
	private static final  String SOLR_ZK_HOST="solr.zk.host";
	private static final String SOLR_ZK_DEFAULT_COLLECTION="solr.zk.default.collection";
	private static final String SOLR_SERVER_URL="solr.server.url";

	/**
	 * 读取配置文件对应的solr_zk_host，solr_zk_default_collection，solr_server_url
	 * 生成对应的SolrServer实例对象 支持单机版Solr和SolrCloud兼容，且SolrCloud优先
	 * 从getClassLoader().getResourceAsStream位置获取文件
	 * 
	 * @throws Exception
	 */
	public static synchronized SolrServer getSolrServerInstance(String configFileName)
			throws Exception {
		// 读取配置文件
		Properties prop = ConfigUtils.loadConfigFile(configFileName);

		String solrCloudUrl = prop.getProperty(SOLR_ZK_HOST);
		String solrCollection = prop.getProperty(SOLR_ZK_DEFAULT_COLLECTION);
		String solrHostUrl = prop.getProperty(SOLR_SERVER_URL);

		return getSolrServerInstance(solrCloudUrl, solrCollection, solrHostUrl);
	}

	/**
	 * 支持单机版Solr和SolrCloud兼容，且SolrCloud优先
	 * 
	 * @throws Exception
	 */
	public static synchronized SolrServer getSolrServerInstance(String solrCloudUrl,
			String solrCollection, String solrHostUrl) throws Exception {
		SolrServer server = serverMap.get(solrCloudUrl + solrCollection
				+ solrHostUrl);
		if (server != null)
			return server;

		if (StringUtils.isNotBlank(solrCloudUrl)&&StringUtils.isNotBlank(solrCollection)) {// 兼容单机版Solr
			CloudSolrServer cloudSolrServer = new CloudSolrServer(solrCloudUrl);
			cloudSolrServer.setDefaultCollection(solrCollection);
			server = cloudSolrServer;
			LOGGER.info("连接Solr[solrCloudUrl="+solrCloudUrl+",solrCollection="+solrCollection+"]");
		} else if (StringUtils.isNotBlank(solrHostUrl)) {
			HttpSolrServer httpSolrServer = new HttpSolrServer(solrHostUrl);
			server = httpSolrServer;
			LOGGER.info("连接Solr[solrHostUrl="+solrHostUrl+"]");
		} else {
			throw new Exception("必须有solrCloudUrl或者solrHostUrl配置的其中一个");
		}
		serverMap.put(solrCloudUrl + solrCollection + solrHostUrl, server);
		return server;
	}
}
