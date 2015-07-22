package com.hitao.codegen.configs.common;

import static com.hitao.codegen.util.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.List;

import com.hitao.codegen.configs.basic.IConfigObject;
import com.hitao.codegen.configs.exception.CodeGenException;
import com.hitao.codegen.configs.exception.ImproperAttributeException;

/***
 * The annotation configuration for class, method or field.
 * 
 * @author zhangjun.ht
 * @created 2011-2-17
 * @version $Id: AnnotationConfig.java 25 2011-02-24 08:40:37Z guest $
 */
public class AnnotationConfig extends ClassElementConfig {

	private static final long serialVersionUID = 8759200446499400637L;

	public static final String MAIN_TAG = "annotation";

	private static final String KEY = "key";
	private static final String VALUE = "value";

	private List<String> keys_ = new ArrayList<String>();
	private List<String> values_ = new ArrayList<String>();
	
	@Override
	public void setConfigObject(String argKey, IConfigObject argValue) {
		if (KEY.equalsIgnoreCase(argKey)) {
			addKey(argValue.toString());
		} else if (VALUE.equalsIgnoreCase(argKey)) {
			addValue(argValue.toString());
		} else {
			super.setConfigObject(argKey, argValue);
		}
	}

	@Override
	protected String getStatement() {
		StringBuffer statement = new StringBuffer();

		String name = getName();
		if (name.indexOf("@") < 0) {
			statement.append("@" + name);
		}

		if (!keys_.isEmpty()) {
			statement.append("(");
			for (int i = 0; i < keys_.size(); i++) {
				statement.append(keys_.get(i));
				statement.append("=");
				statement.append("\""+values_.get(i)+"\"");
				statement.append(",");
			}

			// remove the ","
			statement.setLength(statement.length() - 1);
			statement.append(")");
		}

		return statement.toString();
	}

	@Override
	public int hashCode() {
//		int hashCode = getName().hashCode();
//		for (int i=0; i<keys_.size(); i++) {
//			hashCode += (keys_.get(0).hashCode() + values_.get(0).hashCode());
//		}
		
		return getName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AnnotationConfig) {
			return false;
		}
		return getStatement().equals(((AnnotationConfig) obj).getStatement());
	}

	@Override
	public void validate() throws CodeGenException {
		if (keys_.size() != values_.size()) {
			throw new ImproperAttributeException(
					"The length for keys and values of the annotation to the "
							+ this.getParent().getSourceUrl());
		}
	}

	public List<String> getKeys() {
		return keys_;
	}

	public List<String> getValues() {
		return values_;
	}

	public void addKey(String argKey) {
		keys_.add(argKey);
	}

	public void addValue(String argValue) {
		values_.add(argValue);
	}
	
	public String getAttribute(String argKey){
		int index = keys_.indexOf(argKey);
		if (index >= 0) {
			return values_.get(index);
		} else {
			return EMPTY;
		}
	}
}
