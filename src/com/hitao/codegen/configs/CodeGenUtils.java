package com.hitao.codegen.configs;

import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_IMPLEMENT_NAME_PREFIX;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_IMPLEMENT_NAME_SUFFIX;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_IMPL_RELATIVE_DIRECTORY;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_PACKAGE_PATH;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_PARAMETER_NAME_PREFIX_ARG;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_UNNEED_IMPORT_CLASSES;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_UNNEED_LOAD_CLASS;
import static com.hitao.codegen.constent.StringConstants.DOUBLE_QUOTATION;
import static com.hitao.codegen.constent.StringConstants.ENTER;
import static com.hitao.codegen.constent.StringConstants.LINE_BREAK;
import static com.hitao.codegen.constent.StringConstants.SEMICOLON;
import static com.hitao.codegen.util.StringUtils.EMPTY;
import static java.io.File.separator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.hitao.codegen.configs.basic.IConfigObject;
import com.hitao.codegen.configs.common.FieldConfig;
import com.hitao.codegen.configs.common.MethodConfig;
import com.hitao.codegen.configs.common.ParameterConfig;
import com.hitao.codegen.util.StringUtils;

/**
 * Utility functions for CodeGen.<br>
 * 
 * @author zhangjun.ht
 * @created 2010-11-30
 * @version $Id: CodeGenUtils.java 57 2011-04-29 02:16:16Z guest $
 */
public class CodeGenUtils {

	public static final String IMPLE_RELATIVE_PACKAGE = DAOServiceConfigurationManager
			.getProperty(CODEGEN_DAO_IMPL_RELATIVE_DIRECTORY);

	private CodeGenUtils() {

	}

	private static List<Pattern> unNeedLoadClassesPatternList_ = new ArrayList<Pattern>();
	private static List<Pattern> unNeedImportClassesPatternList_ = new ArrayList<Pattern>();

	static {
		String name = DAOServiceConfigurationManager
				.getProperty(CODEGEN_UNNEED_LOAD_CLASS);
		if (!StringUtils.isEmpty(name)) {
			String[] nameArr = StringUtils.split(name, ' ');
			for (String regex : nameArr) {
				unNeedLoadClassesPatternList_.add(Pattern
						.compile(getRegex(regex)));
			}
		}

		String packs = DAOServiceConfigurationManager
				.getProperty(CODEGEN_UNNEED_IMPORT_CLASSES);
		if (!StringUtils.isEmpty(packs)) {
			String[] nameArr = StringUtils.split(packs, ' ');
			for (String regex : nameArr) {
				unNeedImportClassesPatternList_.add(Pattern
						.compile(getRegex(regex)));
			}
		}

	}

	/***
	 * Get the file writer.
	 * 
	 * @param argFile
	 * @return
	 * @throws IOException
	 */
	public static Writer getFileWriter(File argFile) throws IOException {

		argFile.getParentFile().mkdirs();
		return new FileWriter(argFile);
	}

	/***
	 * get the regular expression as the argExpression.
	 * 
	 * @param argExpression
	 * @return
	 */
	private static String getRegex(String argExpression) {
		String reg = argExpression.replace('.', '#');
		reg = reg.replaceAll("#", "\\\\.");
		reg = reg.replace('*', '#');
		reg = reg.replaceAll("#", ".*");
		reg = reg.replace('?', '#');
		reg = reg.replaceAll("#", ".?");
		reg = "^" + reg + "$";
		return reg;

	}

	/***
	 * check to see whether the argClass is needed load according to the regular
	 * expression.
	 * 
	 * @param argClass
	 * @return true if need or false.
	 */
	public static boolean needloadClass(String argClass) {
		boolean result = true;
		for (Pattern pattern : unNeedLoadClassesPatternList_) {
			if (pattern.matcher(argClass).matches()) {
				result = false;
				break;
			}
		}

		return result;
	}

	/***
	 * check to see whether the argClass is needed imported according to the
	 * regular expression.
	 * 
	 * @param argClass
	 * @return true if need or false.
	 */
	public static boolean needImportClass(String argClass) {
		boolean result = true;
		for (Pattern pattern : unNeedImportClassesPatternList_) {
			if (pattern.matcher(getClassNameFromImportStatement(argClass))
					.matches()) {
				result = false;
				break;
			}
		}

		return result;
	}

	/***
	 * check the ParameterConfig list whether they are equals.
	 * 
	 * @param argPara1
	 * @param argPara2
	 * @return
	 */
	public static boolean parametersEquals(List<ParameterConfig> argPara1,
			List<ParameterConfig> argPara2) {
		boolean result = true;
		if (argPara1 != null && argPara2 != null
				&& argPara1.size() == argPara2.size()) {
			for (int i = 0; i < argPara1.size(); i++) {
				if (argPara1.get(i) == null
						|| (!argPara1.get(i).equals(argPara2.get(i)))) {
					result = false;
					break;
				}
			}
		} else {
			result = false;
		}
		return result;
	}

	public static String getImportStatement(String argImport) {
		if (!StringUtils.isEmpty(argImport)) {
			argImport = clearNewlineSymbol(argImport);

			if (!argImport.startsWith("import ")) {
				argImport = "import " + argImport;
			}
			if (!argImport.endsWith(SEMICOLON)) {
				argImport = argImport + SEMICOLON;
			}
		}
		return argImport;
	}

	public static String getImportClassFromImportStatement(
			String argImportStatement) {
		if (!StringUtils.isEmpty(argImportStatement)) {
			argImportStatement = argImportStatement.trim();

			if (argImportStatement.startsWith("import ")) {
				argImportStatement = argImportStatement.substring("import "
						.length());
			}
			if (argImportStatement.endsWith(SEMICOLON)) {
				argImportStatement = argImportStatement.substring(0,
						argImportStatement.lastIndexOf(SEMICOLON));
			}
		} else {
			return argImportStatement;
		}
		return argImportStatement;
	}

	public static String clearNewlineSymbol(String argString) {
		if (StringUtils.isEmpty(argString)) {
			return EMPTY;
		}
		argString = StringUtils
				.replaceAll(new StringBuffer(argString), LINE_BREAK, "")
				.toString().trim();
		argString = StringUtils
				.replaceAll(new StringBuffer(argString), ENTER, "").toString()
				.trim();
		return argString;
	}

	/***
	 * Clear double quotation.
	 * 
	 * @param argString
	 * @return
	 */
	public static String clearDoubleQuotation(String argString) {
		argString = StringUtils
				.replaceAll(new StringBuffer(argString), DOUBLE_QUOTATION, "")
				.toString().trim();
		return argString;
	}

	/***
	 * Get all classes in brackets. e.g: argImport = List<Class<String>> : you
	 * will get [List, Class, String]
	 * 
	 * @param argImport
	 * @return
	 */
	public static String[] getClassesInGeneric(String argImport) {
		String[] classes = StringUtils.split(argImport, '<');
		if (classes.length > 0) {
			String temp = classes[classes.length - 1];
			int index = temp.indexOf(">");
			if (index > 0) {
				classes[classes.length - 1] = temp.substring(0, index);
			}
		}
		return classes;
	}

	/***
	 * Get the short class name
	 * 
	 * @param argClassName
	 * @return short class name
	 */
	public static String getShortClassName(String argClassName) {
		if (!StringUtils.isEmpty(argClassName)) {
			if (argClassName.indexOf("<") > -1) {
				return getShortClassNameInGeneric(argClassName);
			} else {
				return getSingleShortClassName(argClassName);
			}
		} else {
			return EMPTY;
		}
	}

	/***
	 * Get the short class name without generic type.
	 * 
	 * @param argClassName
	 * @return short class name
	 */
	public static String getShortClassNameWithoutGenericType(String argClassName) {
		if (!StringUtils.isEmpty(argClassName)) {
			if (argClassName.indexOf("<") > -1) {
				return getShortClassNameWithoutGeneric(argClassName);
			} else {
				return getSingleShortClassName(argClassName);
			}
		} else {
			return EMPTY;
		}
	}

	/***
	 * Get the short class name
	 * 
	 * @param argClassName
	 * @return short class name
	 */
	public static String getPackageOfClass(String argClassName) {
		if (!StringUtils.isEmpty(argClassName)) {
			int index = argClassName.indexOf("<");
			if (index > -1) {
				return getPackageOfSimpleClass(argClassName.substring(0, index));
			} else {
				return getPackageOfSimpleClass(argClassName);
			}
		} else {
			return EMPTY;
		}
	}

	private static String getPackageOfSimpleClass(String argClassName) {
		if (argClassName.lastIndexOf(".") >= 0) {
			return argClassName.substring(0, argClassName.lastIndexOf("."));
		} else {
			return EMPTY;
		}

	}

	/***
	 * return class path from import statement.
	 * 
	 * @param argImport
	 * @return class path.
	 */
	public static String getClassNameFromImportStatement(String argImport) {
		if (StringUtils.isEmpty(argImport)) {
			return argImport;
		}

		if (argImport.startsWith("import ")) {
			argImport = argImport.substring("import ".length()).trim();
		}

		if (argImport.indexOf("static") >= 0) {
			argImport = argImport.substring(
					argImport.indexOf("static") + "static".length()).trim();
		}

		if (argImport.endsWith(SEMICOLON)) {
			argImport = argImport.substring(0, argImport.length() - 1).trim();
		}

		return argImport;
	}
	
	/***
	 * Get simple class name from import statement.
	 * 
	 * @param argImport import statement. e.g: import java.lang.String
	 * @return	simple class name. e.g: String
	 */
	public static String getShortClassNameFromImportStatement(String argImport) {
		return getSingleShortClassName(getClassNameFromImportStatement(argImport));
	}

	/***
	 * Just return the short class name.
	 * 
	 * @param argClassName
	 * @return
	 */
	private static String getSingleShortClassName(String argClassName) {
		String result = null;
		if (!StringUtils.isEmpty(argClassName)) {
			int index = argClassName.lastIndexOf(".");
			if (index >= 0) {
				result = argClassName.substring(index + 1,
						argClassName.length());
			} else {
				result = argClassName;
			}
		}
		return result;
	}

	/***
	 * Get the short class name in generic type. eg: argClassName =
	 * "java.util.List<java.lang.Class<java.lang.String>>", it will return
	 * List<Class<String>>>
	 * 
	 * @param argClassName
	 * @return
	 */
	private static String getShortClassNameInGeneric(String argClassName) {
		StringBuffer result = new StringBuffer();
		getShortClassNameInGeneric0(result, getClassesInGeneric(argClassName),
				0);

		return result.toString();
	}

	/***
	 * Get the short class name in generic type. eg: argClassName =
	 * "java.util.List<java.lang.Class<java.lang.String>>", it will return List
	 * 
	 * @param argClassName
	 * @return
	 */
	private static String getShortClassNameWithoutGeneric(String argClassName) {
		String className = getShortClassNameInGeneric(argClassName);
		int index = className.indexOf("<");
		if (index > 0) {
			className = className.substring(0, index);
		}

		return className;
	}

	private static void getShortClassNameInGeneric0(StringBuffer argBuffer,
			String[] classes, int argIndex) {
		if (argIndex < classes.length) {
			argBuffer.append(getSingleShortClassName(classes[argIndex]));
			if (argIndex != classes.length - 1) {
				argBuffer.append("<");
			}
			getShortClassNameInGeneric0(argBuffer, classes, argIndex + 1);
			if (argIndex != classes.length - 1) {
				argBuffer.append(">");
			}
		}
	}

	/***
	 * Set the concrete class for the generic type.
	 * 
	 * e.g: argClass = List<E> argGenericType= E argConcreteClass = String it
	 * will return List<String>
	 * 
	 * @param argClass
	 * @param argGenericVariable
	 * @param argConcreteClass
	 * @return The class with concrete class.
	 */
	public static String getConcreteClassForGenericType(String argClass,
			String argGenericVariable, String argConcreteClass) {
		if (StringUtils.isEmpty(argClass)
				|| StringUtils.isEmpty(argGenericVariable)
				|| StringUtils.isEmpty(argConcreteClass)) {
			return argClass;
		}
		if (argClass.equals(argGenericVariable)) {
			return argConcreteClass;
		}

		return StringUtils.replaceAll(new StringBuffer(argClass),
				"<" + argGenericVariable + ">", "<" + argConcreteClass + ">")
				.toString();
	}

	/***
	 * Check whether the argClass contains the generic variable
	 * argGenericVariable.
	 * 
	 * @param argClass
	 * @param argGenericVariable
	 * @return true if contains, false or else.
	 */
	public static boolean containsGenericVariable(String argClass,
			String argGenericVariable) {
		if (StringUtils.isEmpty(argClass)
				|| StringUtils.isEmpty(argGenericVariable)) {
			return false;
		} else {
			return argClass.equals(argGenericVariable)
					|| argClass.contains("<" + argGenericVariable + ">");
		}
	}

	/***
	 * delete first slash.
	 * 
	 * @param argPath
	 * @return path without first slash.
	 */
	public static String deleteFirstSlash(String argPath) {
		if (argPath.charAt(0) == '\\' || argPath.charAt(0) == '/') {
			argPath = argPath.substring(1);
		}

		return argPath;
	}

	/***
	 * Get relative package from systemConfig.xml for the class argClassPackage.
	 * 
	 * @param argClassPackage
	 *            class package
	 * @param argPackageKey
	 *            the key to relative DAO.
	 * @return relative package.
	 */
	public static String getRelativePackageForClass(String argClassPackage,
			String argPackageKey) {
		return getRelativePackageForClassByKey(argClassPackage,
				CODEGEN_DAO_PACKAGE_PATH, argPackageKey);
	}

	/***
	 * Get relative package from systemConfig.xml for the class argClassPackage.
	 * 
	 * @param argClassPackage
	 *            class package
	 * @param argPackageKey1
	 *            the key represents the package in argClassPackage.
	 * @param argPackageKey
	 *            the key for the final package.
	 * @return relative package.
	 */
	public static String getRelativePackageForClassByKey(
			String argClassPackage, String argPackageKey1, String argPackageKey2) {
		String daoDefaultPackage = DAOServiceConfigurationManager
				.getProperty(argPackageKey1);
		if (StringUtils.isEmpty(daoDefaultPackage)) {
			daoDefaultPackage = ".dao.";
		}

		int index = argClassPackage.lastIndexOf(daoDefaultPackage);
		if (index > 0) {
			String commonPath = argClassPackage.substring(0, index);
			String remainPath = argClassPackage.substring(index
					+ daoDefaultPackage.length());

			String managerOrAoDefaultPackage = DAOServiceConfigurationManager
					.getProperty(argPackageKey2);
			return commonPath + managerOrAoDefaultPackage + remainPath;
		} else {
			// if this class in root package.
			daoDefaultPackage = daoDefaultPackage.substring(0,
					daoDefaultPackage.length() - 1);
			if (argClassPackage.endsWith(daoDefaultPackage)) {
				String commonPath = argClassPackage.substring(0,
						argClassPackage.lastIndexOf(daoDefaultPackage));
				String managerOrAoDefaultPackage = DAOServiceConfigurationManager
						.getProperty(argPackageKey2);
				argClassPackage = commonPath
						+ managerOrAoDefaultPackage.substring(0,
								managerOrAoDefaultPackage.length() - 1);
			}

			return argClassPackage;
		}
	}

	/***
	 * Get relative package from systemConfig.xml for the class argClassPackage.
	 * 
	 * @param argClassPackage
	 *            class package
	 * @param argPackageKey
	 *            the key to relative DAO.
	 * @return relative package.
	 */
	public static String getRelativePackageForClass(String argClassPackage,
			String argPackageKey, boolean argCommonPath, boolean argSuffix) {
		return getRelativePackageForClassByKey(argClassPackage,
				CODEGEN_DAO_PACKAGE_PATH, argPackageKey, argCommonPath,
				argSuffix);
	}

	/***
	 * Get relative package from systemConfig.xml for the class argClassPackage.
	 * 
	 * @param argClassPackage
	 *            class package
	 * @param argPackageKey1
	 *            the key represents the package in argClassPackage.
	 * @param argPackageKey
	 *            the key for the final package.
	 * 
	 * @param argCommonPath
	 *            whether get the common path.
	 * @param argSuffix
	 *            whether get suffix
	 * @return relative package.
	 */
	public static String getRelativePackageForClassByKey(
			String argClassPackage, String argPackageKey1,
			String argPackageKey2, boolean argCommonPath, boolean argSuffix) {
		String daoDefaultPackage = DAOServiceConfigurationManager
				.getProperty(argPackageKey1);
		if (StringUtils.isEmpty(daoDefaultPackage)) {
			daoDefaultPackage = ".dao.";
		}

		int index = argClassPackage.lastIndexOf(daoDefaultPackage);
		if (index > 0) {
			String commonPath = argClassPackage.substring(0, index);
			String remainPath = argClassPackage.substring(index
					+ daoDefaultPackage.length());

			String mainPath = DAOServiceConfigurationManager
					.getProperty(argPackageKey2);

			StringBuffer buffer = new StringBuffer();
			if (argCommonPath) {
				buffer.append(commonPath);
			}
			buffer.append(mainPath);

			if (argSuffix) {
				buffer.append(remainPath);
			}

			if (buffer.toString().startsWith(".")) {
				buffer.deleteCharAt(0);
			}
			return buffer.toString();
		} else {
			// if this class in root package.
			daoDefaultPackage = daoDefaultPackage.substring(0,
					daoDefaultPackage.length() - 1);
			if (argClassPackage.endsWith(daoDefaultPackage)) {
				String commonPath = argClassPackage.substring(0,
						argClassPackage.lastIndexOf(daoDefaultPackage));
				String managerOrAoDefaultPackage = DAOServiceConfigurationManager
						.getProperty(argPackageKey2);
				if (argCommonPath) {
				argClassPackage = commonPath
						+ managerOrAoDefaultPackage.substring(0,
								managerOrAoDefaultPackage.length() - 1);
				} else {
					return managerOrAoDefaultPackage.substring(0,
							managerOrAoDefaultPackage.length() - 1);
				}
			}

			return argClassPackage;
		}
	}

	/***
	 * Get relative class name of the argCodeGenConfig. e.g from UserDAO to
	 * UserManager
	 * 
	 * @param argCodeGenConfig
	 * @param argSuffixFrom
	 * @param argSuffixto
	 * @return relative class name
	 */
	public static String getRelativeClassName(ICodeGenConfig argCodeGenConfig,
			String argSuffixFrom, String argSuffixTo) {
		String daoSuffix = DAOServiceConfigurationManager
				.getProperty(argSuffixFrom);
		String name = argCodeGenConfig.getName();
		if (!StringUtils.isEmpty(daoSuffix)) {
			return name.substring(0, name.lastIndexOf(daoSuffix))
					+ DAOServiceConfigurationManager.getProperty(argSuffixTo,
							EMPTY);
		} else {
			return EMPTY;
		}
	}

	/***
	 * Get the file output path. e.g: D:\\a\b\c\projectName is argOutputDir
	 * com.apache.util is argFilePackage UserDAO is argFileName .java is
	 * argFileType
	 * 
	 * @param argOutputDir
	 *            the output directory.
	 * @param argFilePackage
	 *            the file package in project.
	 * @param argFileName
	 *            file name.
	 * @param argFileType
	 *            file type.
	 * @return
	 */
	public static String getFileOutputPath(String argOutputDir,
			String argFilePackage, String argFileName, String argFileType) {
		StringBuffer filePath = new StringBuffer();
		String filePackage = StringUtils.replaceAll(
				new StringBuffer(argFilePackage), ".", separator).toString();
		filePath.append(argOutputDir + separator)
				.append(filePackage + separator).append(argFileName)
				.append(argFileType);
		return filePath.toString();
	}

	private static String ARG_STRING = "arg";

	/***
	 * Get parameter name according to the configuration value.
	 * 
	 * @param argName
	 * @return
	 */
	public static String getParameterName(String argName) {
		if (!StringUtils.isEmpty(argName)) {
			if (DAOServiceConfigurationManager
					.getBooleanProperty(CODEGEN_PARAMETER_NAME_PREFIX_ARG)) {
				argName = ARG_STRING + StringUtils.capitalize(argName);
			} else {
				if (argName.startsWith(ARG_STRING)) {
					argName = argName.substring(ARG_STRING.length());
				}
			}
			
			argName = StringUtils.uncapitalize(argName);
		}
		return argName;
	}

	/***
	 * Get the different configuration object list between the argNew and
	 * argOld.
	 * 
	 * @param argNew
	 * @param argOld
	 * @return
	 */
	public static List<IConfigObject> getDifferentConfigs(
			IClassCodeGenConfig argNew, IClassCodeGenConfig argOld) {
		List<IConfigObject> list = new ArrayList<IConfigObject>();
		for (MethodConfig method : argNew.getMethodList()) {
			if (!argOld.getMethodList().contains(method)) {
				list.add(method);
			}
		}

		for (FieldConfig field : argNew.getFieldList()) {
			if (!argOld.getFieldList().contains(field)) {
				list.add(field);
			}
		}

		return list;
	}

	/***
	 * get the relative package for the argClassCodegenConfig
	 * 
	 * @param argClassCodegenConfig
	 */
	public static String getRelativePackagePath(
			IClassCodeGenConfig argClassCodegenConfig) {
		return getRelativePackagePath(argClassCodegenConfig.getPackagePath());
	}

	/***
	 * get the relative package for the argPackagePath
	 * 
	 * @param argPackagePath
	 * @return
	 */
	public static String getRelativePackagePath(String argPackagePath) {
		String packagePath = null;
		if (StringUtils.isEmpty(IMPLE_RELATIVE_PACKAGE)) {
			packagePath = argPackagePath;
		} else {
			packagePath = argPackagePath + "." + IMPLE_RELATIVE_PACKAGE;
		}
		return packagePath;
	}

	public static final String SUFFIX = DAOServiceConfigurationManager
			.getProperty(CODEGEN_DAO_IMPLEMENT_NAME_SUFFIX);

	public static final String PREFIX = DAOServiceConfigurationManager
			.getProperty(CODEGEN_DAO_IMPLEMENT_NAME_PREFIX);

	/***
	 * Get class name with prefix and suffix.
	 * 
	 * @param argClassName
	 *            the original class name.
	 * @return
	 */
	public static String getClassName(String argClassName) {
		String className = argClassName;

		if ((!StringUtils.isEmpty(PREFIX))
				&& (!argClassName.startsWith(PREFIX))) {
			className = PREFIX + className;
		}

		if ((!StringUtils.isEmpty(SUFFIX)) && (!argClassName.endsWith(SUFFIX))) {
			className += SUFFIX;
		}

		return className;
	}

	/***
	 * Clear the "{" at start and "}" at the end for the argStatement.
	 * 
	 * @param argStatement
	 * @return 
	 */
	public static String clearBrace(String argStatement) {
		if (!StringUtils.isEmpty(argStatement)) {
			if (argStatement.startsWith("{")) {
				argStatement = argStatement.substring(1);
			}

			if (argStatement.endsWith("}")) {
				argStatement = argStatement.substring(0,
						argStatement.length() - 1);
			}
		}

		return argStatement;
	}
}
