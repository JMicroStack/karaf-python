package org.jmicrostack.karaf.python;

public class PythonRuntimeException extends RuntimeException {
	public PythonRuntimeException(String errorMessage) {
		super(errorMessage);
	}
	public PythonRuntimeException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
}