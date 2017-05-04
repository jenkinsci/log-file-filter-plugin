package com.tsystems.sbs;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tsystems.sbs.LogFileFilterBuildWrapper.DescriptorImpl;

import hudson.console.LineTransformationOutputStream;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.model.Jenkins;

/**
 * This class deals with the actual filtering of the console using the configured regexes. Bear in mind that for now
 * only one line expressions are possible.
 *
 * @author ccapdevi
 *
 */
public class LogFileFilterOutputStream extends LineTransformationOutputStream {
    private static final Logger LOGGER = Logger.getLogger(LogFileFilterOutputStream.class.getName());

    //Logging
    private final OutputStream logger;
    private final Charset charset;
    //Global settings
    private final boolean isEnabledGlobally;
    private final boolean isEnabledDefaultRegexp;
    private final Set<RegexpPair> defaultRegexpPairs;
    private final Set<RegexpPair> customRegexpPairs;
    private String jobName;


    public LogFileFilterOutputStream(OutputStream out, Charset charset, String jobName) {
        this.jobName = jobName;
        this.logger = out;
        this.charset = charset;
        LogFileFilterBuildWrapper.DescriptorImpl descriptor
                = (DescriptorImpl) Jenkins.getInstance().getDescriptor(LogFileFilterBuildWrapper.class);

        isEnabledGlobally = descriptor.isEnabledGlobally();
        isEnabledDefaultRegexp = descriptor.isEnabledDefaultRegexp();
        if (isEnabledGlobally) {
            //Load regexes
            customRegexpPairs = descriptor.getRegexpPairs();
            if (isEnabledDefaultRegexp) {
                defaultRegexpPairs = DefaultRegexpPairs.getDefaultRegexes();
            } else {
                defaultRegexpPairs = Collections.EMPTY_SET;
            }
        } else {
            customRegexpPairs = Collections.EMPTY_SET;
            defaultRegexpPairs = Collections.EMPTY_SET;
        }
    }

    @Override
    protected void eol(byte[] bytes, int len) throws IOException {

        if (isEnabledGlobally) {
            final String inputLine = new String(bytes, 0, len, charset);
            String line = inputLine;

            for (RegexpPair regexpPair : defaultRegexpPairs) {
                line = filterLine(line, regexpPair);
            }

            for (RegexpPair regexpPair : customRegexpPairs) {
                line = filterLine(line, regexpPair);
            }

            if (inputLine.equals(line)) {
                logger.write(bytes, 0, len);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Filtered logfile for " + jobName + " output " + line);
                }
                logger.write(line.getBytes(charset));
            }
        } else {
            logger.write(bytes, 0, len);//If the filter is not enabled, write the bytes as-is to avoid messing with the encoding
        }

    }

    private static String filterLine(String line, RegexpPair regexpPair) {
        String result;
        try {
            Pattern pattern = regexpPair.getCompiledRegexp();
            Matcher matcher = pattern.matcher(line);
            result = matcher.replaceAll(regexpPair.getReplacement());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Exeption when wrapping log output.", e);
            result = line;
        }
        return result;
    }
}
