package com.willmeyer.commander;

/**
 * An exception occurs when a command is used incorrectly -- invalid inputs or invalid state only.
 */
public class UsageException extends Exception {
	
	private static final long serialVersionUID = 7526472295622776147L;
	
	public UsageException(String explanation) {
		super("Bad usage: " + explanation);
		assert explanation != null;
	}
}