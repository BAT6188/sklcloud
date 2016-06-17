package com.skl.cloud.common.util;

import org.apache.commons.lang3.StringUtils;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.common.spring.BeanLocator;
import com.skl.cloud.exception.common.InvalidParameterException;
import com.skl.cloud.service.common.I18NResourceService;

/**
 * @Package com.skl.cloud.common.util
 * @Description: for validate
 * Copyright: Copyright (c) 2015 
 * Company:深圳天彩智通软件有限公司
 * 
 * @author yangbin
 * @date 2015年10月27日
 * @version V1.0
 */
public abstract class AssertUtils {

    /**
      * Assert a boolean expression, throwing {@code BusinessException}
      * if the test result is {@code false}.
      * @Title: isTrue
      * @param expression
      * @param message
      * @throws BusinessException
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(message);
        }
    }

    /**
     * Assert a boolean expression, throwing {@code BusinessException}
     * if the test result is {@code false}.
     * @Title: isTrue
     * @param expression
     * @param errorCode
     * @throws BusinessException
    */
    public static void isTrue(boolean expression, int errorCode) {
        I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
        isTrue(expression, msgResourceService.getMessage(String.valueOf(errorCode)));
    }

    /**
     * Assert a boolean expression, throwing {@code BusinessException}
     * if the test result is {@code false}.
     * @Title: isTrue
     * @param expression
     * @throws BusinessException
    */
    public static void isTrue(boolean expression) {
        isTrue(expression, "[web validate failed] - this expression must be true");
    }

    /**
     * Assert that an object is {@code null} .throwing {@code BusinessException}
     * if the test result is {@code false}.
     * @param object the object to check
     * @param message the exception message to use if the assertion fails
     * @throws BusinessException if the object is not {@code null}
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new BusinessException(message);
        }
    }

    /**
     * Assert that an object is {@code null} .throwing {@code BusinessException}
     * if the test result is {@code false}.
     * @param object the object to check
     * @param errorCode
     * @throws BusinessException if the object is not {@code null}
     */
    public static void isNull(Object object, int errorCode) {
        I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
        isNull(object,  msgResourceService.getMessage(String.valueOf(errorCode)));
    }

    /**
     * Assert that an object is {@code null} .throwing {@code BusinessException}
     * if the test result is {@code false}.
     * @param object the object to check
     * @throws BusinessException if the object is not {@code null}
     */
    public static void isNull(Object object) {
        isNull(object, "[web validate failed] - the object argument must be null");
    }

    /**
     * Assert that an object is not {@code null} .
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     * @param object the object to check
     * @param message
     * @throws BusinessException if the object is {@code null}
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new BusinessException(message);
        }
    }

    /**
     * Assert that an object is not {@code null} .
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     * @param object the object to check
     * @param errorCode
     * @throws BusinessException if the object is {@code null}
     */
    public static void notNull(Object object, int errorCode) {
        I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
        notNull(object,  msgResourceService.getMessage("0x"+ Integer.toHexString(errorCode)));
    }
    
    /**
     * Assert that an object is not {@code null} .
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     * @param object the object to check
     * @param errorCode
     * @throws BusinessException if the object is {@code null}
     */
    public static void notNull(Object object, String errorCode, Object[] args) {
        I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
        notNull(object,  msgResourceService.getMessage(errorCode, args));
    }

    /**
     * Assert that an object is not {@code null} .
     * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
     * @param object the object to check
     * @throws BusinessException if the object is {@code null}
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    /**
     * Assert that an object is not whitespace, empty ("") or {@code null} .
     * @param cs the CharSequence to check
     * @param message
     * @throws BusinessException if the object is {@code null}
     */
    public static void isNotBlank(CharSequence cs, String message) {
        if (StringUtils.isBlank(cs)) {
            throw new InvalidParameterException(message);
        }
    }

    /**
     * Assert that an object is not whitespace, empty ("") or {@code null} .
     * @param cs the CharSequence to check
     * @param errorCode
     * @throws BusinessException if the object is {@code null}
     */
    public static void isNotBlank(CharSequence cs, int errorCode) {
        I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
        isNotBlank(cs,  msgResourceService.getMessage(String.valueOf(errorCode)));
    }

    /**
     * Assert that an object is not whitespace, empty ("") or {@code null} .
     * @param cs the CharSequence to check
     * @throws BusinessException if the object is {@code null}
     */
    public static void isNotBlank(CharSequence cs) {
        isNotBlank(cs, "[web validate failed] - this argument is required not blank");
    }
    
    //-------------------------fulin-------------------------------------
    /**
     * 不是指定的格式类型，抛出BusinessException，错误码为0x50000001，表示参数有误，对应的错误消息可插入传入的args参数
     * @param args
     */
    public static void formatNotCorrect(Object[] args) { 
    	I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
    	throw new BusinessException(0x50000001, msgResourceService.getMessage("0x50000001", args));
    }
    
    /**
     * 参数cs为空或空字符串，抛出BusinessException，错误码为0x50000001，表示参数有误，对应的错误消息可插入传入的args参数
     * @param cs
     * @param args
     */
    public static void isNotBlank(CharSequence cs, Object[] args) {
        if (StringUtils.isBlank(cs)) {
        	I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
        	throw new BusinessException(0x50000001, msgResourceService.getMessage("0x50000001", args));
        }
    }
    
    /**
     * boolean expression 为false，抛出BusinessException，错误码为0x50030001，表示没关注公众号，对应的错误消息可插入传入的args参数
     * @param expression
     * @param args
     */
   public static void isSubscribe(boolean expression, Object[] args) {
       if (!expression) {
    	   I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
    	   throw new BusinessException(0x50030001, msgResourceService.getMessage("0x50030001", args));
       }
   }
   
   /**
    * 如果传来的object为空，抛出BusinessException，错误码为0x50000002，表示无法从数据库查询此数据，对应的错误消息可插入传入的args参数
    * @param object
    * @param args
    */
  public static void existDB(Object object, Object[] args) {
      if (object == null) {
   	   I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
   	   throw new BusinessException(0x50000002, msgResourceService.getMessage("0x50000002", args));
      }
  }
  
  /**
   * 抛出BusinessException，可定错误码，对应的错误消息可插入传入的args参数
   * @param errcode
   * @param args
   */
  public static void throwBusinessEx(int errcode, Object[] args) {
	  I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
  	  throw new BusinessException(errcode, msgResourceService.getMessage("0x"+ Integer.toHexString(errcode), args));
  }
  
  /**
   * 抛出BusinessException，可定错误码，错误消息为默认的
   * @param errcode
   */
  public static void throwBusinessEx(int errcode) {
	  I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
  	  throw new BusinessException(errcode, msgResourceService.getMessage("0x"+ Integer.toHexString(errcode)));
  }
  
  /**
   * 如果传来的object为空，抛出BusinessException，错误码为0x50000001，对应的错误消息可插入传入的args参数
   * @param object
   * @param args
   */
  public static void notNull(Object object, Object[] args) {
	  if (object == null) {
		  I18NResourceService msgResourceService = BeanLocator.getBean(I18NResourceService.class);
          throw new BusinessException(0x50000001, msgResourceService.getMessage("0x50000001",args));
      }
  }
}
