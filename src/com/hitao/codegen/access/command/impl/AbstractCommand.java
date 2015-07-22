package com.hitao.codegen.access.command.impl;

import org.apache.log4j.Logger;

import com.hitao.codegen.access.ICodeGenerate;
import com.hitao.codegen.access.IDaoGenStrategy;
import com.hitao.codegen.access.command.ICommand;
import com.hitao.codegen.access.impl.CodeGenerate;

/***
 * Abstract commond class.
 *
 * @author zhangjun.ht
 * @created 2011-8-8 ÏÂÎç07:25:49
 * @version $Id$
 */
public abstract class AbstractCommand implements ICommand {

	protected Logger logger_ = Logger.getLogger(this.getClass());
	
	private ICodeGenerate codeGenerate = null;
	private IDaoGenStrategy daoGenStrategy = null;
	
	public AbstractCommand(IDaoGenStrategy argDaoGenStrategy) {
		this.daoGenStrategy = argDaoGenStrategy;
		this.codeGenerate = new CodeGenerate(argDaoGenStrategy);
	}
	
	/***
	 * Do something before invoke the command.
	 *
	 * @param argDaoGenStrategy DAO genernation strategy.
	 */
	protected void beforeInvoke(ICodeGenerate codeGenerate, IDaoGenStrategy argDaoGenStrategy) {
		
	}
	
	@Override
	public void invoke() throws Exception{
		beforeInvoke(codeGenerate, daoGenStrategy);
		codeGenerate.call();
		afterInvoke(codeGenerate, daoGenStrategy);
	}
	
	/***
	 * Do something before invoke the command.
	 *
	 * @param argDaoGenStrategy DAO genernation strategy.
	 */
	protected void afterInvoke(ICodeGenerate argCodeGenerate, IDaoGenStrategy argDaoGenStrategy) {
		
	}
}
