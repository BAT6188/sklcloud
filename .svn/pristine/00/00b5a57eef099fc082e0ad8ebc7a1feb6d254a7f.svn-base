package com.skl.cloud.service.common.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.skl.cloud.service.common.I18NResourceService;

@Service
public class I18NResourceServiceImpl implements I18NResourceService {

    private static final String DEFAULT_VALUE = "failed";

    private static Locale local;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void setLocaleAsCN() {
        local = Locale.CHINA;
    }

    @Override
    public void setLocaleAsUS() {
        local = Locale.US;
    }

    @Override
    public Locale getLocal() {
        return local;
    }

    /**
     * set current local
     * @Title: getMessage
     * @param code  key
     * @return String  message description
    */
    @Override
    public void setLocal(Locale locale) {
        I18NResourceServiceImpl.local = locale;

    }

    /**
     * get the resource message by key
     * @Title: getMessage
     * @param code  key
     * @return String  message description
    */
    @Override
    public String getMessage(String code) {
        return getMessage(code, null, DEFAULT_VALUE);
    }

    /**
     * get the resource message by Integer key
     * @Title: getMessage
     * @param code  key
     * @return String  message description
    */
    @Override
    public String getMessage(int code) {
        return getMessage(String.valueOf(code), null, DEFAULT_VALUE);
    }

    /**
     * get the resource message by key, if not found get default value as message description
     * @Title: getMessage
     * @param code  key
     * @return String  message description
    */
    @Override
    public String getMessage(String code, String defaultValue) {
        return getMessage(code, null, defaultValue);
    }

    /**
     * get the resource message by key, the args for key {0}
     * @Title: getMessage
     * @param code  key
     * @return String  message description
    */
    @Override
    public String getMessage(String code, Object[] args) {
        return getMessage(code, args, DEFAULT_VALUE);
    }
    
    /**
     * get the resource message by key, the args for key {0}
     * @Title: getMessage
     * @param code  key
     * @return String  message description
     */
    @Override
    public String getMessage(int code, Object[] args) {
        return getMessage(String.valueOf(code), args, DEFAULT_VALUE);
    }

    /**
     * get the resource message by key, the args for key {0}, if not found return default message
     * @Title: getMessage
     * @param code  key
     * @return String  message description
    */
    @Override
    public String getMessage(String code, Object[] args, String defaultValue) {
        this.setLocaleAsUS();
        return messageSource.getMessage(code, args, defaultValue, getLocal());
    }
}
