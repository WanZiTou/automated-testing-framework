package com.sand.qa.dataprovider;

/**
 * File To DataProvider Interface
 * 
 * @author lium
 */
public interface IData {

	public Object[][] getData(String caseName, String dataFile);

	public Object[][] getData(String caseName, String dataFile, int startRowNum);

	public Object[][] getData(String caseName, String dataFile, int beginNum, int endNum);

}
