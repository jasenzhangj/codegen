package com.hitao.codegen.configs.common;

import static com.hitao.codegen.constent.StringConstants.DOUBLE_BACKSLASH;
import static com.hitao.codegen.constent.StringConstants.ENTER_CHAR;
import static com.hitao.codegen.constent.StringConstants.LINE_BREAK_CHAR;
import static com.hitao.codegen.util.StringUtils.EMPTY;

import com.hitao.codegen.util.StringUtils;

/**
 * Comment configuration for class, method or field.
 * 
 * @author zhangjun.ht
 * @created 2010-11-30
 * @version $Id: CommentConfig.java 34 2011-03-02 07:23:58Z guest $
 */
public class CommentConfig extends ClassElementConfig {

	private static final long serialVersionUID = -8964706376694636182L;

	public static final String MAIN_TAG = "comment";

	// whether to generate the comments as original.
	private boolean isOriginal = false;

	@Override
	public String getStatement() {
		String value = getValue();

		if (isOriginal) {
			return value;
		}

		if (StringUtils.isEmpty(value)) {
			return EMPTY;
		}

		if (value.indexOf("\r\n") > 0) {
			setValue(StringUtils.replaceAll(new StringBuffer(value), "\r\n",
					"\n").toString());
		}

		String comment = getCommentgenerateStrategy().getComments();

		return filterLineBreaker(comment);
	}

	/***
	 * Filter line breaker.
	 * 
	 * @param argComment
	 * @return
	 */
	protected String filterLineBreaker(String argComment) {
		if (argComment.indexOf(LINE_BREAK_CHAR) < 0
				&& argComment.indexOf(ENTER_CHAR) < 0) {
			return argComment;
		}

		StringBuffer statement = new StringBuffer();
		char tempChar;
		for (int i = 0; i < argComment.length(); i++) {
			tempChar = argComment.charAt(i);
			if (tempChar == LINE_BREAK_CHAR || tempChar == ENTER_CHAR) {
				if (isGeneratedForFieldConfig()) {
					statement.append(tempChar + DOUBLE_BACKSLASH);
				} else {
					// I don't know why the comments can't be formatted. So i
					// write the spaces directly.
					statement.append(tempChar + " *            ");
				}
			} else {
				statement.append(tempChar);
			}
		}
		return statement.toString();
	}

	protected CommentgenerateStrategy getCommentgenerateStrategy() {
		if (isGeneratedForFieldConfig()) {
			return new FieldCommentgenerateStrategy(this);
		} else {
			return new DefaultCommentgenerateStrategy(this);
		}

	}

	protected boolean isGeneratedForFieldConfig() {
		return getParent() instanceof FieldConfig;
	}

	protected boolean isGeneratedForMethodConfig() {
		return getParent() instanceof FieldConfig;
	}

	public boolean isOriginal() {
		return isOriginal;
	}

	public void setOriginal(boolean isOriginal) {
		this.isOriginal = isOriginal;
	}

	/***
	 * The interface for generate the comments.
	 * 
	 * @author zhangjun.ht
	 * @created 2011-2-13
	 * @version $Id: CommentConfig.java 34 2011-03-02 07:23:58Z guest $
	 */
	protected interface CommentgenerateStrategy {
		public String getComments();
	}

	protected abstract class AbstractCommentgenerateStrategy implements
			CommentgenerateStrategy {
		protected CommentConfig comment = null;

		public AbstractCommentgenerateStrategy(CommentConfig comment) {
			super();
			this.comment = comment;
		}

	}

	/***
	 * Default strategy for generate the comments.
	 * 
	 * @author zhangjun.ht
	 * @created 2011-2-13
	 * @version $Id: CommentConfig.java 34 2011-03-02 07:23:58Z guest $
	 */
	protected class DefaultCommentgenerateStrategy extends
			AbstractCommentgenerateStrategy {

		public DefaultCommentgenerateStrategy(CommentConfig comment) {
			super(comment);
		}

		@Override
		public String getComments() {
			return StringUtils.nonNull(getValue());
		}

	}

	/***
	 * Strategy for generate the field's comments.
	 * 
	 * @author zhangjun.ht
	 * @created 2011-2-13
	 * @version Id
	 */
	protected class FieldCommentgenerateStrategy extends
			AbstractCommentgenerateStrategy {

		public FieldCommentgenerateStrategy(CommentConfig comment) {
			super(comment);
		}

		@Override
		public String getComments() {
			StringBuffer statement = new StringBuffer();
			statement.append(DOUBLE_BACKSLASH + " ");
			statement.append(StringUtils.nonNull(getValue()));
			return statement.toString();
		}

	}

}
