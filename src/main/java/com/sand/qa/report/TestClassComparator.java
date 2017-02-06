package com.sand.qa.report;

import java.util.Comparator;
import org.testng.IClass;

/**
 * Test Class Comparator
 * 
 * @author nihuaiqing
 * @version 1.0.0
 */
class TestClassComparator implements Comparator<IClass> {
	public int compare(IClass class1, IClass class2) {
		return class1.getName().compareTo(class2.getName());
	}
}
