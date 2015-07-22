package com.hitao.codegen.configs.append;

/***
 * The information to appending codes.
 *
 * @author  zhangjun.ht
 * @created 2011-3-9
 * @version $Id: IAppendCodesInfo.java 45 2011-03-09 08:47:45Z guest $
 */
public interface IAppendCodesInfo {

	/***
	 * Get break line.
	 * @return
	 */
	String getBreakLine();
	
	/***
	 * Set break line.
	 * @param argBreakLine
	 */
	void setBreakLine(String argBreakLine);
	
	/***
	 * Get white space
	 * @return
	 */
	String getWhiteSpace();
	
	/***
	 * Set white space
	 * @param argWhiteSpace
	 */
	void setWhiteSpace(String argWhiteSpace);
	
	/***
	 * add default strategy for finding the index of file to append codes.
	 * @param argElement
	 */
	void addFindAppendIndexStrategy(String argElement);
	
	/***
	 * add strategy which check the first element in the file.
	 * @param argElement
	 */
	void addFindAppendIndexStrategyByStartsWith(String argEndElement);
	
	/***
	 * Add the specific strategy.
	 * @param argIndex
	 * @param argFindAppendIndexStrategy
	 */
	void addFindAppendIndexStrategyByStartsWith(int argIndex, IFindAppendIndexStrategy argFindAppendIndexStrategy);
	
	/***
	 * Get the codes which check the appending codes whether it should be appended.
	 * @return
	 */
	String getCheckConditionCodes();
	
	/***
	 * Set the code for checking the condition whether codes should be appended.
	 * @param argCheckConditionCodes
	 */
	void setCheckConditionCodes(String argCheckConditionCodes);
	
	/***
	 * Check whether the codes should be appended.
	 * @param argAllCodes
	 * @return true if should, or else false.
	 */
	boolean shouldAppendCodes(String argAllCodes);
	
	/***
	 * Get the codes which should be appended.
	 * @return
	 */
	String getAppendCodes();
	
	/***
	 * Set the codes which should be appended.
	 * @param appendCodes
	 */
	void setAppendCodes(String appendCodes);
	
	/***
	 * Get the index which should be append.
	 * @param argAllCodes
	 * @return
	 */
	int getAppendIndex(String argAllCodes);
	
}
