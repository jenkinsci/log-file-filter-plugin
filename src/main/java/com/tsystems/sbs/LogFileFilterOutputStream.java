package com.tsystems.sbs;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tsystems.sbs.LogFileFilterBuildWrapper.DescriptorImpl;

import hudson.console.LineTransformationOutputStream;
import jenkins.model.Jenkins;

/**
 * This class deals with the actual filtering of the console using the configured regexes.
 * Bear in mind that for now only one line expressions are possible.
 * @author ccapdevi
 *
 */
public class LogFileFilterOutputStream extends LineTransformationOutputStream {

	//Logging
	private OutputStream logger;
	private final Charset charset;
	//Global settings
	private boolean isEnabledGlobally;
	private boolean isEnabledDefaultRegexp;
	private Set<RegexpPair> defaultRegexpPairs;
	private Set<RegexpPair> customRegexpPairs;

	public LogFileFilterOutputStream (OutputStream out,Charset charset){
		this.logger = out;
		this.charset = charset;

		//Load global settings
		try {
			LogFileFilterBuildWrapper.DescriptorImpl descriptor = (DescriptorImpl) Jenkins.getInstance().getDescriptor(LogFileFilterBuildWrapper.class);
			
			isEnabledGlobally = descriptor.isEnabledGlobally();
			isEnabledDefaultRegexp = descriptor.isEnabledDefaultRegexp();
			if(isEnabledGlobally) {
				//Load regexes
				customRegexpPairs = descriptor.getRegexpPairs();
				if(isEnabledDefaultRegexp)
					defaultRegexpPairs = DefaultRegexpPairs.getDefaultRegexes();
			}
		} catch (NullPointerException e){
			//Log warning
			try {
				logger.write("[LogFileFilterWarning: the descriptor for Log File Filter plugin hasn't been found. No filter will be applied.] ".getBytes(charset));
				//Disable the filtering
				isEnabledGlobally = false;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	protected void eol(byte[] bytes, int len) throws IOException {

		if(isEnabledGlobally) {
			String line = new String(bytes, 0, len,charset);

			//Perform the actual filtering

			//Filtering with default Regexes
			if(isEnabledDefaultRegexp){
				for(RegexpPair regexpPair : defaultRegexpPairs){
					line = filterLine(line,regexpPair);
				}
			}

			//Filtering with custom Regexes
			if(customRegexpPairs != null && !customRegexpPairs.isEmpty()){
				for(RegexpPair regexpPair : customRegexpPairs){
					line = filterLine(line,regexpPair);
				}
			}

			logger.write(line.getBytes(charset));
		} else {
			logger.write(bytes,0,len);//If the filter is not enabled, write the bytes as-is to avoid messing with the encoding
		}

	}

	private static String filterLine(String line,RegexpPair regexpPair){
		try {
			Pattern pattern = regexpPair.getCompiledRegexp();
			Matcher matcher = pattern.matcher(line);

			return matcher.replaceAll(regexpPair.getReplacement());
		} catch (Exception e){
			//TODO: find a more sophisticated way to check if there have already been errors for that line or log the errors for each line elsewhere outside the console output

			//If there has been already a problem filtering the same line, do not log more warnings about that line
			if(!line.contains("[LogFileFilterWarning")) {
				return line + " [LogFileFilterWarning: error filtering the line: " + e.getMessage() + "]";
			} else
				return line;
		}
	}

	@Override
	public void close() throws IOException {
		super.close();
		logger.close();
	}

	@Override
	public void flush() throws IOException {
		super.flush();
		logger.flush();
	}

}
