package com.hitao.codegen.configs.exception;

/***
 * When set improper attribute, it will throw this exception.
 * 
 * @author zhangjun.ht
 * @created 2011-1-10
 * @version $Id: ImproperAttributeException.java 12 2011-02-20 10:50:23Z guest $
 */
public class ImproperAttributeException extends CodeGenException {

	private static final long serialVersionUID = 2300373130306863851L;

	public ImproperAttributeException() {
		super();
	}

	public ImproperAttributeException(String message) {
		super(message);
	}

	public ImproperAttributeException(String message, Throwable cause) {
		super(message, cause);
	}

}
