package com.hitao.codegen.access.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.append.IAppendCodesInfo;
import com.hitao.codegen.util.FileUtils;


/***
 * This strategy is for reduce code in the configuration file.
 *
 * @author zhangjun.ht
 * @created 2011-8-8 ÏÂÎç05:21:48
 * @version $Id$
 */
public class CodeReduceGenerateStrategy extends DefaultCodeGenerateStrategy{

	@Override
	protected boolean checkGenerateCodes(ICodeGenConfig argCodeGenConfig,
			File argFile) {
		boolean check = true;
		if (argCodeGenConfig.shouldBeReduce()) {
			if (!argFile.exists()) {
				logger_.info("The config file doesn't exist " + argFile.toURI());
				check = false;
			}
		}

		return check;
	}
	
	@Override
	protected void generateCodes(ICodeGenConfig argCodeGenConfig, File argFile,
			String argCodes) {
		List<IAppendCodesInfo> appendCodeInfoList = argCodeGenConfig
				.getAppendCodesInfo(new ArrayList<IAppendCodesInfo>());
		generateCodes(argCodeGenConfig, argFile, argCodes, appendCodeInfoList);
	}

	/***
	 * Reduce the unneed codes.
	 *
	 * @param argCodeGenConfig		code genernation configuration.
	 * @param argFile	the file to be build.
	 * @param argCodes	the string in the file.
	 * @param argAppendCodesInfos	the codes to be reduced.
	 */
	protected void generateCodes(ICodeGenConfig argCodeGenConfig, File argFile,
			String argCodes, List<IAppendCodesInfo> argAppendCodesInfos) {
		
		try {
			String fileContent = FileUtils.loadFile(argFile);
			String breakLine = null;
			String whiteSpace = null;
			String genernateCodes = null;

			StringBuffer allCodes = new StringBuffer();
			for (IAppendCodesInfo appendCodesInfo : argAppendCodesInfos) {
				if (allCodes.length() > 0) {
					fileContent = allCodes.toString();
				}

				breakLine = appendCodesInfo.getBreakLine();
				whiteSpace = appendCodesInfo.getWhiteSpace();
				genernateCodes = whiteSpace + appendCodesInfo.getAppendCodes() + breakLine;

				int index = fileContent.indexOf(genernateCodes);
				// if the file doesn't contain the specific codes.
				if (index >= 0) {
					allCodes.setLength(0);

					// delete the genernateCodes
					allCodes.append(fileContent.substring(0, index)).append(fileContent.substring(index+ genernateCodes.length()));
				} 
			}

			// rebuild the file.
			if (allCodes.length() > 0) {
				// format the new codes.
				getCodeFormatter(argCodeGenConfig, argFile).codeFormate(
						allCodes.toString(), argFile);
			}
		} catch (IOException e) {
			logger_.error("Can't load the file: " + argFile.toURI(), e);
		}
	}

}
