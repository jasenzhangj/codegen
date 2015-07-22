package com.hitao.codegen.configs.common;

import static com.hitao.codegen.util.StringUtils.EMPTY;

import com.hitao.codegen.configs.CodeGenUtils;
import com.hitao.codegen.util.StringUtils;

/***
 * import configuration.
 * 
 * @author zhangjun.ht
 * @created 2010-11-30
 * @version $Id: ImportConfig.java 25 2011-02-24 08:40:37Z guest $
 */
public class ImportConfig extends ClassElementConfig {

	private static final long serialVersionUID = -2804085601116384595L;

	public static final String MAIN_TAG = "import";

	public ImportConfig() {

	}

	public ImportConfig(String argImport) {
		setValue(argImport);
	}

	@Override
	public String getStatement() {
		return CodeGenUtils.getImportStatement(getImportValue());
	}

	protected String getImportValue() {
		String name = super.getName();

		if (StringUtils.isEmpty(name)) {
			name = super.getValue();
		}
		if (StringUtils.isEmpty(name)) {
			return EMPTY;
		}
		return name;
	}

	@Override
	public int hashCode() {
		return getStatement().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ImportConfig)) {
			return false;
		}

		return getStatement().equals(((ImportConfig) obj).getStatement());
	}

}
