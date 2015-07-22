package com.hitao.codegen.configs.basic;

/**
 * Abstract ancestor of configs that do not contain other config objects.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: PrimitiveConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public abstract class PrimitiveConfig
    extends AbstractConfig {

  private static final long serialVersionUID = 8135143193208312482L;

  /**{@inheritDoc}*/
  public void setConfigObject(String argKey, IConfigObject argValue) {
    /* this node contains no children */
  }

  /**
   * no arg constructor
   */
  public PrimitiveConfig() {
    /*nothing to do*/
  }

  /**
   * Constructor
   * 
   * @param argValue  value
   */
  public PrimitiveConfig(String argValue) {
    setValue(argValue);
  }

}

