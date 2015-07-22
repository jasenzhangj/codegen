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

		// ʵ����һ����config�����������������ϸ��Ϣ
		IClassCodeGenConfig classConfig = new ClassConfig();

		/******************* ������Ļ�����Ϣ **********************/
		// ���ð���
		classConfig.setPackagePath("com.taobao.test");
		// ��������
		classConfig.setName("CodeGenTest");
		// ����������η�
		classConfig.setModify("public");
		// ���ü̳е���
		classConfig.setExtendsClass("java.util.ArrayList");
		// ����ʵ�ֵĽӿ�
		classConfig.setImplementsClasses("java.util.List");

		// ע��config
		CommentConfig classComment = new CommentConfig();
		classComment.setValue("���˵����Ϣ");
		// �����ע��
		classConfig.setConfigObject(classComment);

		/******************* �����ֶ���Ϣ **********************/
		// ��������
		FieldConfig nameField = new FieldConfig();
		// ��������
		nameField.setClassName("String");
		// ��������
		nameField.setName("name");

		// ע��config
		CommentConfig fieldComment = new CommentConfig();
		fieldComment.setValue("�ֶ�˵����Ϣ");
		// ����ֶ�ע��
		nameField.setConfigObject(fieldComment);

		/******************* ���÷�����Ϣ **********************/
		// ����config
		MethodConfig methodConfig = new MethodConfig();
		// ���÷����ķ������η������Ժ��඼�и÷���
		methodConfig.setModify("public");
		// ���÷�������
		methodConfig.setName("showName");

		// ��������config
		ParameterConfig paramConfig = new ParameterConfig();
		// ���ò�������
		paramConfig.setClassName("String");
		// ��������
		paramConfig.setName("name");
		// �������Ӳ���
		methodConfig.setConfigObject(paramConfig);

		// ���÷�������
		BodyConfig bodyConfig = new BodyConfig();
		bodyConfig.setValue("System.out.println(name)");
		methodConfig.setConfigObject(bodyConfig);

		// �������Ժͷ���
		classConfig.setConfigObject(nameField);
		classConfig.setConfigObject(methodConfig);

		// ���������Ĳ���
		ICodeGenerateStrategy codeGenerateStrategy = CodeGenerateStrategyFactory
				.getInstance().getGenerateCodeStrategy(classConfig);
		// �������ļ���ָ��Ŀ¼
		codeGenerateStrategy.codeGenerate(classConfig, "D:\\");

	}

}
