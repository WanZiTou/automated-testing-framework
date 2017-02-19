package com.sand.qa.dataprovider;

import java.util.ArrayList;
import java.util.List;

/**
 * Data DataProvider Impl
 * 
 * @author lium
 */
public class DataProviderImpl implements IData {
	// 这里是读取的路径
	protected String defaultPathXml = "resources/testdata/xml/";
	
	protected String defaultPathCvs = "resources/testdata/cvs/";
	
	protected int start = 1;

	protected int max = Integer.MAX_VALUE;

	protected List<Object[]> data = new ArrayList<Object[]>();

	/**
	 * 从文件中获取数据
	 * 
	 * @param caseName
	 *            数据集名称
	 * @param dataFile
	 *            数据文件
	 */
	@Override
	public Object[][] getData(String caseName, String dataFile) {
		// 这里读数据
		generateData(caseName, dataFile);
		// 这里返回数据
		return generateArrayData();
	}

	/**
	 * 从文件中获取数据
	 * 
	 * @param caseName
	 *            数据集名称
	 * @param dataFile
	 *            数据文件
	 * @param startRowNum
	 *            起始的数据记录，默认为1
	 */
	@Override
	public Object[][] getData(String caseName, String dataFile, int startRowNum) {
		start = startRowNum;
		return getData(caseName, dataFile);
	}

	/**
	 * 从文件中获取数据
	 * 
	 * @param caseName
	 *            数据集名称
	 * @param dataFile
	 *            数据文件
	 * @param startRowNum
	 *            起始的数据记录，默认为1
	 * @param Length
	 *            获取数据的长度，默认为全部，包括起始数据
	 */
	@Override
	public Object[][] getData(String caseName, String dataFile,
			int startRowNum, int Length) {
		max = startRowNum + Length;
		return getData(caseName, dataFile, startRowNum);
	}

	public String getDefaultPathXml() {
		return defaultPathXml;
	}

	public void setDefaultPathXml(String defaultPathXml) {
		this.defaultPathXml = defaultPathXml;
	}

	public String getDefaultPathCvs() {
		return defaultPathCvs;
	}

	public void setDefaultPathCvs(String defaultPathCvs) {
		this.defaultPathCvs = defaultPathCvs;
	}

	/**
	 * 具体的文件驱动需要重写这个函数 可以看到为空，
	 */
	protected void generateData(String caseName, String dataFile) {
	}

	protected Object[][] generateArrayData() {
		int i = 0;
		Object[][] o = new Object[data.size()][];
		for (Object[] oo : data) {
			o[i++] = oo;
		}
		data.clear();
		return o;
	}
}
