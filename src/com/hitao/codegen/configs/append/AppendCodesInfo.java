package com.hitao.codegen.configs.append;

import static com.hitao.codegen.constent.StringConstants.BLANK_LINE;
import static com.hitao.codegen.util.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.List;

import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.util.StringUtils;

/***
 * The information for appending codes.
 * 
 * @author zhangjun.ht
 * @created 2011-3-9
 * @version $Id: AppendCodesInfo.java 45 2011-03-09 08:47:45Z guest $
 */
public class AppendCodesInfo implements IAppendCodesInfo {

	// the break line for the appending codes.
	private String breakLine_ = BLANK_LINE;
	private String whiteSpace_ = "\t";
	// this codes is to check whether the codes has been existed in file.
	private String checkConditionCodes_ = EMPTY;
	// the codes should be appended.
	private String appendCodes_ = EMPTY;

	// the default strategy to find the index of the end element.
	private List<IFindAppendIndexStrategy> list_ = new ArrayList<IFindAppendIndexStrategy>();

	public AppendCodesInfo() {

	}

	public AppendCodesInfo(ICodeGenConfig argCodeGenConfig) {
		StringBuffer statement = new StringBuffer();
		argCodeGenConfig.getCodePieceInfo(statement);
		String codes = statement.toString();
		addFindAppendIndexStrategy(argCodeGenConfig.getEndElement());
		appendCodes_ = codes;
		checkConditionCodes_ = codes;
	}

	public AppendCodesInfo(ICodeGenConfig argCodeGenConfig, String argCodes) {
		addFindAppendIndexStrategy(argCodeGenConfig.getEndElement());
		appendCodes_ = argCodes;
		checkConditionCodes_ = argCodes;
	}

	@Override
	public String getBreakLine() {
		return breakLine_;
	}

	@Override
	public void setBreakLine(String argBreakLine) {
		this.breakLine_ = argBreakLine;
	}

	@Override
	public String getWhiteSpace() {
		return whiteSpace_;
	}

	@Override
	public void setWhiteSpace(String argWhiteSpace) {
		this.whiteSpace_ = argWhiteSpace;
	}

	@Override
	public void addFindAppendIndexStrategy(String argElement) {
		if (argElement != null) {
			IFindAppendIndexStrategy strategy = new FindAppendIndexByEndsWithStrategy(
					argElement);
			list_.add(strategy);
		}
	}

	@Override
	public void addFindAppendIndexStrategyByStartsWith(String argEndElement) {
		if (argEndElement != null) {
			IFindAppendIndexStrategy strategy = new FindAppendIndexByStartsWithStrategy(
					argEndElement);
			list_.add(strategy);
		}
	}

	@Override
	public void addFindAppendIndexStrategyByStartsWith(int argIndex,
			IFindAppendIndexStrategy argFindAppendIndexStrategy) {
		if (argFindAppendIndexStrategy != null) {
			list_.add(argIndex, argFindAppendIndexStrategy);
		}
	}

	@Override
	public String getCheckConditionCodes() {
		return checkConditionCodes_;
	}

	@Override
	public void setCheckConditionCodes(String argCheckConditionCodes) {
		this.checkConditionCodes_ = argCheckConditionCodes;
	}

	@Override
	public boolean shouldAppendCodes(String argAllCodes) {
		return !argAllCodes.contains(checkConditionCodes_);
	}

	@Override
	public String getAppendCodes() {
		if (StringUtils.isEmpty(appendCodes_)) {
			appendCodes_ = checkConditionCodes_;
		}
		return appendCodes_;
	}

	@Override
	public int getAppendIndex(String argAllCodes) {
		int index = -1;
		if (argAllCodes != null) {
			for (IFindAppendIndexStrategy strategy : list_) {
				index = strategy.getAppendIndex(argAllCodes);
				if (index != -1) {
					break;
				}
			}
		}
		return index;
	}

	@Override
	public void setAppendCodes(String appendCodes) {
		this.appendCodes_ = appendCodes;
	}

	@Override
	public int hashCode() {
		return checkConditionCodes_.hashCode() + 29;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AppendCodesInfo)) {
			return false;
		}
		return this.getCheckConditionCodes().equals(
				((AppendCodesInfo) obj).getCheckConditionCodes());
	}

	@Override
	public String toString() {
		return getCheckConditionCodes();
	}

	private abstract class AbstractFindAppendIndexStrategy implements
			IFindAppendIndexStrategy {

		// the position for appending the codes.
		private String endElement_;

		public AbstractFindAppendIndexStrategy(String argEndElement) {
			endElement_ = argEndElement;
		}

		public String getEndElement() {
			return endElement_;
		}

		@Override
		public String toString() {
			return endElement_;
		}
	}

	/***
	 * Find the end element at first.
	 * 
	 * @author zhangjun.ht
	 * @created 2011-3-9
	 * @version $Id: AppendCodesInfo.java 45 2011-03-09 08:47:45Z guest $
	 */
	private class FindAppendIndexByStartsWithStrategy extends
			AbstractFindAppendIndexStrategy {

		public FindAppendIndexByStartsWithStrategy(String argEndElement) {
			super(argEndElement);
		}

		@Override
		public int getAppendIndex(String argAllCodes) {
			return argAllCodes.indexOf(getEndElement());
		}

	}

	/***
	 * Find the end element at last.
	 * 
	 * @author zhangjun.ht
	 * @created 2011-3-9
	 * @version $Id: AppendCodesInfo.java 45 2011-03-09 08:47:45Z guest $
	 */
	private class FindAppendIndexByEndsWithStrategy extends
			AbstractFindAppendIndexStrategy {

		public FindAppendIndexByEndsWithStrategy(String argEndElement) {
			super(argEndElement);
		}

		@Override
		public int getAppendIndex(String argAllCodes) {
			return argAllCodes.lastIndexOf(getEndElement());
		}
	}
}
