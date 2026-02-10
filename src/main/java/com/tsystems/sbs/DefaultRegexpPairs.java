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
            = Collections.unmodifiableList(
                    Arrays.asList(
                    new RegexpPair("(https?+://[^:\\s]++):[^@\\s]++@", "$1:********@"),//Passwd URL MASKING
                    new RegexpPair("password=\\S*", "password=********") //PASSWORD MASKING
            ));

    private final static List<RegexpPair> DEFAULT_REGEXES_AWS
            = Collections.unmodifiableList(
            Arrays.asList(
                    new RegexpPair("(AWS_[a-zA-Z_]+=)(\\S+)", "$1********"), // AWS RegExp MASKING
                    new RegexpPair("(aws_[a-zA-Z_]+=)(\\S+)", "$1********"),
                    regexpMaskForJsonKeyEndingWith("accesskeyid"),
                    regexpMaskForJsonKeyEndingWith("secretaccesskey"),
                    regexpMaskForJsonKeyEndingWith("token"),
                    regexpMaskForEscapedJsonKeyWithSuffix("AccessKeyId"),
                    regexpMaskForEscapedJsonKeyWithSuffix("SecretAccessKey"),
                    regexpMaskForEscapedJsonKeyWithSuffix("Token")
            ));

    private final static List<RegexpPair> DEFAULT_REGEXES_DD
            = Collections.unmodifiableList(
            Arrays.asList(
                    new RegexpPair("((?i)(\\bdatadog|dd|dogapi\\b).*)(\\b([a-zA-Z-0-9]{32})\\b)", "$1********"), // Datadog RegExp MASKING API KEY
                    new RegexpPair("((?i)(\\bdatadog|dd|dogapi\\b).*)(\\b([a-zA-Z-0-9]{40})\\b)", "$1********") // Datadog RegExp MASKING APP KEY
            ));

    public static List<RegexpPair> getDefaultRegexes() {
        return DEFAULT_REGEXES;
    }

    public static List<RegexpPair> getDefaultRegexesAWS() {
        return DEFAULT_REGEXES_AWS;
    }

    public static List<RegexpPair> getDefaultRegexesDD() {
        return DEFAULT_REGEXES_DD;
    }

    /**
     * Case-insensitive regexp that matches output key ending with specified word, value of which should be masked
     * Sample input:
     * "ansible_ec2_iam_security_credentials_instance_profiles_accesskeyid": "ASIAU5QBETVVXEXAMPLE"
     */
    private static RegexpPair regexpMaskForJsonKeyEndingWith(String keySuffix) {
        return new RegexpPair("(?i)\"\\w*" + keySuffix + "\\\":\\s*\\\"[\\w\\d/+=*]+\\\"", "\"" + keySuffix + "\": \"********\"");
    }

    /**
     * Case-sensitive regexp that matches escaped output key, value of which should be masked
     * Typically it is masked output of escaped json
     * Sample input:
     * \"AccessKeyId\" : \"ASIAU5QBETVVXEXAMPLE\"
     */
    private static RegexpPair regexpMaskForEscapedJsonKeyWithSuffix(String key) {
        return new RegexpPair("\\\\\\\"" + key + "\\\\\\\"\\s*:\\s*\\\\\\\"[\\w\\d/+=*]+\\\\\\\"", "\\\"" + key + "\\\": \\\"********\\\"");
    }
}
