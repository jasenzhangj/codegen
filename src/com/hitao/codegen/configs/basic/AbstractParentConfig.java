package com.hitao.codegen.configs.basic;

/**
 * Abstract ancestor of configurations that contain other configuration objects.<br>
 * 
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: AbstractParentConfig.java 44 2011-03-08 08:55:34Z guest $
 */
public abstract class AbstractParentConfig extends AbstractConfig implements
		IParentConfig {

	private static final long serialVersionUID = 1L;

	// parent configuration
	private IConfigObject parent_ = null;

	@Override
	public void setParent(IConfigObject argParent) {
		parent_ = argParent;
	}

	@Override
	public IConfigObject getParent() {
		return parent_;
	}

	@Override
	public void setValue(String argValue) {

	}

	@Override
	public Object clone() throws CloneNotSupportedException {
//		IParentConfig parentConfig = (IParentConfig) super.clone();
//
//		if (parent_ != null) {
//			parentConfig.setParent((IConfigObject) parent_.clone());
//		}
		return super.clone();
	}
}
