package com.hitao.codegen.access.impl;

import java.io.File;

import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.util.FileUtils;

/***
 * Delete the generate codes.
 * 
 * @author zhangjun.ht
 * @created 2011-8-8 обнГ04:53:25
 * @version $Id$
 */
public class CodeDeleteGenerateStrategy extends AbstractCodeGenerateStrategy {

	@Override
	protected void generateCodes(ICodeGenConfig argCodeGenConfig, File argFile) {
		if (argCodeGenConfig.shouldBeDelete()) {
			if (argFile.exists()) {
				FileUtils.deleteFileAndEmptyTree(argFile);
			}
		}
	}
}
