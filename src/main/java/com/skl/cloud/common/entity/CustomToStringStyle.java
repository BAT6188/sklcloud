package com.skl.cloud.common.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("serial")
public class CustomToStringStyle extends ToStringStyle {

	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	public static final CustomToStringStyle DEFAULT_STYLE = new CustomToStringStyle();
	
	private CustomToStringStyle() {
		super();
		setFieldSeparator(", ");
	}

	@Override
	protected void appendDetail(StringBuffer buffer, String fieldName,
			Object value) {
		if (value instanceof Date) {
			value = sdf.format((Date) value);
		}
		buffer.append(value);
	}
	
	
}
