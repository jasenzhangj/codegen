package com.hitao.codegen.configs.dao;

import static com.hitao.codegen.configs.common.AbstractCodeGenConfig.GenerateModel.DELETE;

import com.hitao.codegen.configs.common.InterfaceClassConfig;

/***
 * DAO implements configuration object.
 * 
 * @author zhangjun.ht
 * @created 2010-12-3
 * @version $Id: DaoConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public class DaoConfig extends InterfaceClassConfig {

	private static final long serialVersionUID = -1099032213191107188L;

	public static final String MAIN_TAG = "dao";

	@Override
	public void setGenerateModel(GenerateModel generateModel) {
		if (DELETE == generateModel) {
			return;
		} else {
			super.setGenerateModel(generateModel);
		}
	}
}
