package com.hitao.codegen.access;

import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.exception.CodeGenException;

/***
 * This interface represents how to generate codes. Maybe append, generate a new
 * one, or override.
 * 
 * @author zhangjun.ht
 * @created 2011-2-23
 * @version $Id: ICodeGenerateStrategy.java 35 2011-03-02 09:19:57Z guest $
 */
public interface ICodeGenerateStrategy {

	/***
	 * Generate the codes according to the code configuration.
	 * 
	 * @param argCodeGenConfig
	 */
	void codeGenerate(ICodeGenConfig argCodeGenConfig) throws CodeGenException;
	
	
	/***
	 * Generate the codes according to the code configuration and output directory.
	 * 
	 * @param argCodeGenConfig
	 */
	void codeGenerate(ICodeGenConfig argCodeGenConfig, String argOutputDir) throws CodeGenException;
}
