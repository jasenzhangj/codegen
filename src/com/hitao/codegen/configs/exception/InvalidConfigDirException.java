package com.hitao.codegen.configs.exception;

/***
 * When load the invalid configuration directory.
 * 
 * @author zhangjun.ht
 * @created 2011-1-11
 * @version $Id: InvalidConfigDirException.java 12 2011-02-20 10:50:23Z guest $
 */
public class InvalidConfigDirException extends CodeGenException {
	private static final long serialVersionUID = -7618406200110680693L;

	public InvalidConfigDirException() {
		super();
	}

	public InvalidConfigDirException(String message) {
		super(message);
	}

	public InvalidConfigDirException(String message, Throwable cause) {
		super(message, cause);
	}
}
