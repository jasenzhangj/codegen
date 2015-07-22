package com.hitao.codegen.util;

/**
 * Interface for An interface for an object that provides the implementation
 * of a call containing curly brackets (e.g. {}).<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: IMethodChainCallback.java 12 2011-02-20 10:50:23Z guest $
 */
public interface IMethodChainCallback {
  /**
   * Resolves a value to its actual value using the specified parameter.
   * 
   * @param argValue      the value to resolve
   * @param argParameter  a parameter to the resolution
   * @return the value as resolved through the specifics of the callback
   */
  public Object resolve(Object argValue, String argParameter);

}


