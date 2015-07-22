package com.hitao.codegen.access.impl;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

/***
 * Generate codes by the annotation in *.java files.
 * 
 * @author zhangjun.ht
 * @created 2011-1-14
 * @version $Id: AnnotationDaoGen.java 58 2011-05-06 06:40:39Z guest $
 */
public class AnnotationDaoGen extends DaoGen implements Callable<Void> {

	public AnnotationDaoGen(String argBizProjectName, String argDalProjectName,
			String argWebProjectName) throws IOException {
		super(argBizProjectName, argDalProjectName, argWebProjectName);
	}

	public AnnotationDaoGen(File argBizProjectDir, File argDalProjectDir,
			File argWebProjectDir) throws IOException {
		super(argBizProjectDir, argDalProjectDir, argWebProjectDir);
	}
}
