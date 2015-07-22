package com.hitao.codegen.util;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * Library of functions to load resource bundles and property files. <br>
 * 
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: ResourceUtils.java 12 2011-02-20 10:50:23Z guest $
 */
public final class ResourceUtils {
  private static final Logger logger_ = Logger.getLogger(ResourceUtils.class);

  private static final String[] PROPERTY_FILE_EXTS = new String[] {
			".properties", ".xml" };

  private ResourceUtils() {
    //prevent instantiation... this is a library
  }

  /**
   * Gets a resource bundle.
   * 
   * @param argResourceRootName  the base name of the resource bundle, a fully
   *     qualified class name
   * @param argLocale            the locale for which a resource bundle is desired
   * @return a resource bundle for the given base name and locale
   */
  public static ResourceBundle getResourceBundle(
      String argResourceRootName, Locale argLocale) {

    return getResourceBundle(argResourceRootName, argLocale, logger_);
  }

  /**
   * Gets a resource bundle.
   * 
   * @param argResourceRootName  the base name of the resource bundle, a fully
   *     qualified class name
   * @param argLocale            the locale for which a resource bundle is desired
   * @param argLogger            the logger to use to record any errors that occur
   * @return a resource bundle for the given base name and locale
   */
  public static ResourceBundle getResourceBundle(
      String argResourceRootName, Locale argLocale, Logger argLogger) {

    try {
      return ResourceBundle.getBundle(argResourceRootName, argLocale);
    }
    catch (MissingResourceException ex) {
      error(argLogger, "No resource bundle found for base resource name ["
                       + argResourceRootName + "]!", ex);
      throw new ResourceException(ex);
    }
    catch (NullPointerException ex) {
      error(argLogger, "Resource bundle root name [" + argResourceRootName
                       + "] and/or locale is null!", ex);
      throw new ResourceException(ex);
    }
  }

  /**
   * Gets an object for the given key from this resource bundle or one of its
   * parents.
   * 
   * @param argResourceBundle  the bundle from which to retrieve the object
   * @param argKey             the key for the desired object
   * @throws ResourceException if <code>argKey</code> is <code>null</code>
   *     or if no object for the given key can be found
   * @return the object for the given key
   */
  public static Object getResourceObject(
      ResourceBundle argResourceBundle, String argKey) {

    return getResourceObject(argResourceBundle, argKey, logger_);
  }

  /**
   * Gets an object for the given key from this resource bundle or one of its
   * parents.
   * 
   * @param argResourceBundle  the bundle from which to retrieve the object
   * @param argKey             the key for the desired object
   * @param argLogger          the logger to use to record any errors that occur
   * @throws ResourceException if <code>argKey</code> is <code>null</code>
   *     or if no object for the given key can be found
   * @return the object for the given key
   */
  public static Object getResourceObject(
      ResourceBundle argResourceBundle, String argKey, Logger argLogger) {

    if (!isResourceBundleValid(argResourceBundle, argLogger)) {
      return null;
    }

    try {
      return argResourceBundle.getObject(argKey);
    }
    catch (NullPointerException ex) {
      error(argLogger, "Cannot retrieve resource mapped to a null key!", ex);
      throw new ResourceException(ex);
    }
    catch (MissingResourceException ex) {
      warn(argLogger, "No resource mapped to key [" + argKey + "]!", null);
      throw new ResourceException(ex);
    }
  }

  /**
   * Gets a string for the given key from this resource bundle or one of its parents.
   * Calling this method is equivalent to calling
   * <blockquote>
   * <code>(String) {@link #getResourceObject(ResourceBundle, String)}(argResourceBundle, argKey)</code>.
   * </blockquote>
   *
   * @param argResourceBundle  the bundle from which to retrieve the string
   * @param argKey             the key for the desired string
   * @throws ResourceException if <code>argKey</code> is <code>null</code>
   *     or if no object for the given key can be found
   *     or if the object found for the given key is not a string
   * @return the string for the given key
   */
  public static String getResourceString(
      ResourceBundle argResourceBundle, String argKey) {

    return getResourceString(argResourceBundle, argKey, logger_);
  }

  /**
   * Gets a string for the given key from this resource bundle or one of its parents.
   * Calling this method is equivalent to calling
   * <blockquote>
   * <code>(String) {@link #getResourceObject(ResourceBundle, String, Logger)}(argResourceBundle, argKey, argLogger)</code>.
   * </blockquote>
   *
   * @param argResourceBundle  the bundle from which to retrieve the string
   * @param argKey             the key for the desired string
   * @param argLogger          the logger to use to record any errors that occur
   * @throws ResourceException if <code>argKey</code> is <code>null</code>
   *     or if no object for the given key can be found
   *     or if the object found for the given key is not a string
   * @return the string for the given key
   */
  public static String getResourceString(
      ResourceBundle argResourceBundle, String argKey, Logger argLogger) {

    if (!isResourceBundleValid(argResourceBundle, argLogger)) {
      return null;
    }

    try {
      return argResourceBundle.getString(argKey);
    }
    catch (NullPointerException ex) {
      error(argLogger, "Cannot retrieve resource mapped to a null key!", ex);
      throw new ResourceException(ex);
    }
    catch (MissingResourceException ex) {
      warn(argLogger, "No resource mapped to key [" + argKey + "]!", ex);
      throw new ResourceException(ex);
    }
    catch (ClassCastException ex) {
      error(argLogger, "Resource mapped to key [" + argKey
                       + "] is not a String!", ex);
      throw new ResourceException(ex);
    }
  }

  /**
   * Gets a string array for the given key from this resource bundle or one of its parents.
   * Calling this method is equivalent to calling
   * <blockquote>
   * <code>(String[]){@link #getResourceObject(ResourceBundle, String, Logger)}(argResourceBundle, argKey, argLogger)</code>.
   * </blockquote>
   *
   * @param argResourceBundle  the bundle from which to retrieve the string
   * @param argKey             the key for the desired string array
   * @throws ResourceException if <code>key</code> is <code>null</code>
   *     or if no object for the given key can be found
   *     or if the object found for the given key is not a string array
   * @return the string array for the given key
   */
  public static String[] getResourceStringArray(
      ResourceBundle argResourceBundle, String argKey) {

    return getResourceStringArray(argResourceBundle, argKey, logger_);
  }

  /**
   * Gets a string array for the given key from this resource bundle or one of its parents.
   * Calling this method is equivalent to calling
   * <blockquote>
   * <code>(String[]){@link #getResourceObject(ResourceBundle, String, Logger)}(argResourceBundle, argKey, argLogger)</code>.
   * </blockquote>
   *
   * @param argResourceBundle  the bundle from which to retrieve the string
   * @param argKey             the key for the desired string array
   * @param argLogger          the logger to use to record any errors that occur
   * @throws ResourceException if <code>key</code> is <code>null</code>
   *     or if no object for the given key can be found
   *     or if the object found for the given key is not a string array
   * @return the string array for the given key
   */
  public static String[] getResourceStringArray(
      ResourceBundle argResourceBundle, String argKey, Logger argLogger) {

    if (!isResourceBundleValid(argResourceBundle, argLogger)) {
      return null;
    }

    try {
      return argResourceBundle.getStringArray(argKey);
    }
    catch (NullPointerException ex) {
      error(argLogger, "Cannot retrieve resource mapped to a null key!", ex);
      throw new ResourceException(ex);
    }
    catch (MissingResourceException ex) {
      warn(argLogger, "No resource mapped to key [" + argKey + "]!", ex);
      throw new ResourceException(ex);
    }
    catch (ClassCastException ex) {
      error(argLogger, "Resource mapped to key [" + argKey
                       + "] is not a String array!", ex);
      throw new ResourceException(ex);
    }
  }

  private static boolean isResourceBundleValid(
      ResourceBundle argResourceBundle, Logger argLogger) {

    if (argResourceBundle == null) {
      error(
          argLogger, "Cannot retrieve resource from null bundle!",
          new Throwable("STACK TRACE"));
      return false;
    }
    return true;
  }

  /**
   * Loads a properties file specified with <code>argPropertyFileName</code>.
   * 
   * @param argPropertyFileName         file to load
   * @return loaded properties
   */
  public static Properties getProperties(String argPropertyFileName) {
    return getProperties(argPropertyFileName, logger_);
  }

  /**
   * Loads a properties file specified with <code>argPropertyFileName</code>.
   * 
   * @param argPropertyFileName         file to load
   * @param argLogger                   logger to use
   * @return loaded properties
   */
  public static Properties getProperties(
      String argPropertyFileName, Logger argLogger) {

    Properties defaultProperties = null;
    return getProperties(argPropertyFileName, defaultProperties, argLogger);
  }

  /**
   * Loads a properties file specified with <code>argPropertyFileName</code> on
   * top of properties loaded from <code>argDefaultPropertyFileName</code>.
   * 
   * @param argPropertyFileName         file to load
   * @param argDefaultPropertyFileName  name of file with default properties to load first
   * @return loaded properties
   */
  public static Properties getProperties(
      String argPropertyFileName, String argDefaultPropertyFileName) {

    return getProperties(
        argPropertyFileName, argDefaultPropertyFileName, logger_);
  }

  /**
   * Loads a properties file specified with <code>argPropertyFileName</code> on
   * top of properties loaded from <code>argDefaultPropertyFileName</code>.
   * 
   * @param argPropertyFileName         file to load
   * @param argDefaultPropertyFileName  name of file with default properties to load first
   * @param argLogger                   logger to use
   * @return loaded properties
   */
  public static Properties getProperties(
      String argPropertyFileName, String argDefaultPropertyFileName,
      Logger argLogger) {

    return getProperties(
        argPropertyFileName, getProperties(argDefaultPropertyFileName),
        argLogger);
  }

  /**
   * Loads a properties file specified with <code>argPropertyFileName</code> into
   * <code>argDefaultProperties</code>.
   * 
   * @param argPropertyFileName   file to load
   * @param argDefaultProperties  properties to load into
   * @return loaded properties
   */
  public static Properties getProperties(
      String argPropertyFileName, Properties argDefaultProperties) {

    return getProperties(argPropertyFileName, argDefaultProperties, logger_);
  }

  /**
   * Loads a properties file specified with <code>argPropertyFileName</code> into
   * <code>argDefaultProperties</code>.
   * 
   * @param argPropertyFileName   file to load
   * @param argDefaultProperties  properties to load into
   * @param argLogger             logger to use
   * @return loaded properties
   */
  public static Properties getProperties(
      String argPropertyFileName, Properties argDefaultProperties,
      Logger argLogger) {

    Properties properties = new Properties(argDefaultProperties);

    URL[] locations = ClassPathUtils.getConfigUrlList(
        argPropertyFileName, PROPERTY_FILE_EXTS);
    if (locations.length == 0) {
      warn(
          argLogger,
          "'"
                    + argPropertyFileName
                    + "' not found on classpath...trying failover to file system",
          null);
      File pf = new File(argPropertyFileName + ".properties");
      if (pf.exists()) {
        try {
          locations = new URL[] {
            pf.toURI().toURL() };
        }
        catch (MalformedURLException ex) {
          error(argLogger, "CAUGHT EXCEPTION", ex);
        }
      }
    }
    if (locations.length == 0) {
      error(argLogger, "'" + argPropertyFileName
                       + "' not found on classpath or file system", null);
    }
    for (URL loc : locations) {
      try {
        InputStream stream = loc.openStream();
        info(argLogger, "loading " + loc, null);
        properties.load(stream);
      }
      catch (Exception ex) {
        String msg = loc + " property file could not be loaded.";
        warn(argLogger, msg, ex);
        throw new ResourceException(msg, ex);
      }
    }
    return properties;
  }

  /**
   * Safely get a propery from a Properties object that may be <code>null</code>,
   * without throwing a <code>NullPinterException</code>.
   * 
   * @param argProperties    properties object possibly containing the value
   * @param argKey           the value to get
   * @return the value
   */
  public static String getProperty(Properties argProperties, String argKey) {
    return getProperty(argProperties, argKey, logger_);
  }

  /**
   * Safely get a propery from a Properties object that may be <code>null</code>,
   * without throwing a <code>NullPinterException</code>.
   * 
   * @param argProperties    properties object possibly containing the value
   * @param argKey           the value to get
   * @param argLogger        the logger to use
   * @return the value
   */
  public static String getProperty(
      Properties argProperties, String argKey, Logger argLogger) {

    if (!isPropertiesValid(argProperties, argLogger)) {
      return null;
    }
    return argProperties.getProperty(argKey);
  }

  /**
   * Safely get a propery from a Properties object that may be <code>null</code>,
   * without throwing a <code>NullPinterException</code>.
   * 
   * @param argProperties    properties object possibly containing the value
   * @param argKey           the value to get
   * @param argDefaultValue  the value to return if the value is not
   *     in <code>argProperties</code>
   * @return the value
   */
  public static String getProperty(
      Properties argProperties, String argKey, String argDefaultValue) {

    return getProperty(argProperties, argKey, argDefaultValue, logger_);
  }

  /**
   * Safely get a propery from a Properties object that may be <code>null</code>,
   * without throwing a <code>NullPinterException</code>.
   * 
   * @param argProperties    properties object possibly containing the value
   * @param argKey           the value to get
   * @param argDefaultValue  the value to return if the value is not
   *     in <code>argProperties</code>
   * @param argLogger        the logger to use
   * @return the value
   */
  public static String getProperty(
      Properties argProperties, String argKey, String argDefaultValue,
      Logger argLogger) {

    if (!isPropertiesValid(argProperties, argLogger)) {
      // FIXME RDB - shouldn't this return argDefault???
      return null;
    }
    return argProperties.getProperty(argKey, argDefaultValue);
  }

  /**
   * Saves a properties object to a file.
   * 
   * @param argProperties        the properties to save
   * @param argPropertyFileName  the name of the desitination file
   */
  public static void saveProperties(
      Properties argProperties, String argPropertyFileName) {

    saveProperties(argProperties, argPropertyFileName, logger_);
  }

  /**
   * Saves a properties object to a file.
   * 
   * @param argProperties        the properties to save
   * @param argPropertyFileName  the name of the desitination file
   * @param argLogger            the logger to use recording any problems
   */
  public static void saveProperties(
      Properties argProperties, String argPropertyFileName, Logger argLogger) {

    try {
      OutputStream out = FileUtils.getOutputStream(argPropertyFileName, false);

      argProperties.store(out, "");
      out.close();
    }
    catch (Exception ex) {
      warn(argLogger, argPropertyFileName
                      + " property file could not be saved to.", ex);
      throw new ResourceException(ex);
    }
  }

  /**
   * Converts a {@link Properties} to an array of {@link KeyValuePair}s.
   * 
   * @param argProperties  the properties to convert
   * @return an array of corresponding key value pairs
   */
  @SuppressWarnings("unchecked")
  public static KeyValuePair<String, String>[] convertPropertiesToPairs(
      Properties argProperties) {

    Enumeration<String> keys = (Enumeration<String>)argProperties.propertyNames();
    List<KeyValuePair<String, String>> params = new ArrayList<KeyValuePair<String, String>>();

    // Create an array of KeyValuePairs from the Properties object.
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      params.add(new KeyValuePair<String, String>(key, argProperties.getProperty(key)));
    }
    return params.toArray(new KeyValuePair[params.size()]);
  }

  /**
   * Changes all keys for the entries in <code>argProperties</code> to upper case.
   * 
   * @param argProperties  the properties to modify
   * @return  the modified properties
   */
  public static final Properties keysToUpper(Properties argProperties) {
    Properties results = new Properties();
    for (Map.Entry<Object, Object> entry : argProperties.entrySet()) {
      results.put(entry.getKey().toString().toUpperCase(), entry.getValue());
    }

    return results;
  }

  private static boolean isPropertiesValid(
      Properties argProperties, Logger argLogger) {

    if (argProperties == null) {
      error(
          argLogger, "Cannot retrieve property from null properties!",
          new Throwable("STACK TRACE"));
      return false;
    }
    return true;
  }

  private static final void warn(
      Logger argLogger, String argMessage, Throwable t) {

    if (argLogger != null) {
      try {
        if (t != null) {
          argLogger.warn(argMessage, t);
        }
        else {
          argLogger.warn(argMessage);
        }
      }
      catch (Exception ex) {
        if (t != null) {
          t.printStackTrace(System.err);
        }
        System.out.println("PROBLEM WITH LOGGER [" + argLogger + "]");
        ex.printStackTrace(System.err);
      }
    }
  }

  private static final void error(
      Logger argLogger, String argMessage, Throwable t) {

    if (argLogger != null) {
      try {
        argLogger.error(argMessage, t);
      }
      catch (Exception ex) {
        if (t != null) {
          t.printStackTrace(System.err);
        }
        System.out.println("PROBLEM WITH LOGGER [" + argLogger + "]");
        ex.printStackTrace(System.err);
      }
    }
  }

  private static final void info(
      Logger argLogger, Object argMessage, Throwable t) {

    if (argLogger != null) {
      try {
        argLogger.info(argMessage, t);
      }
      catch (Exception ex) {
        if (t != null) {
          t.printStackTrace(System.err);
        }
        System.out.println("PROBLEM WITH LOGGER [" + argLogger + "]");
        ex.printStackTrace(System.err);
      }
    }
  }
}
