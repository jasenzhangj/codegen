package com.hitao.codegen.access.impl;

import java.io.File;

import com.hitao.codegen.configs.ICodeGenConfig;

/***
 * The default strategey for generate the java source file or other configuration file.
 * 
 * @author zhangjun.ht
 * @created 2011-2-23
 * @version $Id: DefaultCodeGenerateStrategy.java 44 2011-03-08 08:55:34Z guest
 *          $
 */
public class DefaultCodeGenerateStrategy extends AbstractCodeGenerateStrategy {

	/***
	 * Check whether to generate codes.
	 * 
	 * @param argCodeGenConfig
	 *            code configuration object.
	 * @param argFile
	 *            the configuration file be generating.
	 * @return true, should be generate; or else false.
	 */
	protected boolean checkGenerateCodes(ICodeGenConfig argCodeGenConfig,
			File argFile) {
		return argCodeGenConfig.shouldBeGenerated();
	}

	@Override
	protected void generateCodes(ICodeGenConfig argCodeGenConfig, File argFile) {
		if (!checkGenerateCodes(argCodeGenConfig, argFile)) {
			return;
		}

		StringBuffer codes = new StringBuffer();
		argCodeGenConfig.getCodePieceInfo(codes);

		generateCodes(argCodeGenConfig, argFile, codes.toString());
	}

	protected void generateCodes(ICodeGenConfig argCodeGenConfig, File argFile,
			String argCodes) {
		getCodeFormatter(argCodeGenConfig, argFile).codeFormate(argCodes,
				argFile);
	}
}
