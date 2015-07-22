package com.hitao.codegen.access;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import com.hitao.codegen.access.command.CommandFactory;
import com.hitao.codegen.access.command.CommandHolder;
import com.hitao.codegen.access.impl.AnnotationDaoGen;
import com.hitao.codegen.access.impl.DaoGen;
import com.hitao.codegen.util.ClassPathUtils;

/***
 * The entrance for the Code genernattion.
 * 
 * @author zhangjun.ht
 * @created 2011-7-19 ÏÂÎç05:36:56
 * @version $Id$
 */
public class AnnotationDaoGenByProjectNameConsole {

	static {

		// Since the logger is not getting configured, I guess we had better do
		// it ourselves.
		// BasicConfigurator.configure();
		new PropertyConfigurator().doConfigure(
				ClassPathUtils.getResource("configs/log4j.properties"),
				LogManager.getLoggerRepository());
	}
	
	public static void main(String[] args) throws Exception {
		String bizProjectName = null;
		String dalProjectName = null;
		String webProjectName = null;
		String command = null;
		
		for (int i = 0; i < args.length; i++) {
			if ("-biz".equals(args[i]) && (++i < args.length)) {
				bizProjectName = args[i];
			} else if ("-dal".equals(args[i]) && (++i < args.length)) {
				dalProjectName = args[i];
			} else if ("-web".equals(args[i]) && (++i < args.length)) {
				webProjectName = args[i];
			} else if ("-cmd".equals(args[i]) && (++i < args.length)) {
				command = args[i];
			} else {
				usageError();
			}
		}
		//command = "delete";
		
		if (!CommandFactory.containCommand(command)) {
			usageError();
		}
		
		CommandHolder.setCommand(command);
		
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
				.println("\t-web     project directory of web.");
		System.err
				.println("\t-cmd   	 command contains: generate, delete.\n");
		throw new RuntimeException("Invalid command line parameters.");
	}

}
