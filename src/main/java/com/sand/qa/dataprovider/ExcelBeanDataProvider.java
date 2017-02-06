package com.sand.qa.dataprovider;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import jxl.Sheet;
import jxl.Workbook;

public class ExcelBeanDataProvider extends DataProviderImpl {

	public final String beanUrl = "com.sand.selenium.bean.";

	/**
	 * 重写 generateData 方法
	 * 
	 * @param caseName
	 *            方法名称
	 * @param dataFile
	 * 
	 */
	@Override
	protected void generateData(String beanName, String dataFile) {
		// 获取数据
		try {
			getCSVList(beanName, dataFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @param caseName
	 * @param dataFile
	 * @return
	 * 
	 * 
	 */
	@SuppressWarnings("resource")
	private void getCSVList(String beanName, String dataFile) throws Exception {

		InputStream is = null;
		Workbook wb = null;
		Sheet st = null;
		Class onwClass = null;

		is = new FileInputStream(defaultPathCvs + dataFile);
		wb = Workbook.getWorkbook(is);
		st = wb.getSheet(beanName);// 获取sheet
		if (st.getRows() == 0) {
			return;
		}
		onwClass = Class.forName(beanUrl + beanName);
		for (int i = 1; i < st.getRows(); i++) {
			Object model = null;
			model = onwClass.newInstance();
			Field fields[] = model.getClass().getDeclaredFields();
			int j = 0;
			for (Field field : fields) {
				System.out.println(field);
				String name = field.getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				String type = field.getGenericType().toString();
				if (type.equals("class java.lang.String")) {
					Method m = model.getClass().getMethod("get" + name);
					m = model.getClass().getMethod("set" + name, String.class);
					m.invoke(model, st.getCell(j, i).getContents());
				}
				j++;
			}
			Object[] o = new Object[1];
			o[0] = model;
			data.add(o);
		}

	}
}
