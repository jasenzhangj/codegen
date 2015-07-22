package com.hitao.codegen.configs.basic;

/**
 * A simple config object for a boolean value as "true" or "false".<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: BooleanConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public class BooleanConfig
    extends VariablePrimitiveConfig
    implements IReflectionParameterCapable<Boolean> {

  private static final long serialVersionUID = -7126065148447128766L;
  private Boolean value_;

  /**
   * Constructor
   */
  public BooleanConfig() {
    super();
  }

  /**
   * Constructor
   * 
   * @param argValue  configured value
   */
  public BooleanConfig(String argValue) {
    super(argValue);
  }

  /**
   * Constructor
   * 
   * @param argValue  configured value
   */
  public BooleanConfig(Boolean argValue) {
    value_ = argValue;
  }

  /**
   * Constructor
   * 
   * @param argValue  configured value
   */
  public BooleanConfig(boolean argValue) {
    value_ = Boolean.valueOf(argValue);
  }

  /**{@inheritDoc}*/
  @Override
  protected void setValueImpl(String argValue) {
    value_ = ConfigUtils.toBoolean(argValue);
  }

  /**
   * Gets the configured value
   * 
   * @return the configured value
   */
  public Boolean getBoolean() {
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
  public Class<Boolean> getParamDataType() {
    return boolean.class;
  }

  /**{@inheritDoc}*/
  public Boolean getParamValue() {
    return getBoolean();
  }

  /**{@inheritDoc}*/
  public String getConfigDataType() {
    return "Boolean";
  }

  /**{@inheritDoc}*/
  public String getConfigValue() {
    String s = getRawValue();
    if (s != null) {
      return s;
    }
    return String.valueOf(getBoolean());
  }
}
