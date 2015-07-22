package com.hitao.codegen.configs.common;

import static com.hitao.codegen.constent.StringConstants.LINE_BREAK;
import static com.hitao.codegen.constent.StringConstants.SEMICOLON;

import java.util.List;

import com.hitao.codegen.configs.IInterfaceCodeGenConfig;
import com.hitao.codegen.configs.append.AppendCodesInfo;
import com.hitao.codegen.configs.append.IAppendCodesInfo;
import com.hitao.codegen.configs.append.IFindAppendIndexStrategy;
import com.hitao.codegen.configs.basic.IConfigObject;

/***
 * the configuration for class's field.
 * 
 * To generate the piece of codes.like below: //comments of property private
 * 
 * @filedAnnotaions private Boolean propertyName = value;
 * 
 * @author zhangjun.ht
 * @created 2010-11-30
 * @version $Id: FieldConfig.java 45 2011-03-09 08:47:45Z guest $
 */
public class FieldConfig extends ClassElementConfig {

	private static final long serialVersionUID = 2150214192979765689L;

	public static final String MAIN_TAG = "field";
	private static final String VALUE = "value";

	private String value_;
	private boolean genSetMethod = true;
	private boolean genGetMethod = true;

	@Override
	public void setConfigObject(String argKey, IConfigObject argValue) {
		if (VALUE.equalsIgnoreCase(argKey)) {
			setFieldValue(argValue.toString());
		} else {
			super.setConfigObject(argKey, argValue);
		}
	}

	/***
	 * Get the code piece. like below: //comment of propertyName private Boolean
	 * 
	 * @filedAnnotaions propertyName = value;
	 */
	@Override
	public String getStatement() {
		StringBuffer statement = new StringBuffer();

		// set comments
		getComment().getCodePieceInfo(statement);
		statement.append(LINE_BREAK);

		getAnnotations(statement);

		// if parent configuration is a object to generate an interface object
		if (!(getParent() instanceof IInterfaceCodeGenConfig)) {
			statement.append(getModify());
		}

		statement.append(" ");
		getShortClassName(statement);
		if (containsConcreteClass(this)) {
			statement.append(getConreteClassStatement());
		}
		statement.append(" " + getName());

		if (value_ != null) {
			// do nothing
			statement.append(" = " + getFieldValue());
		}

		statement.append(SEMICOLON + LINE_BREAK);
		return statement.toString();
	}

	@Override
	public List<IAppendCodesInfo> getAppendCodesInfo(
			List<IAppendCodesInfo> argList) {
		IAppendCodesInfo info = new AppendCodesInfo();
		info.setCheckConditionCodes(" " + getName() + " ");
		info.addFindAppendIndexStrategyByStartsWith(0, findStrategy_);
		info.addFindAppendIndexStrategy(getEndElement());

		// this is the codes should be appended.
		info.setAppendCodes(getCodes().toString());
		addAppendCodeInfo(argList, info);
		return argList;
	}

	public String getFieldValue() {
		return value_;
	}

	public void setFieldValue(String argValue) {
		if (argValue.indexOf(";") > 0) {
			argValue = argValue.substring(0, argValue.indexOf(";"));
		}
		this.value_ = argValue;
	}

	public boolean isGenSetMethod() {
		return genSetMethod;
	}

	public void setGenSetMethod(boolean genSetMethod) {
		this.genSetMethod = genSetMethod;
	}

	public boolean isGenGetMethod() {
		return genGetMethod;
	}

	public void setGenGetMethod(boolean genGetMethod) {
		this.genGetMethod = genGetMethod;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FieldConfig)) {
			return false;
		}

		FieldConfig field1 = (FieldConfig) this;
		FieldConfig field2 = (FieldConfig) obj;

		return field1.getName().equals(field2.getName());
	}

	@Override
	public int hashCode() {
		return this.getName().hashCode();
	}

	private static IFindAppendIndexStrategy findStrategy_ = new FieldAppendIndexStrategy();

	static class FieldAppendIndexStrategy implements IFindAppendIndexStrategy {

		public FieldAppendIndexStrategy() {

		}

		@Override
		public int getAppendIndex(String argAllCodes) {
			int temp = argAllCodes.indexOf("public");

			if (temp > 0) {
				int index = argAllCodes.indexOf("public", temp + 1);
				if (index > 0) {
					index = argAllCodes.lastIndexOf(";", index);
					if (index < temp) {
						return -1;
					} else {
						return index + 1;
					}
				} else {
					return -1;
				}
			} else {
				return -1;
			}
		}

	}
}
