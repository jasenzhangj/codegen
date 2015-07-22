package com.hitao.codegen.configs.dao.mapping;

import java.sql.Connection;

/***
 * The interface for DataSource manager.
 * 
 * @author zhangjun.ht
 * @created 2011-1-13
 * @version $Id: IDataSourceManager.java 12 2011-02-20 10:50:23Z guest $
 */
public interface IDataSourceManager {

	/***
	 * Get connection.
	 * 
	 * @return DB connection.
	 */
	Connection getConnection();
}
