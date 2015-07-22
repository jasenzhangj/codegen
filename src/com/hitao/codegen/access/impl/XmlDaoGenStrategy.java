package com.hitao.codegen.access.impl;

import com.hitao.codegen.configs.CodeGenConfigHelper;

/***
 * This class is represents to generate codes from XML configuration file.
 * 
 * @author zhangjun.ht
 * @created 2011-1-13
 * @version $Id: XmlDaoGenStrategy.java 12 2011-02-20 10:50:23Z guest $
 */
public class XmlDaoGenStrategy extends DaoGenStrategy {

	@Override
	public CodeGenConfigHelper getCodeGenConfigHelperInstance() {
		CodeGenConfigHelper codeConfig = new CodeGenConfigHelper(
				getDaoStringInDir());
		codeConfig.initialize();
		return codeConfig;
	}
}
