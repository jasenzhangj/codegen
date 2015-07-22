package com.hitao.codegen.configs.systemconfig;

import java.util.Collection;

import com.hitao.codegen.configs.basic.ConfigHelper;
import com.hitao.codegen.configs.basic.IConfigObject;

/***
 * The configuration helper class for the SystemConfig.xml
 *
 * @author  zhangjun.ht
 * @created 2011-2-18
 * @version $Id: SystemConfigHelper.java 12 2011-02-20 10:50:23Z guest $
 */
public class SystemConfigHelper extends ConfigHelper<ConifgsConfigSet> {

	@Override
	protected String getConfigFileName() {
		return "SystemConfig";
	}

	@Override
	protected IConfigObject getConfigObject(String argTagName, String argDtype,
			String argSourceDescription) {
		if (ConifgsConfigSet.MAIN_TAG.equalsIgnoreCase(argTagName)){
			return new ConifgsConfigSet();
		}else if (ConfigConfig.MAIN_TAG.equalsIgnoreCase(argTagName)){
			return new ConfigConfig();
		}else {
			return super.getConfigObject(argTagName, argDtype, argSourceDescription);
		}
	}

	public Collection<ConfigConfig> getConfigs(){
		return getRootChildren(ConifgsConfigSet.class);
	}

}
