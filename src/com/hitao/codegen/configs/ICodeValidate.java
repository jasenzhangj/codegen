package com.hitao.codegen.configs;

import com.hitao.codegen.configs.exception.CodeGenException;

/***
 * validate some rules of the code generation.
 *
 * @author zhangjun.ht
 * @created 2010-11-30
 * @version $Id: ICodeValidate.java 12 2011-02-20 10:50:23Z guest $
 */
public interface ICodeValidate {

	void validate() throws CodeGenException;

}
