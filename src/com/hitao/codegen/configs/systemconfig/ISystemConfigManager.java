package com.hitao.codegen.configs.systemconfig;

/***
 * The interface for the system property manager.
 * 
 * @author zhangjun.ht
 * @created 2011-1-12
 * @version $Id: ISystemConfigManager.java 12 2011-02-20 10:50:23Z guest $
 */
public interface ISystemConfigManager {
	String getProperty(String argKey);
}
