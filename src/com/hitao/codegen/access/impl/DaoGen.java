package com.hitao.codegen.access.impl;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import com.hitao.codegen.access.IDaoGenStrategy;
import com.hitao.codegen.access.command.CommandHolder;
import com.hitao.codegen.util.SystemPropertiesLoader;
import com.hitao.codegen.util.SystemPropertiesLoader.LocationType;

/***
 * This class initialize all the resource and generate DAO and DO and other
 * objects.
 * 
 * @author zhangjun.ht
 * @created 2010-11-29
 * @version $Id: DaoGen.java 58 2011-05-06 06:40:39Z guest $
 */
public class DaoGen implements Callable<Void> {

	private IDaoGenStrategy daoGenStrategy_ = null;

	// the biz's project directory
	private File bizProjectDir_ = null;

	// the dal's project directory
	private File dalProjectDir_ = null;

	// the web's project directory
	private File webProjectDir_ = null;

	static {
		try {
			// Initialize system properties.
			new SystemPropertiesLoader(LocationType.CLASSPATH,
					"configs/system.properties").loadSystemProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public DaoGen(String argBizProjectDir, String argDalProjectDir,
			String argWebProjectDir) throws IOException {
		if (argDalProjectDir == null) {
			throw new IllegalArgumentException("The project directory of dal can't be null");
		}
		argBizProjectDir = (argBizProjectDir == null ? argDalProjectDir : argBizProjectDir);
		argWebProjectDir = (argWebProjectDir == null ? argDalProjectDir : argWebProjectDir);
		initial(new File(argBizProjectDir), new File(argDalProjectDir), new File(argWebProjectDir));
	}
	
	public DaoGen(File argBizProjectDir, File argDalProjectDir,
			File argWebProjectDir) throws IOException {
		initial(argBizProjectDir, argDalProjectDir, argWebProjectDir);
	}
	
	protected void initial(File argBizProjectDir, File argDalProjectDir,
			File argWebProjectDir) throws IOException {
		bizProjectDir_ = argBizProjectDir;
		dalProjectDir_ = argDalProjectDir;
		webProjectDir_ = argWebProjectDir;
		initialConfigDir();
	}

	/***
	 * Initialize configuration directories.
	 * 
	 * @param argInDir
	 * @param argOutDir
	 * @throws IOException
	 */
	protected void initialConfigDir() throws IOException {
		daoGenStrategy_ = DaoGenStrategy.getInstance();
		daoGenStrategy_.setDalProjectDir(getDalProjectDir());
		daoGenStrategy_.setBizProjectDir(getBizProjectDir());
		daoGenStrategy_.setWebProjectDir(getWebProjectDir());
	}

	public IDaoGenStrategy getDaoGenStrategy() {
		return daoGenStrategy_;
	}

	public void setDaoGenStrategy(DaoGenStrategy argDaoGenHelper) {
		this.daoGenStrategy_ = argDaoGenHelper;
	}

	public File getBizProjectDir() {
		return bizProjectDir_;
	}

	public File getDalProjectDir() {
		return dalProjectDir_;
	}

	public File getWebProjectDir() {
		return webProjectDir_;
	}

	@Override
	public Void call() throws Exception {
		//new CodeGenerate(getDaoGenStrategy()).call();
		CommandHolder.getCommand(getDaoGenStrategy()).invoke();
		return null;
	}

}
