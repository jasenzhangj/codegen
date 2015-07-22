package com.hitao.codegen.configs.basic;

import java.util.Date;

import com.hitao.codegen.util.StringUtils;

/**
 * ConfigUtils provides a set of static conversion methods to assist in the
 * mapping of string values to appropriate Java objects or primitives.<br>
 * 
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: ConfigUtils.java 12 2011-02-20 10:50:23Z guest $
 */
public final class ConfigUtils {
	private ConfigUtils() {
	}

	/**
	 * Convert the passed string argument into an Integer value.
	 * 
	 * @param value
	 *            the string to convert
	 * @return the string value converted to an Integer.
	 */
	public static final Integer toInteger(String value) {
		return toInteger(value, 0);
	}

	/**
	 * Convert the passed string argument into an Integer value.
	 * 
	 * @param value
	 *            the string to convert
	 * @param defaultValue
	 *            the default value to use in case conversion fails
	 * @return the string value converted to an Integer.
	 */
	public static final Integer toInteger(String value, int defaultValue) {
		Integer result = null;
		if (!StringUtils.isEmpty(value)) {
			result = new Integer(value);
		}
		if (result == null) {
			result = Integer.valueOf(defaultValue);
		}
		return result;
	}

	/**
	 * Return the passed string value represented as a primitive int.
	 * 
	 * @param value
	 *            the string to convert
	 * @return the string value converted to a primitive int.
	 */
	public static final int asInt(String value) {
		return asInt(value, 0);
	}

	/**
	 * Converts the supplied object to an integer.
	 * 
	 * @param argValue
	 *            the value to convert
	 * @return the resulting integer
	 * @throws NumberFormatException
	 *             if <code>argObject</code> is not a {@link Number} and the
	 *             object's toString method does not contain a parsable integer.
	 */
	public static final int toInt(Object argValue) {
		return toInt(argValue, null);
	}

	/**
	 * Converts the supplied object to an integer.
	 * 
	 * @param argValue
	 *            the value to convert
	 * @param argDefault
	 *            the default value to return if <code>argValue</code> cannot be
	 *            converted
	 * @return the resulting integer
	 */
	public static final int toInt(Object argValue, int argDefault) {
		return toInt(argValue, Integer.valueOf(argDefault));
	}

	/**
	 * Converts the supplied object to an integer.
	 * 
	 * @param argValue
	 *            the value to convert
	 * @param argDefault
	 *            the default value to return if <code>argValue</code> cannot be
	 *            converted
	 * @return the resulting integer
	 * @throws NumberFormatException
	 *             if <code>argObject</code> is not a {@link Number},
	 *             <code>argDefault</code> is <code>null</code>, and the
	 *             object's toString method does not contain a parsable integer.
	 */
	public static final int toInt(Object argValue, Integer argDefault) {
		if (argValue instanceof IntegerConfig) {
			return ((IntegerConfig) argValue).getInteger().intValue();
		} else if (argValue instanceof Number) {
			return ((Number) argValue).intValue();
		} else {
			try {
				return Integer.parseInt(argValue.toString());
			} catch (RuntimeException ex) {
				if (argDefault == null) {
					throw ex;
				}
				return argDefault.intValue();
			}
		}
	}

	/**
	 * Return the passed string value represented as a primitive int.
	 * 
	 * @param value
	 *            the string to convert
	 * @param defaultValue
	 *            the default value to return in case conversion fails.
	 * @return the string value converted to a primitive int.
	 */
	public static final int asInt(String value, int defaultValue) {
		return asInt(toInteger(value, defaultValue), defaultValue);
	}

	/**
	 * Return the passed Integer value represented as a primitive int.
	 * 
	 * @param value
	 *            the Integer to convert
	 * @return the Integer value converted to a primitive int.
	 */
	public static final int asInt(Integer value) {
		return asInt(value, 0);
	}

	/**
	 * Return the passed Integer value represented as a primitive int.
	 * 
	 * @param value
	 *            the Integer to convert
	 * @param defaultValue
	 *            the default value to return in case conversion fails.
	 * @return the Integer value converted to a primitive int.
	 */
	public static final int asInt(Integer value, int defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value.intValue();
	}

	// Long conversion functions
	// Long conversion functions
	// Long conversion functions

	/**
	 * Convert the passed string argument into an Integer value.
	 * 
	 * @param value
	 *            the string to convert
	 * @return the string value converted to an Integer.
	 */
	public static final Long toLong(String value) {
		return toLong(value, 0);
	}

	/**
	 * Convert the passed string argument into an Integer value.
	 * 
	 * @param value
	 *            the string to convert
	 * @param defaultValue
	 *            the default value to use in case conversion fails
	 * @return the string value converted to an Integer.
	 */
	public static final Long toLong(String value, long defaultValue) {
		Long result = null;
		if (!StringUtils.isEmpty(value)) {
			result = new Long(value);
		}
		if (result == null) {
			result = Long.valueOf(defaultValue);
		}
		return result;
	}

	/**
	 * Return the passed Integer value represented as a primitive int.
	 * 
	 * @param value
	 *            the Integer to convert
	 * @return the Integer value converted to a primitive int.
	 */
	public static final long asLong(Number value) {
		return asLong(value, 0);
	}

	/**
	 * Return the passed Integer value represented as a primitive int.
	 * 
	 * @param value
	 *            the Integer to convert
	 * @param defaultValue
	 *            the default value to return in case conversion fails.
	 * @return the Integer value converted to a primitive int.
	 */
	public static final long asLong(Number value, long defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value.longValue();
	}

	// Boolean conversion functions
	// Boolean conversion functions
	// Boolean conversion functions

	/**
	 * Determine if the specified string semantically indicates a "true" or
	 * "false" condition.
	 * 
	 * @param argString
	 *            the string to test
	 * @return whether the string represents a "true" or "false" condition
	 */
	public static final Boolean toBoolean(String argString) {
		return toBoolean(argString, false);
	}

	/**
	 * Determine if the specified string semantically indicates a "true" or
	 * "false" condition
	 * 
	 * @param argString
	 *            the string to test
	 * @param argDefault
	 *            the value to return if the test string is null or empty
	 * @return whether the string represents a "true" or "false" condition
	 */
	public static final Boolean toBoolean(String argString, boolean argDefault) {
		return toBooleanImpl(argString, argDefault);
	}

	/**
	 * Convert the value to a Boolean
	 * 
	 * @param argValue
	 *            the value to convert
	 * @return <code>true</code> or <code>false</code>
	 */
	public static final Boolean toBoolean(Object argValue) {
		return toBooleanImpl(argValue, false);
	}

	/**
	 * Convert the value to a Boolean
	 * 
	 * @param argValue
	 *            the value to convert
	 * @param argDefault
	 *            value to use for <code>null</code>
	 * @return <code>true</code> or <code>false</code>
	 */
	public static final Boolean toBoolean(Object argValue, boolean argDefault) {
		return toBooleanImpl(argValue, argDefault);
	}

	private static final Boolean toBooleanImpl(Object argValue,
			boolean argDefault) {
		if (argValue instanceof Boolean) {
			return (Boolean) argValue;
		}
		if (argValue == null) {
			return Boolean.valueOf(argDefault);
		}
		switch (argValue.toString().trim().charAt(0)) {
		case 't':
		case 'T':
		case 'y':
		case 'Y':
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Return the passed string value represented as a primitive boolean.
	 * 
	 * @param value
	 *            the string to convert
	 * @return the string value converted to a primitive boolean.
	 */
	public static final boolean asBool(String value) {
		return asBool(value, false);
	}

	/**
	 * Return the passed string value represented as a primitive boolean.
	 * 
	 * @param value
	 *            the string to convert
	 * @param defaultValue
	 *            the default value to return in case conversion fails.
	 * @return the string value converted to a primitive boolean.
	 */
	public static final boolean asBool(String value, boolean defaultValue) {
		return asBool(toBoolean(value), defaultValue);
	}

	/**
	 * Return the passed Boolean value represented as a primitive boolean.
	 * 
	 * @param value
	 *            the Boolean to convert
	 * @return the Boolean value converted to a primitive boolean.
	 */
	public static final boolean asBool(Boolean value) {
		return asBool(value, false);
	}

	/**
	 * Return the passed Boolean value represented as a primitive boolean.
	 * 
	 * @param value
	 *            the Boolean to convert
	 * @param defaultValue
	 *            the default value to return in case conversion fails.
	 * @return the Boolean value converted to a primitive boolean.
	 */
	public static final boolean asBool(Boolean value, boolean defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value.booleanValue();
	}

	/**
	 * Extracts a Class value from a {@link dtv.util.config.ClassConfig} or a
	 * {@link dtv.util.config.StringConfig}.
	 * 
	 * @param <T>
	 *            type of the class
	 * @param argValue
	 *            the config object to parse
	 * @return a Class object
	 */
	@SuppressWarnings("unchecked")
	public static final <T> Class<T> toClass(IConfigObject argValue) {
		final ClassConfig<T> cc;
		if (argValue instanceof ClassConfig) {
			cc = (ClassConfig<T>) argValue;
		} else {
			cc = new ClassConfig<T>();
			cc.setValue(argValue.toString());
			cc.setSourceInfo(argValue.getSourceUrl(),
					argValue.getSourceLineNumber());
		}
		return cc.getValue();
	}

	/**
	 * Extracts a boolean value from a {@link dtv.util.config.BooleanConfig} or
	 * a {@link dtv.util.config.StringConfig}.
	 * 
	 * @param argValue
	 *            the config object to parse
	 * @return a boolean
	 */
	public static final boolean toBoolean(IConfigObject argValue) {
		final BooleanConfig bc;
		if (argValue instanceof BooleanConfig) {
			bc = (BooleanConfig) argValue;
		} else {
			bc = new BooleanConfig(argValue.toString());
		}
		return bc.getBoolean().booleanValue();
	}

	/**
	 * Extracts a BigDecimal value from a {@link dtv.util.config.DateConfig} or
	 * a {@link dtv.util.config.StringConfig}.
	 * 
	 * @param argValue
	 *            the config object to parse
	 * @return a Date
	 */
	public static final Date toDate(IConfigObject argValue) {
		return toDateConfig(argValue).getDate();
	}

	/**
	 * Extracts a DateConfig value from a {@link dtv.util.config.DateConfig} or
	 * a {@link dtv.util.config.StringConfig}.
	 * 
	 * @param argValue
	 *            the config object to parse
	 * @return a DateConfig
	 */
	public static final DateConfig toDateConfig(IConfigObject argValue) {
		if (argValue instanceof DateConfig) {
			return (DateConfig) argValue;
		}
		return new DateConfig(argValue.toString());
	}

	/**
	 * Extracts an int from an {@link dtv.util.config.IntegerConfig} or a
	 * {@link dtv.util.config.StringConfig}.
	 * 
	 * @param argValue
	 *            the config object to parse
	 * @return an int
	 * @throws NumberFormatException
	 *             if <code>argValue</code> is not an IntegerConfig and the
	 *             toString does not contain a parsable integer.
	 */
	public static final int toInt(IConfigObject argValue) {
		return toInt(argValue, null);
	}

	/**
	 * Extracts a long from an {@link dtv.util.config.IntegerConfig}, a
	 * {@link dtv.util.config.LongConfig}, or a
	 * {@link dtv.util.config.StringConfig}.
	 * 
	 * @param argValue
	 *            the config object to parse
	 * @return a long
	 * @throws NumberFormatException
	 *             if <code>argValue</code> is not an IntegerConfig or
	 *             LongConfig and the toString does not contain a parsable
	 *             integer.
	 */
	public static final long toLong(IConfigObject argValue) {
		if (argValue instanceof LongConfig) {
			return ((LongConfig) argValue).getLong().longValue();
		} else if (argValue instanceof IntegerConfig) {
			return ((IntegerConfig) argValue).getInteger().longValue();
		} else {
			return new LongConfig(argValue.toString()).getLong().longValue();
		}
	}

	/**
	 * Extracts a ClassConfig value from a {@link dtv.util.config.ClassConfig}
	 * or a {@link dtv.util.config.StringConfig}.
	 * 
	 * @param argValue
	 *            the config object to parse
	 * @return a ClassConfig
	 */
	@SuppressWarnings("rawtypes")
	public static final ClassConfig toClassConfig(IConfigObject argValue) {
		if ((argValue instanceof ClassConfig) || (argValue == null)) {
			return (ClassConfig) argValue;
		}
		return new ClassConfig<String>(argValue.toString());
	}

}
