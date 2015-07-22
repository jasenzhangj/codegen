package com.hitao.codegen.access;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.hitao.codegen.configs.CodeGenConfigHelper;
import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.dao.IDAOConfigs;
import com.hitao.codegen.configs.exception.CodeGenException;

/***
 * The strategy interface for code generation.
 * 
 * @author zhangjun.ht
 * @created 2011-1-13
 * @version $Id: IDaoGenStrategy.java 58 2011-05-06 06:40:39Z guest $
 */
public interface IDaoGenStrategy {

	/***
	 * get DAO configuration object list.
	 * 
	 * @return
	 */
	Collection<IDAOConfigs> getDaoConfigs() throws CodeGenException;

	/***
	 * get configuration helper.
	 * 
	 * @return
	 * @throws CodeGenException
	 */
	CodeGenConfigHelper getCodeGenConfigHelper() throws CodeGenException;

	/***
	 * get configuration object by name.
	 * 
	 * @param argClassName
	 * @return
	 * @throws CodeGenException
	 */
	ICodeGenConfig getDaoGenByName(String argClassName)
			throws CodeGenException;

	/***
	 * get the output directory for the DAO by string type.
	 * 
	 * @return
	 */
	String getDaoStringInDir();

	/***
	 * get the output directory for the Manager by string type.
	 * 
	 * @return
	 */
	String getManagerStringOutDir();

	/***
	 * get the output directory for the AO by string type.
	 * 
	 * @return
	 */
	String getAoStringOutDir();

	/***
	 * get the output directory for the DO by string type.
	 * 
	 * @return
	 */
	String getDoStringOutDir();
	
	/***
	 * get the output directory for the SQL map by string type.
	 * 
	 * @return
	 */
	String getSqlMapStringOutDir();
	
	/***
	 * get the output directory for the web project by string type.
	 * 
	 * @return
	 */
	String getWebProjectStringOutDir();
	
	/***
	 * get the output directory for the DAO unit test.
	 * 
	 * @return output directory for the DAO unit test.
	 */
	String getDaoUnitTestStringOutDir();
	
	/***
	 * Set the biz project directory.
	 * @param argBizProjectDir
	 */
	public void setBizProjectDir(File argBizProjectDir) throws IOException;

	/***
	 * Set the Dal project directory.
	 * @param argBizProjectDir
	 */
	public void setDalProjectDir(File argDalProjectDir) throws IOException;
	
	/***
	 * Set the web project directory.
	 * @param argBizProjectDir
	 */
	public void setWebProjectDir(File argWebProjectDir) throws IOException;

}
