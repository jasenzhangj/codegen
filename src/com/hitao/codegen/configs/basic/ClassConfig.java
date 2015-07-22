package com.hitao.codegen.configs.basic;

import com.hitao.codegen.util.ObjectUtils;
import com.hitao.codegen.util.ReflectionException;

/**
 * Configuration for a Class object<br>
 * 
 * @author zhangjun.ht
 * @created 2010-11-10
 * @param <T>
 *            type of the class
 * @version $Id: ClassConfig.java 12 2011-02-20 10:50:23Z guest $        
 */
@SuppressWarnings("rawtypes")
public class ClassConfig<T> extends VariablePrimitiveConfig implements
		IReflectionParameterCapable<Class> {

  private static final long serialVersionUID = -887874894876846147L;

	private Class<T> clazz_;
	private String className_;

  /**
	 * Constructor
	 */
	public ClassConfig() {
	}

  /**
	 * Constructor
	 * 
	 * @param argValue
	 *            configured class
	 */
	public ClassConfig(String argValue) {
		setValue(argValue);
	}

  /**
	 * Constructor
	 * 
	 * @param argValue
	 *            configured class
	 */
	@SuppressWarnings("unchecked")
	public ClassConfig(Class argValue) {
		clazz_ = argValue;
		className_ = clazz_ == null ? null : clazz_.getName();
	}

	@Override
	protected void setValueImpl(String argValue) {
		className_ = argValue.trim();
	}

  /**
	 * Gets the class this config references.
	 * 
	 * @return the class this config references.
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getValue() {
		if (clazz_ == null) {
			try {
				clazz_ = ObjectUtils.getClass(className_);
			} catch (ReflectionException ex) {
				throw new ReflectionException(ex.getMessage() + "::"
						+ getSourceDescription(), ex.getCause());
			}
		}
		return clazz_;
	}

  /** {@inheritDoc} */
	public Class<Class> getParamDataType() {
		return Class.class;
	}

  /** {@inheritDoc} */
	public Class<T> getParamValue() {
		return getValue();
	}

  /** {@inheritDoc} */
	public String getConfigDataType() {
		return "Class";
	}

  /** {@inheritDoc} */
	public String getConfigValue() {
		String s = getRawValue();
		if (s != null) {
			return s;
		}
		return className_;
	}

  /** {@inheritDoc} */
	@Override
	public int hashCode() {
		return className_.hashCode();
	}

  /**
	 * Gets a string representation of the config object.
	 * 
	 * @return a string representation of the config object.
	 */
	@Override
	public String toString() {
		return getClass().getName() + "[" + className_ + "]::"
				+ getSourceDescription();
	}
}
