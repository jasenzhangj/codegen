package com.hitao.codegen.expand;

import static com.hitao.codegen.constent.StringConstants.BLANK_LINE;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_DO_PACKAGE_PATH;
import static com.hitao.codegen.constent.StringConstants.CODEGEN_SQLMAP_PACKAGE_PATH;

import com.hitao.codegen.configs.CodeGenUtils;
import com.hitao.codegen.configs.IClassCodeGenConfig;
import com.hitao.codegen.configs.ICodeGenConfig;
import com.hitao.codegen.configs.common.AbstractCodeGenConfig.GenerateModel;
import com.hitao.codegen.configs.common.AnnotationConfig;
import com.hitao.codegen.configs.common.BodyConfig;
import com.hitao.codegen.configs.common.FieldConfig;
import com.hitao.codegen.configs.common.ImportConfig;
import com.hitao.codegen.configs.common.MethodConfig;
import com.hitao.codegen.configs.dao.DAOConfigs;
import com.hitao.codegen.configs.dao.IDAOConfigs;
import com.hitao.codegen.configs.exception.ImproperAttributeException;

/***
 * This configuration object is for Hitao ERP project
 * 
 * @author zhangjun.ht
 * @created 2011-6-16 ÏÂÎç02:45:22
 * @version $Id$
 */
public class ErpDaoConfigs extends DAOConfigs implements IDAOConfigs {

	private static final long serialVersionUID = 3284967451247441675L;

	@Override
	protected void buildDO() throws ImproperAttributeException {
		if (do_ != null) {
			do_.setInitial(false);

			// create toString method
			MethodConfig method = new MethodConfig();
			method.setName("toString");
			method.setReturnType("String");

			BodyConfig methodBody = new BodyConfig();
			StringBuffer body = new StringBuffer();
			body.append("	MapBuilder mb = new MapBuilder();");
			body.append(BLANK_LINE);
			for (FieldConfig field : do_.getFieldList()) {
				body.append("	mb.append(\"" + field.getName() + "\", "
						+ field.getName() + ");");
				body.append(BLANK_LINE);
			}
			body.append(BLANK_LINE);
			body.append("	return new ToStringBuilder().append(\""
					+ do_.getName() + "\").append(mb).toString();");
			methodBody.setValue(body.toString());
			method.setConfigObject(methodBody);

			AnnotationConfig an = new AnnotationConfig();
			an.setName("Override");
			method.setConfigObject(an);

			method.setConfigObject(new ImportConfig(
					"com.alibaba.citrus.util.internal.ToStringBuilder"));
			method.setConfigObject(new ImportConfig(
					"com.alibaba.citrus.util.internal.ToStringBuilder.MapBuilder"));
			do_.setConfigObject(method);
		}
	}

	@Override
	protected String getModelPackage(String argPackageKey) {
		if (CODEGEN_SQLMAP_PACKAGE_PATH.equals(argPackageKey)) {
			if (dao_ != null) {
				return CodeGenUtils.getRelativePackageForClass(
						dao_.getPackagePath(), CODEGEN_SQLMAP_PACKAGE_PATH,
						false, true);
			} else if (do_ != null) {
				return CodeGenUtils.getRelativePackageForClassByKey(
						do_.getPackagePath(), CODEGEN_DO_PACKAGE_PATH,
						argPackageKey, false, true);
			} else {
				return super.getModelPackage(argPackageKey);
			}
		} else {
			return super.getModelPackage(argPackageKey);
		}
	}

	@Override
	protected ICodeGenConfig buildSpringXmlConfig(
			IClassCodeGenConfig argInterfaceConfig,
			IClassCodeGenConfig argImplementConfig, String argFileName , GenerateModel argGenerateModel) {
		ICodeGenConfig config = super.buildSpringXmlConfig(argInterfaceConfig,
				argImplementConfig, argFileName, argGenerateModel);

		// if it will genernate dal-dao.xml, reset the package path
		if (argInterfaceConfig == dao_) {
			config.setPackagePath("src.main.resources.dal");
		}

		return config;
	}

	@Override
	protected void buildDaoUnitTestConfig() throws ImproperAttributeException {
		super.buildDaoUnitTestConfig();
		String packagePath = daoUnitTestConfig_.getPackagePath();
		if (packagePath.indexOf("dao.") > -1) {
			daoUnitTestConfig_.setPackagePath(packagePath
					.replaceAll("dao.", ""));
		} else {
			daoUnitTestConfig_.setPackagePath(packagePath
					.replaceAll(".dao", ""));
		}
	}
}
