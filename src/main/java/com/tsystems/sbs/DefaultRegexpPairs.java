package com.tsystems.sbs;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The default regexes (which use is activated by the checkbox 'Default regexp') are defined in this class.
 *
 * @author ccapdevi
 *
 */
public final class DefaultRegexpPairs {

    private final static Set<RegexpPair> DEFAULT_REGEXES
            = Collections.unmodifiableSet(
                new LinkedHashSet<RegexpPair>(Arrays.asList(
                    new RegexpPair("(https?+://[^:\\s]++):[^@\\s]++@", "$1:********@"),//Passwd URL MASKING
                    new RegexpPair("-password=\\S*", "-password=********") //PASSWORD MASKING
            )));

    public static Set<RegexpPair> getDefaultRegexes() {
        return DEFAULT_REGEXES;
    }
}
