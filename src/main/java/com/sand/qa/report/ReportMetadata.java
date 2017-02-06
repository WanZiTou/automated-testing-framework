package com.sand.qa.report;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Report Meta Data
 * 
 * @author nihuaiqing
 * @version 1.0.0
 */
public final class ReportMetadata {
	static final String PROPERTY_KEY_PREFIX = "Report.";
	static final String TITLE_KEY = PROPERTY_KEY_PREFIX + "title";
	static final String DEFAULT_TITLE = "Test Results Report";
	static final String COVERAGE_KEY = PROPERTY_KEY_PREFIX + "coverage-report";
	static final String EXCEPTIONS_KEY = PROPERTY_KEY_PREFIX + "show-expected-exceptions";
	static final String OUTPUT_KEY = PROPERTY_KEY_PREFIX + "escape-output";
	static final String XML_DIALECT_KEY = PROPERTY_KEY_PREFIX + "xml-dialect";
	static final String STYLESHEET_KEY = PROPERTY_KEY_PREFIX + "stylesheet";
	static final String LOCALE_KEY = PROPERTY_KEY_PREFIX + "locale";
	static final String VELOCITY_LOG_KEY = PROPERTY_KEY_PREFIX + "velocity-log";

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("EEEE dd MMMM yyyy");
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm z");
	private final Date reportTime = new Date();

	public String getReportDate() {
		return DATE_FORMAT.format(reportTime);
	}

	public String getReportTime() {
		return TIME_FORMAT.format(reportTime);
	}

	public String getReportTitle() {
		return System.getProperty(TITLE_KEY, DEFAULT_TITLE);
	}

	public String getCoverageLink() {
		return System.getProperty(COVERAGE_KEY);
	}

	public File getStylesheetPath() {
		String path = System.getProperty(STYLESHEET_KEY);
		return path == null ? null : new File(path);
	}

	public boolean shouldShowExpectedExceptions() {
		return System.getProperty(EXCEPTIONS_KEY, "false").equalsIgnoreCase("true");
	}

	public boolean shouldEscapeOutput() {
		return System.getProperty(OUTPUT_KEY, "true").equalsIgnoreCase("true");
	}

	public boolean allowSkippedTestsInXML() {
		return !System.getProperty(XML_DIALECT_KEY, "testng").equalsIgnoreCase("junit");
	}

	public boolean shouldGenerateVelocityLog() {
		return System.getProperty(VELOCITY_LOG_KEY, "false").equalsIgnoreCase("true");
	}

	public String getUser() throws UnknownHostException {
		String user = System.getProperty("user.name");
		String host = InetAddress.getLocalHost().getHostName();
		String ip = InetAddress.getLocalHost().getHostAddress();
		return ip + "(" + user + '@' + host + ")";
	}

	public String getJavaInfo() {
		return String.format("Java %s (%s)",
				System.getProperty("java.version"),
				System.getProperty("java.vendor"));
	}

	public String getPlatform() {
		return String
				.format("%s %s (%s)", System.getProperty("os.name"),
						System.getProperty("os.version"),
						System.getProperty("os.arch"));
	}

	public Locale getLocale() {
		if (System.getProperties().containsKey(LOCALE_KEY)) {
			String locale = System.getProperty(LOCALE_KEY);
			String[] components = locale.split("_", 3);
			switch (components.length) {
			case 1:
				return new Locale(locale);
			case 2:
				return new Locale(components[0], components[1]);
			case 3:
				return new Locale(components[0], components[1], components[2]);
			default:
				System.err.println("Invalid locale specified: " + locale);
			}
		}
		return Locale.getDefault();
	}
}
