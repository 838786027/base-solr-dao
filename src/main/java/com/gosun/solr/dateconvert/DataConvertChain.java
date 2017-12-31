package com.gosun.solr.dateconvert;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 数据转换链
 * @author cxp
 */
public class DataConvertChain<T> {
	private List<DataConverter<T>> chain=new LinkedList<DataConverter<T>>();
	/**
	 * 添加转换器
	 */
	public void addConvert(DataConverter<T> converter){
		chain.add(converter);
	}
	
	/**
	 * 从Solr到Java
	 */
	public void convert(List<T> tList){
		assert tList!=null;
		
		for(T t:tList){
			convert(t);
		}
	}
	
	/**
	 * 从Java到Solr
	 */
	public void reconvert(Collection<T> ts){
		assert ts!=null;
		
		for(T t:ts){
			reconvert(t);
		}
	}
	
	/**
	 * 从Solr到Java
	 */
	public void convert(T t){
		for(DataConverter<T> converter:chain){
			converter.convert(t);
		}
	}
	
	/**
	 * 从Java到Solr
	 */
	public void reconvert(T t){
		for(DataConverter<T> converter:chain){
			converter.reconvert(t);
		}
	}
}
