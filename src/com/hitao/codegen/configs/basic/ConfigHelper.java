package com.hitao.codegen.configs.basic;

import static com.hitao.codegen.constent.StringConstants.CODEGEN_CONFIGURATION_FILE_SUFFIX;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hitao.codegen.util.ClassPathUtils;
import com.hitao.codegen.util.FileUtils;
import com.hitao.codegen.util.IParent;
import com.hitao.codegen.util.ResourceUtils;
import com.hitao.codegen.util.StringUtils;

/**
 * The configuration helper provides functionality to parse an xml file and
 * create a useable hierarchy of Java objects representing the configuration
 * data. The configuration helper does this mainly through the creation of
 * specific objects that map to tags in the xml file. The loaded configuration
 * data is added to a configuration table, which contains all objects loaded.
 * Consider the following example xml file <br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @param <R>
 *            root config type
 * @version $Id: ConfigHelper.java 12 2011-02-20 10:50:23Z guest $          
 */
@SuppressWarnings("rawtypes")
public abstract class ConfigHelper<R extends IConfigObject>{
	static final Logger logger_ = Logger.getLogger(ConfigHelper.class);

	private static final String DEFAULT_SUFFIX;

	private static final String[] EXTENSIONS = new String[] { ".xml" };

	protected R rootConfig_;
	protected IConfigLoader configLoader_;

	protected List<IParent> parentConfigs_;
	protected Map<Class, IParent> parentHash_ = new HashMap<Class, IParent>();

	static {
		String suffix = System.getProperty(CODEGEN_CONFIGURATION_FILE_SUFFIX);
		if (StringUtils.isEmpty(suffix)) {
			DEFAULT_SUFFIX = ".codegen.dao.xml";
		} else {
			DEFAULT_SUFFIX = suffix;
		}
	}

	/**
	 * Gets the config object that is the root of config file.
	 *
	 * @return the root config object
	 */
	public R getRootConfig() {
		return rootConfig_;
	}

	protected URL currentFile_ = null;

	/**
	 * Return the root object associated with the specified class. This is not
	 * the root of the xml tree, but one of its children that match the
	 * specified class type.
	 *
	 * @param argClass
	 *            the class to find the root object for
	 * @return the root object associated with the specified class.
	 */
	protected IParent getRootParent(Class<? extends IParent> argClass) {
		return parentHash_.get(argClass);
	}

	/**
	 * Return all of the children of the root object associated with the
	 * specified class. This is not the children of the root of the xml tree,
	 * but the children of one of its children that match the specified class
	 * type.
	 *
	 * @param <T>
	 *            type of the children
	 * @param argClass
	 *            the class to find the root object for
	 * @return a collection of children that are associated with the root
	 *         object.
	 */
	@SuppressWarnings("unchecked")
	protected <T extends IConfigObject> Collection<T> getRootChildren(
			Class<? extends IParent> argClass) {

		IParent config = getRootParent(argClass);
		if (config == null) {
			IParent EMPTY_ =  new IParent(){
				private List<IConfigObject> configList_ = new ArrayList<IConfigObject>();
				@Override
				public List<IConfigObject> getChildren() {
					return configList_;
				}
			};
			parentHash_.put(argClass, EMPTY_);
			config = EMPTY_;
		}
		return config.getChildren();
	}

	/**
	 * Add the loaded root configurations into a hashtable.
	 */
	protected void hashRootConfigs() {
		for (IParent co : parentConfigs_) {
			parentHash_.put(co.getClass(), co);
		}
	}

	protected IConfigLoader createConfigLoader() {
		try {
			return (IConfigLoader) Class.forName(
					System.getProperty(IConfigLoader.class.getName() + "Impl"))
					.newInstance();
		} catch (Throwable ex) {
			return new SaxConfigLoader();
		}
	}

	/**
	 * This method will call loadDocument method to parse the config XML file
	 * into a HashMap.
	 */
	public void initialize() {
		try {
			parentConfigs_ = new ArrayList<IParent>();
			configLoader_ = createConfigLoader();

			URL[] files = null;
			if (isDirectoryBased()) {
				files = FileUtils.getUrls(new File(getConfigFileName()),
						DEFAULT_SUFFIX);
			} else {
				files = ClassPathUtils.getConfigUrlList(getConfigFileName(),
						EXTENSIONS);
			}

			if ((files == null) || (files.length == 0)) {
				logger_.warn("No candidate config files were "
						+ "found for config file name: " + getConfigFileName());
				return;
			}

			for (URL file : files) {
				currentFile_ = file;
				if (logger_.isInfoEnabled()) {
					logger_.info("loading " + currentFile_);
				}
				InputStream is = currentFile_.openStream();
				if (is == null) {
					logger_.error("config file not found:" + currentFile_);
				} else {
					loadDocument(is);
					is.close();
				}
			}
			hashRootConfigs();
		} catch (Exception ex) {
			logger_.warn("Exception initializing config helper with file "
					+ currentFile_ + " for config: " + getConfigFileName(), ex);
			throw new ConfigException("initialize the file failed: "
					+ currentFile_);
		}
	}

	protected boolean isDirectoryBased() {
		return false;
	}

	/**
	 * Return the name of the configuration file to use.
	 *
	 * @return the name of the configuration file to use.
	 */
	protected abstract String getConfigFileName();

	/**
	 * loadDocument method takes an InputStream parameter as the config XML
	 * document. This method will call parseElement method to parse the
	 * document.
	 *
	 * @param argInputStream
	 *            The configuration XML document
	 * @throws IOException
	 *             if the stream cannot be read
	 */
	protected void loadDocument(InputStream argInputStream) throws IOException {

		configLoader_.loadDocument(callback_, currentFile_, argInputStream);
	}

	private final IConfigLoaderCallback callback_ = new IConfigLoaderCallback() {
		@SuppressWarnings("unchecked")
		public IConfigObject getConfigObject(String argTagName,
				String argDtype, IConfigObject argParent,
				String argSourceDescription) {

			IConfigObject configObject = ConfigHelper.this.getConfigObject(
					argTagName, argDtype, argParent, argSourceDescription);
			if (argParent == null) {
				rootConfig_ = (R) configObject;
			}
			return configObject;
		}
	};

	/**
	 * This method can be overriden to change which config objects get reused in
	 * a particular config helper.
	 *
	 * @param argTagName
	 *            the name of the xml tag currently being parsed
	 * @param argDtype
	 *            the dtype of the xml tag currently being parsed
	 * @param argParent
	 *            the parent of current element
	 * @param argSourceDescription
	 *            the location at which the config is used
	 * @return the config object to use for the element being parsed
	 */
	protected IConfigObject getConfigObject(String argTagName, String argDtype,
			IConfigObject argParent, String argSourceDescription) {

		// reuse the root config unless replaceEntireFile() returns true
		if ((argParent == null) && !replaceEntireFile()
				&& (rootConfig_ != null)) {
			return rootConfig_;
		}

		// not reusing, so let's make one
		IConfigObject configObject = getConfigObject(argTagName, argDtype,
				argSourceDescription);
		if (configObject instanceof IParent) {
			parentConfigs_.add((IParent) configObject);
		}
		return configObject;
	}

	protected boolean replaceEntireFile() {
		return false;
	}

	/**
	 * Return the object to use as a configuration object. Overriding this
	 * method is the primary goal of ConfigHelper subclasses. By doing so,
	 * subclasses can control what objects are created for specific tags. The
	 * default implementation of this method is to ensure that a non-null object
	 * is always created by this method. Subclasses are required as well to
	 * return an object. If the subclass can not create an object for the passed
	 * xml element, it should delegate the construction to this method.
	 *
	 * @param argTagName
	 *            the name of the xml tag currently being parsed
	 * @param argDtype
	 *            the dtype of the xml tag currently being parsed
	 * @param argSourceDescription
	 *            the location at which the config is used
	 * @return the object to use as a configuration object.
	 */
	protected IConfigObject getConfigObject(String argTagName, String argDtype,
			String argSourceDescription) {

		// Find the config type
		if (argDtype == null) {
			return new StringConfig();
		}
		ConfigType type = createForName(argDtype, argSourceDescription);
		Class<?> c = type.getConfigObjectClass();
		// Since there is no key/value or key-only constructor for the config
		// object class, try to create one using a nullary constructor.
		try {
			return (IConfigObject) c.newInstance();
		} catch (Exception e) {
			logger_.error("Error creating config object for type [" + type
					+ "]!", e);
			return new StringConfig();
		}
	}

	static final class ConfigType {

		private static final String PROP_FILE_NAME = "ConfigType";

		private Class<?> classInstance_ = null;

		/** storage for the name of the value */
		private final String name_;

		/** storage for the class to instantiate for a given data type */
		private final String configObjectClass_;

		/**
		 * Constructor
		 *
		 * @param argName
		 *            name of the object
		 * @param argSourceDescription
		 *            the location at which the config is used
		 */
		ConfigType(String argName, String argSourceDescription) {
			name_ = argName.trim().toUpperCase();
			configObjectClass_ = lookupClassName(argName, argSourceDescription);
			if (values_ == null) {
				values_ = new HashMap<String, ConfigType>();
			}
			values_.put(name_, this);
		}

		/**
		 * Using the properties file for ConfigType this method will return the
		 * class to be used for a specific config type.
		 *
		 * @param argConfigType
		 *            the config type to retrieve
		 * @param argSourceDescription
		 *            the location at which the config is used
		 * @return name of class used for config type
		 */
		private static String lookupClassName(String argConfigType,
				String argSourceDescription) {
			if (!propsLoaded_) {
				loadProperties();
			}
			String className = classMap_.getProperty(argConfigType
					.toUpperCase());
			if (className == null) {
				logger_.error("ConfigType '" + argConfigType
						+ "' does not exist in '" + PROP_FILE_NAME + "'::"
						+ argSourceDescription, new Throwable("STACK TRACE"));
			}
			return className;
		}

		private static synchronized void loadProperties() {
			if (!propsLoaded_) {
				try {
					classMap_ = ResourceUtils.keysToUpper(ResourceUtils
							.getProperties(PROP_FILE_NAME, logger_));
					propsLoaded_ = true;
				} catch (Exception e) {
					logger_.error("Error loading '" + PROP_FILE_NAME
							+ "' to initialize" + " ConfigTypes", e);
				}
			}
		}

		/**
		 * returns a description of the value.
		 *
		 * @return a description of the value.
		 */
		@Override
		public String toString() {
			return name_;
		}

		/**
		 * the data-type Class for the config type.
		 *
		 * @return the data-type Class for the config type
		 */
		Class<?> getConfigObjectClass() {

			if (classInstance_ == null) {
				try {
					classInstance_ = this.getClass().getClassLoader()
							.loadClass(configObjectClass_);
				} catch (Exception ex) {
					String msg = "The specified configuration class could not be loaded: "
							+ configObjectClass_;
					logger_.error(msg, ex);
					throw new ConfigException(msg, ex);
				}
			}
			return classInstance_;
		}
	}

	static volatile Map<String, ConfigType> values_;

	static boolean propsLoaded_ = false;

	static java.util.Properties classMap_ = new java.util.Properties();

	private ConfigType createForName(String argName, String argSourceDescription) {
		if (argName == null) {
			return null;
		}
		if (values_ == null) {
			values_ = new HashMap<String, ConfigType>();
		}
		ConfigType found = values_.get(argName.trim().toUpperCase());
		if (found == null) {
			found = new ConfigType(argName, argSourceDescription);
		}
		return found;
	}

}
