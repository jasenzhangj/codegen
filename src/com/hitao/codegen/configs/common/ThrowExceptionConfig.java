package com.hitao.codegen.configs.common;

/***
 * The throw exception configuration of the method configuration.
 * 
 * @author zhangjun.ht
 * @created 2010-11-28
 * @version $Id: ThrowExceptionConfig.java 35 2011-03-02 09:19:57Z guest $
 */
public class ThrowExceptionConfig extends ClassElementConfig {

	private static final long serialVersionUID = 6055242536912696174L;

	public static final String MAIN_TAG = "throwException";

	@Override
	public String getStatement() {
		return getShortClassName();
	}

	@Override
	public int hashCode() {
		return getClassName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ThrowExceptionConfig)) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		return ((ThrowExceptionConfig) obj).getClassName().equals(
				getClassName());
	}

}
