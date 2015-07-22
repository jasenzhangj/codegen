package com.hitao.codegen.configs.common;

import static com.hitao.codegen.constent.StringConstants.CODEGEN_CLASS_DEFAUL_MODIFY;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_FIELD_DEFAUL_MODIFY;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_METHOD_DEFAUL_MODIFY;
import static com.hitao.codegen.constent.StringConstants.LINE_BREAK;
import static com.hitao.codegen.util.StringUtils.EMPTY;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.hitao.codegen.configs.CodeGenUtils;
import com.hitao.codegen.configs.DAOServiceConfigurationManager;
import com.hitao.codegen.configs.IClassCodeGenConfig;
import com.hitao.codegen.configs.IClassElementCodeGenConfig;
import com.hitao.codegen.configs.basic.IConfigObject;
import com.hitao.codegen.configs.basic.IParentConfig;
import com.hitao.codegen.util.ObjectUtils;
import com.hitao.codegen.util.StringUtils;

/***
 * This configuration is represents the elements of the class.
 * e.g: import class, class name, interface name, method, field, exception.
 *
 * @author  zhangjun.ht
 * @created 2011-2-23
 * @version $Id: ClassElementConfig.java 57 2011-04-29 02:16:16Z guest $
 */
public abstract class ClassElementConfig extends AbstractCodeGenConfig implements IClassElementCodeGenConfig{

	private static final long serialVersionUID = 2273161355887636560L;
	
	private static final String CLASS_NAME = "class";
	private static final String MODIFY = "modify";
	private static final String GENERIC_TYPE = "genericType";
	private static final String CONCRETE_CLASS = "concreteClass";
	
	// the comments of this property or method or class
	private CommentConfig comment_ = null;

	// the Annotations of this property or method or class
	private Set<AnnotationConfig> annotations_ = new HashSet<AnnotationConfig>();
	
	// the whole class name. e.g: com.hitao.codegen.ConfigClass
	private String className_ = EMPTY;

	// modify of the property. e.g: private, public static, public static final
	private String modify_ = EMPTY;

	// package information
	private Set<String> implicitImportList_ = new HashSet<String>();

	// import list for class's field.
	private Set<ImportConfig> importList_ = new HashSet<ImportConfig>();
	
	// represent the class's generic type. eg: Objecta<? extends Objectb>
	private String genericType_ = EMPTY;

	// The concrete implement class of the generic type.
	private String concreteClass = EMPTY;
	
	@Override
	public void setConfigObject(String argKey, IConfigObject argValue) {
		if (CommentConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof CommentConfig) {
			setComment((CommentConfig) argValue);
		} else if (CLASS_NAME.equalsIgnoreCase(argKey)) {
			setClassName(argValue.toString());
		} else if (MODIFY.equalsIgnoreCase(argKey)) {
			setModify(argValue.toString());
		} else if (GENERIC_TYPE.equalsIgnoreCase(argKey)) {
			setGenericType(argValue.toString());
		} else if (CONCRETE_CLASS.equalsIgnoreCase(argKey)) {
			setConcreteClass(argValue.toString());
		} else if (ImportBlockConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof ImportBlockConfig) {
			addImport(((ImportBlockConfig) argValue));
		} else if (ImportConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof ImportConfig) {
			addImport(((ImportConfig) argValue));
		} else if (AnnotationConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof AnnotationConfig) {
			addAnnotation(((AnnotationConfig) argValue));
		}
	}
	
	public void addAnnotation(AnnotationConfig argAnnotationConfig) {
		annotations_.add(argAnnotationConfig);
		setParentForConfigObject(argAnnotationConfig);
	}

	public void clearAnnotations() {
		annotations_.clear();
	}

	@Override
	public Set<AnnotationConfig> getAnnotations() {
		return annotations_;
	}
	
	/***
	 * Add annotations for the configuration.
	 * 
	 * @param argStatement
	 *            statement buffer
	 */
	protected void getAnnotations(StringBuffer argStatement) {
		// add annotations
		for (AnnotationConfig annotation : getAnnotations()) {
			annotation.getCodePieceInfo(argStatement);
			argStatement.append(LINE_BREAK);
		}

	}

	@Override
	public CommentConfig getComment() {
		if (comment_ == null) {
			comment_ = new CommentConfig();
		}
		return comment_;
	}

	protected void setComment(CommentConfig argComment) {
		this.comment_ = argComment;
		setParentForConfigObject(argComment);
	}

	@Override
	public String getClassName() {
		if (!StringUtils.isEmpty(className_)) {
			return className_;
		} else if (StringUtils.isEmpty(getPackagePath())) {
			return getName();
		} else {
			return getPackagePath() + "." + getName();
		}
	}

	@Override
	public String getPackagePath() {
		String packagePath = super.getPackagePath();
		if (StringUtils.isEmpty(packagePath) && (!StringUtils.isEmpty(className_))) {
			packagePath = CodeGenUtils.getPackageOfClass(className_);
		}
		
		return packagePath;
	}
	
	@Override
	public String getName() {
		String name = super.getName();
		if (StringUtils.isEmpty(name) && (!StringUtils.isEmpty(className_))) {
			name = CodeGenUtils.getShortClassName(className_);
		}
		
		return name;
	}
	
	@Override
	public void setClassName(String argType) {
		this.className_ = argType;
		if (!(this instanceof IClassCodeGenConfig)) {
			addImport(getClassName());
		}
	}

	private static final String defaultClassModify_ = DAOServiceConfigurationManager
			.getProperty(CODEGEN_CLASS_DEFAUL_MODIFY);
	private static final String defaultMethodModify_ = DAOServiceConfigurationManager
			.getProperty(CODEGEN_METHOD_DEFAUL_MODIFY);
	private static final String defaultFiledModify_ = DAOServiceConfigurationManager
			.getProperty(CODEGEN_FIELD_DEFAUL_MODIFY);

	/***
	 * get class/method/field modify.
	 * 
	 * @return
	 */
	@Override
	public String getModify() {
		if (StringUtils.isEmpty(modify_)) {
			if (this instanceof IClassCodeGenConfig) {
				modify_ = defaultClassModify_;
			} else if (this instanceof MethodConfig) {
				modify_ = defaultMethodModify_;
			} else if (this instanceof FieldConfig) {
				modify_ = defaultFiledModify_;
			}
		}

		return modify_;
	}

	@Override
	public void setModify(String argModify) {
		this.modify_ = argModify;
	}


	protected void addImport(String argImport) {
		if (argImport.indexOf("<") > -1) {
			addClassesInGeneric(argImport);
		} else {
			addSingleImport(argImport);
		}
	}

	@Override
	public void removeImportedClass(String argImport) {
		String importStament = CodeGenUtils.getImportStatement(argImport);

		if (implicitImportList_.contains(importStament)) {
			implicitImportList_.remove(importStament);
			for (ImportConfig im : importList_) {
				if (im.getStatement().equals(importStament)) {
					importList_.remove(im);
					break;
				}
			}
		}
	}
	
	@Override
	public void removeAnnotation(String argAnnotation) {
		Iterator<AnnotationConfig>  it = annotations_.iterator();
		AnnotationConfig annotationConfig = null;
		while (it.hasNext()) {
			annotationConfig = it.next();
			if (argAnnotation.equals(annotationConfig.getName())) {
				it.remove();
				break;
			}
		}
	}
	
	@Override
	public boolean containsAnnotation(String argAnnotation) {
		boolean result = false;
		Iterator<AnnotationConfig>  it = annotations_.iterator();
		AnnotationConfig annotationConfig = null;
		while (it.hasNext()) {
			annotationConfig = it.next();
			if (argAnnotation.equals(annotationConfig.getName())) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	@Override
	public void clearImportClasses() {
		implicitImportList_.clear();
		importList_.clear();
	}

	protected void addSingleImport(String argImport) {
		if (!StringUtils.isEmpty(argImport) && argImport.indexOf('.') > 0
				&& (!ObjectUtils.isCommonLangPackageClass(argImport))
				// TODO: I think it is not right idea. But i haven't a better idea
				// now.
				&& (!(argImport.length() == 1))) {
			String importStatement = CodeGenUtils.getImportStatement(argImport);
			if (!implicitImportList_.contains(importStatement)) {
				implicitImportList_.add(importStatement);
				importList_.add(new ImportConfig(importStatement));
			}
		}
	}

	private void addImport(ImportConfig argImport) {
		if (argImport instanceof ImportBlockConfig) {
			for (ImportConfig imp : ((ImportBlockConfig) argImport)
					.getImportList()) {
				addSingleImport(imp.getStatement());
			}
		} else {
			addSingleImport(argImport.getStatement());
		}
		setParentForConfigObject(argImport);
	}

	public Set<ImportConfig> getImportList() {
		return importList_;
	}

	protected void addClassesInGeneric(String argImport) {
		String[] classes = CodeGenUtils.getClassesInGeneric(argImport);
		for (String cs : classes) {
			if (cs.indexOf(".") > -1) {
				addSingleImport(cs.trim());
			}
		}
	}

	/***
	 * Add short class name into StringBuffer.
	 * 
	 * @param argStringBuffer
	 * @return
	 */
	protected StringBuffer getShortClassName(StringBuffer argStringBuffer) {
		return argStringBuffer.append(getShortClassName(getClassName()));
	}

	public static String getShortClassName(String argClassName) {
		return CodeGenUtils.getShortClassName(argClassName);
	}

	public static String getShortClassNameWithoutGenericType(String argClassName) {
		return CodeGenUtils.getShortClassNameWithoutGenericType(argClassName);
	}

	protected String getShortClassName() {
		return CodeGenUtils.getShortClassName(getClassName());
	}

	protected String getPackageOfClass() {
		return CodeGenUtils.getPackageOfClass(getClassName());
	}

	public static String getPackageOfClass(String argClassName) {
		return CodeGenUtils.getPackageOfClass(argClassName);
	}

	@Override
	public String getConcreteClass() {
		String tempClass = getShortClassName(concreteClass);
		if (StringUtils.isEmpty(tempClass)) {
			int index = genericType_.indexOf(" extends ");
			if (index > 0) {
				setConcreteClass(genericType_
						.substring(index + " extends ".length(),
								genericType_.length() - 1));
			}
		}
		return concreteClass;
	}

	@Override
	public void setConcreteClass(String concreteClass) {
		if (!StringUtils.isEmpty(concreteClass)) {
			this.concreteClass = concreteClass;
			addImport(concreteClass);
		}
	}

	public static boolean containsConcreteClass(IClassElementCodeGenConfig argCodeGenConfig) {
		return !StringUtils.isEmpty(argCodeGenConfig.getConcreteClass());
	}

	public static String getConreteClassStatement(
			IClassElementCodeGenConfig argCodeGenConfig) {
		String concreteClass = argCodeGenConfig.getConcreteClass().trim();
		if (StringUtils.isEmpty(concreteClass)) {
			return EMPTY;
		}

		if (concreteClass.charAt(0) != '<') {
			concreteClass = "<" + concreteClass;
		}

		if (concreteClass.charAt(concreteClass.length() - 1) != '>') {
			concreteClass = concreteClass + ">";
		}
		return concreteClass;
	}

	protected boolean containsConcreteClass() {
		return containsConcreteClass(this);
	}

	protected String getConreteClassStatement() {
		return getConreteClassStatement(this);
	}

	@Override
	public String getGenericType() {
		return genericType_;
	}

	@Override
	public void setGenericType(String argGeneric) {
		argGeneric = argGeneric.trim();
		if (!StringUtils.isEmpty(argGeneric) && argGeneric.charAt(0) != '<') {
			this.genericType_ = "<" + argGeneric;
			if (argGeneric.charAt(argGeneric.length() - 1) != '>') {
				this.genericType_ = genericType_ + ">";
			}
		} else {
			this.genericType_ = argGeneric;
		}
	}
	
	public String getFileType() {
		return ".java";
	}
	
	@Override
	public String getEndElement() {
		return "}";
	}
	
	/***
	 * Set parent configuration object for the argument.
	 * 
	 * @param argConfigObject
	 */
	protected void setParentForConfigObject(IConfigObject argConfigObject) {
		if (argConfigObject instanceof IParentConfig) {
			((IParentConfig) argConfigObject).setParent(this);
		}
	}

	@Override
	public StringBuffer getCodePieceInfo(StringBuffer argStringBuffer) {
		if (!isInitial()) {
			initial();
		}
		return argStringBuffer.append(getCodes());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		ClassElementConfig config = (ClassElementConfig) super.clone();
		config.implicitImportList_ = new HashSet<String>();
		config.importList_ = new HashSet<ImportConfig>();
		config.annotations_ = new HashSet<AnnotationConfig>();

		for (ImportConfig im : getImportList()) {
			config.setConfigObject(ImportConfig.MAIN_TAG,
					(ImportConfig) im.clone());
		}

		for (AnnotationConfig annotation : getAnnotations()) {
			config.setConfigObject(AnnotationConfig.MAIN_TAG, annotation);
		}

		return config;
	}
}
