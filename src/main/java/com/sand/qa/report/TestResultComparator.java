package com.sand.qa.report;

import java.util.Comparator;
import org.testng.ITestResult;

/**
 * Result Comparator
 * 
 * @author nihuaiqing
 * @version 1.0.0
 */
class TestResultComparator implements Comparator<ITestResult> {
	public int compare(ITestResult result1, ITestResult result2) {
		return result1.getName().compareTo(result2.getName());
	}
}
