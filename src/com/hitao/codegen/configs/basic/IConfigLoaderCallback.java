package com.hitao.codegen.configs.basic;

/**
 * Interface for callback from a config loader to a config helper.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: IConfigLoaderCallback.java 12 2011-02-20 10:50:23Z guest $
 */
public interface IConfigLoaderCallback {
  /**
   * Gets a config object
   * 
   * @param argTagName            the node tag for which to get a config object
   * @param argDtype              the node dtype for which to get a config object
   * @param argParent             the parent of the config object
   * @param argSourceDescription  the location at which the config is used
   * @return the config object for the node
   */
  public IConfigObject getConfigObject(
      String argTagName, String argDtype, IConfigObject argParent,
      String argSourceDescription);

}

