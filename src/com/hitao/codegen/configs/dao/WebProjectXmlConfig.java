package com.hitao.codegen.configs.dao;

import static com.hitao.codegen.configs.common.AbstractCodeGenConfig.GenerateModel.DELETE;
import static com.hitao.codegen.configs.common.AbstractCodeGenConfig.GenerateModel.REDUCE;

import com.hitao.codegen.configs.common.AbstractCodeGenConfig;
import com.hitao.codegen.util.StringUtils;

/***
 * This configuration is represents the configuration files in web project in
 * webx framework.
 * 
 * @author zhangjun.ht
 * @created 2011-2-24
 * @version $Id: WebProjectXmlConfig.java 25 2011-02-24 08:40:37Z guest $
 */
public abstract class WebProjectXmlConfig extends AbstractCodeGenConfig{

	private static final long serialVersionUID = 6292539350355189077L;

	@Override
	public String getFileType() {
		return ".xml";
	}

	@Override
	protected String getStatement() {
		return getValue();
	}
	
	@Override
	public String getName() {
		String name = super.getName();
		if (!StringUtils.isEmpty(name) && name.indexOf(getFileType()) > 0) {
			name = name.substring(0, name.indexOf(getFileType()));
		}
		return name;
	}
	
	@Override
	public void setGenerateModel(GenerateModel generateModel) {
		if (DELETE == generateModel) {
			super.setGenerateModel(REDUCE);
		} else {
			super.setGenerateModel(generateModel);
		}
	}
}
