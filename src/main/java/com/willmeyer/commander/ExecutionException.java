package com.willmeyer.commander;

/**
 * The root of all execution errors that occur while consoleRunning a command.  They are all required to
 * have status codes as well as a message explaining the error.  The framework will take care of 
 * displaying the errors to the user.
 */
public class ExecutionException extends Exception {
	
	private static final long serialVersionUID = 7526472295622776147L;
	
	protected int statusCode;
	protected String explanation;
	
	public ExecutionException(int statusCode) {
		this.statusCode = statusCode;
		this.explanation = "no further detail";
	}
	
	public ExecutionException() {
		this.statusCode = StatusCodes.ERR_UNKNOWN;
		this.explanation = "An unknown error ocurred";
	}

	public ExecutionException(int statusCode, String explanation) {
		this.statusCode = statusCode;
		this.explanation = explanation;
	}
	
	public int getStatusCode() {
		return this.statusCode;
	}
	
	public String getExplanation() {
		return this.explanation;
	}
	
}
