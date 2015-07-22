package com.hitao.codegen.access.impl;

import static com.hitao.codegen.constent.StringConstants.CODEGEN_GENERATE_CODE_PATTERN_OVERRIDE;
import static com.hitao.codegen.util.StringUtils.EMPTY;

import java.io.File;

import org.apache.log4j.Logger;

import com.hitao.codegen.access.ICodeFormatter;
import com.hitao.codegen.access.ICodeGenerateStrategy;
import com.hitao.codegen.access.IDaoGenStrategy;
import com.hitao.codegen.configs.CodeGenUtils;
import com.hitao.codegen.configs.DAOServiceConfigurationManager;
import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.dao.AOConfig;
import com.hitao.codegen.configs.dao.AOImplConfig;
import com.hitao.codegen.configs.dao.DOConfig;
import com.hitao.codegen.configs.dao.DaoConfig;
import com.hitao.codegen.configs.dao.DaoImplConfig;
import com.hitao.codegen.configs.dao.DaoUnitTestConfig;
import com.hitao.codegen.configs.dao.ManagerConfig;
import com.hitao.codegen.configs.dao.ManagerImplConfig;
import com.hitao.codegen.configs.dao.SqlMapXmlConfig;
import com.hitao.codegen.configs.dao.WebProjectXmlConfig;
import com.hitao.codegen.configs.exception.CodeGenException;

/***
 * The abstract strategy to generate codes.
 * 
 * @author zhangjun.ht
 * @created 2011-2-23
 * @version $Id: AbstractCodeGenerateStrategy.java 35 2011-03-02 09:19:57Z guest
 *          $
 */
public abstract class AbstractCodeGenerateStrategy implements
		ICodeGenerateStrategy {

	protected Logger logger_ = Logger
			.getLogger(this.getClass());

	private IDaoGenStrategy daoGenStrategy_ = DaoGenStrategy.getInstance();

	@Override
	public void codeGenerate(ICodeGenConfig argCodeGenConfig)
			throws CodeGenException {
		codeGenerate(argCodeGenConfig, getOutPutDir(argCodeGenConfig));
	}

	@Override
	public void codeGenerate(ICodeGenConfig argCodeGenConfig,
			String argOutputDir) throws CodeGenException {

		String filePath = CodeGenUtils.getFileOutputPath(argOutputDir,
				argCodeGenConfig.getPackagePath(), argCodeGenConfig.getName(),
				argCodeGenConfig.getFileType());
		File generateFile = new File(filePath);

		logger_.info("************************** genernate the file: "
				+ generateFile.getAbsolutePath()
				+ getGenernateTypeInfo(generateFile, argCodeGenConfig));

		// check whether to generate codes.
		if (shouldBeGenerate(generateFile, argCodeGenConfig)) {
			generateCodes(argCodeGenConfig, generateFile);
		}
	}

	/***
	 * Generate file.
	 * 
	 * @param argCodeGenConfig
	 * @param argFile
	 * @param argCodes
	 */
	protected abstract void generateCodes(ICodeGenConfig argCodeGenConfig,
			File argFile);

	/***
	 * Get output directory for the configuration.
	 * 
	 * @param argCodeGenConfig
	 * @return get code output directiory.
	 */
	protected String getOutPutDir(ICodeGenConfig argCodeGenConfig) {
		String dir = null;
		if (argCodeGenConfig instanceof DaoConfig
				|| argCodeGenConfig instanceof DaoImplConfig) {
			dir = daoGenStrategy_.getDaoStringInDir();
		} else if (argCodeGenConfig instanceof ManagerImplConfig
				|| argCodeGenConfig instanceof ManagerConfig) {
			dir = daoGenStrategy_.getManagerStringOutDir();
		} else if (argCodeGenConfig instanceof AOImplConfig
				|| argCodeGenConfig instanceof AOConfig) {
			dir = daoGenStrategy_.getAoStringOutDir();
		} else if (argCodeGenConfig instanceof DOConfig) {
			dir = daoGenStrategy_.getDoStringOutDir();
		} else if (argCodeGenConfig instanceof SqlMapXmlConfig) {
			dir = daoGenStrategy_.getSqlMapStringOutDir();
		} else if (argCodeGenConfig instanceof WebProjectXmlConfig) {
			dir = daoGenStrategy_.getWebProjectStringOutDir();
		} else if (argCodeGenConfig instanceof DaoUnitTestConfig) {
			dir = daoGenStrategy_.getDaoUnitTestStringOutDir();
		} else {
			logger_.warn("There is a unknow config object: " + argCodeGenConfig);
		}

		return dir;
	}

	/***
	 * Check whether the class file should be generated.
	 * 
	 * @param argGeneratFile
	 *            generating file.
	 * @param argCodeGenConfig
	 *            class file configuration
	 * @return true if should generate , or else false.
	 */
	protected boolean shouldBeGenerate(File argGeneratFile,
			ICodeGenConfig argCodeGenConfig) {
		boolean result = false;
		
		// if the file being generated has been existed, and the override
		// mode is false.
		if (!DAOServiceConfigurationManager
				.getBooleanProperty(CODEGEN_GENERATE_CODE_PATTERN_OVERRIDE)
				&& argGeneratFile.exists() && argCodeGenConfig.shouldBeNew()) {
			// result = false;
		} else if (argCodeGenConfig.shouldBeGenerated()) {
			result = true;
		}
		return result;
	}

	protected String getGenernateTypeInfo(File argGeneratFile,
			ICodeGenConfig argCodeGenConfig) {
		String genernateType = EMPTY;
		if (argCodeGenConfig.shouldBeOverride()) {
			genernateType = "(override)";
		} else if (argCodeGenConfig.shouldBeAppend()) {
			genernateType = "(append)";
		}
		// if the file being generated has been existed, and the override
		// mode is false.
		else if (!Boolean.valueOf(DAOServiceConfigurationManager
				.getProperty(CODEGEN_GENERATE_CODE_PATTERN_OVERRIDE))
				&& argGeneratFile.exists() && argCodeGenConfig.shouldBeNew()) {
			genernateType = "(has been existed)";
		} else if (argCodeGenConfig.shouldBeNew()) {
			genernateType = "(new)";
		} else if (argCodeGenConfig.shouldBeDelete()) {
			genernateType = "(delete)";
		} else if (argCodeGenConfig.shouldBeReduce()) {
			genernateType = "(reduce)";
		} else {
			genernateType = "(has been existed)";
		}
		return genernateType;
	}

	protected String getLoginfo(ICodeGenConfig argCodegenConfig, String filePath) {
		return "Genernate the file: " + filePath;
	}

	protected ICodeFormatter getCodeFormatter(ICodeGenConfig argCodeGenConfig,
			File argFile) {
		return CodeFormatterFactory.getInstance().getCodeFormatter(
				argCodeGenConfig, argFile);
	}
}
