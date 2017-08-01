package com.tsystems.sbs;

import java.io.IOException;
import java.io.OutputStream;

import hudson.Extension;
import hudson.console.ConsoleLogFilter;
import hudson.model.Run;

@Extension
public class LogFileFilterConsoleLogFilter extends ConsoleLogFilter {

    @Override
    public OutputStream decorateLogger(Run build, OutputStream logger) throws IOException, InterruptedException {
        return new LogFileFilterOutputStream(logger, build.getCharset(), build.getFullDisplayName());
    }

}
