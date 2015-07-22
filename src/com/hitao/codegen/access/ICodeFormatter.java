package com.hitao.codegen.access;

import java.io.File;

/***
 * This interface represents how to format the java source codes.
 * 
 * @author zhangjun.ht
 * @created 2010-11-30
 * @version $Id: ICodeFormatter.java 12 2011-02-20 10:50:23Z guest $
 */
public interface ICodeFormatter {

	/***
	 * format the java codes and build a new *.java file.
	 * 
	 * @param argCode
	 *            the java source codes
	 * @param argFile
	 *            the java source file
	 */
	void codeFormate(String argCode, File argFile);

}
