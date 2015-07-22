package com.hitao.codegen.util;

/**
 * Interface for filter as to which names to accept.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: INameFilter.java 12 2011-02-20 10:50:23Z guest $
 */
public interface INameFilter {
  /**
   * Checks if a name passes the filter.
   * 
   * @param argName  name to check
   * @return <code>true</code> if the name passes the filter, otherwise <code>false</code>
   */
  public boolean accept(String argName);

}

