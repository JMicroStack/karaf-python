package org.apache.karaf.python;

public class PythonSimpleResult {
	private String result;
	private String error;
	
	public PythonSimpleResult(String result, String error)
	{
		this.result = result;
		this.error = error;
	}
	
	public String getResult() {
		return this.result;
	}
	
	public String getError() {
		return this.error;
	}
	
	public boolean isError() {
		if (this.error == null || this.error.length() == 0)
			return false;
		return true;
	}
}
