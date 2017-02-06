package com.sand.qa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Initialization Properties
 *  
 * @author nihuaiqing
 */
public class InitProperties {
	//配置文件地址
	public static final String PFILEPATH = File.separatorChar+"resources"+File.separatorChar+"config"+File.separatorChar+"config.properties";
	private InputStreamReader fn = null;
	private Properties config = new Properties();
	public static Map<String, String> mapproperties = new HashMap<String, String>();
	
	public InitProperties() {
		//构造初始配置文件
		init();
	}

	/**
	 * 初始化Property配置文件，放入系统属性变量中
	 */
	private void init() {
		Logger.Defaultlog("初始化配置文件");
		String configPath = System.getProperty("user.dir")+PFILEPATH;
		File file =new File(configPath);
		//如果配置文件存在将配置信息存入System Property中
		if(file.exists()){
			try {
					fn = new InputStreamReader(new FileInputStream(configPath),"UTF-8");
					config.load(fn);
					if (!config.isEmpty()) {
						Set<Object> keys = config.keySet();
						for (Object key : keys) {
							InitProperties.mapproperties.put(key.toString(), config.getProperty(key.toString()));
							if (!System.getProperties().containsKey(key.toString()) && !config.getProperty(key.toString()).isEmpty()) {						
								System.setProperty(key.toString(), config.getProperty(key.toString()));
							}
						}
						keys.clear();
					}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 对外调试使用
	 */
	public static void showAllSystemProperties() {
		Set<String> syskeys = InitProperties.mapproperties.keySet();
		for (Object key : syskeys) {
			if(System.getProperties().containsKey(key)) {
				System.out.println(key.toString() + "  " + System.getProperty(key.toString()));
			}
		}
		syskeys.clear();
	}
}
