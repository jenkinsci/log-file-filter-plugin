package com.tsystems.sbs;

import hudson.Extension;
import hudson.util.PersistedList;
import jenkins.model.GlobalConfiguration;
import org.kohsuke.stapler.DataBoundSetter;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class deals with the plugin configuration and persistence of the data.
 *
 * @author ccapdevi
 *
 */
@Extension
public class LogFileFilterConfig extends GlobalConfiguration implements Serializable {

	private static final long serialVersionUID = 5850114662289551496L;

	private static final Logger LOGGER = Logger.getLogger(LogFileFilterConfig.class.getName());
    
    public static LogFileFilterConfig get() {
    	final LogFileFilterConfig config;
    	try {
    		config = GlobalConfiguration.all().get(LogFileFilterConfig.class);
    	} catch (IllegalStateException e) {
    		LOGGER.log(Level.SEVERE, "Config not found! " + e);
    		throw e;
    	}
    	LOGGER.log(Level.INFO, "Found config.");
    	return config;
    }

    /**
     * Determines whether the plugin is enabled globally for ALL BUILDS.
     */
    private boolean enabledGlobally;

    /**
     * Determines whether the regexp replacements which come fixed with the plugin are enabled.
     */
    private boolean enabledDefaultRegexp;
    private boolean enabledDefaultRegexpAWS;

    /**
     * Represents the custom regexp pairs specified by the user in the global settings.
     */
    private List<RegexpPair> regexpPairs = new PersistedList<>(this);

    @SuppressWarnings("MC_OVERRIDABLE_METHOD_CALL_IN_CONSTRUCTOR")
    public LogFileFilterConfig() {
        load();
    }

    public boolean isEnabledGlobally() {
        return enabledGlobally;
    }

    @DataBoundSetter
    public void setEnabledGlobally(boolean enabledGlobally) {
        this.enabledGlobally = enabledGlobally;
        save();
    }

    public boolean isEnabledDefaultRegexp() {
        return enabledDefaultRegexp;
    }

    public boolean isEnabledDefaultRegexpAWS() {
        return enabledDefaultRegexpAWS;
    }

    @DataBoundSetter
    public void setEnabledDefaultRegexp(boolean enabledDefaultRegexp) {
        this.enabledDefaultRegexp = enabledDefaultRegexp;
        save();
    }

    @DataBoundSetter
    public void setEnabledDefaultRegexpAWS(boolean enabledDefaultRegexpAWS) {
        this.enabledDefaultRegexpAWS = enabledDefaultRegexpAWS;
        save();
    }

    public List<RegexpPair> getRegexpPairs() {
        return regexpPairs;
    }

    @DataBoundSetter
    public void setRegexpPairs(List<RegexpPair> regexpPairs) {
        this.regexpPairs = regexpPairs;
        save();
    }

}
