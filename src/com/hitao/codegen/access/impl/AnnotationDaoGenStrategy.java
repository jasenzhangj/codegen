package com.hitao.codegen.access.impl;

import static com.hitao.codegen.configs.dao.SqlMapXmlConfig.INSERT_STATEMENT_RETURN_TYPE;
import static com.hitao.codegen.configs.dao.SqlMapXmlConfig.SELECT_PAGIN_RETURN_COLLECTION_TYPE;
import static com.hitao.codegen.configs.dao.SqlMapXmlConfig.UPDATE_STATEMENT_RETURN_TYPE;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_AO_PACKAGE_PATH;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_AO_SUFFIX;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_DAO_SUFFIX;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_DO_SUFFIX;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_MANAGER_SUFFIX;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DO_PACKAGE_PATH;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_HOLD_ANNOTATIONS_FOR_DAO;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_MANAGER_PACKAGE_PATH;
import static com.hitao.codegen.constent.StringConstants.ENTER;
import static com.hitao.codegen.constent.StringConstants.LINE_BREAK;
import static com.hitao.codegen.constent.StringConstants.SEMICOLON;
import static com.hitao.codegen.constent.StringConstants.TAB;
import static com.hitao.codegen.util.StringUtils.EMPTY;
import static com.hitao.codegen.util.StringUtils.SPACE_CHAR;
import static java.io.File.separator;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.stmt.BlockStmt;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hitao.codegen.configs.CodeGenConfigHelper;
import com.hitao.codegen.configs.CodeGenUtils;
import com.hitao.codegen.configs.DAOServiceConfigurationManager;
import com.hitao.codegen.configs.IClassCodeGenConfig;
import com.hitao.codegen.configs.IClassElementCodeGenConfig;
import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.basic.IConfigObject;
import com.hitao.codegen.configs.common.AnnotationConfig;
import com.hitao.codegen.configs.common.BodyConfig;
import com.hitao.codegen.configs.common.CommentConfig;
import com.hitao.codegen.configs.common.FieldConfig;
import com.hitao.codegen.configs.common.ImportConfig;
import com.hitao.codegen.configs.common.MethodConfig;
import com.hitao.codegen.configs.common.ParameterConfig;
import com.hitao.codegen.configs.common.ThrowExceptionConfig;
import com.hitao.codegen.configs.dao.AOConfig;
import com.hitao.codegen.configs.dao.AOImplConfig;
import com.hitao.codegen.configs.dao.DOConfig;
import com.hitao.codegen.configs.dao.DaoConfig;
import com.hitao.codegen.configs.dao.DAOConfigs;
import com.hitao.codegen.configs.dao.DaoImplConfig;
import com.hitao.codegen.configs.dao.IDAOConfigs;
import com.hitao.codegen.configs.dao.ManagerConfig;
import com.hitao.codegen.configs.dao.ManagerImplConfig;
import com.hitao.codegen.configs.dao.SqlMapXmlConfig;
import com.hitao.codegen.configs.dao.annotation.DAO;
import com.hitao.codegen.configs.dao.annotation.DO;
import com.hitao.codegen.configs.dao.annotation.DOType;
import com.hitao.codegen.configs.dao.mapping.ColumnMetaDataMapping;
import com.hitao.codegen.configs.dao.mapping.IColumnInfo;
import com.hitao.codegen.util.FileUtils;
import com.hitao.codegen.util.ModifierSet;
import com.hitao.codegen.util.ObjectUtils;
import com.hitao.codegen.util.StringUtils;

/***
 * This class is represents to generate codes from annotations in all DAO
 * interfaces.
 * 
 * @author zhangjun.ht
 * @created 2011-1-13
 * @version $Id: AnnotationDaoGenStrategy.java 58 2011-05-06 06:40:39Z guest $
 */
public class AnnotationDaoGenStrategy extends DaoGenStrategy {

	private Logger logger_ = Logger.getLogger(AnnotationDaoGenStrategy.class);

	private static FilenameFilter daoFileFilter_ = new DAOFilenameFilter();

	private static final String DOTYPE_WHOLE_CLASS_NAME = DOType.class
			.getName();
	private static final String DOTYPE_CLASS_NAME = DOType.class
			.getSimpleName();

	private static final String DO_CLASS_NAME = DO.class.getSimpleName();

	private static final String DAO_CLASS_NAME = DAO.class.getSimpleName();

	public static final String DEFAULT_JAVA_SUFFIX = ".java";

	private static final String AT_ANNOTATAION = "@";
	private static final String RETURN_ANNOTATION = "@return";
	private static final String THROWS_ANNOTATION = "@throws";
	private static final String PARAM_ANNOTATION = "@param";

	@Override
	public CodeGenConfigHelper getCodeGenConfigHelperInstance() {
		CodeGenConfigHelper codeConfig = new CodeGenConfigHelper(
				getDaoStringInDir());
		for (IDAOConfigs daoGenConfig : getDaoGenConfigList()) {
			codeConfig.addDaoGenConfig(daoGenConfig);
		}

		return codeConfig;
	}

	/***
	 * Get all java source files in specific directory.
	 * 
	 * @return
	 */
	protected URL[] getDAOJavaFiles() {
		return FileUtils.getUrls(getDaoInDir(), DEFAULT_JAVA_SUFFIX,
				getDAOFileNameFilter());
	}

	/***
	 * Get DAO file name filter.
	 * 
	 * @return
	 */
	protected FilenameFilter getDAOFileNameFilter() {
		return daoFileFilter_;
	}

	/***
	 * Get DaoGenConfig object list.
	 * 
	 * @return
	 */
	protected List<IDAOConfigs> getDaoGenConfigList() {
		List<IDAOConfigs> list = new ArrayList<IDAOConfigs>();
		URL[] files = getDAOJavaFiles();
		if (files != null) {
			logger_.info("find " + files.length + " *.java files in "
					+ getDaoStringInDir());

			// String file = null;
			// String className = null;
			IDAOConfigs daoGenConfig = null;
			for (URL url : files) {
				daoGenConfig = getDaoGenConfig(url);
				if (daoGenConfig != null) {
					list.add(daoGenConfig);
				}
			}
		}
		return list;
	}

	/***
	 * Get class path.
	 * 
	 * @param argUrl
	 * @return
	 */
	protected String getClassPath(URL argUrl) {
		String classPath = argUrl.getFile().substring(
				getDaoStringInDir().length());
		if (classPath.startsWith(separator)) {
			classPath = classPath.substring(separator.length());
		}
		return classPath;
	}

	/***
	 * get DaoGenConfig configuration object by class name.
	 * 
	 * @param argDaoClass
	 * @return
	 */
	protected IDAOConfigs getDaoGenConfig(URL argUrl) {
		IDAOConfigs daoGenConfig = null;
		List<IConfigObject> configs = getConfigs(argUrl);
		if (!configs.isEmpty()) {
			daoGenConfig = DAOConfigs.createInstance();

			for (IConfigObject config : configs) {
				daoGenConfig.setConfigObject(null, config);
			}
		}
		return daoGenConfig;
	}

	/***
	 * Get DAOconfig configuration object by class name.
	 * 
	 * @param argDaoClass
	 * @return DaoConfig
	 */
	protected List<IConfigObject> getConfigs(URL argUrl) {

		List<IConfigObject> configs = new ArrayList<IConfigObject>();
		logger_.debug("resolving class " + argUrl);

		DaoConfig daoConfig = new DaoConfig();
		daoConfig = (DaoConfig) parseJavaFile(argUrl, daoConfig, true);

		// get DOConfig and add it.
		if (daoConfig != null) {
			DOConfig doConfig = getDOConfig(daoConfig);
			if (doConfig != null) {

				// it should be initialized.
				doConfig.initial();
				ICodeGenConfig codeGenConfig = getChangedDO(doConfig);
				if (codeGenConfig != null && codeGenConfig.shouldBeGenerated()) {
					configs.add(codeGenConfig);
				} else {
					configs.add(doConfig);
				}

				// add the default methods for DAOConfig
				if (daoConfig.getMethodList().isEmpty()) {
					ICodeGenConfig obj = doConfig.getSqlMapXmlConfig();
					if (obj instanceof SqlMapXmlConfig) {
						SqlMapXmlConfig sqlMapconfig = (SqlMapXmlConfig) obj;
						addDefaultMethodsForDaoConfig(daoConfig, sqlMapconfig,
								doConfig);
					}
				}
			}

			// if the DAO class contains DAO annotation.
			if (containsAnnotation(daoConfig, DAO_CLASS_NAME)
					|| containsAnnotation(daoConfig, DO_CLASS_NAME)) {
				configs.add(daoConfig);
				if (containsAnnotation(daoConfig, DAO_CLASS_NAME)) {
					List<? extends IConfigObject> classConfigs = getChangedConfigs(daoConfig);
					if (!classConfigs.isEmpty()) {
						configs.addAll(classConfigs);
					}
				}
			}

			if (!DAOServiceConfigurationManager
					.getBooleanProperty(CODEGEN_HOLD_ANNOTATIONS_FOR_DAO)) {
				if (containsAnnotation(daoConfig, DAO_CLASS_NAME)
						|| containsAnnotation(daoConfig, DO_CLASS_NAME)) {
					daoConfig.setOverride();
				}
				// daoConfig.clearAnnotations();
				daoConfig.removeAnnotation(DO_CLASS_NAME);
				daoConfig.removeAnnotation(DAO_CLASS_NAME);
			}
		}
		return configs;
	}

	/***
	 * Get all configurations which have been changed.
	 * 
	 * @param argCodeGenConfig
	 * @return all changed configuration objects.
	 */
	protected List<IConfigObject> getChangedConfigs(
			IClassCodeGenConfig argCodeGenConfig) {
		List<IConfigObject> classConfigs = new ArrayList<IConfigObject>();

		int[] interfaceTypes = { MANAGER_TYPE, AO_TYPE };
		IClassCodeGenConfig config = null;
		for (int i = 0; i < interfaceTypes.length; i++) {
			config = getChangedInreface(argCodeGenConfig, interfaceTypes[i]);
			if (config != null) {
				classConfigs.add(config);
			}
		}

		int[] implementTypes = { DAO_TYPE, MANAGER_TYPE, AO_TYPE };
		for (int i = 0; i < implementTypes.length; i++) {
			config = getChangedImplement(argCodeGenConfig, implementTypes[i]);
			if (config != null) {
				classConfigs.add(config);
			}
		}

		return classConfigs;
	}

	private static int DAO_TYPE = 0;
	private static int MANAGER_TYPE = 1;
	private static int AO_TYPE = 2;

	/**
	 * Get interface configurations which has been changed. Like MANAGER, AO.
	 * 
	 * @param argConfigType
	 * @param argClassType
	 * @return
	 */
	protected IClassCodeGenConfig getChangedInreface(
			IClassCodeGenConfig argCodeGenConfig, int argClassType) {
		String packageKey = null;
		String suffixKey = null;
		String outputPackage = null;

		if (MANAGER_TYPE == argClassType) {
			packageKey = CODEGEN_MANAGER_PACKAGE_PATH;
			suffixKey = CODEGEN_DAO_MANAGER_SUFFIX;
			outputPackage = getManagerStringOutDir();
		} else if (AO_TYPE == argClassType) {
			packageKey = CODEGEN_AO_PACKAGE_PATH;
			suffixKey = CODEGEN_DAO_AO_SUFFIX;
			outputPackage = getAoStringOutDir();
		} else {
			logger_.error("It's unknow class type for the " + argClassType);
			return null;
		}

		String packagePath = CodeGenUtils.getRelativePackageForClass(
				argCodeGenConfig.getPackagePath(), packageKey);
		String argFileName = CodeGenUtils.getRelativeClassName(
				argCodeGenConfig, CODEGEN_DAO_DAO_SUFFIX, suffixKey);
		String fileOutputPath = CodeGenUtils.getFileOutputPath(outputPackage,
				packagePath, argFileName, argCodeGenConfig.getFileType());

		// get the Manager or AO file path
		File file = new File(fileOutputPath);
		IClassCodeGenConfig interfaceConfig = null;
		if (file.exists()) {
			URL url = null;
			try {
				url = file.toURI().toURL();
			} catch (MalformedURLException e) {
				logger_.error("can't convert the file: " + file
						+ " to url path.", e);
			}
			if (url != null) {

				if (MANAGER_TYPE == argClassType) {
					interfaceConfig = new ManagerConfig();
				} else if (AO_TYPE == argClassType) {
					interfaceConfig = new AOConfig();
				}

				// parse the java file which has been generated.
				interfaceConfig = compareConfigs(argCodeGenConfig,
						interfaceConfig, url);
			}
		} else {
			logger_.warn("The file doesn't exist: " + fileOutputPath);
		}
		return interfaceConfig;
	}

	/***
	 * Get changed DOConfig if the column has been changed in database.
	 * 
	 * @param argDOConfig
	 * @return appended DOConfig
	 */
	public DOConfig getChangedDO(DOConfig argDOConfig) {
		DOConfig doconfig = null;
		String packagePath = argDOConfig.getPackagePath();
		String fileName = argDOConfig.getName();

		String fileOutputPath = CodeGenUtils.getFileOutputPath(
				getDoStringOutDir(), packagePath, fileName,
				argDOConfig.getFileType());

		File file = new File(fileOutputPath);
		if (file.exists()) {
			URL url = null;
			try {
				url = file.toURI().toURL();
			} catch (MalformedURLException e) {
				logger_.error("can't convert the file: " + file
						+ " to url path.", e);
			}
			if (url != null) {
				doconfig = new DOConfig();
				doconfig = (DOConfig) compareConfigs(argDOConfig, doconfig, url);
			}

		} else {
			logger_.warn("The file doesn't exist: " + fileOutputPath);
		}
		return doconfig;
	}

	/**
	 * Get interface configurations which has been changed. Like MANAGER, AO.
	 * 
	 * @param argConfigType
	 * @param argClassType
	 * @return
	 */
	protected IClassCodeGenConfig getChangedImplement(
			IClassCodeGenConfig argCodeGenConfig, int argClassType) {
		String packageKey = null;
		String suffixKey = null;
		String outputPackage = null;
		if (MANAGER_TYPE == argClassType) {
			packageKey = CODEGEN_MANAGER_PACKAGE_PATH;
			suffixKey = CODEGEN_DAO_MANAGER_SUFFIX;
			outputPackage = getManagerStringOutDir();
		} else if (AO_TYPE == argClassType) {
			packageKey = CODEGEN_AO_PACKAGE_PATH;
			suffixKey = CODEGEN_DAO_AO_SUFFIX;
			outputPackage = getAoStringOutDir();
		} else if (DAO_TYPE == argClassType) {
			outputPackage = getDaoStringInDir();
		} else {
			logger_.error("It's unknow class type for the " + argClassType);
			return null;
		}

		String packagePath = null;
		String fileName = null;
		if (DAO_TYPE != argClassType) {
			packagePath = CodeGenUtils.getRelativePackageForClass(
					argCodeGenConfig.getPackagePath(), packageKey);
			fileName = CodeGenUtils.getRelativeClassName(argCodeGenConfig,
					CODEGEN_DAO_DAO_SUFFIX, suffixKey);

			packagePath = CodeGenUtils.getRelativePackagePath(packagePath);
			fileName = CodeGenUtils.getClassName(fileName);
		} else {
			packagePath = CodeGenUtils.getRelativePackagePath(argCodeGenConfig);
			fileName = CodeGenUtils.getClassName(argCodeGenConfig.getName());
		}

		String fileOutputPath = CodeGenUtils.getFileOutputPath(outputPackage,
				packagePath, fileName, argCodeGenConfig.getFileType());

		// get the Manager or AO file path
		File file = new File(fileOutputPath);
		IClassCodeGenConfig implementConfig = null;
		if (file.exists()) {
			URL url = null;
			try {
				url = file.toURI().toURL();
			} catch (MalformedURLException e) {
				logger_.error("can't convert the file: " + file
						+ " to url path.", e);
			}
			if (url != null) {
				if (DAO_TYPE == argClassType) {
					implementConfig = new DaoImplConfig();
				} else if (MANAGER_TYPE == argClassType) {
					implementConfig = new ManagerImplConfig();
				} else if (AO_TYPE == argClassType) {
					implementConfig = new AOImplConfig();
				}
				implementConfig = compareConfigs(argCodeGenConfig,
						implementConfig, url);
			}
		} else {
			logger_.warn("The file doesn't exist: " + fileOutputPath);
		}

		return implementConfig;
	}

	/**
	 * Compare the old and new configurations, and append the difference;
	 * 
	 * @param argNewConfig
	 * @param argResultconfig
	 * @return
	 */
	private IClassCodeGenConfig compareConfigs(
			IClassCodeGenConfig argNewConfig,
			IClassCodeGenConfig argResultconfig, URL argFileUrl) {
		logger_.debug("resolving class " + argFileUrl);
		// parse the java file which has been generated.
		argResultconfig = parseJavaFile(argFileUrl, argResultconfig, false);
		List<IConfigObject> differentConfigs = CodeGenUtils
				.getDifferentConfigs(argNewConfig, argResultconfig);

		// if it has different(added) methods
		if (!differentConfigs.isEmpty()) {

			// clear the manager configuration.
			argResultconfig = clearClassElement(argResultconfig);
			IConfigObject differentConfig = null;
			for (IConfigObject config : differentConfigs) {
				try {
					differentConfig = (IConfigObject) config.clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				argResultconfig.setConfigObject(null, differentConfig);
			}

			// set the append model.
			argResultconfig.setAppendModel();
		} else {
			// argResultconfig = argNewConfig; ??
			argResultconfig.setDoNothing();
		}
		return argResultconfig;
	}

	/****
	 * clear class element for the class configuration.
	 * 
	 * @param argClassCodeGenConfig
	 * @return the class configuration which has been cleared.
	 */
	protected IClassCodeGenConfig clearClassElement(
			IClassCodeGenConfig argClassCodeGenConfig) {
		argClassCodeGenConfig.getMethodList().clear();
		argClassCodeGenConfig.clearImportClasses();
		argClassCodeGenConfig.getFieldList().clear();
		return argClassCodeGenConfig;
	}

	/***
	 * Parse the file with the .java suffix.
	 * 
	 * @param argUrl
	 *            The URL is the file path.
	 * @param classCodeGenConfig
	 * @param argCheckDAOAnnotation
	 *            whether to check the annotation in class file.
	 * @return argClassCodeGenConfig
	 */
	protected IClassCodeGenConfig parseJavaFile(URL argUrl,
			IClassCodeGenConfig argClassCodeGenConfig,
			boolean argCheckDAOAnnotation) {
		IClassCodeGenConfig classCodeGenConfig = argClassCodeGenConfig;

		if (classCodeGenConfig == null) {
			return null;
		}
		try {
			CompilationUnit parser = JavaParser.parse(argUrl.openStream());

			if (argCheckDAOAnnotation && !isFitForDaoGen(parser)) {
				return null;
			}
			// set package path and class name.
			String packagePath = getPackege(parser.getPackage().toString());
			classCodeGenConfig.setPackagePath(packagePath);

			// get import classes.
			Map<String, String> importClasses = new HashMap<String, String>();
			List<ImportDeclaration> imports = parser.getImports();
			if (imports != null) {
				String tempImport = null;
				for (ImportDeclaration im : imports) {
					tempImport = CodeGenUtils.getImportStatement(im.toString());

					// don't import the DOType annotation. Because it will clear
					// DOType annotations after generate codes
					if (tempImport.indexOf(DOTYPE_WHOLE_CLASS_NAME) < 0
							&& needImport(tempImport)) {
						importClasses
								.put(im.getName().getName(), im.toString());
						classCodeGenConfig.setConfigObject(
								ImportConfig.MAIN_TAG, new ImportConfig(
										tempImport));
					}
				}
			}

			CommentConfig classComment = new CommentConfig();
			classCodeGenConfig.setConfigObject(CommentConfig.MAIN_TAG,
					classComment);

			List<TypeDeclaration> types = parser.getTypes();

			if (types != null) {
				for (TypeDeclaration type : types) {
					if (type instanceof ClassOrInterfaceDeclaration) {
						if (type.getJavaDoc() != null) {

							// Set class's comment
							classComment.setValue(type.getJavaDoc().toString());
						}
						classComment.setOriginal(true);

						ClassOrInterfaceDeclaration c = (ClassOrInterfaceDeclaration) type;
						classCodeGenConfig.setName(c.getName());

						// add annotations
						addAnnotationForConfig(c.getAnnotations(),
								classCodeGenConfig);

						// set concrete class.
						classCodeGenConfig
								.setConcreteClass(getAnnotationAttributeValue(
										classCodeGenConfig, DAO_CLASS_NAME,
										"concreteClass"));

						// Set generic types
						List<TypeParameter> typeParameters = c
								.getTypeParameters();
						if (typeParameters != null) {
							for (TypeParameter typeParameter : c
									.getTypeParameters()) {
								// set generic type.
								classCodeGenConfig.setGenericType(typeParameter
										.toString());
							}
						}

						// parse methods.
						parseClassElements(type, classCodeGenConfig,
								importClasses);
					}
				}
			}

		} catch (Exception e) {
			/* don't care */
			logger_.error(
					"It make an error when parsing the file:"
							+ argUrl.getFile(), e);
		}

		return classCodeGenConfig;
	}

	/***
	 * Parse the methods for the class.
	 * 
	 * @param argType
	 * @param argDaoConfig
	 * @param argImportClasses
	 */
	protected void parseClassElements(TypeDeclaration argType,
			IClassCodeGenConfig argDaoConfig,
			Map<String, String> argImportClasses) {
		List<BodyDeclaration> bodys = argType.getMembers();
		if (bodys != null) {

			// Get DO class name.
			String doPackagePath = getRelativePackageForClass(
					argDaoConfig.getPackagePath(), CODEGEN_DO_PACKAGE_PATH);
			String doName = getDOName(argDaoConfig.getName());
			String doClassName = doPackagePath + "." + doName;

			for (BodyDeclaration bodyDeclar : bodys) {
				if (bodyDeclar instanceof MethodDeclaration) {

					MethodDeclaration md = (MethodDeclaration) bodyDeclar;

					// method's name
					String methodName = md.getName();

					// comment's map
					Map<String, String> commentsMap = getCommentForClassElement(md);

					MethodConfig methodConfig = new MethodConfig();
					methodConfig.setName(methodName);

					// add annotations for the method
					addAnnotationForConfig(md.getAnnotations(), methodConfig);

					String returnType = getReturnType(methodConfig, doClassName);
					String mdReturnType = md.getType().toString();

					if (!StringUtils.isEmpty(returnType)) {
						returnType = getClassNameFromImportClasses(returnType,
								argImportClasses);
						methodConfig.setReturnType(returnType);
						argDaoConfig.setOverride();
					} else {
						returnType = getClassNameFromImportClasses(
								mdReturnType, argImportClasses);
						methodConfig.setReturnType(mdReturnType);
					}

					// add import class for all elements in class.
					addImportClassForConfig(methodConfig, returnType,
							argImportClasses);

					// Set method's comment and return's comment.
					CommentConfig methodComment = new CommentConfig();
					methodComment.setValue(commentsMap.get(METHOD_COMMENT));
					methodConfig.setConfigObject(CommentConfig.MAIN_TAG,
							methodComment);
					methodConfig.setReturnComment(commentsMap
							.get(RETURN_COMMENT));

					parseParameters(md, methodConfig, commentsMap, doClassName,
							argDaoConfig, argImportClasses);

					parseExceptions(md, argDaoConfig, methodConfig,
							commentsMap, doClassName);

					BlockStmt body = md.getBody();
					if (body != null) {
						String statement = body.toString();
						if (!StringUtils.isEmpty(statement)) {
							BodyConfig bodyConfig = new BodyConfig();
							statement = CodeGenUtils.clearBrace(statement);
							bodyConfig.setValue(statement);
							methodConfig.setConfigObject(BodyConfig.MAIN_TAG,
									bodyConfig);
						}
					}

					// add default exception
					// addDefaultException(methodConfig,
					// CODEGEN_DAO_DEFAULT_EXCEPTION);

					methodConfig.clearAnnotations();

					// add method configuration into the daoConfig
					argDaoConfig.setConfigObject(MethodConfig.MAIN_TAG,
							methodConfig);
				} else if (bodyDeclar instanceof FieldDeclaration) {
					FieldConfig fieldConfig = new FieldConfig();

					FieldDeclaration fd = (FieldDeclaration) bodyDeclar;
					String modify = ModifierSet.getModify(fd.getModifiers());
					String className = fd.getType().toString();
					if (argImportClasses.containsKey(className)) {
						className = argImportClasses.get(className);
					}
					fieldConfig.setModify(modify);
					fieldConfig.setClassName(className);

					List<VariableDeclarator> list = fd.getVariables();
					if (list != null) {
						for (VariableDeclarator vd : list) {
							String name = vd.getId().getName();
							String value = null;
							if (vd.getInit() != null) {
								value = vd.getInit().toString();
							}
							fieldConfig.setName(name);
							if (!StringUtils.isEmpty(value)) {
								fieldConfig.setValue(value);
							}
						}
					}

					argDaoConfig.setConfigObject(FieldConfig.MAIN_TAG,
							fieldConfig);
				}
			}
		}
	}

	/***
	 * Parse the parameters for the method.
	 * 
	 * @param argMethodDeclaration
	 * @param argMethodConfig
	 * @param argCommentsMap
	 * @param argDoClassName
	 * @param argDaoConfig
	 * @param argImportClasses
	 */
	protected void parseParameters(MethodDeclaration argMethodDeclaration,
			MethodConfig argMethodConfig, Map<String, String> argCommentsMap,
			String argDoClassName, IClassElementCodeGenConfig argDaoConfig,
			Map<String, String> argImportClasses) {
		// add parameter configuration into the method
		// configuration.
		List<Parameter> parameters = argMethodDeclaration.getParameters();
		if (parameters != null) {
			String doName = argDoClassName;
			int index = argDoClassName.lastIndexOf(".");
			if (index > 0) {
				doName = argDoClassName.substring(index + 1);
			}

			for (Parameter parameter : parameters) {
				ParameterConfig parameterConfig = new ParameterConfig();
				addAnnotationForConfig(parameter.getAnnotations(),
						parameterConfig);

				String parameterName = parameter.getId().getName();

				// Set parameter class type.
				String returnType = getReturnType(parameterConfig,
						argDoClassName);
				String parameterType = parameter.getType().toString();

				if (!StringUtils.isEmpty(returnType)) {
					returnType = getClassNameFromImportClasses(returnType,
							argImportClasses);
					parameterConfig.setClassName(returnType);
					parameterConfig.setName(doName);
					argDaoConfig.setOverride();

				} else {
					parameterType = getClassNameFromImportClasses(
							parameterType, argImportClasses);
					parameterConfig.setClassName(parameterType);
					parameterConfig.setName(parameterName);
				}

				// set parameter configuration's
				// comment.
				CommentConfig parameterComment = new CommentConfig();
				parameterComment.setValue(argCommentsMap.get(parameterName));

				parameterConfig.setConfigObject(CommentConfig.MAIN_TAG,
						parameterComment);
				argMethodConfig.setConfigObject(ParameterConfig.MAIN_TAG,
						parameterConfig);
				parameterConfig.clearAnnotations();

			}
		}
	}

	/***
	 * Parse the exceptions for the method.
	 * 
	 * @param argMethodDeclaration
	 * @param argMethodConfig
	 * @param argCommentsMap
	 */
	protected void parseExceptions(MethodDeclaration argMethodDeclaration,
			IClassCodeGenConfig argDaoConfig, MethodConfig argMethodConfig,
			Map<String, String> argCommentsMap, String argDoClassName) {

		// add exception configuration into the method
		// configuration.
		List<NameExpr> throwsExc = argMethodDeclaration.getThrows();
		if (throwsExc != null) {
			for (NameExpr throwExpr : argMethodDeclaration.getThrows()) {
				ThrowExceptionConfig exceptionConifg = new ThrowExceptionConfig();
				String exceptionName = throwExpr.getName();
				exceptionConifg.setClassName(getClassNameFromImports(
						argDaoConfig, exceptionName));

				// set exception configuration's
				// comment.
				CommentConfig exceptinComment = new CommentConfig();
				exceptinComment.setValue(argCommentsMap.get(exceptionName));

				exceptionConifg.setConfigObject(CommentConfig.MAIN_TAG,
						exceptinComment);
				argMethodConfig.setConfigObject(ThrowExceptionConfig.MAIN_TAG,
						exceptionConifg);
			}
		}
	}

	/***
	 * Add import classes for the configuration.
	 * 
	 * @param argClassElementCodeGenConfig
	 * @param argClassName
	 * @param argImportClasses
	 */
	protected void addImportClassForConfig(
			IClassElementCodeGenConfig argClassElementCodeGenConfig,
			String argClassName, Map<String, String> argImportClasses) {
		String className = CodeGenUtils
				.getShortClassNameWithoutGenericType(argClassName);
		if (argImportClasses.containsKey(className)) {
			className = CodeGenUtils
					.getClassNameFromImportStatement(argImportClasses
							.get(className));
			if (!ObjectUtils.isCommonLangPackageClass(className)) {
				argClassElementCodeGenConfig.getImportList().add(
						new ImportConfig(className.trim()));
			}
		}
	}

	/***
	 * Get class name from the imported classes map.
	 * 
	 * @param argName
	 * @param argImportClasses
	 * @return class name in imported classes map.
	 */
	protected String getClassNameFromImportClasses(String argName,
			Map<String, String> argImportClasses) {
		String name = CodeGenUtils.getShortClassNameWithoutGenericType(argName);
		if (argImportClasses.containsKey(name)) {
			String className = CodeGenUtils
					.getClassNameFromImportStatement(argImportClasses.get(name));
			int index = argName.indexOf("<");
			if (index >= 0) {
				className += argName.substring(index);
			}

			return className;
		} else {
			return argName;
		}
	}

	/***
	 * Get return type for the configuration which contains <code>DOType</code>
	 * annotation.
	 * 
	 * @return return type
	 */
	protected String getReturnType(IClassElementCodeGenConfig argConfigObject,
			String doClassName) {
		boolean containsDOType = containsAnnotation(argConfigObject,
				DOTYPE_CLASS_NAME);
		String returnType = null;
		if (containsDOType) {
			String collectionType = getAnnotationAttributeValue(
					argConfigObject, DOTYPE_CLASS_NAME, "collectionType");

			// if not indicate specific collection type, then set the DO class
			// as return type.
			if (StringUtils.isEmpty(collectionType)) {
				returnType = doClassName;
			} else {
				returnType = collectionType + "<" + doClassName + ">";
			}
		} else {
			returnType = EMPTY;
		}

		return returnType;
	}

	/***
	 * Check the parsing class is fit for DAO generation.
	 * 
	 * @param argCompilationUnit
	 * @return true if fit, or else.
	 */
	protected boolean isFitForDaoGen(CompilationUnit argCompilationUnit) {
		boolean result = false;

		if (argCompilationUnit != null) {
			List<TypeDeclaration> types = argCompilationUnit.getTypes();
			if (types != null) {
				String annotationName = null;
				for (TypeDeclaration type : types) {
					if (type instanceof ClassOrInterfaceDeclaration) {
						ClassOrInterfaceDeclaration c = (ClassOrInterfaceDeclaration) type;
						List<AnnotationExpr> annotationList = c
								.getAnnotations();
						if (annotationList != null) {
							for (AnnotationExpr annotation : annotationList) {
								annotationName = annotation.getName().getName();
								if (DAO_CLASS_NAME.equals(annotationName)
										|| DO_CLASS_NAME.equals(annotationName)) {
									result = true;
									break;
								}
							}
						}
						break;
					}
				}
			}
		}
		return result;
	}

	/***
	 * Get package path.
	 * 
	 * @param argPackage
	 *            package name.
	 * @return package path
	 */
	protected String getPackege(String argPackage) {
		String packagePath = argPackage;

		if (!StringUtils.isEmpty(packagePath)) {
			packagePath = CodeGenUtils.clearNewlineSymbol(argPackage);
			int index = packagePath.indexOf("package ");
			if (index >= 0) {
				packagePath = packagePath
						.substring(index + "package ".length());
			}

			if (packagePath.lastIndexOf(SEMICOLON) >= 0) {
				packagePath = packagePath
						.substring(0, packagePath.length() - 1);
			}
		}
		return packagePath;
	}

	/***
	 * Set annotations for the configuration object.
	 * 
	 * @param argAnnotationList
	 *            annotation list.
	 * @param argConfigObject
	 *            configuration object.
	 */
	protected void addAnnotationForConfig(
			List<AnnotationExpr> argAnnotationList,
			ICodeGenConfig argConfigObject) {
		if (argAnnotationList != null) {
			for (AnnotationExpr annotation : argAnnotationList) {
				AnnotationConfig annotationConfig = new AnnotationConfig();
				annotationConfig.setName(annotation.getName().getName());
				if (annotation instanceof NormalAnnotationExpr) {
					List<MemberValuePair> keyValues = ((NormalAnnotationExpr) annotation)
							.getPairs();
					if (keyValues != null) {
						for (MemberValuePair valuePair : keyValues) {
							annotationConfig.addKey(valuePair.getName());
							annotationConfig.addValue(CodeGenUtils
									.clearDoubleQuotation(valuePair.getValue()
											.toString()));
						}
					}
				}
				argConfigObject.setConfigObject(AnnotationConfig.MAIN_TAG,
						annotationConfig);
			}
		}
	}

	/***
	 * check whether the CodeGenConfig contains the annotation of specific name.
	 * 
	 * @param argConfigObject
	 * @param argAnnotationName
	 * @return true if contains, or else.s
	 */
	protected boolean containsAnnotation(
			IClassElementCodeGenConfig argConfigObject, String argAnnotationName) {
		boolean result = false;
		for (AnnotationConfig annotation : argConfigObject.getAnnotations()) {
			if (annotation.getName().equals(argAnnotationName)) {
				result = true;
				break;
			}
		}

		return result;
	}

	/***
	 * Get value in the annotation for the code generation configuration.
	 * 
	 * @param argCodeGenConfig
	 *            code generation configuration
	 * @param argAnnotationName
	 *            annotation name.
	 * @param argAttributeName
	 *            annotation's attribute.
	 * @return value of the annotation's attribute.
	 */
	protected String getAnnotationAttributeValue(
			IClassElementCodeGenConfig argCodeGenConfig,
			String argAnnotationName, String argAttributeName) {
		String result = EMPTY;
		if (!argCodeGenConfig.getAnnotations().isEmpty()) {
			for (AnnotationConfig annotation : argCodeGenConfig
					.getAnnotations()) {
				if (argAnnotationName.equals(annotation.getName())) {
					result = annotation.getAttribute(argAttributeName);
					break;
				}
			}
		}
		return result;
	}

	/***
	 * Set default exception for the method configuration.
	 * 
	 * @param argMethodConfig
	 * @param argExceptionKey
	 */
	protected void addDefaultException(MethodConfig argMethodConfig,
			String argExceptionKey) {
		String exception = DAOServiceConfigurationManager
				.getProperty(argExceptionKey);
		if (!StringUtils.isEmpty(exception)) {
			boolean hasSameException = false;
			String[] exceptions = exception.split(",");
			for (String tempException : exceptions) {
				for (ThrowExceptionConfig exceptionConfig : argMethodConfig
						.getExceptionList()) {
					if (tempException.equals(exceptionConfig.getClassName())) {
						hasSameException = true;
						break;
					}
				}

				if (!hasSameException) {
					ThrowExceptionConfig config = new ThrowExceptionConfig();
					config.setClassName(tempException);
					argMethodConfig.setConfigObject(
							ThrowExceptionConfig.MAIN_TAG, config);
				}
			}
		}
	}

	/***
	 * check to see whether the argClass is needed imported according to the
	 * regular expression.
	 * 
	 * @param argClass
	 * @return true if need or false.
	 */
	protected boolean needImport(String importStatement) {
		return DAOServiceConfigurationManager
				.getBooleanProperty(CODEGEN_HOLD_ANNOTATIONS_FOR_DAO)
				|| CodeGenUtils.needImportClass(importStatement);
	}

	/***
	 * Get whole class name from the import statement.
	 * 
	 * @param argDaoConfig
	 * @param argException
	 * @return
	 */
	private String getClassNameFromImports(IClassCodeGenConfig argDaoConfig,
			String argException) {
		String result = EMPTY;
		String wholeClassName = null;
		for (String importClass : argDaoConfig.getAllImportClass()) {
			wholeClassName = CodeGenUtils
					.getImportClassFromImportStatement(importClass);
			if (wholeClassName.endsWith(argException)) {
				result = wholeClassName;
				break;
			}
		}

		return result;
	}

	/***
	 * Get DAOconfig configuration object by class name.
	 * 
	 * @param argDaoClass
	 * @return DOConfig
	 */
	protected DOConfig getDOConfig(IClassElementCodeGenConfig argCodeGenConfig) {
		DOConfig doConfig = null;

		// if the configuration contains DO annotation.
		if (containsAnnotation(argCodeGenConfig, DO_CLASS_NAME)) {
			String tableName = getAnnotationAttributeValue(argCodeGenConfig,
					DO_CLASS_NAME, "table");
			if (!StringUtils.isEmpty(tableName)) {
				List<IColumnInfo> columnList = ColumnMetaDataMapping
						.getInstance().getColumnsInfo(tableName);

				if (!columnList.isEmpty()) {
					String packagePath = EMPTY;
					String className = EMPTY;

					packagePath = getRelativePackageForClass(
							argCodeGenConfig.getPackagePath(),
							CODEGEN_DO_PACKAGE_PATH);
					className = getDOName(argCodeGenConfig.getName());

					// add Do configuration
					doConfig = new DOConfig();
					doConfig.setPackagePath(packagePath);
					doConfig.setName(className);
					doConfig.setTable(tableName);
					doConfig.setColumnList(columnList);
					doConfig.setDaoName(argCodeGenConfig.getName());
				}
			}
		}

		return doConfig;
	}

	/***
	 * Get DO class name.
	 * 
	 * @param argDaoClass
	 * @return
	 */
	private String getDOName(String argDaoClass) {
		String className = null;
		String daoClassName = argDaoClass;
		int index = daoClassName.indexOf(DAOServiceConfigurationManager
				.getProperty(CODEGEN_DAO_DAO_SUFFIX));
		if (index > 0) {
			className = daoClassName.substring(0, index)
					+ DAOServiceConfigurationManager
							.getProperty(CODEGEN_DAO_DO_SUFFIX);
		} else {
			className = daoClassName
					+ DAOServiceConfigurationManager
							.getProperty(CODEGEN_DAO_DO_SUFFIX);
		}

		return className;
	}

	/**
	 * get relative package for DO
	 */
	protected String getRelativePackageForClass(String argDAOPackage,
			String argPackageKey) {
		return CodeGenUtils.getRelativePackageForClass(argDAOPackage,
				argPackageKey);
	}

	public static final String METHOD_COMMENT = "methodComment";
	public static final String RETURN_COMMENT = "returnComment";

	/***
	 * Get comments map from the comments for class elements.
	 * 
	 * @param argBodyDeclaration
	 * @return
	 */
	protected Map<String, String> getCommentForClassElement(
			BodyDeclaration argBodyDeclaration) {
		Map<String, String> parameterMap = null;
		if (argBodyDeclaration.getJavaDoc() == null) {
			parameterMap = new HashMap<String, String>();
		} else {
			parameterMap = getCommentForClassElement(argBodyDeclaration
					.getJavaDoc().getContent());
		}
		return parameterMap;
	}

	/***
	 * Get comments map from the comments in method.
	 * 
	 * @param argWholdMethodComment
	 * @return comments map
	 */
	protected Map<String, String> getCommentForClassElement(
			String argWholdMethodComment) {
		if (StringUtils.isEmpty(argWholdMethodComment)) {
			return null;
		}

		Map<String, String> parameterMap = new HashMap<String, String>();
		String[] comments = StringUtils.split(argWholdMethodComment, '*');

		// Method's comment
		StringBuffer methdComment = new StringBuffer();
		// the return's comment
		StringBuffer returnComment = null;

		boolean hasSingle = false;
		boolean hasParam = false;
		boolean hasEcp = false;
		boolean hasReturn = false;

		String tempComment = null;
		String paramName = null;
		for (int i = 0; i < comments.length; i++) {
			tempComment = comments[i].trim();
			if (!(LINE_BREAK.equals(tempComment) || TAB.equals(tempComment) || ENTER
					.equals(tempComment))) {
				if (tempComment.startsWith(PARAM_ANNOTATION) || hasParam) {
					hasSingle = true;
					hasParam = true;
					if (i < comments.length - 1
							&& comments[i + 1].trim()
									.startsWith(AT_ANNOTATAION)) {
						hasParam = false;
					}

					if (tempComment.startsWith(PARAM_ANNOTATION)) {
						String[] paramAndComments = StringUtils.split(
								tempComment, SPACE_CHAR);
						if (paramAndComments.length > 1) {
							paramName = paramAndComments[1];
							parameterMap.put(paramName, StringUtils.join(
									paramAndComments, EMPTY, 2,
									paramAndComments.length - 1));
						}
					} else {
						parameterMap.put(paramName, parameterMap.get(paramName)
								+ LINE_BREAK + tempComment);
					}
				} else if (tempComment.startsWith(THROWS_ANNOTATION) || hasEcp) {
					hasSingle = true;
					hasEcp = true;
					if (i < comments.length - 1
							&& comments[i + 1].trim()
									.startsWith(AT_ANNOTATAION)) {
						hasEcp = false;
					}

					if (tempComment.startsWith(THROWS_ANNOTATION)) {
						String[] paramAndComments = StringUtils.split(
								tempComment, SPACE_CHAR);
						if (paramAndComments.length > 1) {
							paramName = paramAndComments[1];
							parameterMap.put(paramName, StringUtils.join(
									paramAndComments, EMPTY, 2,
									paramAndComments.length - 1));
						}
					} else {
						parameterMap.put(paramName, parameterMap.get(paramName)
								+ LINE_BREAK + tempComment);
					}
				} else if (tempComment.startsWith(RETURN_ANNOTATION)
						|| hasReturn) {
					hasSingle = true;
					hasReturn = true;
					if (i < comments.length - 1
							&& comments[i + 1].trim()
									.startsWith(AT_ANNOTATAION)) {
						hasReturn = false;
					}

					if (tempComment.startsWith(RETURN_ANNOTATION)) {
						returnComment = new StringBuffer();
						returnComment.append(tempComment.substring(
								RETURN_ANNOTATION.length()).trim());
					} else {
						returnComment.append(LINE_BREAK);
						returnComment.append(tempComment);
					}
				} else if (!tempComment.startsWith(AT_ANNOTATAION)
						&& !hasSingle) {
					if (methdComment.length() > 0) {
						methdComment.append(LINE_BREAK);
					}
					methdComment.append(tempComment);
				}
			}
		}
		parameterMap.put(METHOD_COMMENT, methdComment.toString());

		if (returnComment != null) {
			parameterMap.put(RETURN_COMMENT, returnComment.toString());
		}
		return parameterMap;
	}

	/***
	 * Add defaults methods for DAOconfig
	 * 
	 * @param classCodeGenConfig
	 *            DAOconfig
	 * @param sqlMapconfig
	 *            SQL-map information.
	 * @param argDoConfig
	 *            DOconfig
	 */
	protected void addDefaultMethodsForDaoConfig(
			IClassCodeGenConfig argClassCodeGenConfig,
			SqlMapXmlConfig argSqlMapconfig, DOConfig argDoConfig) {
		argSqlMapconfig.initial();

		// add insert method
		String insertMethodName = getStatement(argSqlMapconfig
				.getInsertStatementId());
		MethodConfig insertMethod = new MethodConfig();
		addParameter(insertMethod, insertMethodName,
				INSERT_STATEMENT_RETURN_TYPE, argDoConfig);
		argClassCodeGenConfig.setConfigObject(MethodConfig.MAIN_TAG,
				insertMethod);

		// add update method
		String updateMethodName = getStatement(argSqlMapconfig
				.getUpdateStatementId());
		MethodConfig updateMethod = new MethodConfig();
		addParameter(updateMethod, updateMethodName,
				UPDATE_STATEMENT_RETURN_TYPE, argDoConfig);
		argClassCodeGenConfig.setConfigObject(MethodConfig.MAIN_TAG,
				updateMethod);

		// add selectById method
		String selectByIdMethodName = getStatement(argSqlMapconfig
				.getSelectByIdStatementId());
		MethodConfig selectByIdMethod = new MethodConfig();
		setDefaulMethodInfo(selectByIdMethod, selectByIdMethodName,
				argDoConfig.getClassName());
		addDefaultParameterInfo(selectByIdMethod, "id",
				INSERT_STATEMENT_RETURN_TYPE);
		argClassCodeGenConfig.setConfigObject(MethodConfig.MAIN_TAG,
				selectByIdMethod);

		// add pagination method
		String paginMethodName = getStatement(argSqlMapconfig
				.getPaginStatementId());
		MethodConfig paginMethod = new MethodConfig();
		setDefaulMethodInfo(
				paginMethod,
				paginMethodName,
				SELECT_PAGIN_RETURN_COLLECTION_TYPE + "<"
						+ argDoConfig.getName() + ">");
		addDefaultParameterInfo(paginMethod, "params",
				"java.util.Map<Object, Object>");
		addDefaultParameterInfo(paginMethod, "pageNum", "String");
		addDefaultParameterInfo(paginMethod, "pageSize", "String");

		argClassCodeGenConfig.setConfigObject(paginMethod);
	}

	/***
	 * Add parameter configuration object for the method configuration object.
	 * 
	 * @param argMethodConfig
	 *            method configuration
	 * @param argMethodName
	 *            method name
	 * @param argMethodReturnType
	 *            the return type for the method.
	 * @param argDoConfig
	 *            DO configuration object
	 */
	private void addParameter(MethodConfig argMethodConfig,
			String argMethodName, String argMethodReturnType,
			DOConfig argDoConfig) {
		setDefaulMethodInfo(argMethodConfig, argMethodName, argMethodReturnType);
		addDefaultParameterInfo(argMethodConfig, argDoConfig.getName(),
				argDoConfig.getClassName());
	}

	/***
	 * Set the basic information for the method configuration.
	 * 
	 * @param argMethodConfig
	 *            method configuration
	 * @param argMethodName
	 *            method name
	 * @param argMethodReturnType
	 *            the return type for the method.
	 */
	private void setDefaulMethodInfo(MethodConfig argMethodConfig,
			String argMethodName, String argMethodReturnType) {
		argMethodConfig.setName(argMethodName);
		argMethodConfig.setReturnType(argMethodReturnType);
		CommentConfig methodComment = new CommentConfig();
		methodComment.setValue("Auto generated " + argMethodName
				+ " method by Codegen.");
		argMethodConfig.setConfigObject(methodComment);
	}

	/***
	 * Add the default parameter configuration object for the method
	 * configuration.
	 * 
	 * @param argMethodConfig
	 *            method configuration.
	 * @param argParameterName
	 *            parameter name.
	 * @param argParameterClass
	 *            parameter class name.
	 */
	private void addDefaultParameterInfo(MethodConfig argMethodConfig,
			String argParameterName, String argParameterClass) {
		ParameterConfig param = new ParameterConfig();
		param.setClassName(argParameterClass);
		param.setName(argParameterName);
		argMethodConfig.setConfigObject(param);

		CommentConfig paramComment = new CommentConfig();
		paramComment.setValue("Auto generated by Codegen.");
		param.setConfigObject(paramComment);
	}

	/***
	 * Get statement id for SQL map element id.
	 * 
	 * @param argSqlMapId
	 * @return the simple statement
	 */
	private String getStatement(String argSqlMapId) {
		if (StringUtils.isEmpty(argSqlMapId)) {
			return argSqlMapId;
		} else {
			int index = argSqlMapId.lastIndexOf(".");
			if (index > 0) {
				return argSqlMapId.substring(index + 1);
			} else {
				return argSqlMapId;
			}
		}
	}

	/***
	 * DAO file name filter.
	 * 
	 * @author zhangjun.ht
	 * @created 2011-2-24
	 * @version $Id: AnnotationDaoGenStrategy.java 39 2011-03-03 11:27:46Z guest
	 *          $
	 */
	private static class DAOFilenameFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {
			if (name.endsWith(DEFAULT_JAVA_SUFFIX)) {
				int index = name.lastIndexOf(DEFAULT_JAVA_SUFFIX);
				String fileName = name.substring(0, index);
				return fileName.endsWith(DAOServiceConfigurationManager
						.getProperty(CODEGEN_DAO_DAO_SUFFIX));
			} else {
				return true;
			}
		}

	}

}
