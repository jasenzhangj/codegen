package com.hitao.codegen.util;

import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A library of utilities for working with Classes, Objects, and reflection.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: ObjectUtils.java 12 2011-02-20 10:50:23Z guest $
 */
@SuppressWarnings({ "unchecked","rawtypes" })
public final class ObjectUtils {
	/**
	 * system property to check if an exception should be thrown for
	 * non-existant classes
	 */
	public static final String THROW_IF_CLASS_NOT_FOUND_PROPERTY = ObjectUtils.class
			.getName() + ".throwIfClassNotFound";

	private static final Logger logger_ = Logger.getLogger(ObjectUtils.class);

	private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];

	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	private static final Map<Class, Class> PRIMITIVE_CLASS_MAP;
	static {
		Map<Class, Class> m = new HashMap<Class, Class>();
		m.put(Boolean.class, boolean.class);
		m.put(Byte.class, byte.class);
		m.put(Character.class, char.class);
		m.put(Double.class, double.class);
		m.put(Float.class, float.class);
		m.put(Integer.class, int.class);
		m.put(Long.class, long.class);
		m.put(Short.class, short.class);
		m.put(Void.class, void.class);

		PRIMITIVE_CLASS_MAP = Collections.unmodifiableMap(m);
	}

	private static final Map<Class, Class> NON_PRIMITIVE_CLASS_MAP;
	static {
		Map<Class, Class> m = new HashMap<Class, Class>();
		m.put(boolean.class, Boolean.class);
		m.put(byte.class, Byte.class);
		m.put(char.class, Character.class);
		m.put(double.class, Double.class);
		m.put(float.class, Float.class);
		m.put(int.class, Integer.class);
		m.put(long.class, Long.class);
		m.put(short.class, Short.class);
		m.put(void.class, Void.class);

		NON_PRIMITIVE_CLASS_MAP = Collections.unmodifiableMap(m);
	}

	private static final Map<Class, Method> PRIMITIVE_UNWRAP_METHOD_MAP;
	static {
		Map<Class, Method> m = new HashMap<Class, Method>();
		try {
			m.put(Boolean.class,
					Boolean.class.getMethod("booleanValue", EMPTY_CLASS_ARRAY));
			m.put(Byte.class,
					Byte.class.getMethod("byteValue", EMPTY_CLASS_ARRAY));
			m.put(Character.class,
					Character.class.getMethod("charValue", EMPTY_CLASS_ARRAY));
			m.put(Double.class,
					Double.class.getMethod("doubleValue", EMPTY_CLASS_ARRAY));
			m.put(Float.class,
					Float.class.getMethod("floatValue", EMPTY_CLASS_ARRAY));
			m.put(Integer.class,
					Integer.class.getMethod("intValue", EMPTY_CLASS_ARRAY));
			m.put(Long.class,
					Long.class.getMethod("longValue", EMPTY_CLASS_ARRAY));
			m.put(Short.class,
					Short.class.getMethod("shortValue", EMPTY_CLASS_ARRAY));
		} catch (Throwable ex) {
			Logger.getLogger(ObjectUtils.class).error("CAUGHT EXCEPTION", ex);
		}

		PRIMITIVE_UNWRAP_METHOD_MAP = Collections.unmodifiableMap(m);
	}

	private static final Map<Class, String> PRIMITIVE_STRING_MAP;
	static {
		Map<Class, String> m = new HashMap<Class, String>();
		m.put(byte.class, "byte");
		m.put(char.class, "char");
		m.put(double.class, "double");
		m.put(float.class, "float");
		m.put(int.class, "int");
		m.put(long.class, "long");
		m.put(short.class, "short");
		m.put(boolean.class, "boolean");
		m.put(void.class, "void");

		PRIMITIVE_STRING_MAP = Collections.unmodifiableMap(m);
	}

	private static final Map<String, Class> COMMON_CLASS_LOOKUP_MAP;
	static {
		Map<String, Class> m = new HashMap<String, Class>();
		m.put("String", String.class);
		m.put("Integer", Integer.class);
		m.put("int", int.class);
		m.put("Double", Double.class);
		m.put("double", double.class);
		m.put("Float", Float.class);
		m.put("float", float.class);
		m.put("Byte", Byte.class);
		m.put("byte", byte.class);
		m.put("Character", Character.class);
		m.put("char", char.class);
		m.put("Boolean", Boolean.class);
		m.put("boolean", boolean.class);
		m.put("Short", Short.class);
		m.put("short", short.class);
		m.put("Long", Long.class);
		m.put("long", long.class);
		m.put("Date", Date.class);
		m.put("List", List.class);
		m.put("BigDecimal", BigDecimal.class);

		COMMON_CLASS_LOOKUP_MAP = Collections.unmodifiableMap(m);
	}

	private static final Map<String, Class> COMMON_LANG_PACKAGE_CLASS_LOOKUP_MAP;
	static {
		Map<String, Class> m = new HashMap<String, Class>();
		m.put("String", String.class);
		m.put("Integer", Integer.class);
		m.put("int", int.class);
		m.put("Double", Double.class);
		m.put("double", double.class);
		m.put("Float", Float.class);
		m.put("float", float.class);
		m.put("Byte", Byte.class);
		m.put("byte", byte.class);
		m.put("Character", Character.class);
		m.put("char", char.class);
		m.put("Boolean", Boolean.class);
		m.put("boolean", boolean.class);
		m.put("Short", Short.class);
		m.put("short", short.class);
		m.put("Long", Long.class);
		m.put("long", long.class);
		m.put("void", Void.class);
		m.put("Class", Class.class);
		m.put("StringBuffer", StringBuffer.class);
		m.put("Character", Character.class);
		m.put("Class", Class.class);
		m.put("ClassLoader", ClassLoader.class);
		m.put("Comparable", Comparable.class);
		m.put("Enum", Enum.class);
		m.put("Iterable", Iterable.class);
		m.put("Number", Number.class);
		m.put("System", System.class);
		m.put("Thread", Thread.class);
		m.put("Exception", Exception.class);
		m.put("Object", Object.class);
		COMMON_LANG_PACKAGE_CLASS_LOOKUP_MAP = Collections.unmodifiableMap(m);
	}

	/**
	 * prevent instantiation... this is a library
	 */
	private ObjectUtils() {
		/* prevent instantiation */
	}

	/**
	 * Gets the class refered to by <code>argClassKey</code> in
	 * <code>argKeyToClassMap</code>.
	 *
	 * @param argBundleName
	 *            the name of the bundle used to load <code>keyToClassMap</code>
	 *            as should be reported in any error logs
	 * @param argKeyToClassMap
	 *            a lookup Map for actual classes for class keys
	 * @param argClassKey
	 *            the key for the class of which to make an instance
	 * @return Class refered
	 * @todo write tests for this method
	 */
	public static final Class getClass(String argBundleName,
			Properties argKeyToClassMap, String argClassKey) {

		if (argKeyToClassMap == null) {
			String message = "Cannot create an instance mapped in a null Properties set!";
			logger_.error(message, new Throwable("STACK TRACE"));
			return getNotFound(message);
		}
		if (StringUtils.isEmpty(argClassKey)) {
			String message = "Cannot create an instance mapped to a null Properties key!";
			logger_.error(message, new Throwable("STACK TRACE"));
			return getNotFound(message);
		}
		String className = argKeyToClassMap.getProperty(argClassKey);

		if (StringUtils.isEmpty(className)) {
			String message = "Cannot create an instance for key ["
					+ argClassKey + "].  No class name was mapped in the ["
					+ argBundleName + "] set!";
			logger_.error(message, new Throwable("STACK TRACE"));
			return getNotFound(message);
		}
		return getClass(className);
	}

	private static final Class getNotFound(String argMessage) {
		if (Boolean.getBoolean(THROW_IF_CLASS_NOT_FOUND_PROPERTY)) {
			throw new RuntimeException(argMessage);
		}
		return null;
	}

	/**
	 * Gets the class for the specified name, resolving some common class names
	 * without requiring a fully qualified package. (e.g. primitives, primitive
	 * wrappers, <code>Date</code>, <code>List</code>, <code>String</code>,
	 * <code>BigDecimal</code>, etc.)
	 *
	 * @param argClassName
	 *            the name of the class to resolve
	 * @return the resolved class
	 */
	public static final Class getClass(String argClassName) {
		if ((argClassName == null) || (argClassName.length() == 0)) {
			return null;
		}
		Class found = COMMON_CLASS_LOOKUP_MAP.get(argClassName);
		if (found != null) {
			return found;
		}
		try {
			return ObjectUtils.class.getClassLoader().loadClass(argClassName);
		} catch (ClassNotFoundException ex) {
			logger_.error("No class found for name [" + argClassName + "]!", ex);
			throw new ReflectionException(ex);
		}
	}

	/**
	 * Gets the class for the specified name, resolving some common class names
	 * without requiring a fully qualified package. (e.g. primitives, primitive
	 * wrappers, <code>Date</code>, <code>List</code>, <code>String</code>,
	 * <code>BigDecimal</code>, etc.)
	 * 
	 * @param argClassName
	 *            the name of the class to resolve
	 * @param parent
	 *            interface of super class.
	 * @return the resolved class
	 */
	public static final <T> Class<T> getClass(String argClassName,
			Class<T> argParent) {
		if ((argClassName == null) || (argClassName.length() == 0)) {
			return null;
		}
		Class found = COMMON_CLASS_LOOKUP_MAP.get(argClassName);
		if (found != null) {
			return found;
		}
		try {
			Class instanceClass = ObjectUtils.class.getClassLoader().loadClass(
					argClassName);
			if (argParent.isAssignableFrom(instanceClass)) {
				return instanceClass.asSubclass(argParent);
			} else {
				throw new IllegalArgumentException("The " + argClassName
						+ "is not equal or subclas of the " + argParent);
			}
		} catch (ClassNotFoundException ex) {
			logger_.error("No class found for name [" + argClassName + "]!", ex);
			throw new ReflectionException(ex);
		}
	}

	/**
	 * Safely gets the component class of an array class.
	 *
	 * @param argClass
	 *            the class to get the component class for
	 * @return <code>argClass</code> if <code>argClass</code> is
	 *         <code>null</code>, or not an array class; otherwise the component
	 *         class
	 */
	public static final Class getComponentClass(Class argClass) {
		if (argClass == null) {
			return null;
		}
		while (argClass.isArray()) {
			argClass = argClass.getComponentType();
		}
		return argClass;
	}

	/**
	 * Safely gets the name of the class of an object, with no danger of a
	 * <code>NullPointerException</code>.
	 *
	 * @param argObject
	 *            the object for which to retreive class name
	 * @return the String "null" if <code>argObject</code> is <code>null</code>,
	 *         otherwise the name of the class of <code>argObject</code>
	 */
	public static final String getClassFromObject(Object argObject) {
		Class c = (argObject == null) ? null : argObject.getClass();
		return getClass(c, true);
	}

	/**
	 * Safely gets the name of a class, with no danger of a
	 * <code>NullPointerException</code>.
	 *
	 * @param argClass
	 *            the class to get name of
	 * @return the String "null" if <code>argClass</code> is <code>null</code>,
	 *         otherwise the name of the class
	 */
	public static final String getClass(Class argClass) {
		return getClass(argClass, true);
	}

	/**
	 * Safely gets the name of a class, with no danger of a
	 * <code>NullPointerException.</code>
	 *
	 * @param argClass
	 *            the class to get name of
	 * @param argIncludeArrayMarkers
	 *            whether [] should be included for array classes
	 * @return "null" if <code>argClass</code> is null, otherwise the name of
	 *         the class
	 */
	public static final String getClass(Class argClass,
			boolean argIncludeArrayMarkers) {

		if (argClass == null) {
			return "" + null;
		} else if (argClass.isPrimitive()) {
			String className = PRIMITIVE_STRING_MAP.get(argClass);
			if (className == null) {
				throw new IllegalArgumentException("unknown primitive class");
			}
			return className;
		} else if (argClass.isArray()) {
			int arrayLevels = 0;
			StringBuilder className = new StringBuilder(argClass.getName());

			while (className.charAt(0) == '[') {
				className.deleteCharAt(0);
				arrayLevels++;
			}

			switch (className.charAt(0)) {
			case 'L':

				// it's an object array in the format "Lclassname;"
				// remove the leading 'L' from the class name
				className.deleteCharAt(0);
				// remove the trailing ';' from the class name
				className.setLength(className.length() - 1);
				break;
			case 'B':
				className.setLength(0);
				className.append("byte");
				break;
			case 'C':
				className.setLength(0);
				className.append("char");
				break;
			case 'D':
				className.setLength(0);
				className.append("double");
				break;
			case 'F':
				className.setLength(0);
				className.append("float");
				break;
			case 'I':
				className.setLength(0);
				className.append("int");
				break;
			case 'J':
				className.setLength(0);
				className.append("long");
				break;
			case 'S':
				className.setLength(0);
				className.append("short");
				break;
			case 'Z':
				className.setLength(0);
				className.append("boolean");
				break;
			case 'V':
				className.setLength(0);
				className.append("void");
				break;
			default:
				throw new IllegalArgumentException(
						"unknown class getName value");
			}

			if (argIncludeArrayMarkers) {
				for (int i = 0; i < arrayLevels; i++) {
					className.append("[]");
				}
			}
			return className.toString();
		}
		return argClass.getName();
	}

	/**
	 * Gets a primitive class for a primitive wrapper object. (e.g.
	 * <code>new Integer(1)</code> results in <code>int.class</code>, etc.)
	 *
	 * @param obj
	 *            the object to get the primitive class for
	 * @return the primitive class wrapped by <code>obj</code>
	 * @see #getPrimitiveClass(Class)
	 */
	public static final Class getPrimitiveClass(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Class) {
			return getPrimitiveClass((Class) obj);
		}
		return getPrimitiveClass(obj.getClass());
	}

	/**
	 * Gets a primitive class for a primitive wrapper class. (e.g.
	 * <code>Integer</code> results in <code>int</code>, <code>Byte</code>
	 * results in <code>byte</code>, <code>Character</code> results in
	 * <code>char</code>, etc.)
	 *
	 * @param objClass
	 *            class for which to get cooresponding primitive class
	 * @return a primitive class wrapped by <code>objClass</code>, or
	 *         <code>objClass</code> if <code>objClass</code> is not a primitive
	 *         wrapper or <code>null</code> if <code>objClass</code> is
	 *         <code>null</code>
	 */
	public static final Class getPrimitiveClass(Class objClass) {
		Class returnClass = PRIMITIVE_CLASS_MAP.get(objClass);

		if (returnClass == null) {
			returnClass = objClass;
		}
		return returnClass;
	}

	/**
	 * Gets a primitive wrapper class for a primitive class. (e.g.
	 * <code>int</code> results in <code>Integer</code>, <code>byte</code>
	 * results in <code>Byte</code>, <code>char</code> results in
	 * <code>Character</code>, etc.)
	 *
	 * @param objClass
	 *            primitive class for which to get cooresponding wrapper class
	 * @return the wrapper class for <code>objClass</code>, or
	 *         <code>objClass</code> if <code>objClass</code> is not a primitive
	 *         or <code>null</code> if <code>objClass</code> is
	 *         <code>null</code>
	 */
	public static final Class getNonPrimitiveClass(Class objClass) {
		if ((objClass == null) || !objClass.isPrimitive()) {
			return objClass;
		}
		Class returnClass = NON_PRIMITIVE_CLASS_MAP.get(objClass);
		if (returnClass == null) {
			returnClass = objClass;
		}
		return returnClass;
	}

	/**
	 * Gets the classes for all of the objects in <code>objs</code>.
	 *
	 * @param objs
	 *            the objects to get the classes for
	 * @return the classes for each obj in <code>objs</code>
	 */
	public static final Class[] getClasses(Object[] objs) {
		if (objs == null) {
			return null;
		}
		Class[] objClasses = new Class[objs.length];

		for (int i = 0; i < objs.length; i++) {
			objClasses[i] = (objs[i] != null) ? objs[i].getClass() : null;
		}
		return objClasses;
	}

	/**
	 * Gets the primitive classes for all of the objects in <code>objs</code>.
	 *
	 * @param objs
	 *            the objects to get the primitive classes of
	 * @return the primitive class for each member of <code>objs</code>
	 * @see #getPrimitiveClass(Object)
	 */
	public static final Class[] getPrimitiveClasses(Object[] objs) {
		if (objs == null) {
			return null;
		}
		Class[] objClasses = new Class[objs.length];

		for (int i = 0; i < objs.length; i++) {
			objClasses[i] = getPrimitiveClass(objs[i]);
		}
		return objClasses;
	}

	/**
	 * Creates an instance of the class refered to by <code>argClassKey</code>
	 * as resolved from <code>argKeyToClassMap</code>.
	 *
	 * @param argBundleName
	 *            the name of the bundle used to load <code>keyToClassMap</code>
	 *            as should be reported in any error logs
	 * @param argKeyToClassMap
	 *            a lookup Map for actual classes for class keys
	 * @param argClassKey
	 *            the key for the class of which to make an instance
	 * @return new instance
	 */
	public static final Object createInstance(String argBundleName,
			Properties argKeyToClassMap, String argClassKey) {

		return createInstance(argBundleName, argKeyToClassMap, argClassKey,
				EMPTY_CLASS_ARRAY, EMPTY_OBJECT_ARRAY);
	}

	/**
	 * Creates an instance of the class refered to by <code>argClassKey</code>
	 * as resolved from <code>argKeyToClassMap</code> using the arguments passed
	 * in.
	 *
	 * @param argBundleName
	 *            the name of the bundle used to load <code>keyToClassMap</code>
	 *            as should be reported in any error logs
	 * @param argKeyToClassMap
	 *            a lookup Map for actual classes for class keys
	 * @param argClassKey
	 *            the key for the class of which to make an instance
	 * @param argParameterClasses
	 *            the classes used to find the constructor to use
	 * @param argParameters
	 *            the objects passed as arguments to the constructor
	 * @return new instance
	 */
	public static final Object createInstance(String argBundleName,
			Properties argKeyToClassMap, String argClassKey,
			Class[] argParameterClasses, Object[] argParameters) {

		Object newInstance = null;
		Class<?> objClass = getClass(argBundleName, argKeyToClassMap,
				argClassKey);

		if (objClass != null) {
			newInstance = createInstance(objClass, argParameterClasses,
					argParameters);
		}
		return newInstance;
	}

	/**
	 * Creates an instance of <code>objClass</code>.
	 *
	 * @param <T>
	 *            type if object
	 * @param objClass
	 *            class of which to make an instance
	 * @return new instance
	 */
	public static final <T> T createInstance(Class<T> objClass) {
		return createInstance(objClass, EMPTY_CLASS_ARRAY, EMPTY_CLASS_ARRAY);
	}

	/**
	 * Creates an instance of <code>objClass</code> using arguments passed in.
	 *
	 * @param <T>
	 *            type if object
	 * @param objClass
	 *            class of which to make an instance
	 * @param parameterClasses
	 *            the classes used to find the constructor to use
	 * @param parameters
	 *            the objects passed as arguments to the constructor
	 * @return new instance
	 */
	public static final <T> T createInstance(Class<T> objClass,
			Class[] parameterClasses, Object[] parameters) {

		if (objClass == null) {
			return null;
		}
		T newInstance = null;
		Constructor<T> objConstructor = null;

		try {
			objConstructor = objClass.getDeclaredConstructor(parameterClasses);
		} catch (NoSuchMethodException ex) {
			logger_.error(
					"No [" + objClass + "] constructor found for "
							+ "parameter classes ["
							+ ArrayUtils.toString(parameterClasses) + "]!", ex);
			logConstructors(objClass);
			throw new ReflectionException(ex);
		} catch (SecurityException ex) {
			logger_.error(
					"No accessible [" + objClass + "] constructor for "
							+ "parameter classes ["
							+ ArrayUtils.toString(parameterClasses) + "]!", ex);
			logConstructors(objClass);
			throw new ReflectionException(ex);
		}

		try {
			newInstance = objConstructor.newInstance(parameters);
		} catch (Exception ex) {
			logger_.error(
					"Error creating object for class [" + objClass
							+ "] with parameters ["
							+ ArrayUtils.toString(parameters)
							+ "] and parameter classes ["
							+ ArrayUtils.toString(parameterClasses) + "]!", ex);
			throw new ReflectionException(ex);
		}
		return newInstance;
	}

	/**
	 * Invokes a method on <code>obj</code>.
	 *
	 * @param methodName
	 *            the method to call
	 * @param obj
	 *            the object to start calling the object on
	 * @param argSourceDescription
	 *            a description of where this method invocation was configured
	 * @return the result of the method call
	 * @see #invokeMethod(String,Object,Class[],Object[],IMethodChainCallback,
	 *      String)
	 */
	public static final Object invokeMethod(String methodName, Object obj,
			String argSourceDescription) {

		return invokeMethod(methodName, obj, EMPTY_CLASS_ARRAY,
				EMPTY_OBJECT_ARRAY, (IMethodChainCallback) null,
				argSourceDescription);
	}

	/**
	 * Invokes a method on <code>obj</code>.
	 *
	 * @param methodName
	 *            the method to call
	 * @param obj
	 *            the object to start calling the object on
	 * @param parameterClasses
	 *            parameter types used in the first method call
	 * @param parameters
	 *            parameters used in the first method call
	 * @param argSourceDescription
	 *            a description of where this method invocation was configured
	 * @return the result of the method call
	 * @see #invokeMethod(String,Object,Class[],Object[],IMethodChainCallback,
	 *      String)
	 */
	public static final Object invokeMethod(String methodName, Object obj,
			Class[] parameterClasses, Object[] parameters,
			String argSourceDescription) {

		return invokeMethod(methodName, obj, parameterClasses, parameters,
				null, argSourceDescription);
	}

	/**
	 * Invokes a method, handling a callback if any.
	 *
	 * @param methodName
	 *            the method to call
	 * @param obj
	 *            the object to start calling the object on
	 * @param parameterClasses
	 *            parameter types used in the first method call
	 * @param parameters
	 *            parameters used in the first method call
	 * @param callback
	 *            a handler for any callbacks specified by {} in the
	 *            <code>methodName</code>
	 * @param argSourceDescription
	 *            a description of where this method invocation was configured
	 * @return the result of the method call
	 * @see #invokeMethod(String,Object,Class[],Object[],IMethodChainCallback,
	 *      String)
	 */
	public static final Object invokeMethod(String methodName, Object obj,
			Class[] parameterClasses, Object[] parameters,
			IMethodChainCallback callback, String argSourceDescription) {

		if (StringUtils.isEmpty(methodName) || (obj == null)) {
			return null;
		}
		Method targetMethod = null;

		Object target;
		Class targetClass;

		if (obj instanceof Class) {
			targetClass = (Class) obj;
			target = null;
		} else {
			targetClass = obj.getClass();
			target = obj;
		}

		String callbackParam = null;
		int idx1 = methodName.indexOf("{");
		int idx2 = methodName.indexOf("}");

		if ((-1 < idx1) && (idx1 < idx2)) {
			// Parse the Persistence manager type from the call.
			if (!(idx1 + 1 == idx2)) {
				callbackParam = methodName.substring(idx1 + 1, idx2).trim();
				if (callbackParam.length() == 0) {
					logger_.warn("invalid method name '" + methodName + "'");
					callbackParam = null;
				}
			}

			// Parse the base method name from the extended method call.
			methodName = methodName.substring(0, idx1);
		}

		try {
			targetMethod = targetClass.getMethod(methodName, parameterClasses);
		} catch (NoSuchMethodException ex) {
			logger_.error(
					"No [" + targetClass + "] method named [" + methodName
							+ "] found for parameters ["
							+ ArrayUtils.toString(parameterClasses) + "]:"
							+ StringUtils.nonNull(argSourceDescription), ex);
			logMethods(targetClass);
			throw new ReflectionException(ex);
		} catch (SecurityException ex) {
			logger_.error(
					"No accessible [" + targetClass + "] method named ["
							+ methodName + "] for parameters ["
							+ ArrayUtils.toString(parameterClasses) + "]:"
							+ StringUtils.nonNull(argSourceDescription), ex);
			logMethods(targetClass);
			throw new ReflectionException(ex);
		}
		Object returnValue = null;

		try {
			returnValue = targetMethod.invoke(target, parameters);
		} catch (Exception ex) {
			logger_.error(
					"Error invoking [" + targetClass + "] method named ["
							+ methodName + "] for parameters ["
							+ ArrayUtils.toString(parameters) + "]:"
							+ StringUtils.nonNull(argSourceDescription), ex);
			logMethods(targetClass);
			throw new ReflectionException(ex);
		}
		if (callbackParam != null) {
			if (callback == null) {
				logger_.warn("no callback to handle '" + methodName + "{"
						+ callbackParam + "}'");
			} else {
				returnValue = callback.resolve(returnValue, callbackParam);
			}
		}
		return returnValue;
	}

	/**
	 * Invokes a series of methods, walking down the returned values.
	 *
	 * @param methodNameChain
	 *            the methods to call
	 * @param rootTarget
	 *            the object to start calling the object on
	 * @param argSourceDescription
	 *            a description of where this method invocation was configured
	 * @return the result of the method calls, or null if any call along the way
	 *         returns null
	 * @see #invokeMethod(String,Object,Class[],Object[],IMethodChainCallback,
	 *      String)
	 */
	public static final Object invokeMethodChain(String methodNameChain,
			Object rootTarget, String argSourceDescription) {

		return invokeMethodChain(methodNameChain, rootTarget,
				EMPTY_CLASS_ARRAY, EMPTY_OBJECT_ARRAY,
				(IMethodChainCallback) null, argSourceDescription);
	}

	/**
	 * Invokes a series of methods, walking down the returned values.
	 *
	 * @param methodNameChain
	 *            the methods to call
	 * @param rootTarget
	 *            the object to start calling the object on
	 * @param rootParameters
	 *            paramters used in the first method call
	 * @param argSourceDescription
	 *            a description of where this method invocation was configured
	 * @return the result of the method calls, or null if any call along the way
	 *         returns null
	 * @see #invokeMethod(String,Object,Class[],Object[],IMethodChainCallback,
	 *      String)
	 */
	public static final Object invokeMethodChain(String methodNameChain,
			Object rootTarget, Object[] rootParameters,
			String argSourceDescription) {

		return invokeMethodChain(methodNameChain, rootTarget, null,
				rootParameters, null, argSourceDescription);
	}

	/**
	 * Invokes a series of methods, walking down the returned values.
	 *
	 * @param methodNameChain
	 *            the methods to call
	 * @param rootTarget
	 *            the object to start calling the object on
	 * @param paramClasses
	 *            parameter types used in the first method call
	 * @param rootParameters
	 *            paramters used in the first method call
	 * @param argSourceDescription
	 *            a description of where this method invocation was configured
	 * @return the result of the method calls, or null if any call along the way
	 *         returns null
	 * @see #invokeMethod(String,Object,Class[],Object[],IMethodChainCallback,
	 *      String)
	 */
	public static final Object invokeMethodChain(String methodNameChain,
			Object rootTarget, Class[] paramClasses, Object[] rootParameters,
			String argSourceDescription) {

		return invokeMethodChain(methodNameChain, rootTarget, paramClasses,
				rootParameters, null, argSourceDescription);
	}

	/**
	 * Invokes a series of methods, walking down the returned values. The method
	 * names are separated by dots (.). A callback is specified by following a
	 * method name with curly-braces surrounding a parameter passed to the
	 * callback.<br>
	 * e.g. For <code>methodNameChain</code>==
	 * <code>getTranId{TRANSACTION}.getLineItems</code> The method
	 * <code>getTranId()</code> on <code>rootTarget</code> would be called, then
	 * passed into the callback with a paramter of "TRANSACTION". Then the
	 * method <code>getLineItem()</code> would be called on the object returned
	 * from the callback. The result of this last call would then be returned.
	 * If a <code>null</code> value is returned from any method call, null is
	 * returned as the final answer.
	 *
	 * @param methodNameChain
	 *            the methods to call
	 * @param rootTarget
	 *            the object to start calling the object on
	 * @param paramClasses
	 *            parameter types used in the first method call
	 * @param rootParameters
	 *            paramters used in the first method call
	 * @param callback
	 *            a handler for any callbacks specified by {} in the
	 *            <code>methodNameChain</code>
	 * @param argSourceDescription
	 *            a description of where this method invocation was configured
	 * @return the result of the method calls, or null if any call along the way
	 *         returns null
	 */
	public static final Object invokeMethodChain(String methodNameChain,
			Object rootTarget, Class[] paramClasses, Object[] rootParameters,
			IMethodChainCallback callback, String argSourceDescription) {

		if (StringUtils.isEmpty(methodNameChain) || (rootTarget == null)) {
			return null;
		}
		Object currentTarget = rootTarget;
		Object[] currentParameters = (rootParameters != null) ? rootParameters
				: new Object[0];
		String[] methodNames = getMethodNames(methodNameChain);

		for (String methodName : methodNames) {
			currentTarget = ObjectUtils.invokeMethod(methodName, currentTarget,
					paramClasses, currentParameters, callback,
					argSourceDescription);
			if (currentTarget == null) {
				break;
			}
			// Only apply the parameters for the root method in the chain.
			if (currentParameters.length > 0) {
				paramClasses = new Class[0];
				currentParameters = new Object[0];
			}
		}
		return currentTarget;
	}

	/**
	 * Return individual methods in the given method chain.
	 *
	 * The same affect could be had be using methodNameChain.split("\\.") but
	 * that was found to have a bit of a performance hit, possibly due to the
	 * fact that it uses regEx. This version appears to run at about 4x the
	 * speed of the .split("\\.") version
	 *
	 * @param argMethodNameChain
	 * @return method names
	 */
	public static final String[] getMethodNames(String argMethodNameChain) {
		int dotIdx = argMethodNameChain.indexOf('.');

		if (dotIdx == -1) {
			return new String[] { argMethodNameChain };
		}

		ArrayList<String> methodNames = new ArrayList<String>(4);
		int lastIdx = 0;

		while (dotIdx != -1) {
			methodNames.add(argMethodNameChain.substring(lastIdx, dotIdx));
			lastIdx = dotIdx + 1;
			dotIdx = argMethodNameChain.indexOf('.', lastIdx);
		}

		// add the last method call
		methodNames.add(argMethodNameChain.substring(lastIdx,
				argMethodNameChain.length()));

		return methodNames.toArray(new String[methodNames.size()]);
	}

	/**
	 * Determines if a string specifies a valid class.
	 *
	 * @param className
	 *            the class to try to resolve
	 * @return true if the class can be resolved, otherwise false
	 */
	public static final boolean isValidClass(String className) {
		if (StringUtils.isEmpty(className)) {
			return false;
		}
		try {
			/* Class clazz = */ObjectUtils.class.getClassLoader().loadClass(
					className.trim());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/***
	 * Determines if a string is a common class in lang package.
	 *
	 * @param argName
	 * @return
	 */
	public static final boolean isCommonLangPackageClass(String argName) {
		return COMMON_LANG_PACKAGE_CLASS_LOOKUP_MAP.containsKey(argName);
	}

	/**
	 * Determines if a Class <code>argClass</code> implements an interface
	 * <code>argInterface</code>.
	 *
	 * @param argClass
	 *            the class to check
	 * @param argInterface
	 *            the interface to check for
	 * @return true if <code>argInterface</code> is an interface and is
	 *         implemented by <code>argClass</code>; otherwise false
	 */
	public static final boolean isImplementing(Class<?> argClass,
			Class<?> argInterface) {
		// check that the interface really is an interface
		if (!isInterface(argInterface)) {
			return false;
		}

		return argInterface.isAssignableFrom(argClass);
	}

	/**
	 * Determines if the class is an interface, resolving through any array
	 * classes.
	 *
	 * @param argClass
	 *            the class to check
	 * @return true if core class is an interface
	 */
	public static final boolean isInterface(Class argClass) {
		while (argClass.isArray()) {
			argClass = argClass.getComponentType();
		}
		return argClass.isInterface();
	}

	/**
	 * Helper method to log all declared methods on a class.
	 *
	 * @param objClass
	 *            the class for which to log methods
	 */
	private static void logMethods(Class objClass) {
		if (objClass == null) {
			return;
		}
		StringBuilder sb = new StringBuilder("Methods on " + objClass.getName()
				+ ":\n");
		Method[] methods = objClass.getMethods();
		Arrays.sort(methods, new Comparator<Method>() {
			public int compare(Method m1, Method m2) {
				return m1.getName().compareTo(m2.getName());
			}
		});

		for (Method method : methods) {
			sb.append("  ");
			sb.append(method.getName());
			sb.append("(");
			Class<?>[] params = method.getParameterTypes();
			if (params.length > 0) {
				for (int j = 0; j < params.length; j++) {
					if (j > 0) {
						sb.append(",");
					}
					sb.append(params[j].getName());
				}
			}
			sb.append(")\n");
		}
		logger_.warn(sb.toString());
	}

	/**
	 * Helper method to log all declared constructors on a class.
	 *
	 * @param objClass
	 *            the class for which to log constructors
	 */
	private static void logConstructors(Class objClass) {
		if (objClass == null) {
			return;
		}
		StringBuilder sb = new StringBuilder("Constructors on "
				+ objClass.getName() + ":\n");
		Constructor[] constructors = objClass.getDeclaredConstructors();

		for (Constructor constructor : constructors) {
			sb.append("  ");
			sb.append(constructor);
			sb.append("\n");
		}
		logger_.warn(sb.toString());
	}

	/**
	 * Determines if two objects are equal, taking null into account.
	 *
	 * @param argObject1
	 *            the first object to compare
	 * @param argObject2
	 *            the second object to compare
	 * @return <code>true</code> if both are <code>null</code>, or equal returns
	 *         <code>true</code>; otherwise <code>false</code>
	 */
	public static final boolean equivalent(Object argObject1, Object argObject2) {
		return equivalent(argObject1, argObject2, false);
	}

	/**
	 * Determines if two objects are equal, taking null into account.
	 *
	 * @param argObject1
	 *            the first object to compare
	 * @param argObject2
	 *            the second object to compare
	 * @param argCompareElems
	 *            <code>true</code> if, in the case where both
	 *            <code>argObj1</code> and <code>argObj2</code> are collections,
	 *            all elements in <code>argObj1</code> must be found in
	 *            <code>argObj2</code> for equivalence to be satisfied, and vice
	 *            versa; <code>false</code> if only the collections themselves
	 *            are compared
	 * @return <code>true</code> if both are <code>null</code>, or equal returns
	 *         <code>true</code>; otherwise <code>false</code>
	 */
	public static final boolean equivalent(Object argObject1,
			Object argObject2, boolean argCompareElems) {

		// handles both null
		if (argObject1 == null) {
			return (argObject2 == null);
		}
		boolean equivalent = argObject1.equals(argObject2);

		if ((argCompareElems) && (argObject1 instanceof Collection)
				&& (argObject2 instanceof Collection)) {
			Collection collection1 = (Collection) argObject1;
			Collection collection2 = (Collection) argObject2;

			if (collection1.size() != collection2.size()) {
				equivalent = false;
			} else {
				for (Object elem1 : collection1) {
					if (!collection2.contains(elem1)) {
						equivalent = false;
						break;
					}
				}
			}
		}
		return equivalent;
	}

	/**
	 * Determines if an object of type <code>argClass1</code> can be assigned to
	 * a field of type <code>argClass2</code> using reflection.
	 *
	 * @param argClass1
	 *            the assigned Class
	 * @param argClass2
	 *            the receiving Class
	 * @return true if settable, otherwise false
	 * @throws NullPointerException
	 *             if either <code>argClass1</code> or <code>argClass2</code> is
	 *             null
	 */
	public static final boolean isReflectivelyAssignableFrom(
			Class<?> argClass1, Class<?> argClass2) {

		if (argClass1.isAssignableFrom(argClass2)) {
			return true;
		}
		Class primitive = PRIMITIVE_CLASS_MAP.get(argClass2);
		if (primitive == null) {
			return false;
		}
		return argClass1.equals(primitive);
	}

	/**
	 * Calculates a hash code for <code>o</code>.
	 *
	 * @param o
	 *            the object for which to calculate a hash code
	 * @return 0 if <code>o</code> is <code>null</code>, the results of
	 *         {@link ArrayUtils#calculateHashCode(Object[])} if <code>o</code>
	 *         is an array, or otherwise the results of
	 *         <code>o.hashCode()</code>
	 */
	public static final int getHashCode(Object o) {
		if (o == null) {
			return 0;
		} else if (o instanceof Object[]) {
			return ArrayUtils.calculateHashCode((Object[]) o);
		} else {
			return o.hashCode();
		}
	}

	/**
	 * Gets all classes and interfaces for which
	 * {@link Class#isAssignableFrom(java.lang.Class)} would return
	 * <code>true</code>.
	 *
	 * @param argClass
	 *            class of interest
	 * @return the class, all superclasses, and all implemented interfaces
	 */
	public static final Class[] getClassesAndInterfaces(Class argClass) {
		Set<Class> results = new HashSet<Class>();
		Class currentClass = argClass;
		results.add(currentClass);
		while (!"java.lang.Object".equals(currentClass.getName())) {
			results.addAll(Arrays.asList(currentClass.getInterfaces()));
			currentClass = currentClass.getSuperclass();
			results.add(currentClass);
		}
		return results.toArray(new Class[results.size()]);
	}

	/**
	 * Gets the field on <code>argClass</code> that has the name
	 * <code>argFieldName</code>.
	 *
	 * @param argClass
	 *            the class
	 * @param argFieldName
	 *            the name of the field to get
	 * @return <code>null</code> if there is no field with the indicated name,
	 *         otherwise the found field
	 */
	public static final Field getDeclaredFieldIgnoreCase(Class argClass,
			String argFieldName) {

		Field[] fields = argClass.getDeclaredFields();
		String fieldName = argFieldName.toLowerCase(Locale.US);
		for (Field field : fields) {
			if (field.getName().toLowerCase(Locale.US).equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	/**
	 * Gets the bean-style setter for <code>argFieldName</code>. Searches for
	 * methods declared on the passed in class first, followed by methods
	 * declared on superclasses. Method found will be public and not static.
	 *
	 * @param argClass
	 *            the class on which to locate a setter method
	 * @param argFieldName
	 *            the name of the field to be set
	 * @param argParameterType
	 *            the type of the parameter for the setter
	 * @return the setter Method if found, <code>null</code> otherwise
	 */
	public static final Method getSetterIgnoreCase(Class argClass,
			String argFieldName, Class argParameterType) {

		// end a recursive call -- assumes java.lang.Object has no setters
		if ((argClass == argClass.getSuperclass())
				|| (argClass.getSuperclass() == null)
				// probably don't need this last one, but just in case...
				|| argClass.getName().equals(Object.class.getName())) {
			return null;
		}
		Method[] methods = argClass.getDeclaredMethods();
		String setterName = "set" + argFieldName.toLowerCase(Locale.US);
		for (Method method : methods) {
			// check that there is only one parameter
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length != 1) {
				continue;
			}
			// make sure this method is public
			if (!Modifier.isPublic(method.getModifiers())) {
				continue;
			}
			// make sure this is not a static method
			if (Modifier.isStatic(method.getModifiers())) {
				continue;
			}
			// check if name matches
			if (!method.getName().toLowerCase(Locale.US).equals(setterName)) {
				continue;
			}
			// check if the parameter is the correct type
			if (parameterTypes[0].isAssignableFrom(argParameterType)) {
				return method;
			}
			Class primitiveClass = PRIMITIVE_CLASS_MAP.get(argParameterType);
			if ((primitiveClass != null)
					&& parameterTypes[0].isAssignableFrom(primitiveClass)) {
				return method;
			}
		}
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		// RECURSIVE CALL !!
		// not found on this class, check the superclass
		return getSetterIgnoreCase(argClass.getSuperclass(), argFieldName,
				argParameterType);
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

	/**
	 * Casts or converts <code>argValue</code> to be assignable to
	 * <code>argClass</code>.
	 *
	 * @param argValue
	 *            value to casted
	 * @param argTargetClass
	 *            target class
	 * @return a value that is assignable to variable of type
	 *         <code>argClass</code>
	 * @throws ClassCastException
	 *             if <code>argValue</code> cannot be cast or converted
	 */
	public static final Object cast(Object argValue, Class argTargetClass) {
		// null value can be assigned to any (non-primative) class
		if (argValue == null) {
			return null;
		}

		// see if the current value can already be assigned
		if (argTargetClass.isInstance(argValue)) {
			return argValue;
		}

		// if the target is String, use string conversion
		if (argTargetClass == String.class) {
			// ICode instances need to return the value of getCode(), not
			// toString()
			if (argValue instanceof ICode) {
				return ((ICode) argValue).getCode();
			}
			// Default return statement
			return String.valueOf(argValue);
		}

		// if the target is java.util.Date and value is a String, try to parse
		// as ISO date string
		if (argValue instanceof String) {
			if (equivalentClasses(java.util.Date.class, argTargetClass)) {
				Date d = DateUtils.parseIso((String) argValue);
				if (d != null) {
					return d;
				}
				d = DateUtils.parseDate((String) argValue);
				if (d != null) {
					return d;
				}
			}
		}
		if (argTargetClass.isPrimitive()) {
			Object v = cast(argValue, getNonPrimitiveClass(argTargetClass));
			return unwrapPrimitive(v);
		}

		// see if there is a constructor of the target class that can be passed
		// the value
		try {
			Constructor c = argTargetClass
					.getConstructor(new Class[] { argValue.getClass() });
			return c.newInstance(new Object[] { argValue });
		} catch (Exception ex) {
			ClassCastException cex = new ClassCastException(
					getClassFromObject(argValue) + " [" + argValue
							+ "] is not assignable to a "
							+ getClass(argTargetClass));
			cex.initCause(ex);
			throw cex;
		}
	}

	/**
	 * Uwraps a primitive from a primitive wrapper class.
	 *
	 * @param o
	 *            the value to unwrap
	 * @return the unwrapped value
	 */
	public static final Object unwrapPrimitive(Object o) {
		if (o == null) {
			return null;
		}
		Method m = PRIMITIVE_UNWRAP_METHOD_MAP.get(o.getClass());
		try {
			return m.invoke(o, EMPTY_OBJECT_ARRAY);
		} catch (Throwable ex) {
			throw new ReflectionException(ex);
		}
	}

	/**
	 * Determines if two classes are equivalent, taking <code>null</code> and
	 * different {@link ClassLoader} instances into account.
	 *
	 * @param c1
	 *            the first <code>Class</code> to compare
	 * @param c2
	 *            the second <code>Class</code> to compare
	 * @return <code>true</code> if both are <code>null</code>, or both classes
	 *         have the same name; otherwise <code>false</code>
	 */
	public static final boolean equivalentClasses(Class c1, Class c2) {
		// handles both null
		if (c1 == c2) {
			return true;
		}
		// filters out any other nulls
		if ((c1 == null) || (c2 == null)) {
			return false;
		}
		return c1.getName().equals(c2.getName());
	}

	/**
	 * Gets the serialVersionUID for a serializable class.
	 *
	 * @param argClass
	 *            a class that implements {@link Serializable}.
	 * @return the serialVersionUID
	 */
	public static final long getSerialVersionUID(Class argClass) {
		ObjectStreamClass streamClass = ObjectStreamClass.lookup(argClass);
		return streamClass.getSerialVersionUID();
	}

	/**
	 * Returns the greatest object from the passed in set. <code>null</code> is
	 * considered smaller than anything.
	 *
	 * @param argCandidates
	 *            the object set to process
	 * @param <V>
	 *            type of candidates and result
	 * @return the greatest object, or <code>null</code> if all of the values
	 *         are <code>null</code>
	 */
	public static final <V extends Comparable<V>> V greatest(V... argCandidates) {
		int idx = 0;
		V greatest = argCandidates[idx++];
		// find a non-null one
		while ((greatest == null) && (idx < argCandidates.length)) {
			greatest = argCandidates[idx++];
		}
		// now, look at all the others, always grabbing a reference to the new
		// greatest
		V current;
		while (idx < argCandidates.length) {
			current = argCandidates[idx++];
			if (current == null) {
				continue;
			}
			if (greatest.compareTo(current) < 0) {
				greatest = current;
			}
		}
		return greatest;
	}

	/**
	 * Returns the least object in the passed in set. <code>null</code> is
	 * considered smaller than anything.
	 *
	 * @param argCandidates
	 *            the object set to process
	 * @param <V>
	 *            type of candidates and result
	 * @return the least object, or <code>null</code> if any of the values are
	 *         <code>null</code>
	 */
	public static final <V extends Comparable<V>> V least(V... argCandidates) {
		int idx = 0;
		V least = argCandidates[idx++];
		// see if there is a non-null one
		while ((least == null) && (idx < argCandidates.length)) {
			if (argCandidates[idx++] == null) {
				return null;
			}
		}
		// now, look at all the others, always grabbing a reference to the new
		// greatest
		V current;
		while (idx < argCandidates.length) {
			current = argCandidates[idx++];
			if (current == null) {
				return null;
			}
			if (least.compareTo(current) > 0) {
				least = current;
			}
		}
		return least;
	}

	/**
	 * Returns the least object in the passed in set. <code>null</code> is
	 * considered larger than anything.
	 *
	 * @param argCandidates
	 *            the object set to process
	 * @param <V>
	 *            type of candidates and result
	 * @return the least object, or <code>null</code> if all of the values are
	 *         <code>null</code>
	 */
	public static final <V extends Comparable<V>> V leastNonNull(
			V... argCandidates) {
		int idx = 0;
		V least = argCandidates[idx++];
		// see if there is a non-null one
		while ((least == null) && (idx < argCandidates.length)) {
			if (argCandidates[idx++] == null) {
				least = argCandidates[idx++];
			}
		}
		// now, look at all the others, always grabbing a reference to the new
		// greatest
		V current;
		while (idx < argCandidates.length) {
			current = argCandidates[idx++];
			if (current == null) {
				continue;
			}
			if (least.compareTo(current) > 0) {
				least = current;
			}
		}
		return least;
	}

	/**
	 * Cast <code>argObject</code> as a given Class, or return <code>null</code>
	 * if not an instanceof the specified class.
	 *
	 * @param <T>
	 *            the type to cast to
	 * @param argObject
	 *            the object to check
	 * @param argClass
	 *            the desired class
	 * @return argObject if it is an instanceof argClass, otherwise
	 *         <code>null</code>
	 */
	public static final <T> T castIf(Object argObject, Class<T> argClass) {
		if (argClass.isInstance(argObject)) {
			return argClass.cast(argObject);
		}
		return null;
	}
}
