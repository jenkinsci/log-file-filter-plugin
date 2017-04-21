package com.tsystems.sbs;


import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildWrapperDescriptor;
import jenkins.tasks.SimpleBuildWrapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class LogFileFilterBuildWrapper extends SimpleBuildWrapper {

	private final boolean enabledGlobally;
	private final Set<RegexpPair> regexpPairs;

	/**
	 * This annotation tells Hudson to call this constructor, with
	 * values from the configuration form page with matching parameter names.
	 */
	@DataBoundConstructor
	public LogFileFilterBuildWrapper(boolean enabledGlobally,Set<RegexpPair> regexpPairs){
		this.enabledGlobally = enabledGlobally;
		this.regexpPairs = regexpPairs;
	};

	public Set<RegexpPair> getRegexpPairs() {
		return regexpPairs;
	}

	@Override
	public void setUp(Context context, Run<?, ?> build, FilePath workspace, Launcher launcher, TaskListener listener, EnvVars initialEnvironment) throws IOException, InterruptedException {
		// nothing to do here
	}

	/**
	 * Descriptor for {@link LogFileFilterBuildWrapper}.
	 * The class is marked as public so that it can be accessed from views.
	 * 
	 * See LogFileFilterBuilder/*.jelly for the actual HTML fragment for the configuration screen.
	 */
	// this annotation tells Hudson that this is the implementation of an extension point
	@Extension
	public static final class DescriptorImpl extends BuildWrapperDescriptor {

		/**
		 * To persist global configuration information,
		 * simply store it in a field and call save().
		 * 
		 * <p>
		 * If you don't want fields to be persisted, use <tt>transient</tt>.
		 */

		/**
		 * Determines whether the plugin is enabled globally for ALL BUILDS.
		 */
		private boolean enabledGlobally;

		private Set<RegexpPair> regexpPairs;

		public DescriptorImpl(){
			super(LogFileFilterBuildWrapper.class);
			load();
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
		@Override
		public String getDisplayName() {
			return "Log File Filter";
		}

		/**
		 * Applicable to any kind of project
		 */
		@Override
		public boolean isApplicable(AbstractProject<?, ?> item) {
			return true;
		}

		@Override
		public boolean configure(StaplerRequest staplerRequest,JSONObject json) throws FormException {
			
			//Clear the list, so it will be overwritten by a new one
			regexpPairs = new LinkedHashSet<RegexpPair>();

			System.out.println(json.toString());//Print the JSON request for debugging purposes

			//enabledGlobally (determines whether or not to filter the console output using global parameters)
			if(json.has("enabledGlobally"))
				enabledGlobally = json.getBoolean("enabledGlobally");
				
			//The regexp pairs which determine which contents in the console will be filtered
			if(json.has("Regexp Pairs")){

				//Get all submitted pairs
				Object o = json.get("Regexp Pairs");

				//If the regexpPairs from the request contains more than 1 element it is a JSONArray
				if(o instanceof JSONArray) {
					JSONArray regexpPairsJson = json.getJSONArray("Regexp Pairs");

					for (int i=0;i<regexpPairsJson.size();i++){
						JSONObject regexpPair = regexpPairsJson.getJSONObject(i);
						String regexp = regexpPair.getString("regexp");
						String replacement = regexpPair.getString("replacement");

						regexpPairs.add(new RegexpPair(regexp,replacement));
					}
				} else if (o instanceof JSONObject) {//If the regexpPairs from the request contains only 1 element it is a JSONObject
					JSONObject regexpPairObj = json.getJSONObject("Regexp Pairs");
					
					String regexp = regexpPairObj.getString("regexp");
					String replacement = regexpPairObj.getString("replacement");
					
					regexpPairs.add(new RegexpPair(regexp,replacement));
				}
			}

			save();//Persist the changed config 
			return true; //Indicate that everything is good so far
		}

		public boolean isEnabledGlobally(){
			return enabledGlobally;
		}

		public void setEnabledGlobally(boolean enabledGlobally){
			this.enabledGlobally = enabledGlobally;
		}

		public Set<RegexpPair> getRegexpPairs() {
			if(regexpPairs==null)
				regexpPairs = new LinkedHashSet<RegexpPair>();
			return regexpPairs;
		}

		public void setRegexpPairs(Set<RegexpPair> regexpPairs) {
			this.regexpPairs = regexpPairs;
		}

		public RegexpPair getRegexpPair (String regexp){
			//Only iterate regexpPairs if it's not null
			if(regexpPairs!=null){
				for(RegexpPair regexpPair : regexpPairs)
					if(regexpPair.getRegexp().equals(regexp))
						return regexpPair;
			}
			return null;//The desired regexpPair hasn't been found
		}

	}

}
