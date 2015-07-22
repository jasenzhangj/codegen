package com.hitao.codegen.util;

import static com.hitao.codegen.util.StringUtils.isEmpty;
import static com.hitao.codegen.util.StringUtils.nonNull;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

/**
 * Library of functions for working with URLs.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10f
 * @version $Id: ClassPathUtils.java 12 2011-02-20 10:50:23Z guest $
 */
public final class ClassPathUtils {
private static final Logger logger_ = Logger.getLogger(ClassPathUtils.class);
  private static final boolean isDebugEnabled_ = logger_.isDebugEnabled();

  /** resource location of base configurations */
  public static final String BASE_CONFIG_LOCATION = "configs/";

  /** the configuration in all the configuration paths will be loaded. */
  public static final String CODEGEN_CONFIG_PATH = "codegen.config.path";

  // The string to use to separate strings representing the xml tag hierarchy.
  private static List<String> configOverridePaths_;

  /**
   * Should only be called from code that is changing the System property "dtv.config.path".<br>
   * (Currently this is only from JUnit test cases.)
   */
  public static void reinit() {
    configOverridePaths_ = null;
  }

  /**
   * Returns the override paths configured for the current runtime.
   * @return the override paths configured for the current runtime
   */
  public static List<String> getConfigOverridePaths() {
    if (configOverridePaths_ == null) {
      List<String> results = new ArrayList<String>();
      String setting = System.getProperty(CODEGEN_CONFIG_PATH);

      if ((setting == null) || (setting.length() == 0)) {
        // do nothing
      }
      else if (setting.startsWith(":")) {
        for (String path : setting.substring(1).split(":")) {
          if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
          }
          results.add(path);
        }
      }
      else {
        if (!setting.endsWith("/")) {
          setting += "/";
        }
        StringBuilder sb = new StringBuilder(setting.length() + 1);
        for (String s : setting.split("/")) {
          sb.append(s).append("/");
          results.add(sb.toString().substring(0, sb.length() - 1));
        }
      }
      configOverridePaths_ = results;
    }
    return configOverridePaths_;
  }

  /**
   * Gets an locations of a given configuration resource, including base config and customer
   * overrides.
   *
   * @param argBaseFileName the base name of the resource to locate
   * @param argExtensions file name extensions to locate
   * @return an array of existing locations for the resource in the order they should be processed
   */
  public static final URL[] getConfigUrlList(String argBaseFileName, String[] argExtensions) {
    // make sure there is no extension (legacy support)
    String fileName = argBaseFileName;
    String fn = argBaseFileName.toLowerCase();

    for (String ext : argExtensions) {
      if (fn.endsWith(ext)) {
        logger_.warn(
            "getConfigFileName() should no longer include the extension!! [" + argBaseFileName + "]",
            new Throwable("STACK TRACE"));
        // remove the extension
        fileName = fileName.substring(0, fileName.length() - ext.length());
        // do not look for any more extensions, we found one
        break;
      }
    }
    int foundDirSpec = fileName.lastIndexOf("/");
    if (foundDirSpec > -1) {
      logger_.warn("the file name (" + argBaseFileName + ") should not include a directory");
      fileName = fileName.substring(foundDirSpec + 1);
    }

    // find instances of this config file, starting with the base location
    List<URL> locations = new ArrayList<URL>();
    addLocation(BASE_CONFIG_LOCATION + fileName, locations, argExtensions);

    // find overrides to this config file
    List<String> overridePaths = getConfigOverridePaths();
    for (String path : overridePaths) {
      addLocation(path + "/" + fileName, locations, argExtensions);
    }
    // return all locations that exist
    return locations.toArray(new URL[locations.size()]);
  }

  private static void addLocation(String argBaseName, List<URL> argUrlList, String[] argExtensions) {
    for (String ext : argExtensions) {
      boolean added = addIfExists(argBaseName + ext, argUrlList);
      if (added) {
        return;
      }
    }
  }

  private static boolean addIfExists(String argResourceName, List<URL> argUrlList) {
    try {
      URL url = getResource(argResourceName);
      if (url == null) {
        if (isDebugEnabled_) {
          logger_.debug("no file at " + argResourceName);
        }
        return false;
      }
      argUrlList.add(url);
      if (isDebugEnabled_) {
        logger_.debug("adding config file " + argResourceName);
      }
      return true;
    }
    catch (Exception ex) {
      logger_.error("CAUGHT EXCEPTION", ex);
      return false;
    }
  }

  /**
   * Gets an locations of a given directory-based configuration resource, including base config and
   * customer overrides.
   *
   * @param argDirName the base name of the directory to locate
   * @param argFilter filter to test file names against
   * @return an array of existing locations for the resource in the order they should be processed
   */
  public static final URL[] getDirectoryBasedConfigFileList(String argDirName, INameFilter argFilter) {
    final boolean DISTINCT_CONFIGS = true;

    // get name of location that will contain these configs
    List<URL> locations = new ArrayList<URL>();
    // add base config files
    addPackageContents(BASE_CONFIG_LOCATION + argDirName, locations, argFilter, DISTINCT_CONFIGS);

    // find overrides to those config files
    for (String path : getConfigOverridePaths()) {
      addPackageContents(path + "/" + argDirName, locations, argFilter, DISTINCT_CONFIGS);
    }
    // return all locations that exist
    return locations.toArray(new URL[locations.size()]);
  }

  /**
   * Gets all locations for a given package.
   *
   * @param argPackageName the name of the package to locate
   * @return all locations in the classpath
   */
  public static final URL[] getPackageLocations(String argPackageName) {
      String locationName = cleanPackageName(argPackageName);
      List<URL> resources = getResources(locationName);
      return resources.toArray(new URL[resources.size()]);
  }

  /**
   * Gets all the contents of given package.
   *
   * @param argPackageName the package to locate
   * @return contents of the package
   */
  public static final URL[] getPackageContents(String argPackageName) {
    return getPackageContents(null, argPackageName);
  }

  /**
   * Gets all the contents of given package.
   *
   * @param argFilter name filter against which to match contents before adding to list to be
   * returned
   * @param argPackageName the package to locate
   * @return contents of the package
   */
  public static final URL[] getPackageContents(INameFilter argFilter, String argPackageName) {
    return getPackageContents(argFilter, argPackageName, false);
  }

  /**
   * Gets all the contents of given package.
   *
   * @param argFilter name filter against which to match contents before adding to list to be
   * returned
   * @param argPackageName the package to locate
   * @param argDistinctResources - if true, only the first resource with a given name found on the
   * classpath will be included.
   *
   * E.g. if given: argPackageName = "/version1/query", and
   * "patch.jar!/version1/query/QueryConfig.xml" AND "cst-pos.jar!/version1/query/QueryConfig.xml"
   * are available on the classpath, with argDistinctResources = true, only
   * "patch.jar!/version1/query/QueryConfig.xml" will be returned.
   *
   * @return contents of the package
   */
  public static final URL[] getPackageContents(INameFilter argFilter, String argPackageName,
      boolean argDistinctResources) {

    List<URL> results = new ArrayList<URL>();
    for (URL loc : getPackageLocations(argPackageName)) {
      if ("file".equals(loc.getProtocol())) {
        List<URL> l = getFilePackageContents(argFilter, loc);
        results.addAll(l);
      }
      else {
        try {
          List<URL> l = getJarPackageContents(argFilter, loc);
          results.addAll(l);
        }
        catch (Exception ee) {
          logger_.error("Error occurred while processing URL as jar: " + loc + " for package name: "
              + argPackageName + " and filter: " + argFilter, ee);
        }
      }
    }

    if (argDistinctResources && !results.isEmpty()) {
      List<URL> distinctResources = new ArrayList<URL>(results.size());
      List<String> includedResources = new ArrayList<String>(results.size());

      for (URL resource : results) {
        String resourceName = resource.getFile().substring(resource.getFile().indexOf(argPackageName));
        if (!includedResources.contains(resourceName)) {
          distinctResources.add(resource);
          includedResources.add(resourceName);
        }
      }

      results = distinctResources;
    }

    return results.toArray(new URL[results.size()]);
  }

  private static final List<URL> getFilePackageContents(INameFilter argFilter, URL argPackageLocation) {
    if (argPackageLocation == null) {
      throw new NullPointerException("getFilePackageContents cannot accept null argPackageLocation");
    }

    List<URL> urlList = new ArrayList<URL>();
    String fileName = argPackageLocation.getFile();
    String path = argPackageLocation.getPath();

    if (logger_.isDebugEnabled()) {
      logger_.debug("Loading package contents from path: " + path + " fileName: " + fileName);
    }

    File rootFile = FileUtils.getFileForURL(argPackageLocation);

    if (rootFile == null) {
      logger_.error("rootFile is null for package location: " + argPackageLocation + " with file: "
          + argPackageLocation.getFile());
    }

    if (!rootFile.isDirectory()) {
      logger_.error("rootFile did not resolve to a directory " + "for package location: "
          + argPackageLocation + " with file: " + rootFile);
    }

    for (File f : rootFile.listFiles()) {
      if (f.isFile()) {
        if ((argFilter == null) || argFilter.accept(f.getPath())) {
          try {
            urlList.add(f.toURI().toURL());
          }
          catch (MalformedURLException ex) {
            logger_.error("CAUGHT EXCEPTION", ex);
          }
        }
      }
    }
    return urlList;
  }

  private static final List<URL> getJarPackageContents(INameFilter argFilter, URL argPackageLocation) {
    List<URL> urlList = new ArrayList<URL>();

    try {
      String file = java.net.URLDecoder.decode(argPackageLocation.getFile(), "UTF-8");
      int jarEnd = file.lastIndexOf('!');
      String jarFileName = file.substring(0, jarEnd);
      if (file.startsWith("file:")) {
        jarFileName = file.substring(5, jarEnd);
      }
      else {
        jarFileName = file.substring(0, jarEnd);
      }

      if (!new File(jarFileName).exists()) {
        logger_.warn("jar file [" + jarFileName + "] does not exist. " + "(Parsed from url ["
            + argPackageLocation + "])");
      }

      String packageLocation = file.substring(jarEnd + 2);
      INameFilter filter = new PackageContentsNameFilter(argFilter, packageLocation);
      JarFile jar = new JarFile(jarFileName);

      Enumeration<JarEntry> entries = jar.entries();
      while (entries.hasMoreElements()) {
        JarEntry item = entries.nextElement();
        String itemName = item.getName();
        if (filter.accept(itemName)) {
          URL newUrl =
              new URL(argPackageLocation.getProtocol() + ":" + file.substring(0, jarEnd) + "!/" + itemName);
          urlList.add(newUrl);
        }
      }
    }
    catch (IOException ex) {
      logger_.error("CAUGHT EXCEPTION", ex);
    }
    return urlList;
  }

  private static class PackageContentsNameFilter
      implements INameFilter {

    private final INameFilter parentFilter_;
    private final String package_;

    /**
     * Constructor
     *
     * @param argParent parent filter
     * @param argPackage the package to match
     */
    public PackageContentsNameFilter(INameFilter argParent, String argPackage) {
      parentFilter_ = argParent;
      if (argPackage.endsWith("/")) {
        package_ = argPackage;
      }
      else {
        package_ = argPackage + "/";
      }
    }

    /** {@inheritDoc} */
    public boolean accept(String argName) {
      // remove if the beginning doesn't match
      if (!argName.startsWith(package_)) {
        return false;
      }
      //look at the part after the package name
      String endName = argName.substring(package_.length());
      // remove the package itself
      if (endName.length() == 0) {
        return false;
      }
      // remove subpackages
      if (endName.indexOf("/") != -1) {
        return false;
      }
      if (parentFilter_ != null) {
        return parentFilter_.accept(argName);
      }
      return true;
    }
  }

  private static void addPackageContents(String argLocationName, List<URL> argUrlList, INameFilter argFilter,
      boolean argDistinctObjects) {

    URL[] configs = ClassPathUtils.getPackageContents(argFilter, argLocationName, argDistinctObjects);
    argUrlList.addAll(Arrays.asList(configs));
  }

  /**
   * main method that can be used to diagnose the classpath characteristics.
   *
   * @param args command line parameters
   */
  public static void main(String[] args) {
    if (args.length > 0) {
      if ("--package-locations".equals(args[0]) && (args.length == 2)) {
        String msg = getPackageLocationsMessage(args[1]);
        System.out.print(msg);
        return;
      }
      else if ("--package-contents".equals(args[0]) && (args.length == 2)) {
        String msg = getPackageContentsMessage(args[1]);
        System.out.print(msg);
        return;
      }
    }
    showUsage();
  }

  private static void showUsage() {
    System.out.println("\t--package-locations {package}\n\t\tlists all locations for the specified package");
    System.out.println("\t--package-contents {package}\n\t\tlists all contents for the specified package");
  }

  private static String getPackageLocationsMessage(String argPackage) {
    StringBuilder sb = new StringBuilder();
    sb.append("Locations containing package '");
    sb.append(argPackage);
    sb.append("':\n");
    for (URL url : getPackageLocations(argPackage)) {
      sb.append("\t");
      sb.append(url.toExternalForm());
      sb.append("\n");
    }
    return sb.toString();
  }

  private static String getPackageContentsMessage(String argPackage) {
    StringBuilder sb = new StringBuilder();
    sb.append("Contents of package '");
    sb.append(argPackage);
    sb.append("':\n");
    for (URL url : getPackageContents(argPackage)) {
      sb.append("\t");
      sb.append(url.toExternalForm());
      sb.append("\n");
    }
    return sb.toString();
  }

  /**
   * Returns a list of <code>URL</code>s identifying all resource located on the class path and
   * having the specified fully-qualified name.<br>
   * <br>
   *
   * <b>Note</b>: The return list is ordered such that resources located higher on the class path
   * are included as lower-indexed elements.
   *
   * @param argResourceName the fully-qualified name of the resource
   * @return the <code>URL</code>s of any resource named <code>argResourceName</code>; an empty list
   * if no such resources could be located
   */
  public static List<URL> getResources(String argResourceName) {
    try {
      String resourceName = argResourceName;
      String extension = null;

      int extPos = resourceName.lastIndexOf('.');
      if (extPos > 0) {
        extension = resourceName.substring(extPos);
        resourceName = resourceName.substring(0, extPos);
      }
      resourceName = resourceName.replace('.', '/') + extension;

      return getResourcesImpl(resourceName);
    }
    catch (IOException ex) {
      logger_.error("CAUGHT EXCEPTION", ex);
      return new ArrayList<URL>();
    }
  }

  /**
   * Return the URL mapped to the first instance of the specified resource found on the classpath.
   * Will return null if not found.
   * @param argResourceName
   * @return the URL mapped to the first instance of the specified resource found on the classpath.
   * Will return null if not found.
   */
  public static URL getResource(String argResourceName) {
    String resourceName = argResourceName;
    String extension = null;

    int extPos = resourceName.lastIndexOf('.');
    if (extPos > 0) {
      extension = resourceName.substring(extPos);
      resourceName = resourceName.substring(0, extPos);
    }
    resourceName = resourceName.replace('.', '/') + extension;

    return getResourceImpl(resourceName);
  }

  /**
   * Returns a list of <code>URL</code>s identifying all resources located in the specified package
   * and meeting the criteria laid out by the specified filter.<br>
   * <br>
   *
   * All filter-qualifying resources having <code>argPackageName</code> as either a parent (if
   * <code>argIncludeSubpackages</code> is <code>false</code>) or an ancestor (if
   * <code>argIncludeSubpackages</code> is <code>true</code>) package will be returned.<br>
   * <br>
   *
   * <b>Note</b>: The return list is ordered such that resources located higher on the class path
   * are included as lower-indexed elements.
   *
   * @param argPackageName the root name from which all returned resources will be derived,
   * expressed either as a package name or as a package-qualified resource
   * @param argFilter a filter whose criteria must be satisfied in order to include a resource in
   * the return set
   * @param argIncludeSubpackages <code>true</code> if all resources located within
   * <code>argPackageName</code> and any of its descendents should be included in the return set;
   * <code>false</code> if only those resources located in <code>argPackageName</code> should be
   * considered
   * @return a list of <code>URL</code>s locating all resources derived from
   * <code>argPackageName</code> and satisfying <code>argFilter</code>; an empty list if no
   * qualifying resources could be located
   */
  public static List<URL> getResources(String argPackageName, INameFilter argFilter,
      boolean argIncludeSubpackages) {

    List<URL> resources = new ArrayList<URL>();
    try {
      String packageName = argPackageName.replace('.', '/');
      // Find all locations on the class path containing the specified package.
      List<URL> locations = getResourcesImpl(packageName);

      // Iterate through each qualifying URL and derive their relevant contents.
      for (URL loc : locations) {
        if ("file".equals(loc.getProtocol())) {
          addResourcesFromFile(resources, loc, argFilter, argIncludeSubpackages);
        }
        else {
          try {
            addResourcesFromJar(resources, loc, argFilter, argIncludeSubpackages);
          }
          catch (Exception ee) {
            logger_.error("Error occurred while processing URL as jar: " + loc + " for package name: "
                + argPackageName + " and filter: " + argFilter, ee);
          }
        }
      }
    }
    catch (Exception ex) {
      logger_.error("CAUGHT EXCEPTION", ex);
    }
    return resources;
  }

  // Returns a list of all resources on the classpath having the specified name.
  private static List<URL> getResourcesImpl(String argResourceName)
      throws IOException {

    // Find every instance of the named resource on the class path.
    List<URL> resources = new ArrayList<URL>();
    Enumeration<URL> temp = getContextClassLoader().getResources(argResourceName);
    
    if (temp == null) {
    	temp = ClassPathUtils.class.getClassLoader().getResources(argResourceName);
    }
    
    // Convert the archaic Enumeration into a 21st-century Collection.
	if (temp != null) {
		while (temp.hasMoreElements()) {
			resources.add(temp.nextElement());
		}
	}
    return resources;
  }

  // Returns a list of all resources on the classpath having the specified name.
  private static URL getResourceImpl(String argResourceName) {
    // Find the first instance of the named resource on the class path.
	URL resource = getContextClassLoader().getResource(argResourceName);
	if (resource == null) {
		resource = ClassPathUtils.class.getClassLoader().getResource(argResourceName);
	}
	
    return resource;
  }

  /* Adds to the specified list of resources all URLs derived from the specified
   * URL, which represents either a file or a directory of files. */
  private static void addResourcesFromFile(List<URL> argResources, URL argResourceLoc, INameFilter argFilter,
      boolean argIncludeSubPackages)
      throws URISyntaxException, MalformedURLException {

    addResourcesFromFileImpl(argResources, argResourceLoc, argFilter, argIncludeSubPackages, true);
  }

  /* Adds to the specified list of resources all URLs derived from the specified
   * URL, which represents either a file or a directory of files. */
  private static void addResourcesFromFileImpl(List<URL> argResources, URL argResourceLoc,
      INameFilter argFilter, boolean argIncludeSubpackages, boolean argAtRootLevel)
      throws URISyntaxException, MalformedURLException {

    File f = FileUtils.getFileForURL(argResourceLoc);

    /* If the file represents a directory, build a deep collection of all
     * non-directory files located within the hierarchy beneath that
     * directory.  If it's a non-directory file, validate it against the
     * filter and, if it passes, add it to the final resource list. */
    if (f.isDirectory()) {
      if ((argAtRootLevel) || (argIncludeSubpackages)) {
        addResourcesFromPath(argResources, argResourceLoc, argFilter, argIncludeSubpackages);
      }
    }
    else if ((argFilter == null) || (argFilter.accept(f.getPath()))) {
      argResources.add(argResourceLoc);
    }
  }

  /* Adds to the specified list of resources all URLs derived from the specified
   * URL, which represents a directory of files.  Each URL in the returned set
   * is guaranteed to represent a specific file satisified by the specified
   * filter.  This is a deep derivation; all qualifying files located within the
   * specified directory or in any of its sub-directories will be included. */
  private static void addResourcesFromPath(List<URL> argResources, URL argResourceLoc, INameFilter argFilter,
      boolean argIncludeSubpackages)
      throws URISyntaxException, MalformedURLException {

    File rootPath = FileUtils.getFileForURL(argResourceLoc);

    if (isDebugEnabled_) {
      logger_.debug("Loading package contents from path: " + rootPath);
    }

    if (rootPath == null) {
      logger_.error("rootPath is null for URL: [" + argResourceLoc + "]");
      return;
    }
    if ((rootPath != null) && !rootPath.isDirectory()) {
      logger_.error("rootPath [" + rootPath + "] is NOT A DIRECTORY for URL: [" + argResourceLoc + "]");
    }

	File[] files = rootPath.listFiles();
	if (files != null) {
		for (File f : files) {
			addResourcesFromFileImpl(argResources, f.toURI().toURL(),
					argFilter, argIncludeSubpackages, false);
		}
	}
  }

  /* Adds to the specified list of resources all URLs derived from the specified
   * URL, which represents either a file or a package of files located within a
   * Java archive. */
  private static void addResourcesFromJar(List<URL> argResources, URL argResourceLoc, INameFilter argFilter,
      boolean argIncludeSubPackages)
      throws IOException {

    String locFileName = argResourceLoc.getFile();
    int jarEndPos = locFileName.lastIndexOf('!');

    String jarFileName = locFileName.substring(5, jarEndPos);
    JarFile jar = new JarFile(jarFileName);

    Enumeration<JarEntry> entries = jar.entries();
    while (entries.hasMoreElements()) {
      JarEntry jarItem = entries.nextElement();

      String resourceName = jarItem.getName();
      String packageLoc = locFileName.substring(jarEndPos + 2);
      INameFilter filter = new PackageContentsNameFilter2(argFilter, packageLoc, argIncludeSubPackages);

      if (filter.accept(resourceName)) {
        URL resourceLoc =
            new URL(argResourceLoc.getProtocol() + ":" + locFileName.substring(0, jarEndPos) + "!/"
                + resourceName);
        argResources.add(resourceLoc);
      }
    }
  }

  /* A name filter that augments another filter by imposing additional
   * validations relevant to package/directory naming conventions. */
  private static class PackageContentsNameFilter2
      implements INameFilter {

    private final INameFilter parentFilter_;
    private final String package_;
    private final boolean includeSubpackages_;

    // Constructs a PackageContentsNameFilter.
    PackageContentsNameFilter2(INameFilter argParent, String argPackage, boolean argIncludeSubpackages) {
      parentFilter_ = argParent;
      package_ = (argPackage.endsWith("/")) ? argPackage : argPackage + "/";
      includeSubpackages_ = argIncludeSubpackages;
    }

    /** {@inheritDoc} */
    public boolean accept(String argName) {
      // Exclude resources not located within the target package hierarchy.
      if (!argName.startsWith(package_)) {
        return false;
      }
      /* Extract the part of the resource name following the package name.  This
       * may resolve to a child resource of the target package or to a more
       * distant descendant. */
      String endName = argName.substring(package_.length());

      // Exclude the package itself.
      if (endName.length() == 0) {
        return false;
      }
      /* Filter out resources located in sub-packages of the target package if
       * this filter is configured to do so. */
      if ((!includeSubpackages_) && (endName.indexOf("/") >= 0)) {
        return false;
      }
      // If there's another filter to which to delegate validation, do so now.
      return (parentFilter_ != null) ? parentFilter_.accept(argName) : true;
    }
  }

  /**
   * Provided for more robust resource loading on app servers.
   * @return ClassLoader for the current thread context.
   */
  private static ClassLoader getContextClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }

  private static String cleanPackageName(String argPackageName) {
    String packageName = nonNull(argPackageName).trim();

    packageName = packageName.replace('.', '/');
    packageName = ensureTrailingSlash(packageName);
    packageName = ensureNoLeadingSlash(packageName);

    return packageName;
  }

  private static String ensureTrailingSlash(String argString) {
    return (isEmpty(argString) || argString.endsWith("/")) ? argString : argString + "/";
  }

  private static String ensureNoLeadingSlash(String argString) {
    return (isEmpty(argString) || !argString.startsWith("/")) ? argString : argString.substring(1);
  }

  // Prevents construction.
  private ClassPathUtils() {}
}
