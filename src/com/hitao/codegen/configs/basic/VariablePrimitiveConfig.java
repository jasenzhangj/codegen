package com.hitao.codegen.configs.basic;

import com.hitao.codegen.util.StringUtils;

/**
 * Abstract primitive configuration object responsible for filling in variables
 * from the configured string in the form of ${variable} with the cooresponding
 * value from <tt>System.getProperties()</tt>.<br>
 * 
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: VariablePrimitiveConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public abstract class VariablePrimitiveConfig extends PrimitiveConfig {

	private static final long serialVersionUID = 1L;

	private String rawValue_ = null;

	/**
	 * Constructor
	 */
	protected VariablePrimitiveConfig() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param argValue
	 *            the configured value
	 */
	protected VariablePrimitiveConfig(String argValue) {
		super(argValue);
	}

	/** {@inheritDoc} */
	public final void setValue(String argValue) {
		rawValue_ = argValue;
		if (argValue != null) {
			setValueImpl(StringUtils.replaceVariables(argValue,
					System.getProperties()));
		}
	}

	protected abstract void setValueImpl(String argValue);

	/**
	 * Gets the raw configured value.
	 * 
	 * @return the raw configured value.
	 */
	public String getRawValue() {
		return rawValue_;
	}

}
