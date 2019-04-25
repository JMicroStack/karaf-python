package org.jmicrostack.karaf.python;

import java.io.IOException;
import java.net.URI;

import org.jmicrostack.karaf.python.api.PythonProcess;
import org.jmicrostack.karaf.python.api.PythonVersion;

public class PythonEngine {

	private PythonVersion pyEngine;
	
	public PythonEngine() {
		this.pyEngine = PythonVersion.PYTHON3;
	}
	
	public PythonEngine(PythonVersion pyVerison) {
		this.pyEngine = pyVerison;
		
	}

	public PythonProcess run(URI path, String [] env) throws IOException {
		String cmd = pyEngine.getEngineName() + " " + path.getPath();
		PythonProcess pyProcess = new PythonProcessProvider(Runtime.getRuntime().exec(cmd, env));
		return pyProcess;
	}
}
