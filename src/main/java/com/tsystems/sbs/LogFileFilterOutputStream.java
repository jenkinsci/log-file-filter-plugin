package com.tsystems.sbs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import com.tsystems.sbs.LogFileFilterBuildWrapper.DescriptorImpl;

import hudson.console.LineTransformationOutputStream;
import jenkins.model.Jenkins;

public class LogFileFilterOutputStream extends LineTransformationOutputStream {

	private boolean firstLine = true;//For debugging purposes only, allows printing the global config in the first line
	private OutputStream logger;
	
	public LogFileFilterOutputStream (OutputStream out){
		this.logger = out;
	}
	
	@Override
	protected void eol(byte[] bytes, int len) throws IOException {
		String line = new String(bytes, 0, len);
		
		LogFileFilterBuildWrapper.DescriptorImpl descriptor = (DescriptorImpl) Jenkins.getInstance().getDescriptor(LogFileFilterBuildWrapper.class);
		
		boolean isEnabledGlobally = descriptor.isEnabledGlobally();
		
		if(isEnabledGlobally) {
			Set<RegexpPair> regexpPairs = descriptor.getRegexpPairs();
			
			//DEBUG: Write the LogFileFilter global config parameters at the first line for debugging purposes
			if(firstLine){
				line += "Global configuration:============= ";
				
				line += " isEnabledGlobally: "+isEnabledGlobally+"  ";
				for(RegexpPair regexpPair : regexpPairs){
					line += "regexpPairs: "+regexpPair.getRegexp()+" | "+regexpPair.getReplacement()+"    ";
				}
				
				firstLine = false;
			} else {
				//Perform the actual filtering
				for(RegexpPair regexpPair : regexpPairs){
					line = line.replaceAll(regexpPair.getRegexp(),regexpPair.getReplacement());
				}
			}
		}
		
		logger.write(line.getBytes());
	}
	
	/**
     * {@inheritDoc}
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        super.close();
        logger.close();
    }

    /**
     * {@inheritDoc}
     * @throws IOException
     */
    @Override
    public void flush() throws IOException {
        super.flush();
        logger.flush();
    }

}
