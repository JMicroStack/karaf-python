package org.apache.karaf.python.api;

public enum PythonVersion {
	PYTHON2("python2"),
	PYTHON3("python3");
	
	private String engineName;
	
	private PythonVersion(String engineName) {
		this.engineName = engineName;
	}
	
	public String getEngineName()
	{
		return this.engineName;
	}
}
