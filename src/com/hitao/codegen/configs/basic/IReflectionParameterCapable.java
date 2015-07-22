package com.hitao.codegen.configs.basic;

/**
 * Interface for an object with all information needed to save the value and
 * to use reflection to assign the value to a parameter.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @param <T> value type
 * @version $Id: IReflectionParameterCapable.java 12 2011-02-20 10:50:23Z guest $
 */
public interface IReflectionParameterCapable<T> {
  /**
   * Returns the data type of the value derived from this configuration object
   * for use as a parameter to a method call.
   *
   * @return  the class of any method parameters derived from this configuration
   * object
   */
  public Class<T> getParamDataType();

  /**
   * Returns the value to derive from this configuration object for use as a
   * parameter to a method call.
   *
   * @return the method parameter value to extract from this configuration
   * object
   */
  public T getParamValue();

  /**
   * Returns the data type string identifier for configuration elements of this
   * class.
   *
   * @return the string identifying configuration elements of this class
   */
  public String getConfigDataType();

  /**
   * Returns the value of this configuration object as it should appear when
   * output to a text file.
   *
   * @return the string identifying this configuration object's value in a text
   * file
   */
  public String getConfigValue();
}


