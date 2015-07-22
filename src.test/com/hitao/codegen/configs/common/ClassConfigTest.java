package com.hitao.codegen.configs.common;

import com.hitao.codegen.access.ICodeGenerateStrategy;
import com.hitao.codegen.access.impl.CodeGenerateStrategyFactory;
import com.hitao.codegen.configs.IClassCodeGenConfig;
import com.hitao.codegen.configs.exception.CodeGenException;

public class ClassConfigTest {

	/**
	 * @param args
	 * @throws CodeGenException 
	 */
	public static void main(String[] args) throws CodeGenException {

		// 实例化一个类config，用来描述该类的详细信息
		IClassCodeGenConfig classConfig = new ClassConfig();

		/******************* 设置类的基本信息 **********************/
		// 设置包名
		classConfig.setPackagePath("com.taobao.test");
		// 设置类名
		classConfig.setName("CodeGenTest");
		// 设置类的修饰符
		classConfig.setModify("public");
		// 设置继承的类
		classConfig.setExtendsClass("java.util.ArrayList");
		// 设置实现的接口
		classConfig.setImplementsClasses("java.util.List");

		// 注释config
		CommentConfig classComment = new CommentConfig();
		classComment.setValue("类的说明信息");
		// 添加类注释
		classConfig.setConfigObject(classComment);

		/******************* 设置字段信息 **********************/
		// 增加属性
		FieldConfig nameField = new FieldConfig();
		// 属性类型
		nameField.setClassName("String");
		// 属性名称
		nameField.setName("name");

		// 注释config
		CommentConfig fieldComment = new CommentConfig();
		fieldComment.setValue("字段说明信息");
		// 添加字段注释
		nameField.setConfigObject(fieldComment);

		/******************* 设置方法信息 **********************/
		// 方法config
		MethodConfig methodConfig = new MethodConfig();
		// 设置方法的访问修饰符，属性和类都有该方法
		methodConfig.setModify("public");
		// 设置方法名称
		methodConfig.setName("showName");

		// 方法参数config
		ParameterConfig paramConfig = new ParameterConfig();
		// 设置参数类型
		paramConfig.setClassName("String");
		// 参数名称
		paramConfig.setName("name");
		// 方法增加参数
		methodConfig.setConfigObject(paramConfig);

		// 设置方法类容
		BodyConfig bodyConfig = new BodyConfig();
		bodyConfig.setValue("System.out.println(name)");
		methodConfig.setConfigObject(bodyConfig);

		// 增加属性和方法
		classConfig.setConfigObject(nameField);
		classConfig.setConfigObject(methodConfig);

		// 获得生成类的策略
		ICodeGenerateStrategy codeGenerateStrategy = CodeGenerateStrategyFactory
				.getInstance().getGenerateCodeStrategy(classConfig);
		// 生成类文件到指定目录
		codeGenerateStrategy.codeGenerate(classConfig, "D:\\");

	}

}
