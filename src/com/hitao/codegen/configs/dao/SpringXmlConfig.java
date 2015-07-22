package com.hitao.codegen.configs.dao;

import java.util.List;

import com.hitao.codegen.configs.append.AppendCodesInfo;
import com.hitao.codegen.configs.append.IAppendCodesInfo;


public class SpringXmlConfig extends WebProjectXmlConfig{

	private static final long serialVersionUID = 6292539350355189077L;

	@Override
	public String getEndElement() {
		return "</beans>";
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
