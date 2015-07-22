package com.hitao.codegen.configs.exception;

/***
 * If configuration object is invalid, it will throw this exception.
 * 
 * @author zhangjun.ht
 * @created 2011-1-10
 * @version $Id: InvalidConfigException.java 12 2011-02-20 10:50:23Z guest $
 */
public class InvalidConfigException extends CodeGenException {

	private static final long serialVersionUID = -7618406200110680693L;

	public InvalidConfigException() {
		super();
	}

	public InvalidConfigException(String message) {
		super(message);
	}

	public InvalidConfigException(String message, Throwable cause) {
		super(message, cause);
	}

}
