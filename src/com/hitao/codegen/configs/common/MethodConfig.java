package com.hitao.codegen.configs.common;

import static com.hitao.codegen.constent.StringConstants.CODEGEN_HOLD_COMMENTS_FOR_IMPLEMENTS;
import static com.hitao.codegen.constent.StringConstants.LINE_BREAK;
import static com.hitao.codegen.constent.StringConstants.SEMICOLON;
import static com.hitao.codegen.util.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.hitao.codegen.configs.CodeGenUtils;
import com.hitao.codegen.configs.DAOServiceConfigurationManager;
import com.hitao.codegen.configs.IClassElementCodeGenConfig;
import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.IInterfaceCodeGenConfig;
import com.hitao.codegen.configs.append.AppendCodesInfo;
import com.hitao.codegen.configs.append.IAppendCodesInfo;
import com.hitao.codegen.configs.basic.IConfigObject;
import com.hitao.codegen.util.StringUtils;

/***
 * Method configuration for code generation.
 *
 * @author zhangjun.ht
 * @created 2010-11-28
 * @version $Id: MethodConfig.java 54 2011-04-22 09:31:54Z guest $
 */
public class MethodConfig extends ClassElementConfig {

	private static final long serialVersionUID = -8799859544102144511L;

	public static final String MAIN_TAG = "method";

	private static final String RETURN_TYPE = "returnType";
	private static final String OVERRIDE = "override";
	private static final String GEN_COMMENT = "gencomment";
	private static final String RETURN_COMMENT = "returnComment";

	private static final BodyConfig EMPTY_BODY = new BodyConfig();

	// Argument list.
	private List<ParameterConfig> parameterConfigList_ = new ArrayList<ParameterConfig>();

	// throw's exception list.
	private List<ThrowExceptionConfig> exceptionList_ = new ArrayList<ThrowExceptionConfig>();

	// Method body
	private BodyConfig methodBody_ = EMPTY_BODY;

	// Return type eg: com.package.ClassName or void
	private String returnType_ = "void";

	// if user want implement the method by himself, not by the system atuomatic
	// generation.
	private boolean override_;

	// whether generate the comments for this method.
	private boolean genComment_ = true;

	//The comments for the return object.
	private String returnComment_ = null;

	/***
	 * Add parameter configuration
	 *
	 * @param argArgPropertyConfig
	 */
	protected void addParameter(ParameterConfig argArgPropertyConfig) {
		if (!parameterConfigList_.contains(argArgPropertyConfig)) {
			parameterConfigList_.add(argArgPropertyConfig);
			setParentForConfigObject(argArgPropertyConfig);
		} else {
			int index = parameterConfigList_.indexOf(argArgPropertyConfig);
			if (!parameterConfigList_.get(index).getName().equals(argArgPropertyConfig.getName())) {
				parameterConfigList_.add(argArgPropertyConfig);
				setParentForConfigObject(argArgPropertyConfig);
			}
		}
	}

	/***
	 * Add exception configuration
	 *
	 * @param argExceptionConfig
	 */
	protected void addThrowException(ThrowExceptionConfig argExceptionConfig) {
		exceptionList_.add(argExceptionConfig);
		setParentForConfigObject(argExceptionConfig);
	}

	/***
	 * Set body configuration.
	 * @param argBodyConfig
	 */
	protected void setMethodBody(BodyConfig argBodyConfig){
		this.methodBody_ = argBodyConfig;
		setParentForConfigObject(argBodyConfig);
	}

	@Override
	public void setConfigObject(String argKey, IConfigObject argValue) {
		if (ParameterConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof ParameterConfig) {
			addParameter((ParameterConfig) argValue);
		} else if (BodyConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof BodyConfig) {
			setMethodBody((BodyConfig) argValue);
		} else if (RETURN_TYPE.equalsIgnoreCase(argKey)) {
			setReturnType(argValue.toString());
		} else if (ThrowExceptionConfig.MAIN_TAG.equalsIgnoreCase(argKey)
				|| argValue instanceof ThrowExceptionConfig) {
			addThrowException((ThrowExceptionConfig) argValue);
		} else if (OVERRIDE.equalsIgnoreCase(argKey)) {
			setOverride(argValue.toString());
		} else if (GEN_COMMENT.equalsIgnoreCase(argKey)) {
			setGenComment(argValue.toString());
		} else if (RETURN_COMMENT.equalsIgnoreCase(argKey)){
			setReturnComment(argValue.toString());
		} else {
			super.setConfigObject(argKey, argValue);
		}
	}

	@Override
	public String getStatement() {
		StringBuffer statement = new StringBuffer();
		getMethodMainInfo(statement);

		// if parent configuration is a object to generate an interface object
		// so it's method doesn't contain a body.
		if (getParent() instanceof IInterfaceCodeGenConfig) {
			statement.append(SEMICOLON + LINE_BREAK);
		} else {
			// Method's body
			statement.append("{" + LINE_BREAK);
			statement.append(getMethodBody().getStatement());
			statement.append(LINE_BREAK);
			statement.append("}" + LINE_BREAK);
		}

		return statement.toString();
	}

	/**
	 * get all comments and body for the method.
	 * @param argStringBuffer
	 */
	public void getMethodMainInfo(StringBuffer argStringBuffer) {
		// method's comments
		getCommentsInfo(argStringBuffer);

		getAnnotations(argStringBuffer);
		
		
		if (!StringUtils.isEmpty(getModify())) {
			argStringBuffer.append(getModify() + " ");
		}
		argStringBuffer.append(getReturnType() + " " );
		getMethodDefinition(argStringBuffer);
	}
	
	/***
	 * Get method's definition.
	 * @param argStringBuffer
	 */
	protected void getMethodDefinition(StringBuffer argStringBuffer) {
		// Method's body
		List<ParameterConfig> parameterList = getParameterConfigList();
		argStringBuffer.append(getName());
		argStringBuffer.append("(");
		if (!parameterList.isEmpty()) {
			for (ICodeGenConfig parameter : parameterList) {
				parameter.getCodePieceInfo(argStringBuffer);
				argStringBuffer.append(",");
			}
			argStringBuffer.setLength(argStringBuffer.length() - 1);
		}
		argStringBuffer.append(")");

		// Method's exceptions
		if (!exceptionList_.isEmpty()) {
			argStringBuffer.append("throws ");
			for (ICodeGenConfig exception : exceptionList_) {
				exception.getCodePieceInfo(argStringBuffer);
				argStringBuffer.append(",");
			}
			argStringBuffer.setLength(argStringBuffer.length() - 1);
		}
	}

	/***
	 * get method's comment information.
	 * @param argStringBuffer
	 */
	protected void getCommentsInfo(StringBuffer argStringBuffer) {
		if (isGenComment()) {
			argStringBuffer.append("/***" + LINE_BREAK);
			ICodeGenConfig comment = getComment();
			argStringBuffer.append(" * ");
			comment.getCodePieceInfo(argStringBuffer);
			argStringBuffer.append(LINE_BREAK);

			for (IClassElementCodeGenConfig parameter : parameterConfigList_) {
				argStringBuffer.append(" * @param ");
				argStringBuffer.append(parameter.getName());
				argStringBuffer.append(" ");
				parameter.getComment().getCodePieceInfo(argStringBuffer);
				argStringBuffer.append(LINE_BREAK);
			}

			if (!StringUtils.isEmpty(getReturnComment())) {
				argStringBuffer.append(" * @return "
						+ StringUtils.nonNull(getReturnComment()) + LINE_BREAK);
			}

			for (IClassElementCodeGenConfig exception : exceptionList_) {
				argStringBuffer.append(" * @throws ");
				exception.getCodePieceInfo(argStringBuffer);
				argStringBuffer.append(" ");
				exception.getComment().getCodePieceInfo(argStringBuffer);
				argStringBuffer.append(LINE_BREAK);
			}


			argStringBuffer.append(" */" + LINE_BREAK);
		}
	}

	public BodyConfig getMethodBody() {
		return methodBody_;
	}

	/***
	 * Get return type.
	 * @return
	 */
	public String getReturnType() {
		return getShortClassName(returnType_);
	}
	
	/***
	 * Get return type without generic type. 
	 * @return
	 */
	public String getSimpleReturnType() {
		return getShortClassNameWithoutGenericType(returnType_);
	}

	/***
	 * Set return type and add class name needed import.
	 *
	 * @param argReturnType
	 */
	public void setReturnType(String argReturnType) {
		this.returnType_ = argReturnType;
		addImport(argReturnType);
	}

	public void setOverride(String argOverride) {
		override_ = Boolean.valueOf(argOverride);
	}

	public boolean isOverride() {
		return override_;
	}

	public void setGenComment(String argGenComment) {
		this.genComment_ = Boolean.valueOf(argGenComment);
	}

	public void setGenComment(boolean argGenComment) {
		this.genComment_ = argGenComment;
	}

	public boolean isGenComment() {
		return genComment_
				&& (DAOServiceConfigurationManager
						.getBooleanProperty(CODEGEN_HOLD_COMMENTS_FOR_IMPLEMENTS) || !(getParent() instanceof ImplementsClassConfig));
	}

	@Override
	public Set<ImportConfig> getImportList() {
		Set<ImportConfig> set = super.getImportList();
		for (IClassElementCodeGenConfig paramter : parameterConfigList_){
			set.addAll(paramter.getImportList());
		}

		for (IClassElementCodeGenConfig exception : exceptionList_){
			set.addAll(exception.getImportList());
		}

		return set;
	}
	
	@Override
	public List<IAppendCodesInfo> getAppendCodesInfo(
			List<IAppendCodesInfo> argList) {
		AppendCodesInfo info = new AppendCodesInfo();
		
		StringBuffer buffer = new StringBuffer();
		getMethodDefinition(buffer);
		info.setCheckConditionCodes(buffer.toString());
		info.addFindAppendIndexStrategy(getEndElement());

		// this is the codes should be appended.
		info.setAppendCodes(getCodes().toString());
		addAppendCodeInfo(argList, info);
		return argList;
	}

	@Override
	public int hashCode() {
		int hash = this.getName().hashCode();
		for (ParameterConfig parameter : getParameterConfigList()) {
			hash += parameter.hashCode();
		}
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MethodConfig)) {
			return false;
		}
		MethodConfig mehtod1 = (MethodConfig) this;
		MethodConfig mehtod2 = (MethodConfig) obj;
		return StringUtils.equals(mehtod2.getName(),
				mehtod1.getName())
				&& CodeGenUtils.parametersEquals(
						mehtod2.getParameterConfigList(),
						mehtod1.getParameterConfigList());

	}

	/***
	 * the default Method's modify is public.
	 */
	public String getModify() {

		// if this method in interface, ingore it's modify.
		if (getParent() instanceof IInterfaceCodeGenConfig) {
			return EMPTY;
		}

		String modify = super.getModify();
		if (StringUtils.isEmpty(modify)) {
			return "public";
		} else {
			return modify;
		}
	}

	public List<ThrowExceptionConfig> getExceptionList() {
		return exceptionList_;
	}

	public List<ParameterConfig> getParameterConfigList() {
		return parameterConfigList_;
	}

	/***
	 * Get the brief statement of the method.
	 * @return
	 */
	public String getMethodStatement(){
		StringBuffer statement = new StringBuffer();
		statement.append(getName());
		statement.append("(");
		
		
		for (ParameterConfig parameter : getParameterConfigList()){
			statement.append(parameter.getName() + ", ");
		}
		
		if (!getParameterConfigList().isEmpty()) {
			statement.setLength(statement.length() - 2);
		}
		statement.append(")");
		return statement.toString();
	}


	public String getReturnComment() {
		return returnComment_;
	}

	public void setReturnComment(String returnComment) {
		this.returnComment_ = returnComment;
	}

	@Override
	public void removeImportedClass(String argImport) {
		super.removeImportedClass(argImport);

		removeParameter(argImport);
		removeException(argImport);
	}

	protected void removeException(String argException) {
		for (ThrowExceptionConfig exception : getExceptionList()){
			if (exception.getClassName().equals(argException)){
				getExceptionList().remove(exception);
				break;
			}
		}
	}

	protected void removeParameter(String argParameter) {
		for (ParameterConfig parameter : getParameterConfigList()){
			if (parameter.getClassName().equals(argParameter)){
				getParameterConfigList().remove(parameter);
				break;
			}
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		MethodConfig method = (MethodConfig)super.clone();
		method.exceptionList_ = new ArrayList<ThrowExceptionConfig>(getExceptionList().size() + 1);
		for (ThrowExceptionConfig exception : getExceptionList()){
			method.setConfigObject(ThrowExceptionConfig.MAIN_TAG,(ThrowExceptionConfig)exception.clone());
		}

		method.parameterConfigList_ = new ArrayList<ParameterConfig>(getParameterConfigList().size() + 1);
		for (ParameterConfig parameter : getParameterConfigList()){
			method.setConfigObject(ParameterConfig.MAIN_TAG, (ParameterConfig)parameter.clone());
		}

		return method;
	}
}
