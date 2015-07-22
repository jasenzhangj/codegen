package com.hitao.codegen.configs.append;

/***
 * The strategy to find the index to append specify codes in the file.
 *
 * @author  zhangjun.ht
 * @created 2011-3-9
 * @version $Id: IFindAppendIndexStrategy.java 45 2011-03-09 08:47:45Z guest $
 */
public interface IFindAppendIndexStrategy {

	/***
	 * Get index of the file to append the specify codes.
	 * @param argAllCodes the whole codes in the file.
	 * @return index of the file to append the specify codes.
	 */
	public int getAppendIndex(String argAllCodes);
}
