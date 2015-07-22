package com.hitao.codegen.constent;

/***
 * String constants.
 * 
 * @author zhangjun.ht
 * @created 2010-11-30
 * @version $Id: StringConstants.java 59 2011-05-23 05:47:35Z guest $
 */
public interface StringConstants {
	String SEMICOLON = ";";
	String DOUBLE_BACKSLASH = "//";
	String LINE_BREAK = System.getProperty("line.separator");
	String BLANK_LINE = System.getProperty("line.separator");
	char LINE_BREAK_CHAR = '\n';
	String ENTER = "\r";
	char ENTER_CHAR = '\r';
	String TAB = "\t";
	String DOUBLE_QUOTATION = "\"";

	/*************************************************************************/
	/** These variables below are the keys in SystemConfig.xml. **/
	/*************************************************************************/
	String CODEGEN_USE_DEFAULT_ECLIPSE_FORMATTER = "codegen.use.default.eclipse.formatter";
	String CODEGEN_CODE_FORMATTER_FILE = "codegen.code.formatter.file";

	String CODEGEN_CLASS_DEFAUL_MODIFY = "codegen.class.defaul.modify";
	String CODEGEN_METHOD_DEFAUL_MODIFY = "codegen.method.defaul.modify";
	String CODEGEN_FIELD_DEFAUL_MODIFY = "codegen.field.defaul.modify";

	String CODEGEN_DAO_IMPL_RELATIVE_DIRECTORY = "codegen.dao.impl.relative.directory";

	String CODEGEN_UNNEED_LOAD_CLASS = "codegen.unneed.load.class";
	String CODEGEN_DAO_DAO_UNITTEST_DEFAULT_EXTENDS_CLASS = "codegen.dao.dao.unittest.default.extends.class";
	String CODEGEN_DAO_DAOIMPL_DEFAULT_EXTENDS_CLASS = "codegen.dao.daoimpl.default.extends.class";
	String CODEGEN_DAO_DAOIMPL_DEFAULT_EXTENDS_CLASS_HAS_GENERIC = "codegen.dao.daoimpl.default.extends.class.has.generic";
	String CODEGEN_DAO_DAOIMPL_DBROUTE_STATIC_CLASS = "codegen.dao.daoimpl.dbroute.static.class";
	
	String CODEGEN_CONFIGURATION_FILE_SUFFIX = "codegen.configuration.file.suffix";

	String CODEGEN_DAO_IMPLEMENT_NAME_PREFIX = "codegen.dao.implement.name.prefix";
	String CODEGEN_DAO_IMPLEMENT_NAME_SUFFIX = "codegen.dao.implement.name.suffix";

	String CODEGEN_DEFAULT_CONFIG_INDIRECTORY = "codegen.default.config.indirectory";

	String CODEGEN_DAO_DAO_SUFFIX = "codegen.dao.dao.suffix";
	String CODEGEN_DAO_MANAGER_SUFFIX = "codegen.dao.manager.suffix";
	String CODEGEN_DAO_AO_SUFFIX = "codegen.dao.ao.suffix";
	String CODEGEN_DAO_DO_SUFFIX = "codegen.dao.do.suffix";
	String CODEGEN_DAO_PACKAGE_PATH = "codegen.dao.package.path";
	String CODEGEN_MANAGER_PACKAGE_PATH = "codegen.manager.package.path";
	String CODEGEN_AO_PACKAGE_PATH = "codegen.ao.package.path";
	String CODEGEN_DO_PACKAGE_PATH = "codegen.do.package.path";
	String CODEGEN_SQLMAP_PACKAGE_PATH = "codegen.sqlmap.package.path";

	String CODEGEN_PROJECT_NAME = "codegen.project.name";

	String CODEGEN_DAO_DEFAULT_EXCEPTION = "codegen.dao.default.exception";
	String CODEGEN_MANAGER_DEFAULT_EXCEPTION = "codegen.manager.default.exception";

	String CODEGEN_GENERATE_CODE_PATTERN_OVERRIDE = "codegen.generate.code.pattern.override";
	String CODEGEN_HOLD_ANNOTATIONS_FOR_DAO = "codegen.hold.annotations.for.dao";

	String CODEGEN_GENERATE_SET_AND_GET_COMMENTS = "codegen.generate.set.and.get.comments";

	String CODEGEN_UNNEED_IMPORT_CLASSES = "codegen.unneed.import.classes";

	String CODEGEN_PROJECT_SOURCE_FOLDER = "codegen.project.source.folder";
	String CODEGEN_PROJECT_UNITTEST_SOURCE_FOLDER = "codegen.project.unittest.source.folder";
	String CODEGEN_WEB_PROJECT_AUTOCONFIG_FOLDER = "codegen.web.project.autoconfig.folder";
	String CODEGEN_WEB_PROJECT_IBATIS_SQLMAP_CONFIG_DIR = "codegen.web.project.ibatis.sqlmap.config.dir";
	String CODEGEN_WEB_PROJECT_IBATIS_SQLMAP_CONFIG_NAME = "codegen.web.project.ibatis.sqlmap.config.name";
	String CODEGEN_CONFIG_FILE_ENCODING = "codegen.config.file.encoding";

	String CODEGEN_WEB_PROJECT_DAO_CONFIG_NAME = "codegen.web.project.dao.config.name";
	String CODEGEN_WEB_PROJECT_MANAGER_CONFIG_NAME = "codegen.web.project.manager.config.name";
	String CODEGEN_WEB_PROJECT_AO_CONFIG_NAME = "codegen.web.project.ao.config.name";
	
	String CODEGEN_PARAMETER_NAME_PREFIX_ARG = "codegen.parameter.name.prefix.arg";
	
	String CODEGEN_GENERATE_MANAGER_AND_MANAGERIMPL = "codegen.generate.manager.and.managerimpl";
	String CODEGEN_GENERATE_AO_AND_AOIMPL = "codegen.generate.ao.and.aoimpl";

	String CODEGEN_DAO_CONFIGS_CONFIG_CLASS = "codegen.dao.configs.config.class";
	String CODEGEN_HOLD_COMMENTS_FOR_IMPLEMENTS = "codegen.hold.comments.for.implements";

	String CODEGEN_AO_LOG_LOGGER_CLASS = "codegen.ao.log.logger.class";
	String CODEGEN_AO_LOG_LOGGERFACTORY_CLASS = "codegen.ao.log.loggerfactory.class";
}
