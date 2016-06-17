package com.skl.cloud.service.common;

import java.util.Locale;

public interface I18NResourceService {

    public void setLocaleAsCN();

    public void setLocaleAsUS();

    public Locale getLocal();

    /**
     * set current local
     * @Title: getMessage
     * @param code  key
     * @return String  message description
    */
    public void setLocal(Locale locale);

    /**
      * get the resource message by key
      * @Title: getMessage
      * @param code  key
      * @return String  message description
     */
    public String getMessage(String code);
    
    /**
     * get the resource message by Integer key
     * @Title: getMessage
     * @param code  key
     * @return String  message description
    */
    public String getMessage(int code);

    /**
     * get the resource message by key, if not found get default value as message description
     * @Title: getMessage
     * @param code  key
     * @return String  message description
    */
    public String getMessage(String code, String defaultValue);

    /**
     * get the resource message by key, the args for key {0}
     * @Title: getMessage
     * @param code  key
     * @return String  message description
    */
    public String getMessage(String code, Object[] args);
    
    /**
     * get the resource message by key, the args for key {0}
     * @Title: getMessage
     * @param code  key
     * @return String  message description
    */
    public String getMessage(int code, Object[] args) ;

    /**
     * get the resource message by key, the args for key {0}, if not found return default message
     * @Title: getMessage
     * @param code  key
     * @return String  message description
    */
    public String getMessage(String code, Object[] args, String defaultValue);

}
