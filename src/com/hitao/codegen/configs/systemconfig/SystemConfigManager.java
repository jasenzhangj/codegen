package com.hitao.codegen.configs.systemconfig;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/***
 * The utility class for get configuration value from SystemConfig.xml with key.
 *
 * @author zhangjun.ht
 * @created 2011-1-12
 * @version $Id: SystemConfigManager.java 12 2011-02-20 10:50:23Z guest $
 */
public class SystemConfigManager implements ISystemConfigManager{
	private static final Logger logger_ = Logger
			.getLogger(SystemConfigManager.class);
	private static final String SYSTEM_CONFIG_MANAGER_PROPERTY = ISystemConfigManager.class
			.getName();

	private static SystemConfigManager INSTANCE_;
	private Map<String, String> configMap_ = new HashMap<String, String>();

	static {
		String value = System.getProperty(SYSTEM_CONFIG_MANAGER_PROPERTY);
		try {
			INSTANCE_ = (SystemConfigManager)Class.forName(value).newInstance();
		} catch (Exception e) {
			INSTANCE_ = new SystemConfigManager();
		}
	}

	public static SystemConfigManager getInstance(){
		return INSTANCE_;
	}


	private SystemConfigManager(){
		SystemConfigHelper config = new SystemConfigHelper();
		config.initialize();

		loadMap(config.getConfigs());
	}

	protected void loadMap(Collection<ConfigConfig> argConfigs){
		if (argConfigs != null){
			for (ConfigConfig config : argConfigs){
				configMap_.put(config.getName(), config.getValue());
			}
		}else {
			logger_.info("the SystemConfig has not any configs");
		}
	}

	@Override
	public String getProperty(String argKey) {
		return configMap_.get(argKey);
	}
}
