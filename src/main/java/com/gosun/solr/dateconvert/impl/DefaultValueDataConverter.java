package com.gosun.solr.dateconvert.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gosun.solr.annotation.Extra;
import com.gosun.solr.dateconvert.DataConverter;
import com.gosun.util.ReflectUtils;

/**
 * 默认值数据转换器
 * @author caixiaopeng
 *
 */
public class DefaultValueDataConverter<T> implements DataConverter<T>{

	private final static Logger LOGGER = LoggerFactory
			.getLogger(DefaultValueDataConverter.class);
	
	/**
	 * 从Solr读取数据后调用
	 */
	@Override
	public void convert(T t) {
		// note：不作任何处理
	}

	/**
	 * 从Solr存入数据前调用
	 */
	@Override
	public void reconvert(T t) {
		Class<? extends Object> tClass = t.getClass();

		try {
			for (Field field : tClass.getDeclaredFields()) {
				Extra extra = field
						.getAnnotation(Extra.class);
				if (extra == null || extra.DEFAULT_VALUE.equals(extra.defaultValue())) {
					continue;
				}
				String defalutValue = extra.defaultValue();
				Method getMethod = ReflectUtils.obtainGetter(tClass, field);
				Object value = getMethod.invoke(t);
				if (value != null) {
					continue;
				}
				Method setMethod = ReflectUtils.obtainSetter(tClass, field);
				setMethod.invoke(t, srtToObj(defalutValue,field));
			}
		} catch (NoSuchMethodException e) {
			LOGGER.warn("反射获取getter/setter异常", e);
		} catch (SecurityException e) {
			LOGGER.warn("反射获取getter/setter异常", e);
		} catch (IllegalAccessException e) {
			LOGGER.warn("反射调用getter/setter异常", e);
		} catch (IllegalArgumentException e) {
			LOGGER.warn("反射调用getter/setter异常", e);
		} catch (InvocationTargetException e) {
			LOGGER.warn("反射调用getter/setter异常", e);
		}
	}
	
	/**
	 * 通过Field的类型，从字符串转到对应类型
	 * 目前支持：String，Integer，Long
	 */
	private Object srtToObj(String value,Field field){
		Class type=field.getType();
		if(type.equals(String.class)){
			return value;
		}else if(type.equals(Integer.class)){
			return Integer.valueOf(value);
		}else if(type.equals(Long.class)){
			return Long.valueOf(value);
		}
		return null;
	}
}
