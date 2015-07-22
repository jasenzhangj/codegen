package com.hitao.codegen.access.command;

import com.hitao.codegen.access.IDaoGenStrategy;

/***
 * The holder for the user enters command.
 *
 * @author zhangjun.ht
 * @created 2011-8-10 ÏÂÎç01:59:20
 * @version $Id$
 */
public class CommandHolder {

	public static String command = null;
	
	/***
	 * Set the command.
	 *
	 * @param argCommand user enters command.
	 */
	public static void setCommand(String argCommand) {
		CommandHolder.command = argCommand;
	}
	
	/***
	 * Assembled the command with the specific DAO genernataion strtegy. 
	 *
	 * @param argDaoGenStrategy DAO genernataion strtegy.
	 * @return specific command object. <code>ICommand</code>
	 */
	public static ICommand getCommand(IDaoGenStrategy argDaoGenStrategy) {
		return CommandFactory.getCommand(CommandHolder.command, argDaoGenStrategy);
	}
	                                   
}
