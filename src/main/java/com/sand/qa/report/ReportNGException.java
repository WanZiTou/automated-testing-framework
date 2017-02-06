package com.sand.qa.report;

/**
 * Report Exception Class
 * 
 * @author nihuaiqing
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class ReportNGException extends RuntimeException {
	public ReportNGException(String string) {
		super(string);
	}

	public ReportNGException(String string, Throwable throwable) {
		super(string, throwable);
	}
}
