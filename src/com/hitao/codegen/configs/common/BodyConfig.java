package com.hitao.codegen.configs.common;

/***
 * The method's body.
 * 
 * @author zhangjun.ht
 * @created 2010-11-30
 * @version $Id: BodyConfig.java 25 2011-02-24 08:40:37Z guest $
 */
public class BodyConfig extends ClassElementConfig {

	private static final long serialVersionUID = -6736494616084356247L;

	public static final String MAIN_TAG = "body";

	@Override
	public String getStatement() {
		return getValue();
	}
}
