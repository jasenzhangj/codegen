package com.hitao.codegen.configs.basic;

/**
 * IParentConfig is an interface created to encapsulate the functionality added
 * in the {@link com.hitao.codegen.util.config.AbstractParentConfig}<br>
 * 
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: IParentConfig.java 58 2011-05-06 06:40:39Z guest $
 */
public interface IParentConfig extends IConfigObject {

	/***
	 * set parent config object.
	 * 
	 * @param argParent
	 */
	void setParent(IConfigObject argParent);

	/***
	 * get parent config object.
	 * 
	 * @return
	 */
	IConfigObject getParent();

}
