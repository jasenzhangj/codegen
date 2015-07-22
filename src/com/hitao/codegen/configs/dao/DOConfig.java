package com.hitao.codegen.configs.dao;

import static com.hitao.codegen.util.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.List;

import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.basic.IConfigObject;
import com.hitao.codegen.configs.common.ClassConfig;
import com.hitao.codegen.configs.common.CommentConfig;
import com.hitao.codegen.configs.common.FieldConfig;
import com.hitao.codegen.configs.dao.mapping.IColumnInfo;
import com.hitao.codegen.util.StringUtils;

/***
 * DO configuration
 *
 * @author zhangjun.ht
 * @created 2010-11-30
 * @version $Id: DOConfig.java 45 2011-03-09 08:47:45Z guest $
 */
public class DOConfig extends ClassConfig {

	private static final long serialVersionUID = 1L;

	public static final String MAIN_TAG = "do";

	// The table field is the mapping between DB and DO.
	private static final String TABLE = "table";
	
	// DAO name
	private static final String DAO_NAME = "daoName";

	private String table_ = EMPTY;
	private String daoName_ = EMPTY;
	
	private List<IColumnInfo> columnList_ = new ArrayList<IColumnInfo>();
	
	private SqlMapXmlConfig sqlMap_ = null;

	@Override
	public void setConfigObject(String argKey, IConfigObject argValue) {
		if (TABLE.equalsIgnoreCase(argKey)) {
			setTable(argValue.toString());
		} else if (DAO_NAME.equals(argKey)) {
			setDaoName(argValue.toString());
		} else {
			super.setConfigObject(argKey, argValue);
		}
	}

	@Override
	protected String getStatement() {
		addFieldsFromTable();
		return super.getStatement();
	}

	/**
	 * Add fields from table.
	 */
	protected void addFieldsFromTable() {
		for (IColumnInfo column : getColumnList()) {
			FieldConfig field = new FieldConfig();
			field.setClassName(column.getJavaType());
			field.setName(column.getFieldName());
			if (!StringUtils.isEmpty(column.getComment())) {
				CommentConfig comment = new CommentConfig();
				comment.setValue(column.getComment());
				field.setConfigObject(CommentConfig.MAIN_TAG, comment);
			}
			
			this.setConfigObject(FieldConfig.MAIN_TAG, field);
		}
	}

	public String getTable() {
		return table_;
	}

	public void setTable(String table) {
		this.table_ = table;
	}
	
	public List<IColumnInfo> getColumnList() {
		return columnList_;
	}

	public void setColumnList(List<IColumnInfo> columnList) {
		this.columnList_ = columnList;
	}
	
	public String getDaoName() {
		return daoName_;
	}

	public void setDaoName(String daoName) {
		this.daoName_ = daoName;
	}

	public ICodeGenConfig getSqlMapXmlConfig() {
		if (sqlMap_ != null) {
			return sqlMap_;
		}
		// add SQL map configuration
		sqlMap_ = new SqlMapXmlConfig();
		sqlMap_.setTable(getTable());
		sqlMap_.setDaoName(getDaoName());
		sqlMap_.setDoClassName(getClassName());
		sqlMap_.setColumnList(columnList_);
		return sqlMap_;
	}
}
