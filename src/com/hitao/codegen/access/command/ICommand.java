package com.hitao.codegen.access.command;

/***
 * The command to be invoke to genernate the codes.
 *
 * @author zhangjun.ht
 * @created 2011-8-8 обнГ07:24:44
 * @version $Id$
 */
public interface ICommand {
	
	/***
	 * default execution method.
	 *
	 * @throws Exception unknow exception.
	 */
	void invoke() throws Exception;
}
