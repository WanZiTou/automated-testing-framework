package com.sand.qa.listener;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.sand.qa.util.Logger;
import com.sand.qa.webdriver.DriverBase;

/**
 * TestFrameWork Listener Adapter
 * 
 * @author Nihuaiqing
 */
public class SeleniumListenerAdapter extends TestListenerAdapter {

	// 测试集开始监听
	@Override
	public void onStart(ITestContext testContext) {
		super.onStart(testContext);
		Logger.Defaultlog(String.format("测试集【%s】执行开始 ", testContext.getName()));
	}

	// 测试集结束监听
	@Override
	public void onFinish(ITestContext testContext) {
		super.onFinish(testContext);
		Logger.Defaultlog(String.format("测试集【%s】执行结束", testContext.getName()));
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		Logger.log("=======================================================");
		Logger.log("测试方法 【" + tr.getName() + "】 执行失败");
		if (DriverBase.driver != null) {
			// 截图
			String name = tr.getName() + "_"
					+ String.valueOf(System.currentTimeMillis());
			DriverBase.driver.screenShot(name);
			Logger.log("请参考：<a href=\"../screenshot/" + name + ".jpg"
					+ "\" style=\"color:red;\">" + name + ".jpg"
					+ "</font></a>");
		}
		Logger.log("失败原因为：" + tr.getThrowable().getMessage());
		Logger.log("=======================================================");
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		super.onTestSkipped(tr);
		Logger.log("=======================================================");
		Logger.log("测试方法 【" + tr.getName() + "】 执行跳过");
		Logger.log("=======================================================");
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		super.onTestSuccess(tr);
		Logger.log("=======================================================");
		Logger.log("测试方法 【" + tr.getName() + "】 执行成功！");
		Logger.log("=======================================================");
	}

}
