package com.gosun.solr.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 相对与@Filed的额外属性
 * @author caixiaopeng
 *
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Extra {
	public static final String DEFAULT_VALUE ="#default";
	/**
	 * 时区转换。必须用于java.util.Date类型。
	 * eg.-8代表从solr读取数据后，将数据-8小时。也意味着存入solr时将数据+8小时。
	 */
	int timeZoneConvert() default 0;
	/**
	 * #default代表这个属性不起作用
	 */
	String defaultValue() default DEFAULT_VALUE;
}
