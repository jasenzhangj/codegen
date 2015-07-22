//$Id: IConfigLoader.java 12 2011-02-20 10:50:23Z guest $
package com.hitao.codegen.configs.basic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Interface for the logic that actually loads a config file.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: IConfigLoader.java 12 2011-02-20 10:50:23Z guest $
 */
public interface IConfigLoader {
  /**
   * Load the document
   * 
   * @param argCallback     the callback to use for getting config objects
   * @param argCurrentFile  the file being loaded
   * @param argInputStream  the input stream to use to load the file
   * @throws IOException if unable to load the file
   */
  public void loadDocument(
      IConfigLoaderCallback argCallback, URL argCurrentFile,
      InputStream argInputStream)
      throws IOException;

}

