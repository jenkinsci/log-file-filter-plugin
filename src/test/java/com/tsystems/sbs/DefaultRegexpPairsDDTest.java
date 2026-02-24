package com.tsystems.sbs;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultRegexpPairsDDTest {

    private List<RegexpPair> getDefaultRegexpPairs() {
        return DefaultRegexpPairs.getDefaultRegexesDD();
    }

    @Test
    void testDefaultPairsList() {
        List<RegexpPair> defaultRegexpPairs = getDefaultRegexpPairs();
        assertThat(defaultRegexpPairs.size(), greaterThan(0));
    }

    @Test
    void testDefaultPairsApi() {
        List<RegexpPair> defaultRegexpPairs = getDefaultRegexpPairs();

        // Define the input string 32 characters
        String input = "curl -X GET \"https://api.datadoghq.eu/api/v1/validate\" -H \"Accept: application/json\" -H \"DD-API-KEY: characteristicallycharacteristic\"";
        String expected = "curl -X GET \"https://api.datadoghq.eu/api/v1/validate\" -H \"Accept: application/json\" -H \"DD-API-KEY: ********\"";

        StringBuilder replacedInput = new StringBuilder(input);

        for (RegexpPair pair : defaultRegexpPairs) {
            String pattern = pair.getRegexp();
            String replacement = pair.getReplacement();

            Pattern regexPattern = Pattern.compile(pattern);
            Matcher matcher = regexPattern.matcher(replacedInput);

            while (matcher.find()) {
                String matchedPattern = matcher.group();
                String replacedString = replacement;

                // Replace all occurrences of $n with the matched groups
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String group = matcher.group(i);
                    replacedString = replacedString.replace("$" + i, group);
                }

                replacedInput.replace(matcher.start(), matcher.end(), replacedString);
                matcher.region(matcher.start() + replacedString.length(), replacedInput.length());
            }
        }

        String replacedInputString = replacedInput.toString();
        System.out.println("Replaced input result: " + replacedInputString);

        // Test the behavior
        assertEquals(expected, replacedInputString);
    }

    @Test
    void testDefaultPairsKey() {
        List<RegexpPair> defaultRegexpPairs = getDefaultRegexpPairs();

        // Define the input string 32 characters
        String input = "datadog key = 3c0c3965368a6b10f7640dbda46abfd2 secret= 3c0c3965368a6b10f7640dbda46abfdca981c2d3";
        String expected = "datadog key = ******** secret= ********";

        StringBuilder replacedInput = new StringBuilder(input);

        for (RegexpPair pair : defaultRegexpPairs) {
            String pattern = pair.getRegexp();
            String replacement = pair.getReplacement();

            Pattern regexPattern = Pattern.compile(pattern);
            Matcher matcher = regexPattern.matcher(replacedInput);

            while (matcher.find()) {
                String matchedPattern = matcher.group();
                String replacedString = replacement;

                // Replace all occurrences of $n with the matched groups
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String group = matcher.group(i);
                    replacedString = replacedString.replace("$" + i, group);
                }

                replacedInput.replace(matcher.start(), matcher.end(), replacedString);
                matcher.region(matcher.start() + replacedString.length(), replacedInput.length());
            }
        }

        String replacedInputString = replacedInput.toString();
        System.out.println("Replaced input result: " + replacedInputString);

        // Test the behavior
        assertEquals(expected, replacedInputString);
    }

    @Test
    void testDefaultPairsToken() {
        List<RegexpPair> defaultRegexpPairs = getDefaultRegexpPairs();

        // Define the input string 32 characters
        String input = "dAtAdOg token = \"3c0c3965368a6b10f7640dbda46abfdc\";";
        String expected = "dAtAdOg token = \"********\";";

        StringBuilder replacedInput = new StringBuilder(input);

        for (RegexpPair pair : defaultRegexpPairs) {
            String pattern = pair.getRegexp();
            String replacement = pair.getReplacement();

            Pattern regexPattern = Pattern.compile(pattern);
            Matcher matcher = regexPattern.matcher(replacedInput);

            while (matcher.find()) {
                String matchedPattern = matcher.group();
                String replacedString = replacement;

                // Replace all occurrences of $n with the matched groups
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String group = matcher.group(i);
                    replacedString = replacedString.replace("$" + i, group);
                }

                replacedInput.replace(matcher.start(), matcher.end(), replacedString);
                matcher.region(matcher.start() + replacedString.length(), replacedInput.length());
            }
        }

        String replacedInputString = replacedInput.toString();
        System.out.println("Replaced input result: " + replacedInputString);

        // Test the behavior
        assertEquals(expected, replacedInputString);
    }
}
