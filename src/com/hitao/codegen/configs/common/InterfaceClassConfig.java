package com.hitao.codegen.configs.common;

import com.hitao.codegen.configs.IInterfaceCodeGenConfig;

/***
 * This class is represent all the interfaces configuration object. Like
 * DAoimpl, MANAGERimpl, AOimpl
 *
 * @author zhangjun.ht
 * @created 2010-12-3
 * @version $Id: InterfaceClassConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public class InterfaceClassConfig extends ClassConfig implements
		IInterfaceCodeGenConfig {

	private static final long serialVersionUID = -2201925220704491302L;

	@Override
	protected String getClassType() {
		return "interface";
	}

	@Override
	protected void getImplementsInfo(StringBuffer argStringBuffer) {
		// The DAO can't implements any interface.
	}
}
