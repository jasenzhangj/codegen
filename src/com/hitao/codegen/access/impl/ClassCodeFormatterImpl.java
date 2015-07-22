package com.hitao.codegen.access.impl;

import static com.hitao.codegen.constent.StringConstants.CODEGEN_CODE_FORMATTER_FILE;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_USE_DEFAULT_ECLIPSE_FORMATTER;
import static com.hitao.codegen.util.SystemPropertiesLoader.LocationType.CLASSPATH;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditVisitor;

import com.hitao.codegen.access.ICodeFormatter;
import com.hitao.codegen.configs.DAOServiceConfigurationManager;
import com.hitao.codegen.util.FileUtils;
import com.hitao.codegen.util.SystemPropertiesLoader;

/***
 * The implement of java source code formatter.
 *
 * @author zhangjun.ht
 * @created 2010-11-30
 * @version $Id: ClassCodeFormatterImpl.java 44 2011-03-08 08:55:34Z guest $
 */
@SuppressWarnings("unchecked")
public class ClassCodeFormatterImpl implements ICodeFormatter {
	private static final Logger logger_ = Logger
			.getLogger(ClassCodeFormatterImpl.class);

	private static Map<String, Object> params_ = new HashMap<String, Object>();

	static {
		String useDefaultEclipseFormatter = DAOServiceConfigurationManager
				.getProperty(CODEGEN_USE_DEFAULT_ECLIPSE_FORMATTER);
		logger_.info("use default eclipse formatter: "
				+ useDefaultEclipseFormatter);

		if (Boolean.valueOf(useDefaultEclipseFormatter)) {
			params_ = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
		} else {

			try {
				// load the code formatter configuration file.
				SystemPropertiesLoader systemLoader = new SystemPropertiesLoader(
						CLASSPATH,
						DAOServiceConfigurationManager
								.getProperty(CODEGEN_CODE_FORMATTER_FILE));
				systemLoader.call();

				// create a formatter parameter map from system.
				Properties properties = System.getProperties();
				Set<String> set = properties.stringPropertyNames();
				Iterator<String> it = set.iterator();
				String key = null;
				while (it.hasNext()) {
					key = it.next();
					if (key.startsWith("org.eclipse.jdt.core")) {
						params_.put(key, properties.get(key));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		params_.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_6);
		params_.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,
				JavaCore.VERSION_1_6);
		params_.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
	}

	public void codeFormate(String argCodes, File argFile) {
		Writer writer = null;

		try {
			Document doc = new Document(argCodes);
			CodeFormatter codeFormatter = ToolFactory
					.createCodeFormatter(params_);
			String text = doc.get();
			TextEdit edits = codeFormatter.format(
					CodeFormatter.K_COMPILATION_UNIT, text, 0, text.length(),
					0, System.getProperty("line.separator"));

			//build parent directories.
			argFile.getParentFile().mkdirs();
			
			writer = FileUtils.getNonBufferedFileWriter(argFile);

			if (edits != null) {
				WriterVisitor visitor = new WriterVisitor(text, writer);
				edits.accept(visitor);
				visitor.finish();
			} else {
				writer.write(argCodes.toString(), 0, argCodes.length());
				logger_.error("Can't forrmat the file:"
						+ argFile.getCanonicalPath()
						+ ", because the syntax is error in this file . Please check it carefully in the unformatted source file.");
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	
	public static StringWriter getStringWriterInClassForrmate(String argCodes) {
		StringWriter writer = null;
		try {
			Document doc = new Document(argCodes);
			CodeFormatter codeFormatter = ToolFactory
					.createCodeFormatter(params_);
			String text = doc.get();
			TextEdit edits = codeFormatter.format(
					CodeFormatter.K_COMPILATION_UNIT, text, 0, text.length(),
					0, System.getProperty("line.separator"));

			writer = new StringWriter();

			if (edits != null) {
				WriterVisitor visitor = new WriterVisitor(text, writer);
				edits.accept(visitor);
				visitor.finish();
			} else {
				writer.write(argCodes.toString(), 0, argCodes.length());
				logger_.error("Can't forrmat the codes:"
						+ argCodes
						+ ", because the syntax is error in this file .");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		
		return writer;
	}

	private static class WriterVisitor extends TextEditVisitor {
		private final String m_base;
		private final Writer m_writer;
		private int m_offset;

		public WriterVisitor(String base, Writer writer) {
			this.m_base = base;
			this.m_writer = writer;
		}

		private void skip(int offset) {
			if (offset > this.m_offset)
				this.m_offset = offset;
			else if (offset < this.m_offset)
				throw new IllegalStateException();
		}

		private void copy(int offset) {
			if (offset > this.m_offset) {
				try {
					this.m_writer.write(this.m_base, this.m_offset, offset
							- this.m_offset);
				} catch (IOException e) {
					throw new RuntimeException("Error writing to file", e);
				}
				this.m_offset = offset;
			} else if (offset < this.m_offset) {
				throw new IllegalStateException();
			}
		}

		public boolean visit(DeleteEdit edit) {
			copy(edit.getOffset());
			skip(edit.getOffset() + edit.getLength());
			return super.visit(edit);
		}

		public boolean visit(InsertEdit edit) {
			copy(edit.getOffset());
			try {
				this.m_writer.write(edit.getText());
			} catch (IOException e) {
				throw new RuntimeException("Error writing to file", e);
			}
			return super.visit(edit);
		}

		public boolean visit(ReplaceEdit edit) {
			copy(edit.getOffset());
			skip(edit.getOffset() + edit.getLength());
			try {
				this.m_writer.write(edit.getText());
			} catch (IOException e) {
				throw new RuntimeException("Error writing to file", e);
			}
			return super.visit(edit);
		}

		public void finish() {
			copy(this.m_base.length());
		}
	}
}
