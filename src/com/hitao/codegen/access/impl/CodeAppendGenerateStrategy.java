package com.hitao.codegen.access.impl;

import static com.hitao.codegen.constent.StringConstants.BLANK_LINE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.append.IAppendCodesInfo;
import com.hitao.codegen.util.FileUtils;

/***
 * This strategy is represent for append content into the configuration file.
 * 
 * @author zhangjun.ht
 * @created 2011-2-23
 * @version $Id: CodeAppendGenerateStrategy.java 45 2011-03-09 08:47:45Z guest $
 */
public class CodeAppendGenerateStrategy extends DefaultCodeGenerateStrategy {

	/***
	 * Get white space that will be append to the file before append the codes.
	 * 
	 * @return
	 */
	protected String getWhiteSpace() {
		return "\t";
	}

	/***
	 * Get the end element. e.g: "}" for class "</beans>" for spring
	 * configuration file. "</sqlMapConfig>" for sql-map.xml
	 * 
	 * @return the end statement of the configuration element.
	 */
	protected String getEndElement(ICodeGenConfig argCodeGenConfig) {
		return argCodeGenConfig.getEndElement();
	}

	/***
	 * Check whether to append codes.
	 * 
	 * @param argCodeGenConfig
	 *            code configuration object.
	 * @param argFile
	 *            the configuration file be generating.
	 * @return true, should be generate; or else false.
	 */
	protected boolean checkGenerateCodes(ICodeGenConfig argCodeGenConfig,
			File argFile) {
		boolean check = true;
		if (argCodeGenConfig.shouldBeAppend()) {
			if (!argFile.exists()) {
				logger_.info("The config file doesn't exist " + argFile.toURI());
				check = false;
			}
		} else {
			check = false;
		}

		return check;
	}

	protected void generateCodes(ICodeGenConfig argCodeGenConfig, File argFile,
			String argCodes) {
		List<IAppendCodesInfo> appendCodeInfoList = argCodeGenConfig
				.getAppendCodesInfo(new ArrayList<IAppendCodesInfo>());
		generateCodes(argCodeGenConfig, argFile, argCodes, appendCodeInfoList);
	}

	protected void generateCodes(ICodeGenConfig argCodeGenConfig, File argFile,
			String argCodes, List<IAppendCodesInfo> argAppendCodesInfos) {

		try {
			String fileContent = FileUtils.loadFile(argFile);
			int endElementIndex = 0;
			String firstContont = null;
			String breakLine = null;
			String whiteSpace = null;

			StringBuffer allCodes = new StringBuffer();
			for (IAppendCodesInfo appendCodesInfo : argAppendCodesInfos) {
				if (allCodes.length() > 0) {
					fileContent = allCodes.toString();
				}

				breakLine = appendCodesInfo.getBreakLine();
				whiteSpace = appendCodesInfo.getWhiteSpace();

				// if the file doesn't contain the specific codes.
				if (appendCodesInfo.shouldAppendCodes(fileContent)) {
					allCodes.setLength(0);

					// append the codes into the file
					endElementIndex = appendCodesInfo
							.getAppendIndex(fileContent);

					if (endElementIndex > -1) {
						firstContont = fileContent
								.substring(0, endElementIndex);

						// add line breaker.
						if (!firstContont.endsWith(BLANK_LINE)) {
							firstContont += BLANK_LINE;
						}
						allCodes.append(firstContont).append(whiteSpace)
								.append(appendCodesInfo.getAppendCodes())
								.append(breakLine)
								.append(fileContent.substring(endElementIndex));
					} else {
						logger_.warn("Can't find the end element in the file: "
								+ argFile.toURI());
					}

				} else {
					logger_.debug("It needn't append the codes. Because the file: "
							+ argFile.toURI()
							+ " has contained the codes: "
							+ appendCodesInfo.getCheckConditionCodes());
				}
			}

			// if need append codes.
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
