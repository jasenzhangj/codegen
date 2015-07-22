package com.hitao.codegen.access.impl;

import static com.hitao.codegen.constent.StringConstants.CODEGEN_PROJECT_SOURCE_FOLDER;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_PROJECT_UNITTEST_SOURCE_FOLDER;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_WEB_PROJECT_IBATIS_SQLMAP_CONFIG_DIR;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.hitao.codegen.access.IDaoGenStrategy;
import com.hitao.codegen.configs.CodeGenConfigHelper;
import com.hitao.codegen.configs.DAOServiceConfigurationManager;
import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.dao.IDAOConfigs;
import com.hitao.codegen.configs.exception.CodeGenException;
import com.hitao.codegen.util.StringUtils;

/***
 * The helper class for generate classes of DAO. It contains all the codegen
 * information in all configuration files.
 * 
 * @author zhangjun.ht
 * @created 2010-11-29
 * @version $Id: DaoGenStrategy.java 58 2011-05-06 06:40:39Z guest $
 */
public abstract class DaoGenStrategy implements IDaoGenStrategy {
	private static final Logger logger_ = Logger
			.getLogger(IDaoGenStrategy.class);

	// the biz's project directory
	protected File bizProjectDir_ = null;

	// the dal's project directory
	protected File dalProjectDir_ = null;

	// the web's project directory
	protected File webProjectDir_ = null;
	
	// the DAO directory 
	private File daoInDir_ = null;
	
	private String stringDaoInPath_ = null;
	private String stringDoOutPath_ = null;
	private String stringManagerOutPath_ = null;
	private String stringAoOutPath_ = null;
	private String stringSqlMapOutDir_ = null;
	private String stringWebProjectOutDir_ = null;
	private String stringDaoUnitTestOutDir_ = null;

	private static IDaoGenStrategy INSTANCE = null;

	protected CodeGenConfigHelper codeGenConfigHelper_ = null;
	protected boolean load_ = false;

	protected static final String SOURCE_CODE_FOLDER = DAOServiceConfigurationManager
	.getProperty(CODEGEN_PROJECT_SOURCE_FOLDER);
	
	protected static final String CONFIG_SOURCE_FOLDER = DAOServiceConfigurationManager
			.getProperty(CODEGEN_WEB_PROJECT_IBATIS_SQLMAP_CONFIG_DIR);

	protected static final String TEST_SOURCE_CODE_FOLDER = DAOServiceConfigurationManager
	.getProperty(CODEGEN_PROJECT_UNITTEST_SOURCE_FOLDER);
	
	static {
		try {
			String className = DAOServiceConfigurationManager.getProperty(
					IDaoGenStrategy.class.getName(),
					DaoGenStrategy.class.getName());
			INSTANCE = (DaoGenStrategy) Class.forName(className).newInstance();
		} catch (Exception ee) {
			logger_.error(
					"please set the code generate strategy in SystemConfig.xml",
					ee);
		}
	}

	public static IDaoGenStrategy getInstance() {
		return INSTANCE;
	}

	public Collection<IDAOConfigs> getDaoConfigs() throws CodeGenException {
		return getCodeGenConfigHelper().getDaoGens();
	}

	/***
	 * Load all the configuration files and validate all the DAO.
	 * 
	 * @throws IOException
	 * @throws CodeGenException
	 */
	protected void loadConfigFile() throws CodeGenException {
		if (!load_) {
			codeGenConfigHelper_ = getCodeGenConfigHelperInstance();
			Collection<IDAOConfigs> daogenList = codeGenConfigHelper_
					.getDaoGens();
			if (daogenList != null) {
				for (IDAOConfigs dao : daogenList) {
					dao.validate();
				}
			}
			load_ = true;
		}
	}

	protected abstract CodeGenConfigHelper getCodeGenConfigHelperInstance();

	/***
	 * Get config helper class.
	 * 
	 * @throws CodeGenException
	 */
	public synchronized CodeGenConfigHelper getCodeGenConfigHelper()
			throws CodeGenException {
		if (!load_) {
			loadConfigFile();
		}
		return codeGenConfigHelper_;
	}

	@Override
	public ICodeGenConfig getDaoGenByName(String argClassName)
			throws CodeGenException {
		return getCodeGenConfigHelper().getDaoGenByName(argClassName);
	}

	/**
	 * @return Returns the DAO input directory.
	 */
	public String getDaoStringInDir() {
		return stringDaoInPath_;
	}

	/**
	 * @return Returns the DO output directory..
	 */
	public String getDoStringOutDir() {
		return stringDoOutPath_;
	}

	/**
	 * @return Returns the Manager output directory..
	 */
	public String getManagerStringOutDir() {
		return stringManagerOutPath_;
	}

	/**
	 * @return Returns the Manager output directory..
	 */
	public String getAoStringOutDir() {
		return stringAoOutPath_;
	}

	/***
	 * @return returns SQL map output directory.
	 */
	public String getSqlMapStringOutDir() {
		return stringSqlMapOutDir_;
	}
	
	/***
	 * @return returns web project output directory.
	 */
	public String getWebProjectStringOutDir() {
		return stringWebProjectOutDir_;
	}
	
	/***
	 * get the output directory for the DAO unit test.
	 * 
	 * @return output directory for the DAO unit test.
	 */
	public String getDaoUnitTestStringOutDir() {
		return stringDaoUnitTestOutDir_;
	}
	
	/**
	 * Set the output directory for DAO.
	 * 
	 * @param argDaoInDir
	 *            the output directory for DAO.
	 */
	protected void setDaoInDir(File argDalProjectDir) throws IOException {
		File dalDir = argDalProjectDir;
		if (dalDir == null) {
			dalDir = new File(".");
		}
		daoInDir_ = new File(dalDir, getSourcePackage());
		
		if (daoInDir_.isAbsolute()) {
			stringDaoInPath_ = daoInDir_.getAbsolutePath();
		} else {
			stringDaoInPath_ = daoInDir_.getCanonicalPath();
		}
		
		logger_.info("The DAO output directory is: " + stringDaoInPath_);
	}

	/**
	 * Set the output directory for DO.
	 * 
	 * @param argDoOutDir
	 *            the output directory for DO.
	 */
	protected void setDoOutDir(File argDalProjectDir) throws IOException {
		File dalDir = argDalProjectDir;
		if (dalDir == null) {
			dalDir = new File(".");
		}
		
		File doOutDir = new File(dalDir, getSourcePackage());
		
		if (doOutDir.isAbsolute()) {
			stringDoOutPath_ = doOutDir.getAbsolutePath();
		} else {
			stringDoOutPath_ = doOutDir.getCanonicalPath();
		}
		
		logger_.info("The DO output directory is: " + stringDoOutPath_);
	}

	/**
	 * Set the output directory for Manager.
	 * 
	 * @param argManagerOutDir
	 *            the directory for BIZ.
	 */
	protected void setManagerOutDir(File argBizProjectDir) throws IOException {
		File bizDir = argBizProjectDir;
		if (bizDir == null) {
			bizDir = new File(".");
		}
		
		File managerOutDir = new File(bizDir, getSourcePackage());
		
		if (managerOutDir.isAbsolute()) {
			stringManagerOutPath_ = managerOutDir.getAbsolutePath();
		} else {
			stringManagerOutPath_ = managerOutDir.getCanonicalPath();
		}
		
		logger_.info("The Manager output directory is: "
				+ stringManagerOutPath_);
	}

	/**
	 * Set the output directory for AO.
	 * 
	 * @param argBizProjectDir
	 *            the directory for BIZ.
	 */
	protected void setAoOutDir(File argBizProjectDir) throws IOException {
		File bizDir = argBizProjectDir;
		if (bizDir == null) {
			bizDir = new File(".");
		}
		
		File aoOutDir = new File(bizDir, getSourcePackage());
		
		if (aoOutDir.isAbsolute()) {
			stringAoOutPath_ = aoOutDir.getAbsolutePath();
		} else {
			stringAoOutPath_ = aoOutDir.getCanonicalPath();
		}
		
		logger_.info("The AO output directory is: " + stringAoOutPath_);
	}

	/***
	 * Set the SQL output directory.
	 * 
	 * @param argWebProjectDir
	 *            web project output directory.
	 */
	protected void setSqlMapOutDir(File argDalProjectDir) throws IOException {
		File dalDir = argDalProjectDir;
		if (dalDir == null) {
			dalDir = new File(".");
		}
		
		File sqlmapOutDir = new File(dalDir, getConfigPackage());
		
		if (dalDir.isAbsolute()) {
			stringSqlMapOutDir_ = sqlmapOutDir.getAbsolutePath();
		} else {
			stringSqlMapOutDir_ = sqlmapOutDir.getCanonicalPath();
		}
		
		logger_.info("The sql map output directory is: " + stringSqlMapOutDir_);
	}

	/***
	 * Set the spring XML in web project output directory.
	 * 
	 * @param argWebProjectDir
	 *           web project output directory.
	 */
	protected void setWebProjectOutDir(File argWebProjectDir) throws IOException {
		File webDir = argWebProjectDir;
		if (webDir == null) {
			webDir = new File(".");
		}
		
		if (webDir.isAbsolute()) {
			stringWebProjectOutDir_ = webDir.getAbsolutePath();
		} else {
			stringWebProjectOutDir_ = webDir.getCanonicalPath();
		}
		
		logger_.info("The web project output directory is: "
				+ stringWebProjectOutDir_);
	}
	
	/***
	 * Set the output directory for DAO unit test.
	 * 
	 * @param argDalDir
	 *            the output directory for DAL project.
	 * @throws IOException 
	 */
	protected void setDaoUnitTestStringOutDir(File argDalProjectDir) throws IOException {
		File dalDir = argDalProjectDir;
		if (dalDir == null) {
			dalDir = new File(".");
		}
		
		File unitTestOutDir = new File(dalDir, getSourcePackage(TEST_SOURCE_CODE_FOLDER));
		
		if (unitTestOutDir.isAbsolute()) {
			stringDaoUnitTestOutDir_ = unitTestOutDir.getAbsolutePath();
		} else {
			stringDaoUnitTestOutDir_ = unitTestOutDir.getCanonicalPath();
		}
		
		logger_.info("The unit test output directory is: " + stringDaoUnitTestOutDir_);
	}
	
	/***
	 * @return the directory of the DAO.
	 */
	protected File getDaoInDir() {
		return daoInDir_;
	}
	
	public File getBizProjectDir() {
		return bizProjectDir_ == null ? getDalProjectDir() : bizProjectDir_;
	}

	public File getDalProjectDir() {
		return dalProjectDir_;
	}

	public File getWebProjectDir() {
		return webProjectDir_ == null ? getDalProjectDir() : webProjectDir_;
	}

	/***
	 * Set the output directory for BIZ.
	 * 
	 * @param argDaoUnitTestDir
	 *            the output directory for BIZ project.
	 */
	public void setBizProjectDir(File argBizProjectDir) throws IOException {
		if (argBizProjectDir == null) {
			throw new IllegalArgumentException("The biz project directory can't be null.");
		}
		this.bizProjectDir_ = argBizProjectDir;
		
		setManagerOutDir(argBizProjectDir);
		setAoOutDir(argBizProjectDir);
	}

	/***
	 * Set the output directory for DAL.
	 * 
	 * @param argDaoUnitTestDir
	 *            the output directory for DAL project.
	 */
	public void setDalProjectDir(File argDalProjectDir) throws IOException {
		this.dalProjectDir_ = argDalProjectDir;
		setDaoInDir(argDalProjectDir);
		setDaoUnitTestStringOutDir(argDalProjectDir);
		setDoOutDir(argDalProjectDir);
		setSqlMapOutDir(argDalProjectDir);
	}

	/***
	 * Set the output directory for WEB.
	 * 
	 * @param argDaoUnitTestDir
	 *            the output directory for WEB project.
	 */
	public void setWebProjectDir(File argWebProjectDir) throws IOException {
		this.webProjectDir_ = argWebProjectDir;
		setWebProjectOutDir(argWebProjectDir);
	}
	
	/***
	 * Get default source folder.
	 * 
	 * @return default source folder.
	 */
	protected String getSourcePackage() {
		return getSourcePackage(SOURCE_CODE_FOLDER);
	}
	
	protected String getConfigPackage() {
		return getSourcePackage(CONFIG_SOURCE_FOLDER);
	}

	/***
	 * Get source folder through specific source folder.
	 * 
	 * @param argSourceCodeFolder
	 * @return specific source folder.
	 */
	protected String getSourcePackage(String argSourceCodeFolder) {
		String sourceClassFolder = argSourceCodeFolder;
		if (StringUtils.isEmpty(sourceClassFolder)) {
			sourceClassFolder = "src.main.java";
		}
		return StringUtils.replaceAll(
				new StringBuffer(sourceClassFolder), ".", File.separator).toString();
	}
}
