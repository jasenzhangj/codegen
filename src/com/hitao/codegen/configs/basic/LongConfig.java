package com.hitao.codegen.configs.basic;

/**
 * A simple config object for an integer value.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: LongConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public class LongConfig
    extends VariablePrimitiveConfig
    implements IReflectionParameterCapable<Long> {

  private static final long serialVersionUID = 1L;
  protected Long value_;

  /**
   * Constructor
   */
  public LongConfig() {
    super();
  }

  /**
   * Constructor
   * @param argValue  configured value
   */
  public LongConfig(String argValue) {
    super(argValue);
  }

  @Override
  protected void setValueImpl(String argValue) {
    value_ = ConfigUtils.toLong(argValue);
  }

  /**
   * Gets the configured value
   * 
   * @return the configured value
   */
  public Long getLong() {
    return value_;
  }

  /**
   * Return the config object with a useable string representation.  This
   * normally maps to the name of the xml tag that the object is based on.
   *
   * @return the string name
   */
  @Override
  public String toString() {
    return String.valueOf(value_);
  }

  /**{@inheritDoc}*/
  public Class<Long> getParamDataType() {
    return long.class;
  }

  /**{@inheritDoc}*/
  public Long getParamValue() {
    return getLong();
  }

  /**{@inheritDoc}*/
  public String getConfigDataType() {
    return "Long";
  }

  /**{@inheritDoc}*/
  public String getConfigValue() {
    String s = getRawValue();
    if (s != null) {
      return s;
    }
    return getLong().toString();
  }

  /**
   * Constructor
   * @param argValue configured value
   */
  public LongConfig(Long argValue) {
    value_ = argValue;
  }

}

