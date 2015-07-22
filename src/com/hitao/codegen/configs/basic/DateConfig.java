package com.hitao.codegen.configs.basic;

import java.util.Date;

import com.hitao.codegen.util.DateUtils;

/**
 * Configuration object for a date.
 * 
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: DateConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public class DateConfig extends VariablePrimitiveConfig implements
		IReflectionParameterCapable<Date> {

	private static final long serialVersionUID = 1L;
	private Date value_;

	/**
	 * Constructor
	 */
	public DateConfig() {
		super();
	}

	/**
	 * Constructs a <code>DateConfig</code>.
	 * 
	 * @param argDate
	 *            the date wrapped by this configuration object
	 */
	public DateConfig(Date argDate) {
		super();
		value_ = argDate;
	}

	/**
	 * Constructor for the DateConfig object
	 * 
	 * @param argValue
	 *            the date as a string in format "yyyy-mm-dd"
	 */
	public DateConfig(String argValue) {
		super(argValue);
	}

	@Override
	protected void setValueImpl(String value) {
		value_ = DateUtils.parseDate(value);
	}

	/**
	 * Gets the date attribute of the DateConfig object
	 * 
	 * @return The date value
	 */
	public Date getDate() {
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
		return String.valueOf(value_);
	}

	/** {@inheritDoc} */
	public Class<Date> getParamDataType() {
		return Date.class;
	}

	/** {@inheritDoc} */
	public Date getParamValue() {
		return getDate();
	}

	/** {@inheritDoc} */
	public String getConfigDataType() {
		return "Date";
	}

	/** {@inheritDoc} */
	public String getConfigValue() {
		String s = getRawValue();
		if (s != null) {
			return s;
		}
		return DateUtils.format(getDate());
	}
}
