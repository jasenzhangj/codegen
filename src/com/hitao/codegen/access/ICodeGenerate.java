package com.hitao.codegen.access;

import java.util.concurrent.Callable;

import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.exception.CodeGenException;

/***
 * Generate the codes according to the code configuration.
 * 
 * @author zhangjun.ht
 * @created 2010-12-1
 * @version $Id: ICodeGenerate.java 12 2011-02-20 10:50:23Z guest $
 */
public interface ICodeGenerate extends Callable<Void> {

	/***
	 * Generate the codes according to the code configuration.
	 * 
	 * @param argCodeGenConfig
	 */
	void codeGenerate(ICodeGenConfig argCodeGenConfig)
			throws CodeGenException;
}
