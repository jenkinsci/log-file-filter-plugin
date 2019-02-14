package com.tsystems.sbs;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
    	LogFileFilterConfig config = null;
    	try {
    		config = GlobalConfiguration.all().get(LogFileFilterConfig.class);
    	} catch (IllegalStateException e) {
    		LOGGER.log(Level.SEVERE, "Config not found!");
    		throw e;
    	}
    	LOGGER.log(Level.INFO, "Found config.");
    	return config;
    }


    /**
     * To persist global configuration information, simply store it in a field and call save().
     *
     * <p>
     * If you don't want fields to be persisted, use <tt>transient</tt>.
     */
    /**
     * Determines whether the plugin is enabled globally for ALL BUILDS.
     */
    private boolean enabledGlobally;

    /**
     * Determines whether the regexp replacements which come fixed with the plugin are enabled.
     */
    private boolean enabledDefaultRegexp;

    /**
     * Represents the custom regexp pairs specified by the user in the global settings.
     */
    private final Set<RegexpPair> regexpPairs = new LinkedHashSet<RegexpPair>();

    /**
     * This human readable name is used in the configuration screen.
     */
    @Override
    public String getDisplayName() {
        return "";
    }

    @Override
    public boolean configure(StaplerRequest staplerRequest, JSONObject json) throws FormException {

        //Clear the list, so it will be overwritten by a new one
        regexpPairs.clear();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Reading configuration " +  json.toString());
        }

        //enabledGlobally (determines whether or not to filter the console output using global parameters)
        if (json.has("enabledGlobally")) {
            enabledGlobally = json.getBoolean("enabledGlobally");
        }

        if (json.has("enabledDefaultRegexp")) {
            enabledDefaultRegexp = json.getBoolean("enabledDefaultRegexp");
        }

        //The regexp pairs which determine which contents in the console will be filtered
        if (json.has("Regexp Pairs")) {

            //Get all submitted pairs
            Object o = json.get("Regexp Pairs");

            //If the regexpPairs from the request contains more than 1 element it is a JSONArray
            if (o instanceof JSONArray) {
                JSONArray regexpPairsJson = json.getJSONArray("Regexp Pairs");

                for (int i = 0; i < regexpPairsJson.size(); i++) {
                    JSONObject regexpPair = regexpPairsJson.getJSONObject(i);
                    String regexp = regexpPair.getString("regexp");
                    String replacement = regexpPair.getString("replacement");

                    regexpPairs.add(new RegexpPair(regexp, replacement));
                }
            } else if (o instanceof JSONObject) {//If the regexpPairs from the request contains only 1 element it is a JSONObject
                JSONObject regexpPairObj = json.getJSONObject("Regexp Pairs");

                String regexp = regexpPairObj.getString("regexp");
                String replacement = regexpPairObj.getString("replacement");

                regexpPairs.add(new RegexpPair(regexp, replacement));
            }
        }

        save();//Persist the changed config
        return true; //Indicate that everything is good so far
    }

    public boolean isEnabledGlobally() {
        return enabledGlobally;
    }

    public void setEnabledGlobally(boolean enabledGlobally) {
        this.enabledGlobally = enabledGlobally;
    }

    public boolean isEnabledDefaultRegexp() {
        return enabledDefaultRegexp;
    }

    public void setEnabledDefaultRegexp(boolean enabledDefaultRegexp) {
        this.enabledDefaultRegexp = enabledDefaultRegexp;
    }

    public Set<RegexpPair> getRegexpPairs() {
        return regexpPairs;
    }

}
