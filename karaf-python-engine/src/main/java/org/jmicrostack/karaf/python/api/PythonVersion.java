package org.jmicrostack.karaf.python.api;

public enum PythonVersion {
	PYTHON2("python2"),
	PYTHON3("python3"),
	PYTHON35("python3.5"),
	PYTHON36("python3.6"),
	PYTHON37("python3.7");
	
	
	private String engineName;
	
	private PythonVersion(String engineName) {
		this.engineName = engineName;
	}
	
	public String getEngineName()
	{
		return this.engineName;
	}
}
