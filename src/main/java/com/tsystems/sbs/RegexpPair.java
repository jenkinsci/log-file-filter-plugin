package com.tsystems.sbs;

public class RegexpPair {

	private String regexp;
	private String replacement;
	
	public RegexpPair(String regexp,String replacement){
		this.regexp = regexp;
		this.replacement = replacement;
	}
	
	public String getRegexp() {
		return regexp;
	}
	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}
	public String getReplacement() {
		return replacement;
	}
	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}
	
}
