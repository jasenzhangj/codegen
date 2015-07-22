package com.hitao.codegen.configs;

import java.util.List;
import java.util.Set;

import com.hitao.codegen.configs.basic.IConfigObject;
import com.hitao.codegen.configs.basic.IParentConfig;
import com.hitao.codegen.configs.common.FieldConfig;
import com.hitao.codegen.configs.common.MethodConfig;

/***
 * This interface represent a class configuration.
 * 
 * @author zhangjun.ht
 * @created 2010-12-1
 * @version $Id: IClassCodeGenConfig.java 29 2011-03-01 01:47:28Z guest $
 */
public interface IClassCodeGenConfig extends IClassElementCodeGenConfig, IConfigObject,
		IParentConfig {

	/***
	 * get all classes need imported.
	 * 
	 * @return
	 */
	Set<String> getAllImportClass();

	/***
	 * get fields list.
	 * 
	 * @return
	 */
	List<FieldConfig> getFieldList();

	/***
	 * get methods list.
	 * 
	 * @return
	 */
	List<MethodConfig> getMethodList();

	/***
	 * Set extended class
	 * 
	 * @param extendsClass
	 */
	void setExtendsClass(String argExtendsClass);

	/***
	 * Set implement class.
	 */
	void setImplementsClasses(String argImplementsClasses);
}