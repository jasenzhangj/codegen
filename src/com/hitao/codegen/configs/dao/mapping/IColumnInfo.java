package com.hitao.codegen.configs.dao.mapping;

/***
 * This interface represents the information of the DB' column.
 * 
 * @author zhangjun.ht
 * @created 2011-1-15
 * @version $Id: IColumnInfo.java 12 2011-02-20 10:50:23Z guest $
 */
public interface IColumnInfo {

	/***
	 * get java class type.
	 * 
	 * @return java class type.
	 */
	String getJavaType();

	/***
	 * get the comment of the column.
	 * 
	 * @return the comment of the column.
	 */
	String getComment();

	/***
	 * get the field name.
	 * 
	 * @return the field name.
	 */
	String getFieldName();
	
	/***
	 * Get the column name.
	 * 
	 * @return the column name
	 */
	String getColumnName();
}
