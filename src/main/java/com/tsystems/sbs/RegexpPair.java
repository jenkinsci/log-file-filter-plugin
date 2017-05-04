package com.tsystems.sbs;

import java.util.regex.Pattern;

/**
 * This class represents a Regexp/Replacement pair. It also compiles internally the regexp into a Pattern object to
 * encourage that it is compiled only once to avoid performance issues.
 *
 * @author ccapdevi
 *
 */
public class RegexpPair {

    private String regexp;
    private String replacement;
    private Pattern compiledRegexp;

    public RegexpPair(String regexp, String replacement) {
        this.regexp = regexp;
        this.replacement = replacement;
        this.compiledRegexp = Pattern.compile(this.regexp);
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
        this.compiledRegexp = Pattern.compile(this.regexp);
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public Pattern getCompiledRegexp() {
        return compiledRegexp;
    }

}
