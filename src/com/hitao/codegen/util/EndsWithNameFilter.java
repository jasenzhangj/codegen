package com.hitao.codegen.util;

/**
 * Name filter that accepts names that end with specified strings.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: EndsWithNameFilter.java 12 2011-02-20 10:50:23Z guest $
 */
public class EndsWithNameFilter
        implements INameFilter {

  private final String[] filteredEndings_;
  private final boolean inclusionary_;
  
  /**
   * Constructs an inclusionary <code>EndsWithNameFilter</code>.
   * 
   * @param argFilteredEndings the suffixes against which to compare names
   */
  public EndsWithNameFilter(String... argFilteredEndings) {
    this(true, argFilteredEndings);
  }
  
  /**
   * Constructs an <code>EndsWithNameFilter</code>.
   * 
   * @param argFilteredEndings the suffixes against which to compare names
   * @param argInclusionary <code>true</code> if the presence of any suffix
   * from <code>argFilteredEndings</code> in a given name should qualify that
   * name for acceptance by this filter; <code>false</code> if such a condition
   * should result in exclusion by this filter
   */
  public EndsWithNameFilter(boolean argInclusionary, String... argFilteredEndings) {
    filteredEndings_ = new String[argFilteredEndings.length];
    for (int i = 0; i < argFilteredEndings.length; i++) {
      filteredEndings_[i] = argFilteredEndings[i].toLowerCase();
    }
    inclusionary_ = argInclusionary;
  }

  /** {@inheritDoc} */
  public boolean accept(String argName) {
    String name = argName.toLowerCase();
    for (String element : filteredEndings_) {
      if (name.endsWith(element)) {
        return inclusionary_;
      }
    }
    return !inclusionary_;
  }

}

