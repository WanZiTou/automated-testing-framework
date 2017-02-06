package com.sand.qa.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Random;

import org.testng.annotations.DataProvider;

import com.sand.qa.dataprovider.ExcelBeanDataProvider;
import com.sand.qa.dataprovider.ExcelDataProvider;
import com.sand.qa.dataprovider.XmlDataProvider;

public class Base {

	// 初始化
	static {
		Logger.setLog();
		new InitProperties();
	}

	/**
	 * xml数据驱动调用
	 */
	@DataProvider(name = "xml")
	protected Object[][] xmlData(Method m) {
		return new XmlDataProvider().getData(m.getName(), m.getDeclaringClass()
				.getSimpleName() + ".xml");
	}

	/**
	 * excel数据驱动调用bean
	 */
	@DataProvider(name = "excel")
	protected Object[][] excelData(Method m) {
		// 约定测试方法名称：测试名_bean名，例如：addUser_User
		String[] s = m.getName().split("_");
		return new ExcelBeanDataProvider().getData(s[1], m.getDeclaringClass()
				.getSimpleName() + ".xls");
	}
	/**
	 * xls数据驱动调用map
	 * */
	@DataProvider(name = "xls")
	public Iterator<Object[]> dataFortestMethod(Method m) throws IOException {
		//将模块名称和用例的编号传给 ExcelDataProvider ，然后进行读取excel数据
		String moduleName=m.getDeclaringClass().getSimpleName();
		return new ExcelDataProvider(moduleName, m.getName());
	}
	/**
	 * 模拟键盘按键操作
	 * 
	 * @param key
	 *            of KeyEvent e.g. pressKey(KeyEvent.VK_ENTER) 点击回车键
	 * 
	 */
	public static void pressKey(int key) {
		try {
			Robot rob = new Robot();
			rob.keyPress(key);
			rob.keyRelease(key);
		} catch (AWTException e) {
		}
	}

	/**
	 * 设置等待时间 单位为秒
	 */
	public static void delay(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param type
	 *            所期望返回的随机数的类型，包括int，nint（负整数），double，ndouble（负double），char，
	 *            uchar（特殊字符）, china（中文）
	 * @param length
	 *            所期望返回的随机数的长度
	 * @return 随机数
	 * @throws Exception
	 */
	public static String getRandomData(String type, int length) {
		String data = "";
		String negativeData = "";
		String charData[] = { "!", "@", "#", "$", "%", "^", "&", "*" };
		if (type.equals("int")) {
			for (int i = 0; i < length - 1; i++) {
				data += (int) (10 * Math.random());
			}
			data = (int) (9 * Math.random() + 1) + data;
		} else if (type.equals("nint")) {
			for (int i = 0; i < length - 1; i++) {
				data += (int) (10 * Math.random());
			}
			data = "-" + (int) (9 * Math.random() + 1) + data;
		} else if (type.equals("double")) {
			for (int i = 0; i < length - 3; i++) {
				data += (int) (10 * Math.random());
			}
			for (int i = 0; i < 2; i++) {
				negativeData += (int) (10 * Math.random());
			}
			data = (int) (9 * Math.random() + 1) + data + "." + negativeData;
		} else if (type.equals("ndouble")) {
			for (int i = 0; i < length - 3; i++) {
				data += (int) (10 * Math.random());
			}
			for (int i = 0; i < 2; i++) {
				negativeData += (int) (10 * Math.random());
			}
			data = "-" + (int) (9 * Math.random() + 1) + data + "."
					+ negativeData;
		} else if (type.equals("char")) {
			for (int i = 0; i < length; i++) {
				data += String
						.valueOf((char) ('a' + (int) (Math.random() * 26)));
			}
		} else if (type.equals("uchar")) {
			for (int i = 0; i < length; i++) {
				Random rnd = new Random();
				data += charData[rnd.nextInt(8)];
			}
		} else if (type.equals("china")) {
			for (int i = 0; i < length; i++) {
				data += "中";
			}
		}
		return data;
	}

	/**
	 * 模拟粘贴文本
	 * 
	 * @param text
	 */
	public static void paste(String text) {
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(new StringSelection(text), null);
		try {
			Robot rob = new Robot();
			rob.keyPress(KeyEvent.VK_CONTROL);
			rob.keyPress(KeyEvent.VK_V);
			rob.keyRelease(KeyEvent.VK_CONTROL);
			rob.keyRelease(KeyEvent.VK_V);
		} catch (AWTException e) {
		}
	}

}
