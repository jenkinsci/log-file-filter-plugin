package com.tsystems.sbs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The default regexes (which use is activated by the checkbox 'Default regexp') are defined in this class.
 *
 * @author ccapdevi
 *
 */
public final class DefaultRegexpPairs {

    private final static List<RegexpPair> DEFAULT_REGEXES
            = Collections.<RegexpPair>unmodifiableList(
                    Arrays.<RegexpPair>asList(
                    new RegexpPair("(https?+://[^:\\s]++):[^@\\s]++@", "$1:********@"),//Passwd URL MASKING
                    new RegexpPair("password=\\S*", "password=********") //PASSWORD MASKING
            ));

    public static List<RegexpPair> getDefaultRegexes() {
        return DEFAULT_REGEXES;
    }
}
