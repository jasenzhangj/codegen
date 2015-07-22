package com.hitao.codegen.configs.basic;

/**
 * This interface provides a mechanism for the configuration helper to parse an
 * XML configuration file.  It enables the helper to load the tree structure and
 * establish the parent/child relationships common in XML files.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: IConfigObject.java 58 2011-05-06 06:40:39Z guest $
 */
public interface IConfigObject 
        extends java.io.Serializable, IHasSourceDescription, Cloneable{

  /**
   * This method provides a mechanism for the configuration helper to set
   * created objects within this object.  It enables the helper to load the tree
   * structure and establish the parent/child relationships common in XML files.
   *
   * @param argKey    The key representing the XML tag.
   * @param argValue  The value object representing the XML tag.
   */
  public void setConfigObject(String argKey, IConfigObject argValue);
  
  /**
   * This method provides a mechanism for the configuration helper to set
   * created objects within this object.  It enables the helper to load the tree
   * structure and establish the parent/child relationships common in XML files.
   *
   * @param argValue  The value object representing the XML tag.
   */
  public void setConfigObject(IConfigObject argValue);

  /**
   * Sets the value of the config from the text contents of the node
   * representing the config object.
   * 
   * @param argValue  the text contents of the config node
   */
  public void setValue(String argValue);
  

  /**
   * Sets information about the source configuration file that led to this
   * configuration object.
   * 
   * @param argSourceUrl         the source location for this config object
   * @param argSourceLineNumber  the source line number for this config object
   */
  public void setSourceInfo(String argSourceUrl, int argSourceLineNumber);

  /**
   * Gets the source line number for this config object.
   * 
   * @return  the source line number for this config object
   */
  public int getSourceLineNumber();

  /**
   * Gets the source location for this config object.
   * 
   * @return the source location for this config object.
   */
  public String getSourceUrl();

  /***
   * Clone object.
   * @return
   */
  public Object clone() throws CloneNotSupportedException;
}

