package com.hitao.codegen.configs;

import java.util.List;

import com.hitao.codegen.configs.append.IAppendCodesInfo;
import com.hitao.codegen.configs.basic.IParentConfig;
import com.hitao.codegen.configs.common.AbstractCodeGenConfig.GenerateModel;

/***
 * The common interface for code configuration.
 * 
 * @author zhangjun.ht
 * @created 2010-11-26
 * @version $Id: ICodeGenConfig.java 45 2011-03-09 08:47:45Z guest $
 */
public interface ICodeGenConfig extends ICodePieceInfo, ICodeValidate,
		ICodeGenInitial, IParentConfig {

	/***
	 * The name of the configuration object.
	 * 
	 * @return name of the configuration object.
	 */
	String getName();

	/***
	 * Set the name for configuration object.
	 * 
	 * @param argName
	 */
	void setName(String argName);

	/***
	 * get package path.
	 * 
	 * @return
	 */
	String getPackagePath();

	/**
	 * Gets the dirty flag
	 * 
	 * @return the dirty flag
	 */
	boolean isDirty();

	/**
	 * Clears the dirty flag
	 */
	void setClean();

	/**
	 * Sets the dirty flag
	 */
	void setDirty();

	/***
	 * set package path.
	 * 
	 * @param packagePath
	 */
	void setPackagePath(String packagePath);

	/**
	 * gets the value of the config from the text contents of the node
	 * representing the config object.
	 * 
	 */
	String getValue();

	/***
	 * check the config object whether it has been built.
	 * 
	 * @return
	 */
	boolean isInitial();

	/***
	 * Set the flag for initialization.
	 * 
	 * @param argInitialFlag
	 */
	void setInitial(boolean argInitialFlag);

	/***
	 * Check whether configuration should be override.
	 * 
	 * @return true if it is, or else.
	 */
	boolean shouldBeOverride();

	/***
	 * Set the override mode for this configuration.
	 */
	void setOverride();

	/***
	 * Set the do nothing mode for this configuration.
	 */
	void setDoNothing();

	/***
	 * Check whether configuration file should be generated.
	 * 
	 * This method is different from the <b>shouldBeNew</b>. This method is mean
	 * that this configuration objct should do somen thing, maybe delete, new,
	 * or reduce and so on. But <b>shouldBeNew</b> is just mean create a new
	 * file.
	 * 
	 * @return true if it is, or else.
	 */
	boolean shouldBeGenerated();

	/***
	 * Check whether configuration file should be new.
	 * 
	 * @return true if it is, or else.
	 */
	boolean shouldBeNew();

	/***
	 * Check whether configuration file should be append.
	 * 
	 * @return true if it is, or else.
	 */
	boolean shouldBeAppend();

	/***
	 * Check whether configuration file should be delete.
	 * 
	 * @return true if it is, or else.
	 */
	boolean shouldBeDelete();

	/***
	 * Check whether generateed codes should be delete in configuration.
	 * 
	 * @return true if it is, or else.
	 */
	boolean shouldBeReduce();

	/***
	 * Set the append mode for this configuration.
	 */
	void setAppendModel();

	/***
	 * Set generate model.
	 * 
	 * @param argGenerateModel GenerateModel
	 */
	void setGenerateModel(GenerateModel argGenerateModel);

	/***
	 * Get the file type for generate.
	 * 
	 * @return
	 */
	String getFileType();

	/***
	 * Get the end element for the configuration file.
	 * 
	 * @return
	 */
	String getEndElement();

	/***
	 * Get the code generate codes model.
	 * 
	 * @return
	 */
	GenerateModel getGenerateModel();

	/***
	 * Get the append information when it should be append codes in the file.
	 * 
	 * @param argList
	 * @return
	 */
	List<IAppendCodesInfo> getAppendCodesInfo(List<IAppendCodesInfo> argList);
}
