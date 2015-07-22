package com.hitao.codegen.util;

/**
 * Interface for a named value.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @param <K> type of the key
 * @param <V> type of the value
 * @version $Id: IKeyedValue.java 12 2011-02-20 10:50:23Z guest $
 */
public interface IKeyedValue<K, V> {
  /**
   * Gets the key.
   * 
   * @return  the key
   */
  public K getKey();
  
  /**
   * Gets the value.
   * 
   * @return  the value
   */
  public V getValue();
  
}

