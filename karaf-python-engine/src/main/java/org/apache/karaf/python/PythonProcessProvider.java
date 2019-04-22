package org.apache.karaf.python;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.karaf.python.api.PythonProcess;

public class PythonProcessProvider implements PythonProcess{
	
	private Process shellProcess;
	
	public PythonProcessProvider(Process process)
	{
		this.shellProcess = process;
	}

	@Override
	public OutputStreamWriter getInputDataWriter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStreamReader getOutputDataReader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStreamReader getErrorDataReader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	
}
