package com.sand.qa.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.Reporter;

/**
 * Logger
 * 
 * @author nihuaiqing
 * @version 1.0.0
 */
public class Logger{
	
     private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     //日志开关
	 public static boolean isLog = true;	 
	 //框架默认日志开关
	 public static boolean isDefaut = true;	 
	 //控制台输出开关
	 public static boolean isToStandardOut = true;
	 //日志格式开关
	 public static boolean isFormat = true;
	 
     public static int verbose = 1;     
     
     public static void log(String s, int level, boolean logToStandardOut) {
    	 if(isLog){
    		 Reporter.log(logPrefix(s), level, logToStandardOut);
    	 }    	 
     }
     
     public static void Defaultlog(String s) {
    	 if(isLog && isDefaut) {
    		 Reporter.log(logPrefix(s), verbose, isToStandardOut);
    	 }
     }
     
     public static void log(String s) {
    	 log(s,verbose,isToStandardOut);
     }
     
     public static void log(String s, int level){
    	 log(s,level,isToStandardOut);
     }
     
     public static void log(String s, boolean logToStandardOut){
    	 log(s,verbose,logToStandardOut);
     }
	  
	private static String logPrefix(String s) {
		Date logtime = new Date();
		if(isFormat) {
			return "[" + System.getProperty("Project.Name", "sand") + " "+ DATE_FORMAT.format(logtime) + "] " + s;
		}
		return s;
	}
	
	public static void setLog() {
		if (System.getProperty("Logger", "true").equalsIgnoreCase("false")) {
			Logger.isLog = false;
		}
		if (System.getProperty("Logger.StandardOut", "false").equalsIgnoreCase("true")) {
			Logger.isToStandardOut = true;
		}
		if (System.getProperty("Logger.FrameWorkOut", "true").equalsIgnoreCase("false")) {
			Logger.isDefaut = false;
		}
		if (System.getProperty("Logger.Format", "true").equalsIgnoreCase("false")) {
			Logger.isFormat = false;
		}		
	}

}
