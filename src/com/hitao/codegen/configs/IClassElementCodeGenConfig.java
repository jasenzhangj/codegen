package com.hitao.codegen.configs;

import java.util.Set;

import com.hitao.codegen.configs.common.AnnotationConfig;
import com.hitao.codegen.configs.common.CommentConfig;
import com.hitao.codegen.configs.common.ImportConfig;

/***
 * This interface is represent the configuration object of the class's element.
 * e.g: import class, class name, interface name, method, field, exception.
 *
 * @author  zhangjun.ht
 * @created 2011-2-23
 * @version $Id: IClassElementCodeGenConfig.java 57 2011-04-29 02:16:16Z guest $
 */
public interface IClassElementCodeGenConfig extends ICodeGenConfig{

	/***
	 * The comment for configuration object. Maybe it will be method, property,
	 * or class etc.
	 *
	 * @return
	 */
	CommentConfig getComment();

	/***
	 * get the type for configuration object.
	 *
	 * @return the whole class path.
	 */
	String getClassName();

	/***
	 * Set the modify.
	 * @param argModify
	 */
	void setModify(String argModify);

	/***
	 * Get the modify.
	 * @return
	 */
	String getModify();
	
	/***
	 * Set the type for configuration object.
	 *
	 * @param argType
	 *            the whole class path.
	 */
	void setClassName(String argType);
	
	/***
	 * Get the generic type class.
	 * @return
	 */
	String getGenericType();

	/***
	 * Set the generic type class.
	 * @param argGeneric
	 */
	void setGenericType(String argGeneric);

	/***
	 * Get the concrete class for the generic class type.
	 * @return
	 */
	String getConcreteClass();

	/***
	 * Set the concrete class for the generic class type.
	 * @return
	 */
	void setConcreteClass(String concreteClass);
	
	/***
	 * Remove imported class.
	 * @param argImport
	 */
	void removeImportedClass(String argImport);
	
	/***
	 * Remove specific annotation.
	 * @param argAnnotation
	 */
	void removeAnnotation(String argAnnotation);
	
	/***
	 * Check whether class or method contains specific annotation.
	 * @param argAnnotation
	 */
	boolean containsAnnotation(String argAnnotation);

	/***
	 * Get import configuration list.
	 * @return
	 */
	Set<ImportConfig> getImportList() ;
	
	/***
	 * Get annotations for the configuration.
	 * @return
	 */
	Set<AnnotationConfig> getAnnotations(); 
	
	/***
	 * Clear all imported classes.
	 */
	void clearImportClasses();
}
