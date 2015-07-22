package com.hitao.codegen.configs.dao;

import com.hitao.codegen.configs.basic.AbstractSetConfig;


/***
 * "DaoGen" configuration.
 *
 * @author zhangjun.ht
 * @created 2010-11-26
 * @version $Id: CodeGenConfigSet.java 32 2011-03-02 02:28:51Z guest $
 */
public class CodeGenConfigSet extends AbstractSetConfig<DAOConfigs> {


	private static final long serialVersionUID = 4321048409038032571L;

	public static final String MAIN_TAG = "codegen";

	@Override
	public String getChildTag() {
		return DAOConfigs.MAIN_TAG;
	}

}
