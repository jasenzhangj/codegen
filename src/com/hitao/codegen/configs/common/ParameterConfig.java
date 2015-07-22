package com.hitao.codegen.configs.common;

import com.hitao.codegen.configs.CodeGenUtils;
import com.hitao.codegen.util.StringUtils;

/**
 * The configuration for argument of method.
 *
 * @author zhangjun.ht
 * @created 2010-11-26
 * @version $Id: ParameterConfig.java 44 2011-03-08 08:55:34Z guest $
 */
public class ParameterConfig extends ClassElementConfig {

	private static final long serialVersionUID = 4496242062752087973L;

	public static final String MAIN_TAG = "parameter";

	@Override
	public int hashCode() {
		return getClassName().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ParameterConfig)) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		ParameterConfig param1 = (ParameterConfig) this;
		ParameterConfig param2 = (ParameterConfig) obj;

		return StringUtils.equals(param1.getClassName(), param2.getClassName());
	}

	@Override
	public String getStatement() {
		StringBuffer statement = new StringBuffer();
		
		// Generate the codes like below:
		// final List<String> argName
		if (!StringUtils.isEmpty(getModify())) {
			statement.append(getModify() + " ");
		}
		String shortClassName = getShortClassName(getClassName());

		if (containsConcreteClass()){
			shortClassName = shortClassName+getConreteClassStatement();
		}
		statement.append(shortClassName);

		String paramName = getName();
		if (StringUtils.isEmpty(paramName)) {
			paramName = StringUtils.uncapitalize(shortClassName);
		}
		statement.append(" " + paramName);
		return statement.toString();
	}
	
	@Override
	public String getName() {
		return CodeGenUtils.getParameterName(super.getName());
	}
}
