package org.jmicrostack.karaf.python.api;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public interface PythonProcess {
	public OutputStreamWriter getInputDataWriter();
	public InputStreamReader getOutputDataReader();
	public InputStreamReader getErrorDataReader();
	public void stop();
}
