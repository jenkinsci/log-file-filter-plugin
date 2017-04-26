package com.tsystems.sbs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tsystems.sbs.LogFileFilterBuildWrapper.DescriptorImpl;

import hudson.console.LineTransformationOutputStream;
import jenkins.model.Jenkins;

/**
 * This class deals with the actual filtering of the console using the configured regexes.
 * @author ccapdevi
 *
 */
public class LogFileFilterOutputStream extends LineTransformationOutputStream {

	private boolean firstLine = true;//For debugging purposes only, allows printing the global config in the first line
	private OutputStream logger;
	
	public LogFileFilterOutputStream (OutputStream out){
		this.logger = out;
	}
	
	@Override
	protected void eol(byte[] bytes, int len) throws IOException {
		StringBuilder line = new StringBuilder(new String(bytes, 0, len));
		
		//Load global settings
		LogFileFilterBuildWrapper.DescriptorImpl descriptor = (DescriptorImpl) Jenkins.getInstance().getDescriptor(LogFileFilterBuildWrapper.class);
		
		boolean isEnabledGlobally = descriptor.isEnabledGlobally();
		boolean isEnabledDefaultRegexp = descriptor.isEnabledDefaultRegexp();
		
		if(isEnabledGlobally) {
			Set<RegexpPair> defaultRegexpPairs = DefaultRegexpPairs.getDefaultRegexes();
			Set<RegexpPair> customRegexpPairs = descriptor.getRegexpPairs();
			
			
			//DEBUG: Write the LogFileFilter global config parameters at the first line for debugging purposes
			if(firstLine){
				line.append("Log File Filter global configuration:============= ");
				
				line.append(" isEnabledGlobally: "+isEnabledGlobally+"  ");
				line.append(" isEnabledDefaultRegexp: "+isEnabledDefaultRegexp+"  ");
				for(RegexpPair regexpPair : defaultRegexpPairs){
					line.append("defaultRegexpPairs: "+regexpPair.getRegexp()+" | "+regexpPair.getReplacement()+"    ");
				}
				for(RegexpPair regexpPair : customRegexpPairs){
					line.append("customRegexpPairs: "+regexpPair.getRegexp()+" | "+regexpPair.getReplacement()+"    ");
				}
				
				firstLine = false;
			} else {
				//Perform the actual filtering
				
				//Filtering with default Regexes
				if(isEnabledDefaultRegexp){
					for(RegexpPair regexpPair : defaultRegexpPairs){
						Pattern pattern = regexpPair.getCompiledRegexp();
						Matcher matcher = pattern.matcher(line.toString());
						
						line = new StringBuilder(matcher.replaceAll(regexpPair.getReplacement()));
					}
				}
				
				//Filtering with custom Regexes
				if(customRegexpPairs != null && !customRegexpPairs.isEmpty()){
					for(RegexpPair regexpPair : customRegexpPairs){
						Pattern pattern = regexpPair.getCompiledRegexp();
						Matcher matcher = pattern.matcher(line.toString());
						
						line = new StringBuilder(matcher.replaceAll(regexpPair.getReplacement()));
						
					}
				}
				
			}
		}
		
		logger.write(line.toString().getBytes());
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
