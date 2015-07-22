package com.hitao.codegen.configs.basic;

/**
 * A simple config object for an integer value.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: IntegerConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public class IntegerConfig
    extends VariablePrimitiveConfig
    implements IReflectionParameterCapable<Integer> {

  private static final long serialVersionUID = 1L;
  protected Integer value_;

  /**
   * Constructor
   */
  public IntegerConfig() {
    super();
  }

  /**
   * Constructor
   * @param argValue  configured value
   */
  public IntegerConfig(String argValue) {
    super(argValue);
  }

  @Override
  protected void setValueImpl(String value) {
    value_ = ConfigUtils.toInteger(value);
  }

  /**
   * Gets configured value
   * @return configured value
   */
  public Integer getInteger() {
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
  public Class<Integer> getParamDataType() {
    return int.class;
  }

  /**{@inheritDoc}*/
  public Integer getParamValue() {
    return getInteger();
  }

  /**{@inheritDoc}*/
  public String getConfigDataType() {
    return "Integer";
  }

  /**{@inheritDoc}*/
  public String getConfigValue() {
    String s = getRawValue();
    if (s != null) {
      return s;
    }
    return getInteger().toString();
  }

  /**
   * Constructor
   * @param argValue configured value
   */
  public IntegerConfig(Integer argValue) {
    value_ = argValue;
  }

}


