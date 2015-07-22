package com.hitao.codegen.configs.basic;

import org.apache.log4j.Logger;

/**
 * Abstract configuration object.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: AbstractConfig.java 58 2011-05-06 06:40:39Z guest $
 */
public abstract class AbstractConfig implements IConfigObject, Cloneable {

	private static final long serialVersionUID = 1L;

	private int sourceLineNumber_ = -1;
	private String sourceUrl_ = "";
	private String sourceString_ = null;

	@Override
	public void setConfigObject(IConfigObject argValue) {
		setConfigObject(null, argValue);
	}
	
	/** {@inheritDoc} */
	public final int getSourceLineNumber() {
		return sourceLineNumber_;
	}

	/** {@inheritDoc} */
	public String getSourceUrl() {
		return sourceUrl_;
	}

	/** {@inheritDoc} */
	public void setSourceInfo(String argSourceUrl, int argSourceLineNumber) {
		sourceUrl_ = argSourceUrl;
		sourceLineNumber_ = argSourceLineNumber;
		sourceString_ = createSourceString(argSourceUrl, argSourceLineNumber);
	}

	/** {@inheritDoc} */
	public String getSourceDescription() {
		return sourceString_;
	}

	/**
	 * Warn about an unsupported config.
	 *
	 * @param argKey
	 *            the key.
	 * @param argValue
	 *            the value.
	 */
	protected void warnUnsupported(String argKey, IConfigObject argValue) {
		StringBuilder sb = new StringBuilder();
		sb.append("unsupported config ");
		sb.append(argKey).append("=").append(argValue);
		sb.append("::").append(getSourceDescription(argValue));
		sb.append("::").append(getClass().getName());
		Logger.getLogger(getClass()).warn(sb);
	}

	/**
	 * Warn about a deprecated config.
	 *
	 * @param argKey
	 *            the key.
	 * @param argValue
	 *            the value.
	 */
	protected void warnDeprecated(String argKey, IConfigObject argValue) {
		StringBuilder sb = new StringBuilder();
		sb.append("deprecated config ");
		sb.append(argKey).append("=").append(argValue);
		sb.append("::").append(getSourceDescription(argValue));
		sb.append("::").append(getClass().getName());
		Logger.getLogger(getClass()).warn(sb);
	}

	/**
	 * Get the source description for a config.
	 *
	 * @param argSubconfig
	 *            the config for which to get the source description.
	 * @return the source description.
	 */
	protected String getSourceDescription(IConfigObject argSubconfig) {
		if (argSubconfig == null) {
			return getSourceDescription();
		}
		return argSubconfig.getSourceDescription();
	}

	/**
	 * Create the source string.
	 *
	 * @param argSourceUrl
	 *            the URL.
	 * @param argSourceLineNumber
	 *            the line number.
	 * @return String containing the URL and line number.
	 */
	private static String createSourceString(String argSourceUrl,
			int argSourceLineNumber) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		sb.append(argSourceUrl);
		sb.append(":");
		sb.append(argSourceLineNumber);
		sb.append(")");
		return sb.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
