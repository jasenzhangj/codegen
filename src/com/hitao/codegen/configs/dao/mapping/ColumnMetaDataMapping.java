package com.hitao.codegen.configs.dao.mapping;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hitao.codegen.util.StringUtils;

/***
 * The utility class for get the columns information in specific table.
 *
 * @author zhangjun.ht
 * @created 2011-1-13
 * @version $Id: ColumnMetaDataMapping.java 52 2011-03-16 07:50:41Z guest $
 */
public class ColumnMetaDataMapping {

	private static final Logger logger_ = Logger
			.getLogger(ColumnMetaDataMapping.class);

	private static final String SYSTEM_COLUMN_METADATA_MAPPING_PROPERTY = ColumnMetaDataMapping.class
			.getName();
	private static ColumnMetaDataMapping INSTANCE_;

	static {
		String value = System
				.getProperty(SYSTEM_COLUMN_METADATA_MAPPING_PROPERTY);
		try {
			INSTANCE_ = (ColumnMetaDataMapping) Class.forName(value)
					.newInstance();
		} catch (Exception e) {
			INSTANCE_ = new ColumnMetaDataMapping();
		}
	}

	private static Map<Integer, String> typeMapingMap = null;

	static {
		typeMapingMap = new HashMap<Integer, String>();
		typeMapingMap.put(Types.BIT, "Byte");
		typeMapingMap.put(Types.TINYINT, "Integer");
		typeMapingMap.put(Types.SMALLINT, "Integer");
		typeMapingMap.put(Types.INTEGER, "Integer");
		typeMapingMap.put(Types.BIGINT, "Long");

		typeMapingMap.put(Types.FLOAT, "Float");
		typeMapingMap.put(Types.REAL, "Float");
		typeMapingMap.put(Types.DOUBLE, "Double");
		typeMapingMap.put(Types.NUMERIC, "Double");
		typeMapingMap.put(Types.DECIMAL, "Double");

		typeMapingMap.put(Types.CHAR, "String");
		typeMapingMap.put(Types.VARCHAR, "String");
		typeMapingMap.put(Types.LONGVARCHAR, "String");
		typeMapingMap.put(Types.NUMERIC, "String");
		typeMapingMap.put(Types.DECIMAL, "String");
		typeMapingMap.put(Types.CLOB, "String");

		typeMapingMap.put(Types.BOOLEAN, "Boolean");

		typeMapingMap.put(Types.DATE, "java.util.Date");
		typeMapingMap.put(Types.TIME, "java.util.Date");
		typeMapingMap.put(Types.TIMESTAMP, "java.util.Date");

		typeMapingMap.put(Types.BLOB, "Byte[]");
	}

	public static ColumnMetaDataMapping getInstance() {
		return INSTANCE_;
	}

	public List<IColumnInfo> getColumnsInfo(String argTable) {
		List<IColumnInfo> columnList = new ArrayList<IColumnInfo>();
		Connection connection = DataSourceManager.getInstance().getConnection();
		if (connection != null) {
			try {
				DatabaseMetaData dmd = connection.getMetaData();
				ResultSet set = dmd.getColumns(null, null, argTable, null);
				ColumnInfo columnInfo = null;
				while (set.next()) {
					columnInfo = new ColumnInfo();
					columnInfo.columnName = set.getString("COLUMN_NAME");
					columnInfo.javaType = getColumnJavaType(set.getInt("DATA_TYPE"));
					columnInfo.comment = set.getString("REMARKS");
					// set.getString("TYPE_NAME");
					columnList.add(columnInfo);
				}

			} catch (SQLException e) {
				logger_.warn("It's wrong to mapping " + argTable
						+ "'s columns information.", e);
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					logger_.error(
							"It make an error when close the connection with DB.",
							e);
				}
			}
		}
		return columnList;
	}

	public String getColumnJavaType(int argColumnType) {
		return typeMapingMap.containsKey(argColumnType) ? typeMapingMap
				.get(argColumnType) : "Object";
	}

	static class ColumnInfo implements IColumnInfo{
		private String javaType = null;
		private String comment = null;
		private String columnName = null;


		@Override
		public String getJavaType() {
			return javaType;
		}

		@Override
		public String getComment() {
			return comment;
		}

		@Override
		public String getFieldName() {
			return StringUtils.toCamelCase(columnName);
		}

		public String getColumnName() {
			return columnName;
		}
	}
}
