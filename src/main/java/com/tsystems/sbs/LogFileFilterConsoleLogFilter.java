package com.tsystems.sbs;

import java.io.IOException;
import java.io.OutputStream;

import hudson.Extension;
import hudson.console.ConsoleLogFilter;
import hudson.model.AbstractBuild;

@Extension
public class LogFileFilterConsoleLogFilter extends ConsoleLogFilter {

	@Override
	public OutputStream decorateLogger(AbstractBuild build, OutputStream logger) throws IOException, InterruptedException {
		return new LogFileFilterOutputStream(logger);
	}

}
