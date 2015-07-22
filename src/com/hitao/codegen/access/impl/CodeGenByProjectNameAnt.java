package com.hitao.codegen.access.impl;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.hitao.codegen.util.ClassPathUtils;

/***
 * Ant task that generates the source code for DAO access classes by set project
 * name.
 * 
 * @author zhangjun.ht
 * @created 2011-3-9
 * @version $Id: CodeGenByProjectNameAnt.java 47 2011-03-10 01:05:04Z guest $
 */
public class CodeGenByProjectNameAnt extends Task {

	private Logger logger_ = Logger.getLogger(CodeGenByProjectNameAnt.class);

	// the biz's project directory
	private File bizProjectDir_ = null;

	// the dal's project directory
	private File dalProjectDir_ = null;

	// the web's project directory
	private File webProjectDir_ = null;

	// the dal's project basic directory
	private File dalRootDir_ = null;

	static {

		// Since the logger is not getting configured, I guess we had better do
		// it ourselves.
		// BasicConfigurator.configure();
		new PropertyConfigurator().doConfigure(
				ClassPathUtils.getResource("configs/log4j.properties"),
				LogManager.getLoggerRepository());
	}

	@Override
	public void execute() throws BuildException {
		DaoGen main;
		try {
			initializeDir();
			File bizProjectDir = getBizProjectDir();
			File dalProjectDir = getDalProjectDir();
			File webProjectDir = getWebProjectDir();

			if (bizProjectDir == null || dalProjectDir == null
					|| webProjectDir == null) {
				logger_.error("Please set the dalRootDir attribute or bizProjectDir, dalProjectDir, webProjectDir attribute for CodeGenByProjectNameAnt.");
				return;
			}

			main = new AnnotationDaoGen(bizProjectDir, dalProjectDir,
					webProjectDir);
			main.call();

		} catch (IOException e) {
			e.printStackTrace();
			logger_.error("Generate codes faild.");
		} catch (Exception e) {
			e.printStackTrace();
			logger_.error("Generate codes faild.");
		}
	}

	protected void initializeDir() {
		File bizProjectDir = getBizProjectDir();
		File dalProjectDir = getDalProjectDir();
		File webProjectDir = getWebProjectDir();
		File dalRootDir = getDalRootDir();

		if ((bizProjectDir == null || dalProjectDir == null || webProjectDir == null)
				&& dalRootDir != null) {
			logger_.info("the root directory of the dal project is: "+ dalRootDir.toURI());
			File webxProjectRoot = dalRootDir.getParentFile();
			if (webxProjectRoot != null) {
				String tempDir = null;
				for (File projectDir : webxProjectRoot.listFiles()) {
					if (projectDir.isDirectory()) {
						tempDir = projectDir.toURI().toString();
						if (tempDir.endsWith("/")) {
							tempDir = tempDir
									.substring(0, tempDir.length() - 1);
						}

						if (tempDir.endsWith("-biz")) {
							setBizProjectDir(projectDir);
						} else if (tempDir.endsWith("-dal")) {
							setDalProjectDir(projectDir);
						} else if (tempDir.endsWith("-web")) {
							setWebProjectDir(projectDir);
						}
					}
				}
			} else {
				logger_.error("Can't resolve the directory:"
						+ dalRootDir.toURI());
			}
		}
	}

	public File getBizProjectDir() {
		return bizProjectDir_;
	}

	public void setBizProjectDir(File argBizProjectDir) {
		this.bizProjectDir_ = argBizProjectDir;
	}

	public File getDalProjectDir() {
		return dalProjectDir_;
	}

	public void setDalProjectDir(File argDalProjectDir) {
		this.dalProjectDir_ = argDalProjectDir;
	}

	public File getWebProjectDir() {
		return webProjectDir_;
	}

	public void setWebProjectDir(File argWebProjectDir) {
		this.webProjectDir_ = argWebProjectDir;
	}

	public File getDalRootDir() {
		return dalRootDir_;
	}

	public void setDalRootDir(File argDalRootDir) {
		this.dalRootDir_ = argDalRootDir;
	}
}
