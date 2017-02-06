package com.sand.qa.listener;

import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestResult;

import com.sand.qa.util.Logger;

/**
 * TestFrameWork Main Listener
 * 
 * @author NiHuaiqing
 */
public class SeleniumListener implements IExecutionListener, ISuiteListener, IInvokedMethodListener{

	//测试开始监听
	@Override
	public void onExecutionStart() {
		Logger.Defaultlog("=======================================================");
		Logger.Defaultlog("                    测试框架执行开始");
		Logger.Defaultlog("=======================================================");
	}

	//测试结束监听
	@Override
	public void onExecutionFinish() {
		Logger.Defaultlog("=======================================================");
		Logger.Defaultlog("                    测试框架执行结束");
		Logger.Defaultlog("=======================================================");
	}

	@Override
	public void onStart(ISuite suite) {
		Logger.Defaultlog("测试套件【" + suite.getName() + "】执行开始");
	}

	@Override
	public void onFinish(ISuite suite) {
		Logger.Defaultlog("测试套件【" + suite.getName() + "】执行结束");
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		Logger.Defaultlog("测试方法【" + method.getTestMethod().getMethodName() + "】执行开始");
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		Logger.Defaultlog("测试方法【" + method.getTestMethod().getMethodName() + "】执行结束");
	}
	
}
