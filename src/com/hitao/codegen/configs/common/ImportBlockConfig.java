package com.hitao.codegen.configs.common;

import static com.hitao.codegen.constent.StringConstants.ENTER;
import static com.hitao.codegen.constent.StringConstants.LINE_BREAK;
import static com.hitao.codegen.constent.StringConstants.TAB;
import static com.hitao.codegen.util.StringUtils.EMPTY;

import java.util.HashSet;
import java.util.Set;

import com.hitao.codegen.configs.CodeGenUtils;
import com.hitao.codegen.util.StringUtils;

/***
 * The import block is to import many import statements one time.
 *
 * @author zhangjun.ht
 * @created 2010-12-2
 * @version $Id: ImportBlockConfig.java 12 2011-02-20 10:50:23Z guest $
 */
public class ImportBlockConfig extends ImportConfig {

    private static final long serialVersionUID = 5468319078610099886L;

	public static final String MAIN_TAG = "importblock";

	/***
	 * Get import configuration list from the import block configuration.
	 * @return import configuration list.
	 */
	public Set<ImportConfig> getImportList() {
		Set<ImportConfig> importList = new HashSet<ImportConfig>();
		String value = getValue();
		if (!StringUtils.isEmpty(value)) {
			ImportConfig importConfig = null;
			value = value.replaceAll(LINE_BREAK, EMPTY);
			value = value.replaceAll(ENTER, EMPTY);
			value = value.replaceAll(TAB, EMPTY);
			String[] blocks = StringUtils.split(value, ';');
			String temp;
			for (String im : blocks) {
				temp = im.trim();
				if (StringUtils.isEmpty(temp)){
					continue;
				}
				importConfig = new ImportConfig();
				importConfig.setValue(CodeGenUtils.getImportStatement(temp));
				importList.add(importConfig);
			}
		}

		return importList;
	}
}
