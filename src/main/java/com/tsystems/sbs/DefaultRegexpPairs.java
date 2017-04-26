package com.tsystems.sbs;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The default regexes (which use is activated by the checkbox 'Default regexp') are defined in this class.
 * @author ccapdevi
 *
 */
public final class DefaultRegexpPairs {

	private static Set<RegexpPair> defaultRegexes = new LinkedHashSet<RegexpPair>(Arrays.asList(new RegexpPair[]{
			new RegexpPair("(https?:\\/\\/\\S*):\\S*@(\\S*\\.\\S*)","$1:********@$2"),//MAIL URL MASKING
			new RegexpPair("-password=\\S*","-password=********"),//PASSWORD MASKING
	}));

	public static Set<RegexpPair> getDefaultRegexes() {
		return defaultRegexes;
	}
	
}
