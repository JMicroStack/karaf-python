package org.jmicrostack.karaf.python;

public class PythonSimpleResult {
	private String result;
	private String error;
	private String log;
	
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
		if (this.error == null || this.error.length() == 0 || !this.error.contains("Traceback"))
			return false;
		return true;
	}
}
