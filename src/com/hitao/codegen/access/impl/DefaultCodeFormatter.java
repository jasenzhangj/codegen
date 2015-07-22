package com.hitao.codegen.access.impl;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;

import com.hitao.codegen.access.ICodeFormatter;
import com.hitao.codegen.util.FileUtils;

/***
 * Create a XML file and format it. But i don't know how to format it by JDK API
 * currently. So i will resolve it later.
 * 
 * @author zhangjun.ht
 * @created 2011-2-19
 * @version $Id: DefaultCodeFormatter.java 23 2011-02-24 08:39:57Z guest $
 */
public class DefaultCodeFormatter implements ICodeFormatter {

	private static final Logger logger_ = Logger
			.getLogger(DefaultCodeFormatter.class);

	@Override
	public void codeFormate(String argCode, File argFile) {
		Writer writer = null;

		try {
			// build parent directories.
			argFile.getParentFile().mkdirs();

			writer = FileUtils.getNonBufferedFileWriter(argFile);

			writer.write(argCode.toString(), 0, argCode.length());

		} catch (IOException e) {
			logger_.error(
					"it make an error when create file " + argFile.toURI()
							+ ".", e);
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
