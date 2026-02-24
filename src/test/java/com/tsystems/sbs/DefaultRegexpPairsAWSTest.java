package com.tsystems.sbs;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultRegexpPairsAWSTest {

    private List<RegexpPair> getDefaultRegexpPairs() {
        return DefaultRegexpPairs.getDefaultRegexesAWS();
    }

    @Test
    void testDefaultPairsList() {
        List<RegexpPair> defaultRegexpPairs = getDefaultRegexpPairs();
        assertThat(defaultRegexpPairs.size(), greaterThan(0));
    }

    @Test
    void testDefaultPairs() {
        List<RegexpPair> defaultRegexpPairs = getDefaultRegexpPairs();

        // Define the input string
        String input = "AWS_ACCESS_KEY_ID=R2RHTXG7QKSRWMOHEIAMH4A AWS_SECRET_ACCESS_KEY=/lD8T9bXuZUW/F/8MutOB1vDXK2uG/gNHUe/d8bG AWS_SESSION_TOKEN=Z1XKqTnKIHd7eLJhBZb9QWVcG0Rj3f8z1uYgO4Xm6vNiD5F7cM9pAa/2SsPqRrTtEoUyHwC+DxGJlWbVfNkOYK6hI3eX1L0j2+//////////";
        String expected = "AWS_ACCESS_KEY_ID=******** AWS_SECRET_ACCESS_KEY=******** AWS_SESSION_TOKEN=********";

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
