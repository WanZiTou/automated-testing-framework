package com.sand.qa.report;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ResourceBundle;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.testng.IReporter;

/**
 * Abstract Report Interface
 * 
 * @author HanLingZhi
 * @version 1.0.0
 */
public abstract class AbstractReporter implements IReporter {
	private static final String ENCODING = "UTF-8";
	protected static final String TEMPLATE_EXTENSION = ".vm";
	private static final String META_KEY = "meta";
	protected static final ReportMetadata META = new ReportMetadata();
	private static final String UTILS_KEY = "utils";
	private static final ReportNGUtils UTILS = new ReportNGUtils();
	private static final String MESSAGES_KEY = "messages";
	private static final ResourceBundle MESSAGES = ResourceBundle.getBundle(
			"com.sand.qa.report.messages.report", META.getLocale());
	private final String classpathPrefix;

	protected AbstractReporter(String classpath) {
		classpathPrefix = classpath;
		Velocity.setProperty("resource.loader", "classpath");
		Velocity.setProperty("classpath.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		if (!META.shouldGenerateVelocityLog()) {
			Velocity.setProperty("runtime.log.logsystem.class",
					"org.apache.velocity.runtime.log.NullLogSystem");
		}
		try {
			Velocity.init();
		} catch (Exception ex) {
			throw new ReportNGException("Failed to initialise Velocity.", ex);
		}
	}

	protected VelocityContext createContext() {
		VelocityContext context = new VelocityContext();
		context.put(META_KEY, META);
		context.put(UTILS_KEY, UTILS);
		context.put(MESSAGES_KEY, MESSAGES);
		return context;
	}

	protected void generateFile(File file, String templateName,
			VelocityContext context) throws Exception {
		// Writer writer = new BufferedWriter(new FileWriter(file));
		OutputStream out = new FileOutputStream(file);
		Writer writer = new BufferedWriter(
				new OutputStreamWriter(out, ENCODING));
		try {
			Velocity.mergeTemplate(classpathPrefix + templateName, ENCODING, context, writer);
			writer.flush();
		} finally {
			writer.close();
		}
	}

	protected void copyClasspathResource(File outputDirectory,
			String resourceName, String targetFileName) throws IOException {
		String resourcePath = classpathPrefix + resourceName;
		InputStream resourceStream = getClass().getClassLoader()
				.getResourceAsStream(resourcePath);
		copyStream(outputDirectory, resourceStream, targetFileName);
	}

	protected void copyFile(File outputDirectory, File sourceFile,
			String targetFileName) throws IOException {
		InputStream fileStream = new FileInputStream(sourceFile);
		try {
			copyStream(outputDirectory, fileStream, targetFileName);
		} finally {
			fileStream.close();
		}
	}

	protected void copyImgFile(File outputDirectory, String resourceName,
			String targetFileName) throws IOException {
		String resourcePath = classpathPrefix + resourceName;
		InputStream resourceStream = getClass().getClassLoader()
				.getResourceAsStream(resourcePath);
		File resourceFile = new File(outputDirectory, targetFileName);
		FileOutputStream output = new FileOutputStream(resourceFile);
		BufferedInputStream bis = new BufferedInputStream(resourceStream);
		BufferedOutputStream bos = new BufferedOutputStream(output);
		byte[] by = new byte[1024];
		while (bis.read(by) != -1) {
			bos.write(by, 0, by.length);
			bos.flush();
		}
		try {
			resourceStream.close();
			output.close();
			bis.close();
			bos.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	protected void copyStream(File outputDirectory, InputStream stream,
			String targetFileName) throws IOException {
		File resourceFile = new File(outputDirectory, targetFileName);
		BufferedReader reader = null;
		Writer writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(stream, ENCODING));
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(resourceFile), ENCODING));
			String line = reader.readLine();
			while (line != null) {
				writer.write(line);
				writer.write('\n');
				line = reader.readLine();
			}
			writer.flush();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}

	protected void removeEmptyDirectories(File outputDirectory) {
		if (outputDirectory.exists()) {
			for (File file : outputDirectory
					.listFiles(new EmptyDirectoryFilter())) {
				file.delete();
			}
		}
	}

	private static final class EmptyDirectoryFilter implements FileFilter {
		public boolean accept(File file) {
			return file.isDirectory() && file.listFiles().length == 0;
		}
	}
}
