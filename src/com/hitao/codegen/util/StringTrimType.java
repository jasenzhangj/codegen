package com.hitao.codegen.util;

/**
 * A type-safe enumeration for ways to trim a string. This class allows a trim method to be
 * configured in a file.<br>
 * <br>
 * 
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: StringTrimType.java 12 2011-02-20 10:50:23Z guest $
 */
public enum StringTrimType {
  /** don't trim */
  NONE,
  /** trim on the left side only */
  LEFT,
  /** trim on the right side only */
  RIGHT,
  /** trim on both sides */
  FULL;
}
