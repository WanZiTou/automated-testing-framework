package com.sand.qa.webdriver;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.sand.qa.util.Base;
import com.sand.qa.util.Logger;

public class DriverBase extends Base {
	private WebDriver dr = null;
	public static WebDriverPlus driver = null;
	public static int implicitlyWait = 5;
	public static int webDriverWait = 5;

	/**
	 * 启动浏览器
	 */
	public void launch() {
		/*ProfilesIni allProfiles = new ProfilesIni();   
		FirefoxProfile profile = allProfiles.getProfile("selenium");  */
		  
		String browser = System.getProperty("WebDriver.Browser");
		String browserLocation = System
				.getProperty("WebDriver.Browser.Location");
		// launch browser
		if (browser.equalsIgnoreCase("Firefox")) {
			Logger.log("打开Firefox浏览器");
			if (browserLocation != null && !browserLocation.isEmpty()) {
				System.setProperty("webdriver.firefox.bin", browserLocation);
			}
			dr = new FirefoxDriver();
			//dr =new FirefoxDriver(profile); 
		} else if (browser.equalsIgnoreCase("Chrome")) {
			Logger.log("打开Chrome浏览器");
			if (browserLocation != null && !browserLocation.isEmpty()) {
				System.setProperty("webdriver.chrome.driver", browserLocation);
			}
			dr = new ChromeDriver();
		} else {
			Logger.log("打开IE浏览器");
			if (browserLocation != null && !browserLocation.isEmpty()) {
				System.setProperty("webdriver.ie.driver", browserLocation);
			}
			dr = new InternetExplorerDriver();
		}
		driver = new WebDriverPlus(dr);
		implicitlyWait = Integer.parseInt(System.getProperty(
				"WebDriver.ImplicitlyWait").trim());
		webDriverWait = Integer.parseInt(System.getProperty(
				"WebDriver.WebDriverWait").trim());
		Logger.log("设置全局显示等待:" + implicitlyWait);
		driver.manage().timeouts()
				.implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	/**
	 * 输入值并验证输入正确
	 * 
	 * @param element
	 * @param text
	 */
	public void input(WebElement element, String text) {
		element.clear();
		element.sendKeys(text);
		valueToBe(element, text);
	}

	/**
	 * 通过Js注入输入值并验证输入正确，当给日期等控件输入值时，推荐使用此方法
	 * 
	 * @param element
	 * 
	 * @param text
	 */
	public void inputByJs(WebElement element, String text) {
		jsExecutor(element, String.format("arguments[0].value=\"%s\"", text));
		valueToBe(element, text);
	}

	/**
	 * 选择下拉列表值但不验证是否选择成功
	 * 
	 * @param element
	 * 
	 * @param text
	 */
	public void selectWithoutCheck(WebElement element, String text) {
		Select sel = new Select(element);
		sel.selectByVisibleText(text);
	}
	
	public void select(WebElement element, String value)  {
		
		Select select = new Select(element);

		try {
			Logger.log("select by Value " + value);
			select.selectByValue(value);
		} catch (Exception notByValue) {
			Logger.log("select by VisibleText " + value);
			select.selectByVisibleText(value);
		}
	}

	/**
	 * 将鼠标焦点移至本元素
	 * 
	 * @param element
	 */
	public void moveToElement(WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).perform();
	}

	/**
	 * 使用action单击当前元素
	 * 
	 * @param element
	 */
	public void click(WebElement element) {
		Actions action = new Actions(driver);
		action.click(element).perform();
	}
	/**
	 * 双击当前元素
	 * 
	 * @param element
	 */
	public void doubleClick(WebElement element) {
		Actions action = new Actions(driver);
		action.doubleClick(element).perform();
	}

	/**
	 * 在当前元素右击
	 * 
	 * @param element
	 */
	public void rightClick(WebElement element) {
		Actions action = new Actions(driver);
		action.contextClick(element).perform();
	}
	/**
	 * 得到当前alert窗口并点击ok
	 * 
	 * @param element
	 */
	public void alertConfirm() throws Exception {
		Alert alert = driver.switchTo().alert();
		try {
			alert.accept();
		} catch (Exception notFindAlert) {
			throw notFindAlert;
		}
	}
	/**
	 * 得到当前alert窗口并点击dismiss
	 * 
	 * @param element
	 */
	public void alertDismiss() throws Exception {
		Alert alert = driver.switchTo().alert();
		try {
			alert.dismiss();
		} catch (Exception notFindAlert) {
			throw notFindAlert;
		}
	}
	/**
	 * 得到当前alert窗口text
	 * 
	 * @param element
	 */
	public Alert getAlertText() throws Exception {
		Alert alert = driver.switchTo().alert();
		try {
			return alert;
		} catch (Exception notFindAlert) {
			throw notFindAlert;
		}
	}



	/**
	 * 拖拽当前页面元素至指定元素位置
	 * 
	 * @param source
	 *            起始位置元素
	 * @param target
	 *            指定位置元素
	 */
	public void dragAndDropTo(WebElement source, WebElement target) {
		(new Actions(driver)).dragAndDrop(source, target).perform();
	}

	/**
	 * 获取元素对齐方式
	 * 
	 * @param element
	 * @return left or right
	 */
	public String getTextAlign(WebElement element) {
		return element.getCssValue("text-align");
	}

	/**
	 * 判断当前元素是否存在
	 * 
	 * @param element
	 * 
	 * @param timeout
	 *            设置寻找元素超时时间
	 * @return true or false
	 */
	public boolean isElementPresent(By by, int timeout) {
		try {
			driver.manage().timeouts()
					.implicitlyWait(timeout, TimeUnit.SECONDS);
			driver.findElement(by);
			driver.manage().timeouts()
					.implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
			return true;
		} catch (NoSuchElementException e) {
			driver.manage().timeouts()
					.implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
			return false;
		}
	}

	/**
	 * 判断当前元素是否存在
	 * 
	 * @param element
	 * 
	 * @param timeout
	 *            设置寻找元素超时时间
	 * @return true or false
	 */
	public boolean isElementPresent(WebElement element) {
		try {
			driver.manage().timeouts()
					.implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
			element.getLocation();
			driver.manage().timeouts()
					.implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
			return true;
		} catch (NoSuchElementException e) {
			driver.manage().timeouts()
					.implicitlyWait(implicitlyWait, TimeUnit.SECONDS);
			return false;
		}
	}

	/**
	 * 验证元素加载完成，直到超时
	 * 
	 * @param element
	 */
	public void toBePresent(final By by) {
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return isElementPresent(by, implicitlyWait);
					}
				});
	}

	/**
	 * 验证元素不加载，直到超时
	 * 
	 * @param element
	 */
	public void toBeNotPresent(final By by) {
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return !isElementPresent(by, 3);
					}
				});
	}

	/**
	 * 验证元素可见，直到超时
	 * 
	 * @param element
	 */
	public void toBeVisible(WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(ExpectedConditions.visibilityOf(element));
		obumbrateElement(element);
	}

	/**
	 * 验证元素不可见，直到超时
	 * 
	 * @param element
	 */
	public void toBeInvisible(final WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return !element.isDisplayed();
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 验证元素可用，直到超时
	 * 
	 * @param element
	 */
	public void toBeEnable(WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(ExpectedConditions.elementToBeClickable(element));
		obumbrateElement(element);
	}

	/**
	 * 验证元素只读
	 * 
	 * @param element
	 */
	public boolean toBeReadonly(WebElement element) {
		lightElement(element);
		delay(1);
		if (element.getAttribute("readonly").equals("true")) {
			obumbrateElement(element);
			return true;
		} else {
			obumbrateElement(element);
			return false;
		}

	}

	/**
	 * 验证元素不可用，直到超时
	 * 
	 * @param element
	 */
	public void toBeDisable(final WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return !element.isEnabled();
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 验证元素text与期望text相同，直到超时
	 * 
	 * @param element
	 * 
	 * @param text
	 *            期望 text
	 */
	public void textToBe(final WebElement element, final String text) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return element.getText().equals(text);
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 验证元素text为空，直到超时
	 * 
	 * @param element
	 */
	public void textToBeEmpty(final WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return element.getText().isEmpty();
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 验证元素text不为空，直到超时
	 * 
	 * @param element
	 */
	public void textToBeNotEmpty(final WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return !element.getText().isEmpty();
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 验证元素text包含期望text，直到超时
	 * 
	 * @param element
	 * 
	 * @param text
	 *            期望被包含的 text
	 */
	public void textContains(final WebElement element, final String text) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return element.getText().contains(text);
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 验证元素value与期望value相同，直到超时
	 * 
	 * @param element
	 * 
	 * @param value
	 *            期望 value
	 */
	public void valueToBe(final WebElement element, final String value) {
		
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return element.getAttribute("value").equals(value);
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 验证元素value为空，直到超时
	 * 
	 * @param element
	 */
	public void valueToBeEmpty(final WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return element.getAttribute("value").isEmpty();
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 验证元素value不为空，直到超时
	 * 
	 * @param element
	 */
	public void valueToBeNotEmpty(final WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return !element.getAttribute("value").isEmpty();
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 验证元素src不为空，直到超时
	 * 
	 * @param element
	 */
	public void srcToBeNotEmpty(final WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return !element.getAttribute("src").isEmpty();
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 验证元素value包含期望value，直到超时
	 * 
	 * @param element
	 * 
	 * @param value
	 *            期望被包含的 value
	 */
	public void valueContains(final WebElement element, final String value) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return element.getAttribute("value").contains(value);
					}
				});
		obumbrateElement(element);
	}
	/**
	 * 验证元素value包含期望value，直到超时
	 * 
	 * @param element
	 * 
	 * @param value
	 *            期望被包含的 value
	 */
	public void valueContains_innerHTML(final WebElement element, final String value) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						
						return element.getAttribute("innerHTML").contains(value);
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 验证单选框、复选框元素被选中，直到超时
	 * 
	 * @param element
	 */
	public void toBeSelected(WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(ExpectedConditions.elementToBeSelected(element));
		obumbrateElement(element);
	}

	/**
	 * 验证单选框、复选框元素不被选中，直到超时
	 * 
	 * @param element
	 */
	public void toBeNotSelected(WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(ExpectedConditions.elementSelectionStateToBe(element,
						false));
		obumbrateElement(element);
	}

	/**
	 * 验证当前元素获取焦点，直到超时
	 * 
	 * @param element
	 */
	public void toBeActive(final WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return driver.switchTo().activeElement().getLocation()
								.equals(element.getLocation());
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 验证当前元素没有获取焦点，直到超时
	 * 
	 * @param element
	 */
	public void toBeNotActive(final WebElement element) {
		lightElement(element);
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(new Function<WebDriver, Boolean>() {
					public Boolean apply(WebDriver driver) {
						return !driver.switchTo().activeElement().getLocation()
								.equals(element.getLocation());
					}
				});
		obumbrateElement(element);
	}

	/**
	 * 拖拽滚动条使当前元素至视野内
	 * 
	 * @param element
	 */
	public void scrollIntoView(WebElement element) {
		jsExecutor(element, "arguments[0].scrollIntoView();");
	}

	/**
	 * 执行 javascript
	 * 
	 * @param element
	 * 
	 * @param script
	 *            e.g. arguments[0].scrollIntoView();
	 */
	public void jsExecutor(WebElement element, String script) {
		try {
			((JavascriptExecutor) driver).executeScript(script, element);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 高亮当前元素
	 * 
	 * @param element
	 */
	public void lightElement(WebElement element) {
		jsExecutor(element, "arguments[0].style.border = '4px solid red'");
	}

	/**
	 * 去掉当前元素高亮效果
	 * 
	 * @param element
	 */
	private void obumbrateElement(WebElement element) {
		try {
			Thread.sleep(100);
			jsExecutor(element, "arguments[0].style.border = 'none'");
		} catch (NoSuchElementException e) {
			return;
		} catch (Exception e) {
		}
	}

	/**
	 * 通过元素标签和标签内内容返回一个新元素, 新元素通过xpath定位，xpath值为：//tag[contains(text(),'name')]
	 * 
	 * @param tag
	 *            标签值
	 * @param name
	 *            name值
	 * @return Element
	 */
	public WebElement getElementByTagAndName(String tag, String name) {
		return driver.findElement(By.xpath("//" + tag + "[contains(text(),'"
				+ name + "')]"));
	}

	public void isAlertPresent() {
		new WebDriverWait(driver, DriverBase.webDriverWait)
				.until(ExpectedConditions.alertIsPresent());

	}

	/**
	 * 模拟键盘按键组合操作
	 * 
	 * @param key
	 *            of KeyEvent e.g. pressKey(KeyEvent.VK_ENTER) 点击回车键 t 84 j 74
	 *            enter 100 alt 18 tab 9 control 17
	 */
	public static void pressKeysum(int key, int keys) {
		try {
			Robot rob = new Robot();
			rob.keyPress(key);
			rob.keyPress(keys);
			rob.keyRelease(keys);
			rob.keyRelease(key);
		} catch (AWTException e) {
		}
	}
	

	/*
	 * 打开网址
	 */
	public void openurl(String url) {
		driver.get(url);
	}

	/**
	 * 刷新方法包装
	 * */
	public void refresh() {
		driver.navigate().refresh();
	}

	/**
	 * 后退方法包装
	 * */
	public void back() {
		driver.navigate().back();
	}

	/** 跳出frame */
	public void outFrame() {

		driver.switchTo().defaultContent();
	}

	/**
	 * 切换frame - 根据String类型（frame名字）
	 * */
	public void inFrame(String frameId) {
		driver.switchTo().frame(frameId);
	}

	/**
	 * @author gaoqiang
	 * @method 获取所有窗口的窗口句柄
	 */
	public Map windowAll(){
		Map<Integer,String> windowMap=new LinkedHashMap<Integer, String>();
		Set<String>handles=driver.getWindowHandles();
		 Iterator<String>it=handles.iterator();
		 int m=1;
		 while(it.hasNext()){
			 windowMap.put(m, it.next());
			 m++;
		 }		
		 return windowMap;
	}
/**
	 * @author gaoqiang
	 * @method 关闭需要的窗口之外的所有窗口
	 */
	public void closeWindowOther(int b){
		Map<Integer,String> windowMap=new LinkedHashMap<Integer, String>();
		windowMap=windowAll();
		
		for(int x=b+1;x<windowMap.size()+1;x++){
			switchWind(windowMap, x);
			delay(2);
			driver.close();
		}
		switchWind(windowMap, b);
	}
/**
	 * 
	 * @return 进入一个窗口,根据窗口的顺序
	 */
	public void switchWind(Map map,int x){
	
		 driver.switchTo().window(map.get(x).toString());
		
	}
	

	public void switchFrameByName(String name){
		
		 driver.switchTo().frame(name);
	}
	//切换到当前窗口
	public void switchWindow(String currentWindow){
		for (String handle : driver.getWindowHandles()) {

			if (currentWindow == handle) {
				continue;
			}
			driver.switchTo().window(handle);
		}
	}
	
	
	public void openNewWindow(int x,String url){		
		for(int g=1;g<x;g++) {
			((JavascriptExecutor)driver).executeScript("window.open('"+url+"')");
			
		}
		
	}
	public  String tableCellByTbody(String tableId,int row, int column){
        String text = null;
        //avoid get the head line of the table
        //row=row+1;
        //.//*[@id='logTbody']/tr[1]/td[1]
        String xpath="//*[@id='"+tableId+"']/tr["+row+"]/td["+column+"]";
        WebElement table=driver.findElement(By.xpath(xpath)); //*[@id="table138"]/tbody/tr[1]/td[1]/strong
        text=table.getText();
        return text;
        
    }
	
	public  String getCellText(WebElement table,int rowIndex,int cellIndex){
		 List<WebElement> rows=table.findElements(By.tagName("tr")); 
		 WebElement row=rows.get(rowIndex);
	     List<WebElement>  tds= row.findElements(By.tagName("td"));
	     String tdText=tds.get(cellIndex).getText();
		 return tdText;
           
    }  

}
