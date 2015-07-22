package com.hitao.codegen.configs.basic;

/**
 * Interface for a config that can be cascaded.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: ICascadableConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public interface ICascadableConfig
    extends IConfigObject {

  /**
   * Copy any values from <code>argValue</code>.
   * 
   * @param argValue  the config to cascase from
   */
  public void cascadeValues(IConfigObject argValue);

}

