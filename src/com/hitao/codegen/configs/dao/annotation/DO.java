package com.hitao.codegen.configs.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * This annotation is represent DO.
 * 
 * @author zhangjun.ht
 * @created 2011-1-13
 * @version $Id: DO.java 12 2011-02-20 10:50:23Z guest $
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface DO {
	String table();
}
