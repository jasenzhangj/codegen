package com.hitao.codegen.configs.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * This annotation is represent that the interface it described is DAO.
 * 
 * @author zhangjun.ht
 * @created 2011-1-13
 * @version $Id: DAO.java 54 2011-04-22 09:31:54Z guest $
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface DAO {
	String concreteClass() default "";
	String extendsClass() default "";
}
