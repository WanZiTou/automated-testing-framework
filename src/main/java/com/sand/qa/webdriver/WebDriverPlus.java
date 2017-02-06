package com.sand.qa.webdriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;


public class WebDriverPlus extends EventFiringWebDriver  {
	
	public static final String PFILEPATH = "/test-output/screenshot";
	
	public WebDriverPlus(WebDriver driver) {
		super(driver);
	}
	
	
	/**
	 * take screenShot by name
	 * 
	 * @param name
	 *            name of screenShot
	 * @author kim
	 */
	public void screenShot(String name) {
		String path = System.getProperty("user.dir")+PFILEPATH;
		File file = new File(path);
		if(!file.exists()){
			file.mkdir();
		}
		File screenShotFile = ((TakesScreenshot) DriverBase.driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screenShotFile, new File(path +"/"+ name + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * take screenShot
	 * 
	 * @author kim
	 */
	public void screenShot() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		screenShot(df.format(new Date()).toString());
	}
	
}
