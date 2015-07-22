package com.hitao.codegen.configs.dao;

import java.util.List;

import com.hitao.codegen.configs.append.AppendCodesInfo;
import com.hitao.codegen.configs.append.IAppendCodesInfo;

/***
 * This configuration is represents sql-map.xml configuration file in web project in
 * webx framework.
 *
 * @author  zhangjun.ht
 * @created 2011-2-24
 * @version $Id: SqlMapConfigXmlConfig.java 45 2011-03-09 08:47:45Z guest $
 */
public class SqlMapConfigXmlConfig extends WebProjectXmlConfig {

	private static final long serialVersionUID = 6011582559923472630L;

	@Override
	public String getEndElement() {
		return "</sqlMapConfig>";
	}

	@Override
	public List<IAppendCodesInfo> getAppendCodesInfo(
			List<IAppendCodesInfo> argList) {
		IAppendCodesInfo info = new AppendCodesInfo(this, getCodes().toString());
		addAppendCodeInfo(argList, info);
		return argList;
	}
	
	@Override
	public boolean shouldBeOverride() {
		return false;
	}
}
