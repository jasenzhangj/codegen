package com.hitao.codegen.configs.common;

import static com.hitao.codegen.util.StringUtils.EMPTY;

import java.util.List;

import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.append.IAppendCodesInfo;
import com.hitao.codegen.configs.basic.AbstractParentConfig;
import com.hitao.codegen.configs.basic.IConfigObject;
import com.hitao.codegen.configs.basic.IParentConfig;
import com.hitao.codegen.configs.exception.CodeGenException;

/***
 * The common configuration for generate codes.
 * 
 * @author zhangjun.ht
 * @created 2010-11-26
 * @version $Id: AbstractCodeGenConfig.java 57 2011-04-29 02:16:16Z guest $
 */
public abstract class AbstractCodeGenConfig extends AbstractParentConfig
		implements ICodeGenConfig {

	private static final long serialVersionUID = -6626365690023395547L;

	private static final String PACKAGE = "package";
	private static final String NAME = "name";
	private GenerateModel generateMode_ = GenerateModel.NEW;

	// the name of the property, method, class.
	private String name_ = EMPTY;

	// the package path for generate the source file.
	private String packagePath_ = EMPTY;

	// The dirty flag.
	private boolean isDirty_ = false;

	// the value of the configuration from the text contents of the node
	private String value_ = EMPTY;

	// flag for initialize.
	private boolean initialFlag_ = false;

	// The code piece.
	private StringBuffer codes_ = new StringBuffer();

	@Override
	public void setConfigObject(String argKey, IConfigObject argValue) {
		if (NAME.equalsIgnoreCase(argKey)) {
			setName(argValue.toString());
		} else if (PACKAGE.equalsIgnoreCase(argKey)) {
			setPackagePath(argValue.toString());
		}
	}

	@Override
	public String getName() {
		return name_;
	}

	@Override
	public void setName(String argName) {
		this.name_ = argName;
	}

	@Override
	public void setPackagePath(String packagePath) {
		this.packagePath_ = packagePath;
	}

	@Override
	public String getPackagePath() {
		return packagePath_;
	}

	@Override
	public boolean isDirty() {
		return isDirty_;
	}

	@Override
	public void setDirty() {
		isDirty_ = true;
	}

	/** {@inheritDoc} */
	public void setClean() {
		isDirty_ = false;
	}

	@Override
	public void setValue(String argValue) {
		this.value_ = argValue;
	}

	@Override
	public String getValue() {
		return value_;
	}

	@Override
	public boolean shouldBeOverride() {
		return GenerateModel.OVERRIDE == generateMode_;
	}

	@Override
	public void setOverride() {
		generateMode_ = GenerateModel.OVERRIDE;
	}
	
	@Override
	public boolean shouldBeAppend() {
		return GenerateModel.APPEND == generateMode_;
	}

	@Override
	public boolean shouldBeDelete() {
		return GenerateModel.DELETE == generateMode_;
	}
	
	@Override
	public boolean shouldBeReduce() {
		return GenerateModel.REDUCE == generateMode_;
	}
	
	@Override
	public void setAppendModel() {
		generateMode_= GenerateModel.APPEND;
	}

	@Override
	public GenerateModel getGenerateModel() {
		return generateMode_;
	}
	
	@Override
	public void  setDoNothing() {
		generateMode_= GenerateModel.DO_NOTHING;
	}
	
	@Override
	public boolean shouldBeGenerated() {
		return GenerateModel.DO_NOTHING != generateMode_;
	}
	
	@Override
	public boolean shouldBeNew() {
		return GenerateModel.NEW == generateMode_;
	}
	
	@Override
	public void setGenerateModel(GenerateModel generateModel) {
		generateMode_ = generateModel;
	}
	
	@Override
	public List<IAppendCodesInfo> getAppendCodesInfo(List<IAppendCodesInfo> argList){
		return argList;
	}
	
	protected void addAppendCodeInfo(List<IAppendCodesInfo> argList,
			IAppendCodesInfo argAppendCodesInfo) {
		if (!argList.contains(argAppendCodesInfo)) {
			argList.add(argAppendCodesInfo);
		}
	}
	
	public boolean isInitial() {
		return initialFlag_;
	}

	public void setInitial(boolean argInitialFlag) {
		this.initialFlag_ = argInitialFlag;
	}

	@Override
	public void validate() throws CodeGenException {

	}

	@Override
	public synchronized void initial() {
		if (!initialFlag_) {
			// clear the original statement.
			codes_.setLength(0);
			generateCodes(codes_);
			setInitial(true);
		}
	}
	
	protected StringBuffer generateCodes(StringBuffer argStringBuffer) {
		return argStringBuffer.append(getStatement());
	}

	/***
	 * Get Statement for the configuration object.
	 * 
	 * @return
	 */
	protected abstract String getStatement();

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

	protected StringBuffer getCodes() {
		return codes_;
	}

	@Override
	public StringBuffer getCodePieceInfo(StringBuffer argStringBuffer) {
		if (!isInitial()) {
			initial();
		}
		return argStringBuffer.append(getCodes());
	}

	@Override
	public String toString() {
		return getStatement();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		AbstractCodeGenConfig object = (AbstractCodeGenConfig)super.clone();
		object.codes_ = new StringBuffer();
		object.generateMode_ = GenerateModel.NEW;
		object.initialFlag_ = false;
		object.isDirty_ = false;
		return object;
	}

	/***
	 * the mode for generate file.
	 *
	 * @author  zhangjun.ht
	 * @created 2011-2-23
	 * @version $Id: AbstractCodeGenConfig.java 57 2011-04-29 02:16:16Z guest $
	 */
	public static enum GenerateModel {
		// create a new one(default).
		NEW, 
		// append content into the existed file.
		APPEND, 
		// override the existed file.
		OVERRIDE,
		// delete the existed file.
		DELETE,
		// reduce the code in configuration file
		REDUCE,
		// do nothing.
		DO_NOTHING;
	}
}
