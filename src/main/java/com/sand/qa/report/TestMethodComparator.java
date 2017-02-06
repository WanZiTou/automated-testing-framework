package com.sand.qa.report;

import java.util.Comparator;
import org.testng.ITestNGMethod;

/**
 * Method Comparator
 * 
 * @author nihuaiqing
 * @version 1.0.0
 */
class TestMethodComparator implements Comparator<ITestNGMethod> {
	public int compare(ITestNGMethod method1, ITestNGMethod method2) {
		int compare = method1.getRealClass().getName()
				.compareTo(method2.getRealClass().getName());
		if (compare == 0) {
			compare = method1.getMethodName()
					.compareTo(method2.getMethodName());
		}
		return compare;
	}
}
