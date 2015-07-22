package com.hitao.codegen.configs.basic;

import com.hitao.codegen.util.StringUtils;

/**
 * Config Object for a String.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: StringConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public class StringConfig extends AbstractConfig implements
		IReflectionParameterCapable<String> {

	private static final long serialVersionUID = 1L;
	private String rawValue_;
	private String value_;
	private boolean isNull_;

	/**
	 * Constructor
	 */
	public StringConfig() {
		value_ = "";
		isNull_ = false;
	}

	/** {@inheritDoc} */
	public void setConfigObject(String argKey, IConfigObject argValue) {
		if ("Null".equalsIgnoreCase(argKey)) {
			isNull_ = ConfigUtils.toBoolean(argValue);
		} else {
			warnUnsupported(argKey, argValue);
		}
	}

	/**
	 * Constructor
	 *
	 * @param argValue
	 *            configured value
	 */
	public StringConfig(String argValue) {
		setValue(argValue);
	}

	/** {@inheritDoc} */
	public void setValue(String argValue) {
		rawValue_ = argValue;
		setValueImpl(StringUtils.replaceVariables(argValue,
				System.getProperties()));
	}

	protected void setValueImpl(String value) {
		if (isNull_ && StringUtils.isEmpty(value)) {
			value_ = null;
		} else {
			value_ = value;
			if (value_ == null) {
				isNull_ = true;
			}
		}
	}

	/**
	 * Gets the configured value
	 *
	 * @return the configured value
	 */
	public String getString() {
		return value_;
	}

	/**
	 * Return the config object with a useable string representation. This
	 * normally maps to the name of the xml tag that the object is based on.
	 *
	 * @return the string name
	 */
	@Override
	public String toString() {
		return value_;
	}

	/** {@inheritDoc} */
	public Class<String> getParamDataType() {
		return String.class;
	}

	/** {@inheritDoc} */
	public String getParamValue() {
		return getString();
	}

	/** {@inheritDoc} */
	public String getConfigDataType() {
		return "String";
	}

	/** {@inheritDoc} */
	public String getConfigValue() {
		if (isNull_) {
			return "<Null dtype=\"Boolean\">true</Null>";
		}
		return getRawValue();
	}

	/**
	 * Gets the hash code
	 *
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		return value_.hashCode();
	}

	/**
	 * determine if equals another object
	 *
	 * @param argOther
	 *            other object
	 * @return <code>true</code> if equal
	 */
	@Override
	public boolean equals(Object argOther) {
		if (argOther == this) {
			return true;
		}
		if (!(argOther instanceof StringConfig)) {
			return false;
		}
		StringConfig other = (StringConfig) argOther;
		if ((other.value_ == null) && (value_ == null)) {
			return true;
		}
		if (value_ == null) {
			return false;
		}
		return value_.equals(other.value_);
	}

	/**
	 * Gets the raw configured value.
	 *
	 * @return the raw configured value.
	 */
	public String getRawValue() {
		return rawValue_;
	}

}
