package com.hitao.codegen.configs;


/****
 * The piece of codes in class. Maybe it is a property, comment or method and so
 * on.
 * 
 * @author zhangjun.ht
 * @created 2010-11-26
 * @version $Id: ICodePieceInfo.java 29 2011-03-01 01:47:28Z guest $
 */
public interface ICodePieceInfo {
	StringBuffer getCodePieceInfo(StringBuffer argStringBuffer);
}
