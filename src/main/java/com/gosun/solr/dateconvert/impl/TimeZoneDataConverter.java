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
 * Solr时区-数据转换器
 * 
 * @author cxp
 */
public class TimeZoneDataConverter<T> implements DataConverter<T> {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(TimeZoneDataConverter.class);
	
	/**
	 * 从Solr读取数据后调用
	 */
	@Override
	public void convert(T t) {
		Class<? extends Object> tClass = t.getClass();

		try {
			for (Field field : tClass.getDeclaredFields()) {
				Extra extra = field
						.getAnnotation(Extra.class);
				if (extra == null || extra.timeZoneConvert() == 0) {
					continue;
				}
				int opHour = extra.timeZoneConvert();
				Method getMethod = ReflectUtils.obtainGetter(tClass, field);
				Object value = getMethod.invoke(t);
				if (value == null || !(value instanceof Date)) {
					continue;
				}
				Date date = (Date) value;
				date = DateUtils.addHours(date, opHour);
				Method setMethod = ReflectUtils.obtainSetter(tClass, field);
				setMethod.invoke(t, date);
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
	 * 从Solr存入数据前调用
	 */
	@Override
	public void reconvert(T t) {
		Class<? extends Object> tClass = t.getClass();

		try {
			for (Field field : tClass.getDeclaredFields()) {
				Extra extra = field
						.getAnnotation(Extra.class);
				if (extra == null || extra.timeZoneConvert() == 0) {
					continue;
				}
				int opHour = extra.timeZoneConvert();
				Method getMethod = ReflectUtils.obtainGetter(tClass, field);
				Object value = getMethod.invoke(t);
				if (value == null || !(value instanceof Date)) {
					continue;
				}
				Date date = (Date) value;
				date = DateUtils.addHours(date, -opHour);
				Method setMethod = ReflectUtils.obtainSetter(tClass, field);
				setMethod.invoke(t, date);
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
}
