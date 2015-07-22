package com.hitao.codegen.configs.exception;

/***
 * The root exception of code generation framework.
 * 
 * @author zhangjun.ht
 * @created 2011-1-10
 * @version $Id: CodeGenException.java 12 2011-02-20 10:50:23Z guest $
 */
public class CodeGenException extends Exception {

	private static final long serialVersionUID = 5355914399935405372L;

	public CodeGenException() {
		super();
	}

	public CodeGenException(String message) {
		super(message);
	}

	public CodeGenException(String message, Throwable cause) {
		super(message, cause);
	}

}
