package com.tsystems.sbs;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * This class represents a Regexp/Replacement pair. It also compiles internally the regexp into a Pattern object to
 * encourage that it is compiled only once to avoid performance issues.
 *
 * @author ccapdevi
 *
 */
public class RegexpPair extends AbstractDescribableImpl<RegexpPair> implements Serializable{

	private static final long serialVersionUID = -5445325528650992703L;
	private String regexp;
    private String replacement;
    private transient Pattern compiledRegexp;

    @DataBoundConstructor
    public RegexpPair(String regexp, String replacement) {
        this.regexp = regexp;
        this.replacement = replacement;
        this.compiledRegexp = Pattern.compile(this.regexp);
    }

    public String getRegexp() {
        return regexp;
    }

    @DataBoundSetter
    public void setRegexp(String regexp) {
        this.regexp = regexp;
        this.compiledRegexp = Pattern.compile(this.regexp);
    }

    public String getReplacement() {
        return replacement;
    }

    @DataBoundSetter
    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public Pattern getCompiledRegexp() {
        return compiledRegexp;
    }

    @SuppressWarnings("unused")
    protected Object readResolve() {
        this.compiledRegexp = Pattern.compile(this.regexp);
        return this;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<RegexpPair> {
        public String getDisplayName() { return "Regular expression and replacement string pair."; }
    }

}
