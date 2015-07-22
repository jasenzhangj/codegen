package com.hitao.codegen.access.impl;

import java.util.HashMap;
import java.util.Map;

import com.hitao.codegen.access.ICodeGenerateStrategy;
import com.hitao.codegen.configs.ICodeGenConfig;

/***
 * The factory class to build strategy of the generate codes.
 * 
 * @author zhangjun.ht
 * @created 2011-3-6
 * @version $Id$
 */
public class CodeGenerateStrategyFactory {

	private static CodeGenerateStrategyFactory INSTANCE_ = null;

	private static Map<String, ICodeGenerateStrategy> generateModelMap_ = new HashMap<String, ICodeGenerateStrategy>();

	static {
		String value = System.getProperty(CodeFormatterFactory.class.getName());
		try {
			INSTANCE_ = (CodeGenerateStrategyFactory) Class.forName(value)
					.newInstance();
		} catch (Exception e) {
			INSTANCE_ = new CodeGenerateStrategyFactory();
		}
	}

	private CodeGenerateStrategyFactory() {

	}

	/***
	 * get CodeGenerateStrategyFactory instance.
	 * 
	 * @return CodeGenerateStrategyFactory instance
	 */
	public static CodeGenerateStrategyFactory getInstance() {
		return INSTANCE_;
	}

	/***
	 * get strategy of the generate codes.
	 * 
	 * @param argCodeGenConfig
	 * @return the code genernate strategy
	 */
	public ICodeGenerateStrategy getGenerateCodeStrategy(
			ICodeGenConfig argCodeGenConfig) {
		String generateModelName = argCodeGenConfig.getGenerateModel().name();
		if (generateModelMap_.containsKey(generateModelName)) {
			return generateModelMap_.get(generateModelName);
		}

		ICodeGenerateStrategy strategy = null;
		if (argCodeGenConfig.shouldBeAppend()) {
			strategy = new CodeAppendGenerateStrategy();
		} else if (argCodeGenConfig.shouldBeDelete()) {
			strategy = new CodeDeleteGenerateStrategy();
		} else if (argCodeGenConfig.shouldBeReduce()) {
			strategy = new CodeReduceGenerateStrategy();
		} else {
			strategy = new DefaultCodeGenerateStrategy();
		}
		generateModelMap_.put(generateModelName, strategy);
		return strategy;
	}
}
