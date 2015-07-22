package com.hitao.codegen.configs.basic;


/**
 * Interface for Interface for an object that exposes information about the source config that lead to the object.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: IHasSourceDescription.java 12 2011-02-20 10:50:23Z guest $
 */
public interface IHasSourceDescription {
  /**
   * Get a description of the configuration file location that led to this
   * object.  This is useful for logging problems when generating the document
   * this object is a member of.
   *
   * @return a description of the configuration file location that
   *      lead to this object.
   */
  public String getSourceDescription();

}

