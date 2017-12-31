package com.gosun.solr.query.condition;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Solr查询条件组合工具
 * @author caixiaopeng
 */
public class SolrConditionUtils {
	
	/**
	 * 数组转换成Solr查询条件
	 * @return eg.key:(a b) eg.key:(a AND b)
	 */
	public static String list(String key,List<String> list,LogicOp op){
		String value=list(list,op);
		if(StringUtils.isBlank(value)){
			return "";
		}
		return keyValue(key, value);
	}
	
	/**
	 * 数组转换成Solr查询条件
	 * @return 例如：(001001003001 001001003002)
	 */
	public static String list(List<String> list,LogicOp op){
		StringBuilder result=new StringBuilder();
		for(Object element:list){
			if(StringUtils.isNotBlank(result.toString())){
				switch(op){
				case AND:
					result.append(" AND ");
					break;
				case OR:
					result.append(" OR ");
					break;
				case IN:
					result.append(" ");
					break;
				default:
					break;
				}
			}
			result.append(element);
		}
		
		return StringUtils.isNotBlank(result.toString())?"("+result.toString()+")":"";
	}
	
	/**
	 * 数组转换成Solr查询条件
	 * @return eg.(a b c)		eg.(a AND b AND c)
	 */
	public static String list(String key,String[] arrary,LogicOp op){
		List<String> list=Arrays.asList(arrary);
		return list(key,list, op);
	}
	
	/**
	 * 数组转换成Solr查询条件
	 * @return eg.(a b c)		eg.(a AND b AND c)
	 */
	public static String list(String[] arrary,LogicOp op){
		List<String> list=Arrays.asList(arrary);
		return list(list, op);
	}
	
	/**
	 * 将一个键值对转换成Solr查询条件
	 * @return key:value
	 */
	public static String keyValue(String key,Object value){
		if(StringUtils.isNotBlank(key)&&value!=null){
			return key+":"+value;
		}else{
			return "";
		}
	}
}
