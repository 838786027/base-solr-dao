package com.gosun.solr.query.condition;

import org.apache.commons.lang3.StringUtils;

/**
 * Solr查询条件-范围工具
 * @author caixiaopeng
 *
 */
public class SolrRangeUtils {
	/**
	 * 返回Solr查询条件-范围格式字符串
	 */
	public static String range(Object start,Object end,boolean isIncludeBorder){
		StringBuilder result=new StringBuilder();
		if(isIncludeBorder){
			result.append("[");
		}else{
			result.append("{");
		}
		if(start==null||StringUtils.isBlank(start.toString())){
			result.append("*");
		}else{
			result.append(start);
		}
		result.append(" TO ");
		if(end==null||StringUtils.isBlank(end.toString())){
			result.append("*");
		}else{
			result.append(end);
		}
		if(isIncludeBorder){
			result.append("]");
		}else{
			result.append("}");
		}
		return result.toString();	
	}
}
