package com.hitao.codegen.util;


/**
 * Interface to represent a short code.  ICode implementations are cast
 * to Strings by the getCode() method in ObjectUtils.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: ICode.java 12 2011-02-20 10:50:23Z guest $
 */
public interface ICode {
  /**
   * Obtains the short code or abbreviation for the object.
   * @return String The short code.
   */
  public String getCode();
  
}

