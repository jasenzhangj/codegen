package com.hitao.codegen.configs;

import static com.hitao.codegen.constent.StringConstants.CODEGEN_DEFAULT_CONFIG_INDIRECTORY;

import java.util.Collection;

import com.hitao.codegen.configs.basic.ConfigHelper;
import com.hitao.codegen.configs.basic.IConfigObject;
import com.hitao.codegen.configs.dao.AOConfig;
import com.hitao.codegen.configs.dao.AOImplConfig;
import com.hitao.codegen.configs.dao.CodeGenConfigSet;
import com.hitao.codegen.configs.dao.DOConfig;
import com.hitao.codegen.configs.dao.DaoConfig;
import com.hitao.codegen.configs.dao.DAOConfigs;
import com.hitao.codegen.configs.dao.DaoImplConfig;
import com.hitao.codegen.configs.dao.IDAOConfigs;
import com.hitao.codegen.configs.dao.ManagerConfig;
import com.hitao.codegen.configs.dao.ManagerImplConfig;
import com.hitao.codegen.util.StringUtils;

/***
 * The configuration helper of the codegen.
 *
 * @author zhangjun.ht
 * @created 2010-11-29
 * @version $Id: CodeGenConfigHelper.java 44 2011-03-08 08:55:34Z guest $
 */
public class CodeGenConfigHelper extends ConfigHelper<CodeGenConfigSet>{


	// the directory of all configuration files.
	private String dir_ = null;
	//private Collection<DaoGenConfig> daoGenList_ = new ArrayList<DaoGenConfig>();

	public CodeGenConfigHelper(String argDir) {
		if (argDir == null) {
			dir_ = DAOServiceConfigurationManager.getProperty(CODEGEN_DEFAULT_CONFIG_INDIRECTORY);
		} else {
			dir_ = argDir;
		}
	}

	@Override
	protected String getConfigFileName() {
		return dir_;
	}

	@Override
	protected boolean isDirectoryBased() {
		return true;
	}


	@Override
	protected IConfigObject getConfigObject(String argTagName, String argDtype,
			String argSourceDescription) {
		if (CodeGenConfigSet.MAIN_TAG.equalsIgnoreCase(argTagName)) {
			return new CodeGenConfigSet();
		} else if (DAOConfigs.MAIN_TAG.equalsIgnoreCase(argTagName)) {
			return new DAOConfigs();
		} else if (DaoImplConfig.MAIN_TAG.equalsIgnoreCase(argTagName)) {
			return new DaoImplConfig();
		} else if (DaoConfig.MAIN_TAG.equalsIgnoreCase(argTagName)) {
			return new DaoConfig();
		} else if (ManagerImplConfig.MAIN_TAG.equalsIgnoreCase(argTagName)) {
			return new ManagerImplConfig();
		} else if (ManagerConfig.MAIN_TAG.equalsIgnoreCase(argTagName)) {
			return new ManagerConfig();
		} else if (AOImplConfig.MAIN_TAG.equalsIgnoreCase(argTagName)) {
			return new AOImplConfig();
		} else if (AOConfig.MAIN_TAG.equalsIgnoreCase(argTagName)) {
			return new AOConfig();
		} else if (DOConfig.MAIN_TAG.equalsIgnoreCase(argTagName)) {
			return new DOConfig();
		} else {
			return super.getConfigObject(argTagName, argDtype,
					argSourceDescription);
		}
	}

	/***
	 * get DaoGenConfig list. If daoGenList_
	 * @return
	 */
	public Collection<IDAOConfigs> getDaoGens(){
		return getRootChildren(CodeGenConfigSet.class);
	}

	/***
	 * Add DAOGenConfig object.
	 * this method is invoked in annotation strategy.
	 * @param argDaoGenConfig
	 * @see com.hitao.codegen.access.impl.AnnotationDaoGenStrategy
	 */
	public void addDaoGenConfig(IDAOConfigs argDaoGenConfig){
		getRootChildren(CodeGenConfigSet.class).add(argDaoGenConfig);
	}

	public ICodeGenConfig getDaoGenByName(String argClassName){
		Collection<IDAOConfigs> daoGens = getDaoGens();
		ICodeGenConfig temp = null;
		if (!StringUtils.isEmpty(argClassName)){
			for (IDAOConfigs daoGen : daoGens){
				temp = daoGen.getConfigByName(argClassName);
				if (temp != null){
					break;
				}
			}
		}
		return temp;
	}
}
