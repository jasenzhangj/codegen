//$Id: XmlDAOGenConsole.java 58 2011-05-06 06:40:39Z guest $
package com.hitao.codegen.access;

import com.hitao.codegen.access.impl.AnnotationDaoGen;
import com.hitao.codegen.access.impl.DaoGen;

/***
 * Generate codes with *.xml in console.
 * 
 * this version don't support genernating codes by xml any more. Please use the
 * <code>AnnotationDaoGenByProjectNameConsole</code> as the entrance.
 * 
 * @author zhangjun.ht
 * @created 2010-12-1
 * @version $Id: XmlDAOGenConsole.java 58 2011-05-06 06:40:39Z guest $
 */
@Deprecated
public class XmlDAOGenConsole {
	public static void main(String[] args) throws Exception {

		String bizProjectName = null;
		String dalProjectName = null;
		String webProjectName = null;

		for (int i = 0; i < args.length; i++) {
			if ("-biz".equals(args[i]) && (++i < args.length)) {
				bizProjectName = args[i];
			} else if ("-dal".equals(args[i]) && (++i < args.length)) {
				dalProjectName = args[i];
			} else if ("-web".equals(args[i]) && (++i < args.length)) {
				webProjectName = args[i];
			} else {
				usageError();
			}
		}
		
		DaoGen main = new AnnotationDaoGen(bizProjectName, dalProjectName, webProjectName);
		main.call();
		System.exit(0);
	}

	private static void usageError() {
		System.err
				.println("Usage: DAOGen [-models] [-dir <source directory>] [-dest <source directory>]");
		System.err
				.println("\t-biz     project directory of biz.");
		System.err
				.println("\t-dal     project directory of dal.");
		System.err
				.println("\t-web     project directory of web\n");
		throw new RuntimeException("Invalid command line parameters.");
	}

}
