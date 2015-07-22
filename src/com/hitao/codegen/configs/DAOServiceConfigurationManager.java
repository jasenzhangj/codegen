package com.hitao.codegen.configs;

import static com.hitao.codegen.util.StringUtils.EMPTY;

import java.util.Map;

import com.hitao.codegen.configs.systemconfig.ISystemConfigManager;
import com.hitao.codegen.configs.systemconfig.SystemConfigManager;
import com.hitao.codegen.util.StringUtils;

/***
 * A utility class responsible for accessing a wide variety of configurable
 * application parameters (DAO service layer) and returning their runtime values
 * to the requesting object.
 * 
 * @author zhangjun.ht
 * @created 2011-1-12
 * @version $Id: DaoServiceConfigurationManager.java 57 2011-04-29 02:16:16Z guest $
 */
public class DAOServiceConfigurationManager {

	private static ISystemConfigManager MANAGER = SystemConfigManager
			.getInstance();
	
	public static final String DAO_KEY = "dao";
	public static final String MANAGER_KEY = "manager";
	public static final String METHOD_KEY = "method";
	public static final String LOG_KEY = "logger";
	public static final String METHOD_COMMENTS = "methodComment";
	public static final String DEFAULT_RETURN_VALUE = "defaultReturnValue";
	public static final String DB_ROUTE = "dbroute";
	public static final String METHOD_PARMETER = "param";
	public static final String EXCEPTION_NAME = "exceptionName";
	public static final String SQL_NAME = "sqlName";

	public static String getProperty(String argKey) {
		return getProperty(argKey, EMPTY);
	}

	public static String getProperty(String argKey, String argDefaultValue) {
		String value = getProperty(argKey, (Map<String, String>) null);
		if (StringUtils.isEmpty(argKey)) {
			return argDefaultValue;
		}
		return value;
	}

	public static String getProperty(String argKey,
			Map<String, String> argParameterMap) {
		String value = MANAGER.getProperty(argKey);
		if (argParameterMap != null && !argParameterMap.isEmpty()
				&& !StringUtils.isEmpty(value)) {
			value = StringUtils.replaceVariables(value, argParameterMap);
		}
		return value;
	}

	public static boolean getBooleanProperty(String argKey) {
		return Boolean.valueOf(getProperty(argKey));
	}
}
