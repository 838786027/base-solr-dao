package com.gosun.solr.query.condition;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solr日期操作工具
 * @author cxp
 */
public class SolrDateUtils {
	private static final Logger LOGGER=LoggerFactory.getLogger(SolrDateUtils.class);
	public static final String SOLR_DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss'Z'"; // Solr日期格式
	
	/**
	 * 以Solr日期格式的形式，返回日期范围
	 * @param isIncludeBorder true：包含边界 false：不包含边界
	 * @return 例如 [2017-07-21T18:00:00Z TO 2017-07-21T19:00:00Z]
	 */
	public static String range(Date startDate,Date endDate,boolean isIncludeBorder){
		String startDateStr="*",endDateStr="*";
		if(startDate!=null){
			startDateStr=getSolrDate(startDate);
		}
		if(endDate!=null){
			endDateStr=getSolrDate(endDate);
		}
		return SolrRangeUtils.range(startDateStr, endDateStr, isIncludeBorder);
	}
	
	/**
	 * 以Solr日期格式的形式，返回日期范围
	 * @param format 表明字符串格式
	 * @param isIncludeBorder  true：包含边界 false：不包含边界
	 * @return 例如 [2017-07-21T18:00:00Z TO 2017-07-21T19:00:00Z]
	 * @throws ParseException 
	 */
	public static String range(String startDateStr,String endDateStr,String format,boolean isIncludeBorder) throws ParseException{
		DateFormat dateFormat=new SimpleDateFormat(format);
		return range(startDateStr, endDateStr,dateFormat, isIncludeBorder);
	}
	
	/**
	 * 以Solr日期格式的形式，返回日期范围
	 * @param dataFormat 表明字符串格式
	 * @param isIncludeBorder  true：包含边界 false：不包含边界
	 * @return 例如 [2017-07-21T18:00:00Z TO 2017-07-21T19:00:00Z]
	 * @throws ParseException 
	 */
	public static String range(String startDateStr,String endDateStr,DateFormat dateFormat,boolean isIncludeBorder) throws ParseException{
		Date startDate=null;
		Date endDate=null;
		if(StringUtils.isNotBlank(startDateStr)){
			startDate=dateFormat.parse(startDateStr);
		}
		if(StringUtils.isNotBlank(endDateStr)){
			endDate=dateFormat.parse(endDateStr);
		}
		
		return range(startDate, endDate, isIncludeBorder);
	}
	
	/**
	 * 将日期转为Solr日期格式字符串
	 * @return eg.2017-07-21T19:00:00Z
	 */
	public static String getSolrDate(Date date){
		return new SimpleDateFormat(SOLR_DATE_FORMAT).format(date);
	}
	
	/**
	 * 将日期转为Solr日期格式字符串
	 * @return eg.2017-07-21T19:00:00Z
	 * @throws ParseException
	 */
	public static String getSolrDate(String dateStr,String format) throws ParseException{
		DateFormat dateFormat=new SimpleDateFormat(format);
		return getSolrDate(dateStr,dateFormat);
	}
	
	/**
	 * 将日期转为Solr日期格式字符串
	 * @return eg.2017-07-21T19:00:00Z
	 * @throws ParseException
	 */
	public static String getSolrDate(String dateStr,DateFormat dateFormat) throws ParseException{
		Date date=dateFormat.parse(dateStr);
		return getSolrDate(date);
	}
}
