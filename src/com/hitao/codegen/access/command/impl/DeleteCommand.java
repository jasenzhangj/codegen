package com.hitao.codegen.access.command.impl;

import static com.hitao.codegen.configs.common.AbstractCodeGenConfig.GenerateModel.DELETE;

import java.util.Collection;

import com.hitao.codegen.access.ICodeGenerate;
import com.hitao.codegen.access.IDaoGenStrategy;
import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.dao.IDAOConfigs;
import com.hitao.codegen.configs.exception.CodeGenException;

/***
 * The delete command.
 *
 * @author zhangjun.ht
 * @created 2011-8-8 ÏÂÎç07:33:19
 * @version $Id$
 */
public class DeleteCommand extends AbstractCommand{

	public DeleteCommand(IDaoGenStrategy argDaoGenStrategy) {
		super(argDaoGenStrategy);
	}

	@Override
	protected void beforeInvoke(ICodeGenerate codeGenerate, IDaoGenStrategy argDaoGenStrategy) {
		try {
			Collection<IDAOConfigs> daoConfigs = argDaoGenStrategy.getDaoConfigs();
			for (IDAOConfigs daogen : daoConfigs) {
				for (ICodeGenConfig config : daogen.getConfigs()) {
					config.setGenerateModel(DELETE);
				}
			}
		} catch (CodeGenException e) {
			logger_.error("It make an error when get DAO configurations", e);
		}
	}
}
