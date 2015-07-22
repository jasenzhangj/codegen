package com.hitao.codegen.configs.systemconfig;

import com.hitao.codegen.configs.basic.AbstractParentConfig;
import com.hitao.codegen.configs.basic.IConfigObject;

/**
 * The data source configuration in SystemConfig.xml
 * 
 * @author zhangjun.ht
 * @created 2011-1-13
 * @version $Id: DataSourceConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public class DataSourceConfig extends AbstractParentConfig {

	private static final long serialVersionUID = 2654410336572699922L;

	public static final String MAIN_TAG = "datasource";

	private static final String CONNECTION_URL = "connection-url";
	private static final String DRIVER_CLASS = "driver-class";
	private static final String USER_NAME = "user-name";
	private static final String PASSWORD = "password";
	private static final String MIN_POOL_SIZE = "min-pool-size";
	private static final String MAX_POOL_SIZE = "max-pool-size";
	private static final String METADATA = "metadata";

	private String connectionUrl_ = null;
	private String driverClass_ = null;
	private String userName_ = null;
	private String password_ = null;
	private String minPoolSize_ = null;
	private String maxPoolSize_ = null;
	private String metadata_ = null;

	@Override
	public void setConfigObject(String argKey, IConfigObject argValue) {
		if (CONNECTION_URL.equalsIgnoreCase(argKey)) {
			setConnectionUrl(argValue.toString());
		} else if (DRIVER_CLASS.equalsIgnoreCase(argKey)) {
			setDriverClass(argValue.toString());
		} else if (USER_NAME.equalsIgnoreCase(argKey)) {
			setUserName(argValue.toString());
		} else if (PASSWORD.equalsIgnoreCase(argKey)) {
			setPassword(argValue.toString());
		} else if (MIN_POOL_SIZE.equalsIgnoreCase(argKey)) {
			setMinPoolSize(argValue.toString());
		} else if (MAX_POOL_SIZE.equalsIgnoreCase(argKey)) {
			setMaxPoolSize(argValue.toString());
		} else if (METADATA.equalsIgnoreCase(argKey)) {
			setMetadata(argValue.toString());
		}
	}

	public String getConnectionUrl() {
		return connectionUrl_;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl_ = connectionUrl;
	}

	public String getDriverClass() {
		return driverClass_;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass_ = driverClass;
	}

	public String getUserName() {
		return userName_;
	}

	public void setUserName(String userName) {
		this.userName_ = userName;
	}

	public String getPassword() {
		return password_;
	}

	public void setPassword(String password) {
		this.password_ = password;
	}

	public String getMinPoolSize() {
		return minPoolSize_;
	}

	public void setMinPoolSize(String minPoolSize) {
		this.minPoolSize_ = minPoolSize;
	}

	public String getMaxPoolSize() {
		return maxPoolSize_;
	}

	public void setMaxPoolSize(String maxPoolSize) {
		this.maxPoolSize_ = maxPoolSize;
	}

	public String getMetadata() {
		return metadata_;
	}

	public void setMetadata(String metadata) {
		this.metadata_ = metadata;
	}

}
