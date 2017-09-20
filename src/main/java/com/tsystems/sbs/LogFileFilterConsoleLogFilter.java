package com.tsystems.sbs;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

import hudson.Extension;
import hudson.console.ConsoleLogFilter;
import hudson.model.Run;

@Extension
public class LogFileFilterConsoleLogFilter extends ConsoleLogFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
    public OutputStream decorateLogger(Run build, OutputStream logger) throws IOException, InterruptedException {
    	if(logger == null)
    		return null;
    	
    	Charset charset = null;
    	String displayName = null;
    	if (build != null) {
	    	charset = build.getCharset();
	    	displayName = build.getFullDisplayName();
    	} else {
    		charset = Charset.defaultCharset();
    		displayName = "LogFileFilterLogger";
    	}
    	
        return new LogFileFilterOutputStream(logger, charset, displayName);
    }
    
	//Overriding this method allows filtering the logs of the slaves (master-slave communication)
	/*
    @Override
    public OutputStream decorateLogger(Computer computer, OutputStream logger)
    		throws IOException, InterruptedException {
    	if(logger == null)
    		return null;
    	
    	Charset charset = null;
    	String displayName = null;
    	if (computer != null) {
	    	charset = computer.getDefaultCharset();
	    	displayName = computer.getDisplayName();
    	} else {
    		charset = Charset.defaultCharset();
    		displayName = "LogFileFilterLogger";
    	}
    	
    	if(charset == null)
    		charset = put some default charset here?
    	
        return new LogFileFilterOutputStream(logger, charset, displayName);
    }*/

}
