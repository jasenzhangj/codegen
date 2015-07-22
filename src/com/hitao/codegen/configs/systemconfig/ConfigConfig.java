package com.hitao.codegen.configs.systemconfig;

import com.hitao.codegen.configs.basic.AbstractParentConfig;
import com.hitao.codegen.configs.basic.IConfigObject;
import com.hitao.codegen.util.StringUtils;

/***
 * The configuration object in SystemConfig.xml
 * 
 * @author zhangjun.ht
 * @created 2011-1-12
 * @version $Id: ConfigConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public class ConfigConfig extends AbstractParentConfig {

	private static final long serialVersionUID = -1612151772061116063L;

	public static final String MAIN_TAG = "config";

	private static final String NAME = "name";
	private static final String VALUE = "value";

	private String name_ = null;
	private String value_ = null;

	@Override
	public void setConfigObject(String argKey, IConfigObject argValue) {
		if (NAME.equalsIgnoreCase(argKey)) {
			setName(argValue.toString());
		} else if (VALUE.equalsIgnoreCase(argKey)) {
			setValue(argValue.toString());
		}
	}

	public String getName() {
		return name_;
	}

	public void setName(String name) {
		this.name_ = name;
	}

	public String getValue() {
		return value_;
	}

	public void setValue(String value) {
		if (!StringUtils.isEmpty(value.trim())) {
			this.value_ = value;
		}
	}
}
