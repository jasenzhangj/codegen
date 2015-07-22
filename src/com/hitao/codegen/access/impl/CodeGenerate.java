package com.hitao.codegen.access.impl;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.hitao.codegen.access.ICodeGenerate;
import com.hitao.codegen.access.ICodeGenerateStrategy;
import com.hitao.codegen.access.IDaoGenStrategy;
import com.hitao.codegen.configs.IClassCodeGenConfig;
import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.dao.IDAOConfigs;
import com.hitao.codegen.configs.exception.CodeGenException;

/***
 * This base class is to generate file.
 * 
 * @author zhangjun.ht
 * @created 2010-11-30
 * @version $Id: CodeGenerate.java 58 2011-05-06 06:40:39Z guest $
 */
public class CodeGenerate implements ICodeGenerate {
	private Logger logger_ = Logger.getLogger(CodeGenerate.class);

	private IDaoGenStrategy daoGenStrategy_ = null;
	private CodeGenerateStrategyFactory FACTORY = CodeGenerateStrategyFactory.getInstance();
	
	//private static volatile Map<String, ICodeGenConfig> isGeningMap_ = new HashMap<String, ICodeGenConfig>();

	public CodeGenerate(IDaoGenStrategy argDaoGenStrategy) {
		this.daoGenStrategy_ = argDaoGenStrategy;
	}
	
	@Override
	public Void call() {
		if (daoGenStrategy_ != null) {
			Collection<IDAOConfigs> daoConfigs;
			try {
				daoConfigs = daoGenStrategy_.getDaoConfigs();

				if (daoConfigs != null && !daoConfigs.isEmpty()) {
					for (IDAOConfigs daogen : daoConfigs) {
						for (ICodeGenConfig config : daogen.getConfigs()) {
							codeGenerate(config);
						}
					}
				} else {
					logger_.info("There are no config files in the "
							+ daoGenStrategy_.getDaoStringInDir()
							+ " directory with "
							+ System.getProperty("codegen.configuration.file.suffix")
							+ " suffix or no DAO annotations.");
				}
			} catch (CodeGenException e) {
				logger_.error("It make an error when get DAO configurations", e);
			}
		}
		return null;
	}

	/***
	 * You can do something before generate the file.
	 * 
	 * @param argCodeGenConfig
	 *            class file configuration
	 * @throws CodeGenException
	 *             exception for generating class file.
	 */
	protected void beforeCodeGenerate(ICodeGenConfig argCodeGenConfig)
			throws CodeGenException {
		// String path = getFilePath(argCodeGenConfig);

		// logger_.info("Start generate the codegenConfig: " + path);

		//isGeningMap_.put(path, argCodeGenConfig);
	}

	@Override
	public void codeGenerate(ICodeGenConfig argCodeGenConfig)
			throws CodeGenException {
		if (argCodeGenConfig == null || argCodeGenConfig.isDirty()) {
			return;
		}

		beforeCodeGenerate(argCodeGenConfig);
		
		getCodeGenerateStrategy(argCodeGenConfig).codeGenerate(argCodeGenConfig);
		
		afterCodeGenerate(argCodeGenConfig);
	}
	
	/***
	 * Get the codes generate strategy.
	 * @param argCodeGenConfig
	 * @return the codes generate strategy
	 */
	protected ICodeGenerateStrategy getCodeGenerateStrategy(ICodeGenConfig argCodeGenConfig) {
		return FACTORY.getGenerateCodeStrategy(argCodeGenConfig);
	}

	/***
	 * You can do something after generate the file.
	 * 
	 * @param argCodegenConfig
	 *            class file configuration
	 */
	protected void afterCodeGenerate(ICodeGenConfig argCodegenConfig) {
		argCodegenConfig.setDirty();
		// logger_.info("End generate the codegenConfig: "
		// + argCodegenConfig.getPackagePath()+ "." + argCodegenConfig.getName()
		// + argCodegenConfig.getFileType());
		// logger_.info("********************************************************************************");
		
		//isGeningMap_.remove(argCodegenConfig.getPackagePath()+ argCodegenConfig.getName());
	}


	/***
	 * Get the path of the configuration.
	 * 
	 * @param argCodegenConfig
	 * @return he path of the configuration
	 */
	protected String getFilePath(ICodeGenConfig argCodegenConfig) {
		String path = null;
		if (argCodegenConfig instanceof IClassCodeGenConfig) {
			path = ((IClassCodeGenConfig) argCodegenConfig).getClassName();
		} else {
			path = argCodegenConfig.getPackagePath() + "." 
					+ argCodegenConfig.getName() + argCodegenConfig.getFileType();
		}
		return path;
	}

	protected String getLoginfo(ICodeGenConfig argCodegenConfig, String filePath) {
		return "Genernate the file: " + filePath;
	}

}
