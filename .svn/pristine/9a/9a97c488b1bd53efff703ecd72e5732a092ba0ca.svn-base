package com.gosun.solr.DO;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

import com.gosun.solr.annotation.Extra;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

public class TestBean {
	@Field
	@CsvBindByName(column="id")
	private String id;
	
	@Field("intValue")
	@CsvBindByName(column="intValue")
	private Integer intValue;
	
	@Field("strValue")
	@CsvBindByName(column="strValue")
	private String strValue;
	
	@Field("dateValue")
	@Extra(timeZoneConvert=-8)
	@CsvBindByName(column="dateValue")
	@CsvDate("yyyy-MM-dd'T'HH:mm:ss'Z'")
	private Date dateValue;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getIntValue() {
		return intValue;
	}
	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}
	public String getStrValue() {
		return strValue;
	}
	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	public Date getDateValue() {
		return dateValue;
	}
	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateValue == null) ? 0 : dateValue.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((intValue == null) ? 0 : intValue.hashCode());
		result = prime * result
				+ ((strValue == null) ? 0 : strValue.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestBean other = (TestBean) obj;
		if (dateValue == null) {
			if (other.dateValue != null)
				return false;
		} else if (!dateValue.equals(other.dateValue))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (intValue == null) {
			if (other.intValue != null)
				return false;
		} else if (!intValue.equals(other.intValue))
			return false;
		if (strValue == null) {
			if (other.strValue != null)
				return false;
		} else if (!strValue.equals(other.strValue))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TestBean [id=" + id + ", intValue=" + intValue + ", strValue="
				+ strValue + ", dateValue=" + dateValue + "]";
	}
	
}
