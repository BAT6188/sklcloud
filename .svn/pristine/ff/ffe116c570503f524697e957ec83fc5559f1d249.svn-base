package com.skl.cloud.common.xml;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JaxbDateSerializer extends XmlAdapter<String, Date>{
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");  
	  
    @Override  
    public String marshal(Date date) throws Exception {  
        return dateFormat.format(date);  
    }  
  
    @Override  
    public Date unmarshal(String date) throws Exception {  
        return dateFormat.parse(date);  
    }  
}
