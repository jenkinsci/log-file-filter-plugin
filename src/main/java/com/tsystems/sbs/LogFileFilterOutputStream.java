package com.tsystems.sbs;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hudson.console.LineTransformationOutputStream;
import jenkins.model.Jenkins;
import org.apache.commons.io.IOUtils;

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
    private final String jobName;


    public LogFileFilterOutputStream(OutputStream out, Charset charset, String jobName) {
        this.jobName = jobName;
        this.logger = out;
        this.charset = charset;
        Jenkins jenkinsInstance = Jenkins.getInstance();
        LogFileFilterConfig.DescriptorImpl descriptor	
		= (LogFileFilterConfig.DescriptorImpl) jenkinsInstance.getDescriptorOrDie(LogFileFilterConfig.class);

		isEnabledGlobally = descriptor.isEnabledGlobally();
		isEnabledDefaultRegexp = descriptor.isEnabledDefaultRegexp();
		if (isEnabledGlobally) {
			//Load regexes
			customRegexpPairs = descriptor.getRegexpPairs();
			if (isEnabledDefaultRegexp) {
				defaultRegexpPairs = DefaultRegexpPairs.getDefaultRegexes();
			} else {
				defaultRegexpPairs = Collections.emptySet();
			}
		} else {
			customRegexpPairs = Collections.emptySet();
			defaultRegexpPairs = Collections.emptySet();
		}
    }

    @Override
    public void close() throws IOException {
        try {
            // need to send the final EOL before closing the actual sink.
            super.close();
        } finally {
            // seems we need to do this, see JENKINS-45057.
            IOUtils.closeQuietly(logger);
        }
    }

    @Override
    protected void eol(byte[] bytes, int len) throws IOException {
        boolean changed = false;
        
        if (isEnabledGlobally) {
            final String inputLine = new String(bytes, 0, len, charset);
            String line = inputLine;

            for (RegexpPair regexpPair : defaultRegexpPairs) {
                String newLine = filterLine(line, regexpPair);
                if (newLine != null) {
                    changed = true;
                    line = newLine;
                }
            }

            for (RegexpPair regexpPair : customRegexpPairs) {
                String newLine = filterLine(line, regexpPair);
                if (newLine != null) {
                    changed = true;
                    line = newLine;
                }
            }

            if (changed) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, 
                            "Filtered logfile for {0} output ''{1}''.", new Object[]{jobName, line});
                }
                logger.write(line.getBytes(charset));
            } else {
                // no change, write the bytes as-is to avoid messing with the encoding
                logger.write(bytes, 0, len);
            }
        } else {
            logger.write(bytes, 0, len);
        }

    }

    private String filterLine(String line, RegexpPair regexpPair) {
        String result = null;
        try {
            Pattern pattern = regexpPair.getCompiledRegexp();
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                result = matcher.replaceAll(regexpPair.getReplacement());
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, 
                    "Exception when wrapping log output for " + jobName + 
                    " in line '" + line + "' got: '" + e + "'.", e);
        }
        return result;
    }
}
