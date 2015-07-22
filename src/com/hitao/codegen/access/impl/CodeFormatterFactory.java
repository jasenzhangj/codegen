package com.hitao.codegen.access.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.hitao.codegen.access.ICodeFormatter;
import com.hitao.codegen.configs.ICodeGenConfig;

/***
 * The factory to get code formatter class.
 * 
 * @author zhangjun.ht
 * @created 2011-2-19
 * @version $Id: CodeFormatterFactory.java 23 2011-02-24 08:39:57Z guest $
 */
public class CodeFormatterFactory {

	private static CodeFormatterFactory INSTANCE_ = null;

	private static Map<String, ICodeFormatter> foramatterMap_ = new HashMap<String, ICodeFormatter>();
	private static String JAVA_TYPE = ".java";
	
	static {
		String value = System.getProperty(CodeFormatterFactory.class.getName());
		try {
			INSTANCE_ = (CodeFormatterFactory) Class.forName(value)
					.newInstance();
		} catch (Exception e) {
			INSTANCE_ = new CodeFormatterFactory();
		}
	}

	private CodeFormatterFactory() {
		
	}

	public static CodeFormatterFactory getInstance() {
		return INSTANCE_;
	}

	public ICodeFormatter getCodeFormatter(ICodeGenConfig argCodeGenConfig,
			File argFile) {
		String fileType = argCodeGenConfig.getFileType();
		if (foramatterMap_.containsKey(fileType)) {
			return foramatterMap_.get(fileType);
		}
		
		ICodeFormatter formatter = null;
		if (JAVA_TYPE.equals(fileType)) {
			formatter = new ClassCodeFormatterImpl();
		} else {
			formatter = new DefaultCodeFormatter();
		}
		foramatterMap_.put(fileType, formatter);
		return formatter;
	}
}
