package com.sand.qa.report;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IInvokedMethod;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;

/**
 * Report Data
 * 
 * @author nihuaiqing revise NiHuaiqing
 * @version 1.0.0
 */
public class ReportNGUtils {
	private static final NumberFormat DURATION_FORMAT = new DecimalFormat(
			"#0.000");

	public long getDuration(ITestContext context) {
		long duration = getDuration(context.getPassedConfigurations()
				.getAllResults());
		duration += getDuration(context.getPassedTests().getAllResults());
		duration += getDuration(context.getSkippedConfigurations()
				.getAllResults());
		duration += getDuration(context.getSkippedTests().getAllResults());
		duration += getDuration(context.getFailedConfigurations()
				.getAllResults());
		duration += getDuration(context.getFailedTests().getAllResults());
		return duration;
	}

	private long getDuration(Set<ITestResult> results) {
		long duration = 0;
		for (ITestResult result : results) {
			duration += (result.getEndMillis() - result.getStartMillis());
		}
		return duration;
	}

	public String formatDuration(long startMillis, long endMillis) {
		long elapsed = endMillis - startMillis;
		return formatDuration(elapsed);
	}

	public String formatDuration(long elapsed) {
		double seconds = (double) elapsed / 1000;
		return DURATION_FORMAT.format(seconds);
	}

	public List<Throwable> getCauses(Throwable t) {
		List<Throwable> causes = new LinkedList<Throwable>();
		Throwable next = t;
		while (next.getCause() != null) {
			next = next.getCause();
			causes.add(next);
		}
		return causes;
	}

	public List<String> getTestOutput(ITestResult result) {
		return Reporter.getOutput(result);
	}

	public List<String> getAllOutput() {
		return Reporter.getOutput();
	}

	public boolean hasArguments(ITestResult result) {
		return result.getParameters().length > 0;
	}

	public String getArguments(ITestResult result) {
		Object[] arguments = result.getParameters();
		List<String> argumentStrings = new ArrayList<String>(arguments.length);
		for (Object argument : arguments) {
			argumentStrings.add(renderArgument(argument));
		}
		return commaSeparate(argumentStrings);
	}

	private String renderArgument(Object argument) {
		if (argument == null) {
			return "null";
		} else if (argument instanceof String) {
			return "\"" + argument + "\"";
		} else if (argument instanceof Character) {
			return "\'" + argument + "\'";
		} else if (argument instanceof String[]) {
			String[] s = (String[]) argument;
			StringBuffer testStrBuff = new StringBuffer("");
			for (int i = 0; i < s.length; i++) {
				testStrBuff.append(s[i] + ",");
			}
			return "\'" + testStrBuff.toString() + "\'";
		} else {
			return argument.toString();
		}
	}

	public boolean hasDependentGroups(ITestResult result) {
		return result.getMethod().getGroupsDependedUpon().length > 0;
	}

	public String getDependentGroups(ITestResult result) {
		String[] groups = result.getMethod().getGroupsDependedUpon();
		return commaSeparate(Arrays.asList(groups));
	}

	public boolean hasDependentMethods(ITestResult result) {
		return result.getMethod().getMethodsDependedUpon().length > 0;
	}

	public String getDependentMethods(ITestResult result) {
		String[] methods = result.getMethod().getMethodsDependedUpon();
		return commaSeparate(Arrays.asList(methods));
	}

	public boolean hasSkipException(ITestResult result) {
		return result.getThrowable() instanceof SkipException;
	}

	public String getSkipExceptionMessage(ITestResult result) {
		return hasSkipException(result) ? result.getThrowable().getMessage()
				: "";
	}

	public boolean hasGroups(ISuite suite) {
		return !suite.getMethodsByGroups().isEmpty();
	}

	private String commaSeparate(Collection<String> strings) {
		StringBuilder buffer = new StringBuilder();
		Iterator<String> iterator = strings.iterator();
		while (iterator.hasNext()) {
			String string = iterator.next();
			buffer.append(string);
			if (iterator.hasNext()) {
				buffer.append(", ");
			}
		}
		return buffer.toString();
	}

	public String escapeString(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			buffer.append(escapeChar(s.charAt(i)));
		}
		return buffer.toString();
	}

	private String escapeChar(char character) {
		switch (character) {
		// case '<':
		// return "&lt;";
		// case '>':
		// return "&gt;";
		// case '"':
		// return "&quot;";
		// case '\'':
		// return "&apos;";
		// case '&':
		// return "&amp;";
		default:
			return String.valueOf(character);
		}
	}

	public String escapeHTMLString(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case ' ':
				// 除了最后一个，在一个连续的空格都被转换为一个非中断的空格 (&nbsp;)。
				char nextCh = i + 1 < s.length() ? s.charAt(i + 1) : 0;
				buffer.append(nextCh == ' ' ? "&nbsp;" : " ");
				break;
			case '\n':
				buffer.append("<br/>\n");
				break;
			default:
				buffer.append(escapeChar(ch));
			}
		}
		return buffer.toString();
	}

	public String stripThreadName(String threadId) {
		if (threadId == null) {
			return null;
		} else {
			int index = threadId.lastIndexOf('@');
			return index >= 0 ? threadId.substring(0, index) : threadId;
		}
	}

	public long getStartTime(List<IInvokedMethod> methods) {
		long startTime = System.currentTimeMillis();
		for (IInvokedMethod method : methods) {
			startTime = Math.min(startTime, method.getDate());
		}
		return startTime;
	}

	public long getEndTime(ISuite suite, IInvokedMethod method,
			List<IInvokedMethod> methods) {
		boolean found = false;
		for (IInvokedMethod m : methods) {
			if (m == method) {
				found = true;
			}
			// Once a method is found, find subsequent method on same thread.
			else if (found
					&& m.getTestMethod().getId()
							.equals(method.getTestMethod().getId())) {
				return m.getDate();
			}
		}
		return getEndTime(suite, method);
	}

	private long getEndTime(ISuite suite, IInvokedMethod method) {
		// 从测试套件中的所有测试中，找到最新的结束时间.
		for (Map.Entry<String, ISuiteResult> entry : suite.getResults()
				.entrySet()) {
			ITestContext testContext = entry.getValue().getTestContext();
			for (ITestNGMethod m : testContext.getAllTestMethods()) {
				if (method == m) {
					return testContext.getEndDate().getTime();
				}
			}
			// 如果我们找不到一个匹配的测试方法，它必须是一个配置方法。
			for (ITestNGMethod m : testContext.getPassedConfigurations()
					.getAllMethods()) {
				if (method == m) {
					return testContext.getEndDate().getTime();
				}
			}
			for (ITestNGMethod m : testContext.getFailedConfigurations()
					.getAllMethods()) {
				if (method == m) {
					return testContext.getEndDate().getTime();
				}
			}
		}
		throw new IllegalStateException("Could not find matching end time.");
	}
}
