package com.hitao.codegen.configs.basic;

/**
 * An exception that indicates there is a problem with configuration.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: ConfigException.java 12 2011-02-20 10:50:23Z guest $
 */
public class ConfigException
    extends RuntimeException {

  /**
   * Constructor
   */
  public ConfigException() {
    super();
  }

  /**
   * Constructor
   * @param message message for the exception
   */
  public ConfigException(String message) {
    super(message);
  }

  /**
   * Constructor
   * @param cause initial cause of the exception
   */
  public ConfigException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructor
   * @param badObject object with problems leading to the exception
   * @param cause initial cause of the exception
   */
  public ConfigException(Object badObject, Throwable cause) {
    super("problem with " + badObject, cause);
  }

  /**
   * Constructor
   * @param message message for the exception
   * @param cause initial cause of the exception
   */
  public ConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  static final long serialVersionUID = 6410326234195067629L;
  private String configLocationDescription_;

  /**
   * Sets configuration location information that led to the exception
   * 
   * @param argValue configuration location information that led to the exception
   */
  public void setConfigLocationDescription(String argValue) {
    configLocationDescription_ = argValue;
  }

  /**
   * Gets the message for the exception
   * @return the message for the exception
   */
  @Override
  public String getMessage() {
    String msg = super.getMessage();
    if (configLocationDescription_ != null) {
      msg += " at ";
      msg += configLocationDescription_;
    }
    return msg;
  }

}

