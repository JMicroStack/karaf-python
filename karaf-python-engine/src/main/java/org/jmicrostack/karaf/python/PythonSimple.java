package org.jmicrostack.karaf.python;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jmicrostack.karaf.python.api.PythonVersion;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class PythonSimple {

	private PythonVersion pyEngine;
	private Bundle context;

	public PythonSimple() {
		this.pyEngine = PythonVersion.PYTHON3;
	}

	public PythonSimple(PythonVersion pyVerison) {
		this.pyEngine = pyVerison;
	}

	public PythonSimple(PythonVersion pyVerison, Bundle context) {
		this.pyEngine = pyVerison;
		this.context = context;
	}

	public void setPythonVersion(PythonVersion pyVerison) {
		this.pyEngine = pyVerison;
	}
	
	public void setContext(Bundle context) {
		this.context = context;
	}

	public PythonSimpleResult run(URI pathToScript, String data, String args, String[] env) throws IOException {

		if ( args == null)
			args = "";
		Path basePath = Paths.get("scripts", context.getSymbolicName(), context.getVersion().toString(), "python",
				pathToScript.toString()).toAbsolutePath();

		String cmd = pyEngine.getEngineName() + " " + basePath.toString();
		Process pyProcess = Runtime.getRuntime().exec(
				new String[] { pyEngine.getEngineName(), basePath.toString(), args },
				new String[] { "PYWORK_HOME=" + basePath.getParent().toString() });

		BufferedReader sreader = new BufferedReader(new InputStreamReader(pyProcess.getInputStream()));
		BufferedReader serror = new BufferedReader(new InputStreamReader(pyProcess.getErrorStream()));
		BufferedWriter sdata = new BufferedWriter(new OutputStreamWriter(pyProcess.getOutputStream()));

		StringBuffer result = new StringBuffer();
		StringBuffer error = new StringBuffer();

		String line;
		try {
			// Write input data to script by pipe
			if (data != null) {
				sdata.write(data);
				sdata.close();
			}

			// Read result
			while ((line = sreader.readLine()) != null)
				result.append(line);
			try {
				pyProcess.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Read error
			while ((line = serror.readLine()) != null)
				error.append(line);
			try {
				pyProcess.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			while ((line = serror.readLine()) != null)
				error.append(line);
			System.out.println(error.toString());
		}

		pyProcess.destroy();
		return new PythonSimpleResult(result.toString(), error.toString());
	}
}
