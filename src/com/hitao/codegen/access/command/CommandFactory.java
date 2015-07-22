package com.hitao.codegen.access.command;

import java.util.HashMap;
import java.util.Map;

import com.hitao.codegen.access.IDaoGenStrategy;
import com.hitao.codegen.access.command.impl.DefaultCommand;
import com.hitao.codegen.access.command.impl.DeleteCommand;

/***
 * The factory of the commands.
 * 
 * @author zhangjun.ht
 * @created 2011-8-9 ÉÏÎç10:23:58
 * @version $Id$
 */
public class CommandFactory {

	private static final Map<String, ICommand> commandMap = new HashMap<String, ICommand>();

	private static final String DELETE_COMMAND = "delete";
	private static final String GENERATE_COMMAND = "generate";

	static {
		commandMap.put(DELETE_COMMAND, null);
		commandMap.put(GENERATE_COMMAND, null);

		// this represents the default command.
		commandMap.put(null, null);
	}

	private CommandFactory() {

	}

	/***
	 * Get Command instance by command name and the genernattion strategy.
	 * 
	 * @param argCommand
	 *            command name
	 * @param argDaoGenStrategy
	 *            genernattion strategy
	 * @return Command instance. <code>ICommand</code>
	 */
	public static ICommand getCommand(String argCommand,
			IDaoGenStrategy argDaoGenStrategy) {
		if (!containCommand(argCommand)) {
			return null;
		}

		argCommand = getCommand(argCommand);
		ICommand command = null;
		if (DELETE_COMMAND.equals(argCommand)) {
			command = new DeleteCommand(argDaoGenStrategy);
		} else if (GENERATE_COMMAND.equals(argCommand) || argCommand == null) {
			command = new DefaultCommand(argDaoGenStrategy);
		}

		return command;
	}

	/***
	 * Check whether this factory contains the specific command.
	 * 
	 * @param argCommand
	 *            command string.
	 * @return true if contains, or else false.
	 */
	public static boolean containCommand(String argCommand) {
		return commandMap.containsKey(getCommand(argCommand));
	}

	/***
	 * Get the command in specific format.
	 * 
	 * @param argCommand
	 *            orginal command string.
	 * @return formatted command string.
	 */
	private static String getCommand(String argCommand) {
		return argCommand != null ? argCommand.toLowerCase() : argCommand;
	}
}
