package com.hitao.codegen.configs.common;

/***
 * This class is represent all the implement class configuration object. Like
 * DAoimpl, MANAGERimpl, AOimpl
 * 
 * @author zhangjun.ht
 * @created 2010-11-30
 * @version $Id: ImplementsClassConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public class ImplementsClassConfig extends ClassConfig {

	private static final long serialVersionUID = -5527731295697071625L;

	protected boolean needExpandName() {
		return true;
	}

}
