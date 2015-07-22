package com.hitao.codegen.configs.dao;

import static com.hitao.codegen.configs.DAOServiceConfigurationManager.DAO_KEY;
import static com.hitao.codegen.configs.DAOServiceConfigurationManager.DB_ROUTE;
import static com.hitao.codegen.configs.DAOServiceConfigurationManager.DEFAULT_RETURN_VALUE;
import static com.hitao.codegen.configs.DAOServiceConfigurationManager.EXCEPTION_NAME;
import static com.hitao.codegen.configs.DAOServiceConfigurationManager.LOG_KEY;
import static com.hitao.codegen.configs.DAOServiceConfigurationManager.MANAGER_KEY;
import static com.hitao.codegen.configs.DAOServiceConfigurationManager.METHOD_COMMENTS;
import static com.hitao.codegen.configs.DAOServiceConfigurationManager.METHOD_KEY;
import static com.hitao.codegen.configs.DAOServiceConfigurationManager.METHOD_PARMETER;
import static com.hitao.codegen.configs.DAOServiceConfigurationManager.SQL_NAME;
import static com.hitao.codegen.configs.common.AbstractCodeGenConfig.GenerateModel.APPEND;
import static com.hitao.codegen.configs.common.AbstractCodeGenConfig.GenerateModel.DO_NOTHING;
import static com.hitao.codegen.configs.common.ClassElementConfig.getConreteClassStatement;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_AO_LOG_LOGGERFACTORY_CLASS;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_AO_LOG_LOGGER_CLASS;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_AO_PACKAGE_PATH;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_AO_SUFFIX;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_CONFIGS_CONFIG_CLASS;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_DAOIMPL_DBROUTE_STATIC_CLASS;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_DAOIMPL_DEFAULT_EXTENDS_CLASS;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_DAOIMPL_DEFAULT_EXTENDS_CLASS_HAS_GENERIC;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_DAO_SUFFIX;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_DAO_UNITTEST_DEFAULT_EXTENDS_CLASS;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_DEFAULT_EXCEPTION;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DAO_MANAGER_SUFFIX;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DO_PACKAGE_PATH;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_GENERATE_AO_AND_AOIMPL;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_GENERATE_MANAGER_AND_MANAGERIMPL;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_MANAGER_DEFAULT_EXCEPTION;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_MANAGER_PACKAGE_PATH;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_SQLMAP_PACKAGE_PATH;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_WEB_PROJECT_AO_CONFIG_NAME;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_WEB_PROJECT_AUTOCONFIG_FOLDER;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_WEB_PROJECT_DAO_CONFIG_NAME;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_WEB_PROJECT_IBATIS_SQLMAP_CONFIG_DIR;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_WEB_PROJECT_IBATIS_SQLMAP_CONFIG_NAME;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_WEB_PROJECT_MANAGER_CONFIG_NAME;
import static com.hitao.codegen.constent.StringConstants.LINE_BREAK;
import static com.hitao.codegen.util.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hitao.codegen.configs.CodeGenUtils;
import com.hitao.codegen.configs.DAOServiceConfigurationManager;
import com.hitao.codegen.configs.IClassCodeGenConfig;
import com.hitao.codegen.configs.IClassElementCodeGenConfig;
import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.ICodeGenInitial;
import com.hitao.codegen.configs.ICodeValidate;
import com.hitao.codegen.configs.basic.AbstractParentConfig;
import com.hitao.codegen.configs.basic.IConfigObject;
import com.hitao.codegen.configs.common.AbstractCodeGenConfig.GenerateModel;
import com.hitao.codegen.configs.common.AnnotationConfig;
import com.hitao.codegen.configs.common.BodyConfig;
import com.hitao.codegen.configs.common.CommentConfig;
import com.hitao.codegen.configs.common.FieldConfig;
import com.hitao.codegen.configs.common.ImplementsClassConfig;
import com.hitao.codegen.configs.common.ImportConfig;
import com.hitao.codegen.configs.common.MethodConfig;
import com.hitao.codegen.configs.common.ParameterConfig;
import com.hitao.codegen.configs.common.ThrowExceptionConfig;
import com.hitao.codegen.configs.dao.annotation.DAO;
import com.hitao.codegen.configs.exception.CodeGenException;
import com.hitao.codegen.configs.exception.ImproperAttributeException;
import com.hitao.codegen.configs.exception.InvalidConfigException;
import com.hitao.codegen.util.ObjectUtils;
import com.hitao.codegen.util.StringUtils;

/***
 * "DAO" access layer configuration object. Note: please validate all the
 * configuration objects(invoke validate method) before use this object.
 * 
 * @author zhangjun.ht
 * @created 2010-11-26
 * @version $Id: DaoConfigs.java 59 2011-05-23 05:47:35Z guest $
 */
public class DAOConfigs extends AbstractParentConfig implements ICodeValidate,
		IDAOConfigs {

	private static final long serialVersionUID = 1L;

	private transient Logger logger_ = Logger.getLogger(DAOConfigs.class);

	public static final String MAIN_TAG = "daogen";

	protected List<ICodeGenConfig> configs_ = new ArrayList<ICodeGenConfig>();
	
	// DAO implement
	protected IClassCodeGenConfig daoImpl_ = null;

	// DAO
	protected IClassCodeGenConfig dao_ = null;

	// MANAGER implement
	protected IClassCodeGenConfig managerImpl_ = null;

	// MANAGER
	protected IClassCodeGenConfig manager_ = null;

	// AO implement
	protected IClassCodeGenConfig aoImpl_ = null;

	// AO
	protected IClassCodeGenConfig ao_ = null;

	// DO
	protected IClassCodeGenConfig do_ = null;

	// unit test class for DAO
	protected IClassCodeGenConfig daoUnitTestConfig_ = null;
	
	public static final boolean GENERATE_AO = DAOServiceConfigurationManager
			.getBooleanProperty(CODEGEN_GENERATE_AO_AND_AOIMPL);

	public static final boolean GENERATE_MANAGER = DAOServiceConfigurationManager
			.getBooleanProperty(CODEGEN_GENERATE_MANAGER_AND_MANAGERIMPL);
	private static Class<DAOConfigs> instanceClass;

	static {
		String inClass = DAOServiceConfigurationManager
				.getProperty(CODEGEN_DAO_CONFIGS_CONFIG_CLASS);
		try {
			instanceClass = ObjectUtils.getClass(inClass, DAOConfigs.class);
		} catch (Exception ex) {
			instanceClass = DAOConfigs.class;
		}

	}

	/***
	 * create DAO configurations object.
	 * 
	 * @return
	 */
	public static IDAOConfigs createInstance() {
		return ObjectUtils.createInstance(instanceClass);
	}

	protected void setParentForTheConfig(ICodeGenConfig argConfig) {
		if (argConfig != null) {
			argConfig.setParent(this);
			addConfig(configs_, argConfig);
		}
	}

	/***
	 * add "DAOIMPL" configuration
	 * 
	 * @param argDaoImplConfig
	 */
	protected void setDaoImpl(IClassCodeGenConfig argDaoImplConfig) {
		daoImpl_ = argDaoImplConfig;
		setParentForTheConfig(argDaoImplConfig);
	}

	/***
	 * add "DAO" configuration
	 * 
	 * @param argDaoConfig
	 */
	protected void setDao(IClassCodeGenConfig argDaoConfig) {
		dao_ = argDaoConfig;
		setParentForTheConfig(argDaoConfig);
	}

	/***
	 * add "DO" configuration
	 * 
	 * @param argDoConfig
	 */
	protected void setDo(IClassCodeGenConfig argDoConfig) {
		do_ = argDoConfig;
		setParentForTheConfig(argDoConfig);
	}

	/***
	 * add "MANAGERIMPL" configuration
	 * 
	 * @param argManagerImplConfig
	 */
	protected void setManagerImpl(IClassCodeGenConfig argManagerImplConfig) {
		managerImpl_ = argManagerImplConfig;
		setParentForTheConfig(argManagerImplConfig);
	}

	/***
	 * add "MANAGER" configuration
	 * 
	 * @param argManagerConfig
	 */
	protected void setManager(IClassCodeGenConfig argManagerConfig) {
		manager_ = argManagerConfig;
		setParentForTheConfig(argManagerConfig);
	}

	/***
	 * add "AOIMPL" configuration
	 * 
	 * @param argAoImplConfig
	 */
	protected void setAoImpl(IClassCodeGenConfig argAoImplConfig) {
		aoImpl_ = argAoImplConfig;
		setParentForTheConfig(argAoImplConfig);
	}

	/***
	 * add "AO" configuration
	 * 
	 * @param argAoConfig
	 */
	protected void setAo(IClassCodeGenConfig argAoConfig) {
		ao_ = argAoConfig;
		setParentForTheConfig(argAoConfig);
	}

	public IClassCodeGenConfig getDaoUnitTestConfig() {
		return daoUnitTestConfig_;
	}

	public void setDaoUnitTestConfig(IClassCodeGenConfig argDaoUnitTestConfig) {
		this.daoUnitTestConfig_ = argDaoUnitTestConfig;
		setParentForTheConfig(argDaoUnitTestConfig);
	}
	
	@Override
	public void setConfigObject(String argKey, IConfigObject argValue) {
		if (DaoImplConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof DaoImplConfig) {
			setDaoImpl((DaoImplConfig) argValue);
		} else if (DaoConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof DaoConfig) {
			setDao((DaoConfig) argValue);
		} else if (ManagerImplConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof ManagerImplConfig) {
			setManagerImpl((ManagerImplConfig) argValue);
		} else if (ManagerConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof ManagerConfig) {
			setManager((ManagerConfig) argValue);
		} else if (DOConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof DOConfig) {
			setDo((DOConfig) argValue);
		} else if (AOImplConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof AOImplConfig) {
			setAoImpl((AOImplConfig) argValue);
		} else if (AOConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof AOConfig) {
			setAo((AOConfig) argValue);
		} else if (DaoUnitTestConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof DaoUnitTestConfig) {
			setDaoUnitTestConfig((DaoUnitTestConfig)argValue);
		}
	}

	/**
	 * get the class configuration by class name.
	 * 
	 * @param argClassName
	 * @return IClassCodeGenConfig
	 */
	public ICodeGenConfig getConfigByName(String argClassName) {
		ICodeGenConfig config = null;
		for (ICodeGenConfig tempConfig : getConifgs0()) {
			config = getConfigByName(argClassName, tempConfig);
			if (config != null) {
				break;
			}
		}

		return config;
	}

	/***
	 * get DAOimpl configuration by class name.
	 * 
	 * @param argClassName
	 * @return IClassCodeGenConfig
	 */
	protected ICodeGenConfig getConfigByName(String argClassName,
			ICodeGenConfig argConfig) {
		return !StringUtils.isEmpty(argClassName)
				&& argConfig != null
				&& argClassName.equals(argConfig.getPackagePath() + "."
						+ argConfig.getName()) ? argConfig : null;
	}

	// validate flag
	private boolean validateFlag_ = false;
	byte[] syn = new byte[0];

	@Override
	public void validate() throws CodeGenException {
		synchronized (syn) {
			if (!initialFlag_) {
				initial();
			}
			if (!validateFlag_) {
				validateFlag_ = true;

				// validate all configuration.
				for (ICodeGenConfig tempConfig : getConifgs0()) {
					validateConfig(tempConfig);
				}
			}
		}
	}

	/***
	 * Validate the configuration.
	 * 
	 * @param argConfigList
	 * @throws CodeGenException
	 */
	protected void validateConfig(ICodeValidate argConfig)
			throws CodeGenException {
		if (argConfig != null) {
			argConfig.validate();
		}
	}

	private boolean initialFlag_ = false;

	/***
	 * initialize all the configuration object.
	 */
	private void initial() throws CodeGenException {
		if (!initialFlag_) {
			initialFlag_ = true;

			buildDO();
			buildSqlMap(do_);

			if (dao_ == null) {
				return;
			}

			buildDaoConfig();
			buildDaoImplConfig();
			buildManagerConfig();
			buildManagerImplConfig();
			buildAOConfig();
			buildAOImplConfig();
			buildDaoUnitTestConfig();

			// initialize all configurations.
			for (ICodeGenConfig tempConfig : getConifgs0()) {
				initialConfig(tempConfig);
			}
		}
	}

	/***
	 * Initialize the configuration list.
	 * 
	 * @param argConfigList
	 */
	protected void initialConfig(ICodeGenInitial argConfig) {
		if (argConfig != null) {
			argConfig.initial();
		}
	}

	/***
	 * Build the DO configuration
	 * 
	 * @throws ImproperAttributeException
	 */
	protected void buildDO() throws ImproperAttributeException {
		// DO nothing
	}

	/***
	 * Build DAO configuration.
	 * 
	 * @throws ImproperAttributeException
	 */
	protected void buildDaoConfig() throws ImproperAttributeException {
		boolean shouldOverride = false;
		for (MethodConfig method : dao_.getMethodList()) {
			// add default exception
			if (!addDefaultException(method,
					CODEGEN_DAO_DEFAULT_EXCEPTION) && !shouldOverride) {
				shouldOverride = true;
			}
		}

		if (shouldOverride) {
			dao_.setOverride();
		}
	}
	
	/***
	 * Build the unit test class for DAO.
	 * @throws ImproperAttributeException 
	 */
	protected void buildDaoUnitTestConfig() throws ImproperAttributeException {
		if (daoUnitTestConfig_ != null) {
			return ;
		}
		
		daoUnitTestConfig_ = new DaoUnitTestConfig();
		daoUnitTestConfig_.setName(dao_.getName() + "Test");
		daoUnitTestConfig_.setPackagePath(dao_.getPackagePath());
		
		// set default extends class for unit test class
		daoUnitTestConfig_.setExtendsClass(DAOServiceConfigurationManager
				.getProperty(CODEGEN_DAO_DAO_UNITTEST_DEFAULT_EXTENDS_CLASS));
		
		// Add DAO into the manager as a field.
		FieldConfig fieldConfig = new FieldConfig();
		String daoFieldName = StringUtils.ensureFirstLowerCase(dao_.getName());
		fieldConfig.setName(daoFieldName);
		fieldConfig.setClassName(dao_.getClassName());
		if (hasGenericType()) {
			fieldConfig.setConcreteClass(dao_.getConcreteClass());
		}
		// Don't generate the get method for the DAO.
		fieldConfig.setGenGetMethod(false);
		
		daoUnitTestConfig_.setConfigObject(FieldConfig.MAIN_TAG, fieldConfig);
		
		daoUnitTestConfig_.setConfigObject(new ImportConfig("org.junit.Assert"));
		daoUnitTestConfig_.setConfigObject(new ImportConfig("org.junit.Test"));
		
		for (MethodConfig daoMethod : dao_.getMethodList()) {
			MethodConfig testMethod = new MethodConfig();
			testMethod.setName(daoMethod.getName() + "Test");
			testMethod.setReturnType("void");
			
			AnnotationConfig annotation = new AnnotationConfig();
			annotation.setName("Test");
			testMethod.setConfigObject(annotation);
			
			// add DAO exception
			addDefaultException(testMethod, CODEGEN_DAO_DEFAULT_EXCEPTION);
			
			// the method body
			StringBuffer methodBody = new StringBuffer();
			for (ParameterConfig parameterConfig : daoMethod.getParameterConfigList()) {
				methodBody.append(parameterConfig.getClassName() + " ").append(parameterConfig.getName()).append(" = null;");
				methodBody.append(LINE_BREAK);
				methodBody.append(daoMethod.getReturnType()).append(" result").append(" = ");
			}
			daoUnitTestConfig_.setConfigObject(testMethod);
		}
		
		// set parent configuration Object.
		setParentForTheConfig(daoUnitTestConfig_);
	}

	/***
	 * Build DAO implement
	 * 
	 * @throws ImproperAttributeException
	 */
	protected void buildDaoImplConfig() throws ImproperAttributeException {
		if (daoImpl_ != null) {
			if (!daoImpl_.shouldBeGenerated()) {
				buildSpringXmlConfig(dao_, daoImpl_,
						DAOServiceConfigurationManager
								.getProperty(CODEGEN_WEB_PROJECT_DAO_CONFIG_NAME),DO_NOTHING);
				return;
			} else if (daoImpl_.shouldBeAppend()) {
				handleMethodsForDAOimpl();
				return;
			}
		}

		daoImpl_ = new DaoImplConfig();

		// add all classes need imported.
		addImport(daoImpl_, dao_);

		// import the DBroute class which must be static class.
		if (hasDBrouteStaticClass_) {
			addImportClass(
					daoImpl_,
					"static "
							+ DAOServiceConfigurationManager
									.getProperty(CODEGEN_DAO_DAOIMPL_DBROUTE_STATIC_CLASS));
		}

		// set package for DAO implements.
		setRelativePackagePath(daoImpl_, dao_);

		// set class name.
		daoImpl_.setName(dao_.getName());

		// set class' comment
		setCommentConfig(daoImpl_, dao_);

		// set default extends class
		String extendClass = DAOServiceConfigurationManager
				.getProperty(CODEGEN_DAO_DAOIMPL_DEFAULT_EXTENDS_CLASS);
		AnnotationConfig annotation = null;
		for (Iterator<AnnotationConfig> it = dao_.getAnnotations().iterator(); it.hasNext();) {
			annotation = it.next();
			if (annotation.getName().equals(DAO.class.getSimpleName())) {
				String tempClass = annotation.getAttribute("extendsClass");
				if (!StringUtils.isEmpty(tempClass)) {
					extendClass = tempClass;
				}
				break;
			}
		}
		
		boolean hasDOGeneric = DAOServiceConfigurationManager
				.getBooleanProperty(CODEGEN_DAO_DAOIMPL_DEFAULT_EXTENDS_CLASS_HAS_GENERIC);
		if (hasDOGeneric && do_ != null) {
			extendClass += "<" + do_.getName() + ">";
		}
		if (!StringUtils.isEmpty(extendClass)) {
			daoImpl_.setExtendsClass(extendClass);
		}

		// set implement class
		daoImpl_.setImplementsClasses(dao_.getClassName());

		// copy the methods from DAO.
		copyMethod(daoImpl_, dao_);

		// handle methods for the DAOimpl.
		handleMethodsForDAOimpl();

		addImportClass(daoImpl_, dao_.getClassName());

		// set parent configuration Object.
		setParentForTheConfig(daoImpl_);
		
		buildSpringXmlConfig(dao_, daoImpl_,
				DAOServiceConfigurationManager
						.getProperty(CODEGEN_WEB_PROJECT_DAO_CONFIG_NAME),APPEND);
	}

	/***
	 * handle methods for the DAOimpl.
	 * 
	 * @throws ImproperAttributeException
	 */
	protected void handleMethodsForDAOimpl() throws ImproperAttributeException {
		if (hasGenericType()) {
			String genericVariable = getGenericVariable();
			for (MethodConfig method : daoImpl_.getMethodList()) {
				for (ParameterConfig parameter : method
						.getParameterConfigList()) {
					// set the concrete class for the parameters and return
					// type.
					if (CodeGenUtils.containsGenericVariable(
							parameter.getClassName(), genericVariable)) {
						parameter.setClassName(CodeGenUtils
								.getConcreteClassForGenericType(
										parameter.getClassName(),
										genericVariable,
										dao_.getConcreteClass()));
					}

				}
				if (CodeGenUtils.containsGenericVariable(
						method.getReturnType(), genericVariable)) {
					method.setReturnType(CodeGenUtils
							.getConcreteClassForGenericType(
									method.getReturnType(), genericVariable,
									dao_.getConcreteClass()));
				}
			}
		}

		for (MethodConfig method : daoImpl_.getMethodList()) {
			addOverrideAnnotation(method);

			BodyConfig body = new BodyConfig();
			body.setValue(getDefaultMethodBody(DAO_RETURN_TYPE, method));
			method.setConfigObject(BodyConfig.MAIN_TAG, body);

			// add default exception
			addDefaultException(method, CODEGEN_DAO_DEFAULT_EXCEPTION);
		}
	}

	/***
	 * Build the MANAGER class
	 * 
	 * @throws InvalidConfigException
	 */
	protected void buildManagerConfig() {
		if (!GENERATE_MANAGER) {
			return;
		} else if (manager_ != null) {
			if (!manager_.shouldBeGenerated()) {
				return;
			} else if (manager_.shouldBeAppend()) {
				handleMethodsForManager();
				return;
			}
		}

		manager_ = new ManagerConfig();

		// set package for the Manager
		manager_.setPackagePath(getModelPackage(CODEGEN_MANAGER_PACKAGE_PATH));

		// add all classes need imported.
		addImport(manager_, dao_);

		// set class' comment
		setCommentConfig(manager_, dao_);

		// set the class name.
		String name = CodeGenUtils.getRelativeClassName(dao_,
				CODEGEN_DAO_DAO_SUFFIX, CODEGEN_DAO_MANAGER_SUFFIX);
		manager_.setName(name);

		// set the generic type for the manager
		setGenericType(manager_);

		copyMethod(manager_, dao_);

		// handle all the methods for the MANAGER.
		handleMethodsForManager();

		// set parent configuration Object.
		setParentForTheConfig(manager_);
	}

	/***
	 * Append codes for the Manager.
	 */
	protected void handleMethodsForManager() {

		// delete the un-needed imported classes and exception.
		addExceptionForMethods(manager_, CODEGEN_MANAGER_DEFAULT_EXCEPTION);

		// Remove the DAO exception.
		removeDefaultException(manager_, CODEGEN_DAO_DEFAULT_EXCEPTION);
	}

	/***
	 * Build Manager implement
	 * 
	 * @throws ImproperAttributeException
	 */
	protected void buildManagerImplConfig() throws ImproperAttributeException {
		if (!GENERATE_MANAGER) {
			return;
		} else if (managerImpl_ != null) {
			if (!managerImpl_.shouldBeGenerated()) {
				buildSpringXmlConfig(manager_, managerImpl_,
						DAOServiceConfigurationManager
								.getProperty(CODEGEN_WEB_PROJECT_MANAGER_CONFIG_NAME), DO_NOTHING);
				return;
			} else if (managerImpl_.shouldBeAppend()) {
				handleMethodsForManagerImpl();
				return;
			}
		}

		managerImpl_ = new ManagerImplConfig();

		// add all classes need imported.
		addImport(managerImpl_, dao_);

		// set package for the Manager implement
		setRelativePackagePath(managerImpl_, manager_);

		// set class' comment
		setCommentConfig(managerImpl_, dao_);

		// set the class name.
		managerImpl_.setName(manager_.getName());

		// set implement class
		managerImpl_.setImplementsClasses(manager_.getClassName());

		// copy the methods from DAO implement.
		copyMethod(managerImpl_, daoImpl_);

		// Add DAO into the manager as a field.
		FieldConfig fieldConfig = new FieldConfig();
		String daoFieldName = StringUtils.ensureFirstLowerCase(dao_.getName());
		fieldConfig.setName(daoFieldName);
		fieldConfig.setClassName(dao_.getClassName());
		if (hasGenericType()) {
			fieldConfig.setConcreteClass(dao_.getConcreteClass());
		}
		// Don't generate the get method for the DAO.
		fieldConfig.setGenGetMethod(false);
		managerImpl_.setConfigObject(FieldConfig.MAIN_TAG, fieldConfig);

		// Replace the generic class type with the concrete class.
		if (hasGenericType()) {
			managerImpl_.setImplementsClasses(manager_.getClassName()
					+ getConreteClassStatement(dao_));

		}

		// handle methods for the manager implement.
		handleMethodsForManagerImpl();

		// set parent configuration Object.
		setParentForTheConfig(managerImpl_);

		buildSpringXmlConfig(manager_, managerImpl_,
				DAOServiceConfigurationManager
						.getProperty(CODEGEN_WEB_PROJECT_MANAGER_CONFIG_NAME), APPEND);
	}

	/***
	 * Append codes for the Manager implement.
	 */
	protected void handleMethodsForManagerImpl() {
		for (MethodConfig method : managerImpl_.getMethodList()) {
			addOverrideAnnotation(method);
			BodyConfig body = new BodyConfig();
			body.setValue(getDefaultMethodBody(MANAGER_RETURN_TYPE, method));
			method.setConfigObject(BodyConfig.MAIN_TAG, body);

			// add default exception
			addDefaultException(method,
					CODEGEN_MANAGER_DEFAULT_EXCEPTION);

		}
		// Remove the DAO exception.
		removeDefaultException(managerImpl_, CODEGEN_DAO_DEFAULT_EXCEPTION);

		addDefaultImportClass(managerImpl_, CODEGEN_DAO_DEFAULT_EXCEPTION);
	}

	/***
	 * Build AO
	 * 
	 * @throws InvalidConfigException
	 */
	protected void buildAOConfig() throws InvalidConfigException {
		if (!GENERATE_AO) {
			return;
		} else if (ao_ != null) {
			if (!ao_.shouldBeGenerated()) {
				return;
			} else if (ao_.shouldBeAppend()) {
				handleMethodsForAO();
				return;
			}
		}

		ao_ = new AOConfig();

		// set package for the Manager
		ao_.setPackagePath(getModelPackage(CODEGEN_AO_PACKAGE_PATH));

		// add all classes need imported.
		addImport(ao_, dao_);

		// set class' comment
		setCommentConfig(ao_, dao_);

		// set the class name.
		String name = CodeGenUtils.getRelativeClassName(dao_,
				CODEGEN_DAO_DAO_SUFFIX, CODEGEN_DAO_AO_SUFFIX);
		ao_.setName(name);

		// set the generic type
		setGenericType(ao_);

		copyMethod(ao_, dao_);

		// handle methods for the AO.
		handleMethodsForAO();

		// set parent configuration Object.
		setParentForTheConfig(ao_);
	}

	/***
	 * Append codes for the AO.
	 */
	protected void handleMethodsForAO() {
		// delete the un-needed imported classes and exception.
		for (MethodConfig methodConfig : ao_.getMethodList()) {
			// clear all exceptions.
			methodConfig.getExceptionList().clear();
		}

		// Remove the DAO exception.
		removeDefaultException(ao_, CODEGEN_DAO_DEFAULT_EXCEPTION);
	}

	/***
	 * Build Manager implement
	 * 
	 * @throws ImproperAttributeException
	 */
	protected void buildAOImplConfig() throws ImproperAttributeException {
		if (!GENERATE_AO) {
			return;
		} else if (aoImpl_ != null) {
			if (!aoImpl_.shouldBeGenerated()) {
				buildSpringXmlConfig(ao_, aoImpl_,
						DAOServiceConfigurationManager
								.getProperty(CODEGEN_WEB_PROJECT_AO_CONFIG_NAME), DO_NOTHING);
				return;
			} else if (aoImpl_.shouldBeAppend()) {
				handleMethodsForAOImpl();
				return;
			}
		}

		aoImpl_ = new AOImplConfig();

		// set package for the Manager implement
		setRelativePackagePath(aoImpl_, ao_);

		// add all classes need imported.
		addImport(aoImpl_, dao_);

		// set class' comment
		setCommentConfig(aoImpl_, dao_);

		// set the class name.
		aoImpl_.setName(ao_.getName());

		// set implement class
		aoImpl_.setImplementsClasses(ao_.getClassName());

		// copy the methods from DAO implement.
		copyMethod(aoImpl_, daoImpl_);

		IClassCodeGenConfig classConfig = null;
		if (GENERATE_MANAGER) {
			classConfig = manager_;
		} else {
			classConfig = dao_;
		}

		// add the log class into AO
		addLogFieldForAoImpl(aoImpl_);

		// Add manager(DAO) into the AO as a field.
		FieldConfig fieldConfig = new FieldConfig();
		String managerFieldName = StringUtils.ensureFirstLowerCase(classConfig
				.getName());
		fieldConfig.setName(managerFieldName);
		fieldConfig.setClassName(classConfig.getClassName());
		if (hasGenericType()) {
			fieldConfig.setConcreteClass(dao_.getConcreteClass());
		}
		// Don't generate the get method for the DAO.
		fieldConfig.setGenGetMethod(false);
		aoImpl_.setConfigObject(FieldConfig.MAIN_TAG, fieldConfig);

		// Replace the generic class type with the concrete class.
		if (hasGenericType()) {
			aoImpl_.setImplementsClasses(ao_.getClassName()
					+ getConreteClassStatement(dao_));
		}

		// handle methods for the AO implement.
		handleMethodsForAOImpl();

		// set parent configuration Object.
		setParentForTheConfig(aoImpl_);
		
		buildSpringXmlConfig(ao_, aoImpl_,
				DAOServiceConfigurationManager
						.getProperty(CODEGEN_WEB_PROJECT_AO_CONFIG_NAME), APPEND);
	}

	/***
	 * Append codes for the Manager implement.
	 */
	protected void handleMethodsForAOImpl() {
		for (MethodConfig method : aoImpl_.getMethodList()) {
			addOverrideAnnotation(method);
			BodyConfig body = new BodyConfig();
			body.setValue(getDefaultMethodBody(AO_RETURN_TYPE, method));
			method.setConfigObject(BodyConfig.MAIN_TAG, body);

			// clear all exceptions
			method.getExceptionList().clear();
		}

		// Remove the DAO exception.
		removeDefaultException(aoImpl_, CODEGEN_DAO_DEFAULT_EXCEPTION);

		if (GENERATE_MANAGER) {
			addDefaultImportClass(aoImpl_, CODEGEN_MANAGER_DEFAULT_EXCEPTION);
		} else {
			addDefaultImportClass(aoImpl_, CODEGEN_DAO_DEFAULT_EXCEPTION);
		}
	}

	/***
	 * build SqlMap configuration.
	 */
	protected void buildSqlMap(IClassCodeGenConfig argDoConfig) {
		if (argDoConfig instanceof DOConfig) {
			DOConfig doconfig = (DOConfig) argDoConfig;
			if (doconfig.shouldBeNew()) {
				// the sqlMapXml_ needn't generate when the DOCONFIG is in append model or do nothing model.
				ICodeGenConfig sqlMapXml_ = doconfig.getSqlMapXmlConfig();
				sqlMapXml_
						.setPackagePath(getModelPackage(CODEGEN_SQLMAP_PACKAGE_PATH));
				
				// set parent configuration Object.
				setParentForTheConfig(sqlMapXml_);
	
				buildSqlMapConfig(sqlMapXml_);
			}
		}
	}

	/***
	 * Build SqlMapConfig configuration.
	 * 
	 * @param argCodeGenConfig
	 */
	private void buildSqlMapConfig(ICodeGenConfig argCodeGenConfig) {
		ICodeGenConfig sqlMapConfigXml_ = new SqlMapConfigXmlConfig();
		sqlMapConfigXml_.setPackagePath(DAOServiceConfigurationManager
				.getProperty(CODEGEN_WEB_PROJECT_IBATIS_SQLMAP_CONFIG_DIR));
		sqlMapConfigXml_.setName(DAOServiceConfigurationManager
				.getProperty(CODEGEN_WEB_PROJECT_IBATIS_SQLMAP_CONFIG_NAME));
		// build the file path for the SQLMAP configuration file.<sqlMap
		// resource="filepath.xml"/>
		String path = argCodeGenConfig.getPackagePath();
		path = StringUtils.replaceAll(new StringBuffer(path), ".", "/")
				.toString()
				+ "/"
				+ argCodeGenConfig.getName()
				+ argCodeGenConfig.getFileType();
		sqlMapConfigXml_.setAppendModel();
		sqlMapConfigXml_.setValue("<sqlMap resource=\"" + path + "\"/>");
		
		// set parent configuration Object.
		setParentForTheConfig(sqlMapConfigXml_);
	}

	/***
	 * Build spring XML configuration (dao.xml.vm, mangager.xml.vm, ao.xml.vm).
	 * 
	 * @param argInterfaceConfig
	 * @param argImplementConfig
	 * @param codeGenConfig
	 * @param argFileName
	 *            the key in SystemConfig.xml for configuration file name.
	 * @param argGenerateModel
	 * 				code genernation model.
	 */
	protected ICodeGenConfig buildSpringXmlConfig(
			IClassCodeGenConfig argInterfaceConfig,
			IClassCodeGenConfig argImplementConfig, String argFileName, GenerateModel argGenerateModel) {
		ICodeGenConfig codeGenConfig = new SpringXmlConfig();
		codeGenConfig.setName(argFileName);
		codeGenConfig.setPackagePath(DAOServiceConfigurationManager
				.getProperty(CODEGEN_WEB_PROJECT_AUTOCONFIG_FOLDER));
		codeGenConfig.setGenerateModel(argGenerateModel);
		String id = StringUtils.uncapitalize(argInterfaceConfig.getName());
		
		// just the DaoImplConfig has the parent bean daoBase.
		if (argImplementConfig instanceof DaoImplConfig) {
			codeGenConfig.setValue("<bean id=\"" + id + "\" class=\""
					+ argImplementConfig.getClassName() + "\" parent=\"daoBase\"/>");
		} else {
			codeGenConfig.setValue("<bean id=\"" + id + "\" class=\""
					+ argImplementConfig.getClassName() + "\"/>");
		}
		setParentForTheConfig(codeGenConfig);

		return codeGenConfig;
	}

	/***
	 * Add log field for AOimpl
	 * 
	 * @param argAoImpl
	 */
	protected void addLogFieldForAoImpl(IClassCodeGenConfig argAoImpl) {
		FieldConfig logField = new FieldConfig();
		logField.setName("logger");
		logField.setClassName(DAOServiceConfigurationManager
				.getProperty(CODEGEN_AO_LOG_LOGGER_CLASS));

		// Don't generate the set and get methods for the this field.
		logField.setGenGetMethod(false);
		logField.setGenSetMethod(false);

		ImportConfig importConfig = new ImportConfig(
				DAOServiceConfigurationManager
						.getProperty(CODEGEN_AO_LOG_LOGGERFACTORY_CLASS));
		logField.getImportList().add(importConfig);
		logField.setFieldValue("LoggerFactory.getLogger(" + argAoImpl.getName()
				+ ".class)");
		argAoImpl.setConfigObject(FieldConfig.MAIN_TAG, logField);
	}

	/***
	 * Check the DAO whether it contan's generic type and concrete class.
	 * 
	 * @return true if it is , or else.
	 * @throws ImproperAttributeException
	 */
	protected boolean hasGenericType() throws ImproperAttributeException {
		if (!StringUtils.isEmpty(dao_.getGenericType())) {
			if (StringUtils.isEmpty(dao_.getConcreteClass())) {
				throw new ImproperAttributeException(
						"Please must set 'concreteClass' attribute for the DAO Annotation or in conifguration xml for the generate type "
								+ dao_.getClassName() + dao_.getGenericType());

			} else {
				return true;
			}
		}
		return false;
	}

	/***
	 * Get the generic variable in generic statement.
	 * 
	 * @return generic variable
	 */
	protected String getGenericVariable() {
		String genericStatement = dao_.getGenericType();
		if (!StringUtils.isEmpty(genericStatement)) {
			String genericVariable = EMPTY;
			genericStatement = genericStatement.trim();

			if (genericStatement.startsWith("<")) {
				genericStatement = genericStatement.substring(1);
			}
			if (genericStatement.endsWith(">")) {
				genericStatement = genericStatement.substring(0,
						genericStatement.length() - 1);
			}

			int extendsIndex = genericStatement.indexOf(" extends");
			if (extendsIndex > 0) {
				genericVariable = genericStatement.substring(0, extendsIndex)
						.trim();
			} else {
				genericVariable = genericStatement;
			}

			return genericVariable;
		} else {
			return EMPTY;
		}
	}

	/***
	 * Add import configuration from argClassCodeGenConfigSrc to
	 * argClassCodeGenConfigDes
	 * 
	 * @param argClassCodeGenConfigSrc
	 * @param argClassCodeGenConfigDes
	 */
	protected void addImport(IClassCodeGenConfig argClassCodeGenConfigDes,
			IClassCodeGenConfig argClassCodeGenConfigSrc) {
		if (argClassCodeGenConfigSrc != null
				&& argClassCodeGenConfigDes != null) {
			String importStaement = null;
			for (ImportConfig im : argClassCodeGenConfigSrc.getImportList()) {
				importStaement = im.getStatement();
				if (CodeGenUtils.needImportClass(importStaement)
						&& (!argClassCodeGenConfigSrc
								.containsAnnotation(CodeGenUtils
										.getShortClassNameFromImportStatement(importStaement)))) {
					argClassCodeGenConfigDes.setConfigObject(
							ImportConfig.MAIN_TAG, im);
				}
			}
		}
	}

	/***
	 * Set generic type and concrete class.
	 * 
	 * @param argClassCodeGenConfig
	 */
	protected void setGenericType(IClassCodeGenConfig argClassCodeGenConfig) {
		argClassCodeGenConfig.setGenericType(dao_.getGenericType());
		argClassCodeGenConfig.setConcreteClass(dao_.getConcreteClass());
	}

	/***
	 * Set comment configuration object from argClassCodeGenConfigSrc to
	 * argClassCodeGenConfigDes.
	 * 
	 * @param argClassCodeGenConfigDes
	 * @param argClassCodeGenConfigSrc
	 */
	protected void setCommentConfig(
			IClassCodeGenConfig argClassCodeGenConfigDes,
			IClassCodeGenConfig argClassCodeGenConfigSrc) {
		if (argClassCodeGenConfigSrc != null
				&& argClassCodeGenConfigDes != null) {
			try {
				argClassCodeGenConfigDes.setConfigObject(
						CommentConfig.MAIN_TAG,
						(IConfigObject) (argClassCodeGenConfigSrc.getComment()
								.clone()));
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * get relative package for Manager and AO. e.g: DAO package is :
	 * a.b.c.dao.classname Result: Manage package is
	 * a.b.c.biz.manager.classname.
	 */
	protected String getModelPackage(String argPackageKey) {
		if (dao_ != null) {
			return CodeGenUtils.getRelativePackageForClass(
					dao_.getPackagePath(), argPackageKey);
		} else if (do_ != null) {
			return CodeGenUtils.getRelativePackageForClassByKey(
					do_.getPackagePath(), CODEGEN_DO_PACKAGE_PATH,
					argPackageKey);
		} else {
			return EMPTY;
		}

	}

	protected String getBriefImplName(ICodeGenConfig argConfigImpl) {
		String name = argConfigImpl.getName();

		String prefix = DAOServiceConfigurationManager
				.getProperty("codegen.dao.implement.name.prefix");
		if ((!StringUtils.isEmpty(prefix)) && name.startsWith(prefix)) {
			name = name.substring(prefix.length());
		}

		String suffix = DAOServiceConfigurationManager
				.getProperty("codegen.dao.implement.name.suffix");
		if ((!StringUtils.isEmpty(suffix)) && name.endsWith(suffix)) {
			name = name.substring(0, name.length() - suffix.length());
		}

		return name;
	}

	/**
	 * Copy method configurations from DAO.
	 * 
	 * @param argClassCodeGenConfigDest
	 * @param argClassCodeGenConfigSrc
	 */
	protected void copyMethod(IClassCodeGenConfig argClassCodeGenConfigDest,
			IClassCodeGenConfig argClassCodeGenConfigSrc) {
		MethodConfig methodConfig = null;
		for (MethodConfig method : argClassCodeGenConfigSrc.getMethodList()) {
			try {
				methodConfig = (MethodConfig) method.clone();
				
				if (argClassCodeGenConfigDest instanceof ImplementsClassConfig) {
					addOverrideAnnotation(methodConfig);
				}

				// clear all annotations in DAO.
				// methodConfig.clearAnnotations();
				
				argClassCodeGenConfigDest.setConfigObject(
						MethodConfig.MAIN_TAG, methodConfig);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * add the <code>@Override</code> annotation for the class element.
	 * 
	 * @param classElementConfig
	 */
	protected void addOverrideAnnotation(
			IClassElementCodeGenConfig classElementConfig) {
		if (!classElementConfig.containsAnnotation("Override")) {
			AnnotationConfig an = new AnnotationConfig();
			an.setName("Override");
			classElementConfig.setConfigObject(an);
		}
	}

	/***
	 * Set the relative package for the argClassCodegenConfig1 through the
	 * argClassCodegenConfig2.
	 * 
	 * @param argClassCodegenConfig1
	 * @param argClassCodegenConfig2
	 */
	protected void setRelativePackagePath(
			IClassCodeGenConfig argClassCodegenConfig1,
			IClassCodeGenConfig argClassCodegenConfig2) {
		argClassCodegenConfig1.setPackagePath(CodeGenUtils
				.getRelativePackagePath(argClassCodegenConfig2));
	}

	/***
	 * Add the exception for the methods of the class configuration with
	 * specific exception key.
	 * 
	 * @param argClassCodeGenConfig
	 * @param argExceptionKey
	 */
	protected void addExceptionForMethods(
			IClassCodeGenConfig argClassCodeGenConfig, String argExceptionKey) {
		for (MethodConfig methodConfig : argClassCodeGenConfig.getMethodList()) {
			addDefaultException(methodConfig, argExceptionKey);
		}
	}

	/***
	 * Set default exception for the method configuration.
	 * 
	 * @param argMethodConfig
	 * @param argExceptionKey
	 * @return true if it has, false or else.
	 */
	protected boolean addDefaultException(MethodConfig argMethodConfig,
			String argExceptionKey) {
		String exception = DAOServiceConfigurationManager
				.getProperty(argExceptionKey);
		boolean hasSameException = false;
		if (!StringUtils.isEmpty(exception)) {
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

		return hasSameException;
	}

	/***
	 * Add the import class from the system properties for the class.
	 * 
	 * @param argCodeGenConfig
	 * @param argExceptionKey
	 */
	protected void addDefaultImportClass(IClassCodeGenConfig argCodeGenConfig,
			String argExceptionKey) {
		String exception = DAOServiceConfigurationManager
				.getProperty(argExceptionKey);
		if (!StringUtils.isEmpty(exception)) {
			argCodeGenConfig.setConfigObject(ImportConfig.MAIN_TAG,
					new ImportConfig(exception));
		}
	}

	/***
	 * Add the import class for the class.
	 * 
	 * @param argCodeGenConfig
	 * @param argClass
	 */
	protected void addImportClass(IClassCodeGenConfig argCodeGenConfig,
			String argClass) {
		if (!StringUtils.isEmpty(argClass)) {
			argCodeGenConfig.setConfigObject(ImportConfig.MAIN_TAG,
					new ImportConfig(argClass));
		}
	}

	/***
	 * Remove default exception for the method configuration.
	 * 
	 * @param argCodeGenConfig
	 * @param argExceptionKey
	 */
	protected void removeDefaultException(
			IClassElementCodeGenConfig argCodeGenConfig, String argExceptionKey) {
		String exception = DAOServiceConfigurationManager
				.getProperty(argExceptionKey);
		if (!StringUtils.isEmpty(exception)) {
			String[] exceptions = exception.split(",");
			for (String tempException : exceptions) {
				argCodeGenConfig.removeImportedClass(tempException);
			}
		}
	}

	private List<ICodeGenConfig> getConifgs0() {
		return configs_;
	}

	private static final String DAO_RETURN_TYPE = "codegen.dao.daoimpl.returntype.";
	private static final String MANAGER_RETURN_TYPE = "codegen.dao.managerimpl.returntype.";
	private static final String AO_RETURN_TYPE = "codegen.dao.aoimpl.returntype.";

	private static final String DEFAULT_RETURN_TYPE = "default";
	private static final boolean hasDBrouteStaticClass_;
	private static final String dbrouteName_;
	static {
		String dbRoute = DAOServiceConfigurationManager
				.getProperty(CODEGEN_DAO_DAOIMPL_DBROUTE_STATIC_CLASS);
		hasDBrouteStaticClass_ = !StringUtils.isEmpty(dbRoute);
		dbrouteName_ = CodeGenUtils
				.getShortClassNameWithoutGenericType(dbRoute);
	}

	/***
	 * Get default statement through the method configuration's return type.
	 * 
	 * @param the
	 *            variable using in the SystemConfig.xml
	 * @param argMethodConfig
	 * @return method body.
	 */
	protected String getDefaultMethodBody(String argType,
			MethodConfig argMethodConfig) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		String returnType = argMethodConfig.getSimpleReturnType();

		// put the variables for the template context.
		if (!DAO_RETURN_TYPE.equals(argType)) {
			if (MANAGER_RETURN_TYPE.equals(argType)) {
				parameterMap.put(DAO_KEY,
						StringUtils.ensureFirstLowerCase(dao_.getName()));
			} else if (AO_RETURN_TYPE.equals(argType)) {
				String name = GENERATE_MANAGER ? manager_.getName() : dao_
						.getName();
				parameterMap.put(MANAGER_KEY,
						StringUtils.ensureFirstLowerCase(name));
				parameterMap.put(LOG_KEY, LOG_KEY);

				parameterMap.put(METHOD_COMMENTS, CodeGenUtils
						.clearNewlineSymbol(argMethodConfig.getComment()
								.getValue()));
				String exceptionName = GENERATE_MANAGER ? "ManagerException"
						: "DAOException";
				parameterMap.put(EXCEPTION_NAME, exceptionName);
			}

			if (!"void".equals(returnType)) {
				parameterMap.put(DEFAULT_RETURN_VALUE,
						getDefaultTypeValue(returnType));
				returnType = DEFAULT_RETURN_TYPE;
			}
			parameterMap.put(METHOD_KEY, argMethodConfig.getMethodStatement());
		} else {
			if (hasDBrouteStaticClass_) {
				parameterMap.put(DB_ROUTE, dbrouteName_);
			}
			
			// set SQL name
			parameterMap.put(SQL_NAME, dao_.getName() + "." + argMethodConfig.getName());

			// put param name into parameter map.
			putMethodParameterName(argMethodConfig, parameterMap);
		}

		String defaultStatment = DAOServiceConfigurationManager.getProperty(
				argType + returnType, parameterMap);

		// default return statement.
		if (StringUtils.isEmpty(defaultStatment)) {
			defaultStatment = "return null;";
		}
		return defaultStatment;
	}

	/***
	 * Get the parameter name with specific index.
	 * 
	 * @param argMethodConfig
	 *            method configuration.
	 * @param argIndex
	 *            the index of the parameter list.
	 * @return parameter name.
	 */
	protected void putMethodParameterName(MethodConfig argMethodConfig,
			Map<String, String> parameterMap) {
		List<ParameterConfig> parameterList = argMethodConfig
				.getParameterConfigList();
		int i = 0;
		for (ParameterConfig paramConfig : parameterList) {
			parameterMap.put(METHOD_PARMETER + (i++), paramConfig.getName());
		}
	}

	private static final Map<String, String> defaultTypeValueMap_;

	static {
		defaultTypeValueMap_ = new HashMap<String, String>();
		defaultTypeValueMap_.put("int", "0");
		defaultTypeValueMap_.put("byte", "0");
		defaultTypeValueMap_.put("short", "0");
		defaultTypeValueMap_.put("double", "0d");
		defaultTypeValueMap_.put("float", "0f");
		defaultTypeValueMap_.put("long", "0l");
		defaultTypeValueMap_.put("char", "\'0\'");
		defaultTypeValueMap_.put("boolean", "false");
	}

	/***
	 * Get default value for the return type.
	 * 
	 * @param argType class type.
	 * @return
	 */
	protected String getDefaultTypeValue(String argType) {
		String defaultValue = defaultTypeValueMap_.get(argType);
		if (StringUtils.isEmpty(defaultValue)) {
			defaultValue = "null";
		}
		return defaultValue;
	}

	@Override
	public String toString() {
		if (dao_ != null) {
			return "this DAOgen is for " + dao_.getClassName();
		} else {
			return super.toString();
		}
	}

	@Override
	public synchronized List<ICodeGenConfig> getConfigs() {
		try {
			validate();
		} catch (CodeGenException e) {
			logger_.error("make an error when get configurations.", e);
		}
		return Collections.unmodifiableList(configs_);
	}

	/***
	 * Add configuration object for the list.
	 * 
	 * @param argList
	 * @param argCodeGenConfig
	 */
	private void addConfig(List<ICodeGenConfig> argList,
			ICodeGenConfig argCodeGenConfig) {
		if (argCodeGenConfig != null) {
			argList.add(argCodeGenConfig);
		}
	}
}
