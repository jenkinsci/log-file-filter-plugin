package com.tsystems.sbs;

import org.junit.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;


public class DefaultRegexpPairsAWSTest {
    private List<RegexpPair> getDefaultRegexpPairs() {
        return DefaultRegexpPairs.getDefaultRegexesAWS();
    }
    @Test
    public void testDefaultPairsList() {
        List<RegexpPair> defaultRegexpPairs = getDefaultRegexpPairs();
        assertThat(defaultRegexpPairs.size(), greaterThan(0));

    }

    @Test
    public void testDefaultPairs() {
        List<RegexpPair> defaultRegexpPairs = getDefaultRegexpPairs();

        // Define the input string
        String input = "AWS_ACCESS_KEY_ID=ASIAQ42TKRXRW7GOEHMH AWS_SECRET_ACCESS_KEY=/lD8T9bXuZUW/F/8MutOB1vDXK2uG/gNHUe/d8bG AWS_SESSION_TOKEN=IQoJb3JpZ2luX2VjEF0aCWV1LXdlc3QtMSJHMEUCIQCML/uc20DoPT4BuxzRcspTZRWCLZeKxdhOCzt9GfVehQIgaSHU0fWmraj7iXv1UqNfagTaoPZZ189TyiIfabqKZNEqpAII1v//////////ARADGgwwNjE5MTQ5MTAxNzkiDDFOYsVLshKybCBrHSr4AYngLlJWHrHKeVcD9hfn+3Um37+ITcrq5mFbfC7uPuzV2JiGn3Sk1H8WcBLQTI87fdbqGNhKP9yC6ExDq3grTFkvHhUo4ctS/nQDrGoK8lRJIhymZO4DPB9BTLndUKaMHSIN";
        String expected = "AWS_ACCESS_KEY_ID=******** AWS_SECRET_ACCESS_KEY=******** AWS_SESSION_TOKEN=********";


        // Test the regular expression on each RegexpPair
        // ToDo: Need to move this to a separate method and build a final result with all the replacements and regexes
        for (RegexpPair pair : defaultRegexpPairs) {
            String pattern = pair.getRegexp();
            String replacement = pair.getReplacement();

            // Create a Pattern object
            Pattern regexPattern = Pattern.compile(pattern);

            // Match the pattern against the input string
            Matcher matcher = regexPattern.matcher(input);

            // Replace the matched pattern with the replacement
            String replacedInput = matcher.replaceAll(replacement);

            System.out.println("Pattern: " + replacedInput);

            // Test the behavior
            assertThat(replacedInput.contains(pattern), is(false));
            assertEquals(expected, replacedInput);

        }
    }
}

