package org.jmicrostack.karaf.python;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jmicrostack.karaf.python.api.PythonVersion;
import org.osgi.framework.Bundle;

public class PythonSimple {

	private static final Logger LOGGER = LoggerFactory.getLogger(PythonSimple.class);

	private PythonVersion pyEngine;
	private Bundle context;
	private String systemUserName;

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

	/**
	 * For property work must add to sudo for user which call other permission.
	 * Warning: HOME for user must be place in standard path: /home/{user} Example:
	 * user_name ALL=(ALL:ALL) NOPASSWD: ALL
	 * 
	 * @param userName
	 */
	public void setSystemUserName(String userName) {
		this.systemUserName = userName;
	}

	public String getSystemUserName() {
		return this.systemUserName;
	}

	public PythonSimpleResult run(URI pathToScript, Object data, String args, String[] env) throws IOException {

		if (args == null)
			args = "";

		Path basePath = Paths.get("scripts", context.getSymbolicName(), context.getVersion().toString(), "python",
				pathToScript.toString()).toAbsolutePath();

		Process pyProcess;

		if (this.systemUserName == null) {
			if (args.length() > 0)
				pyProcess = Runtime.getRuntime().exec(
						new String[] { pyEngine.getEngineName(), basePath.toString(), args },
						new String[] { "PYWORK_HOME=" + basePath.getParent().toString(),
								"KARAF_HOME=" + System.getProperty("karaf.home") });
			else
				pyProcess = Runtime.getRuntime().exec(new String[] { pyEngine.getEngineName(), basePath.toString() },
						new String[] { "PYWORK_HOME=" + basePath.getParent().toString(),
								"KARAF_HOME=" + System.getProperty("karaf.home") });
		} else {
			if (args.length() > 0)
				pyProcess = Runtime.getRuntime()
						.exec(new String[] { "sudo", "-Eu", this.systemUserName, pyEngine.getEngineName(),
								basePath.toString(), args },
								new String[] { "PYWORK_HOME=" + basePath.getParent().toString(),
										"KARAF_HOME=" + System.getProperty("karaf.home"),
										"HOME=/home/" + this.systemUserName });
			else {
				pyProcess = Runtime.getRuntime()
						.exec(new String[] { "sudo", "-Eu", this.systemUserName, pyEngine.getEngineName(),
								basePath.toString() },
								new String[] { "PYWORK_HOME=" + basePath.getParent().toString(),
										"KARAF_HOME=" + System.getProperty("karaf.home"),
										"HOME=/home/" + this.systemUserName });
			}
		}

		BufferedReader sreader = new BufferedReader(new InputStreamReader(pyProcess.getInputStream(), "UTF8"));
		BufferedReader serror = new BufferedReader(new InputStreamReader(pyProcess.getErrorStream(), "UTF8"));
		BufferedWriter sdata = new BufferedWriter(new OutputStreamWriter(pyProcess.getOutputStream(), "UTF8"));

		StringBuffer result = new StringBuffer();
		StringBuffer error = new StringBuffer();

		int cherror, chdata;
		try {
			// Write input data to script by pipe
			if (data != null) {
				sdata.write(data.toString());
				sdata.close();
			}

			// Parallel reading errors and data.
			// Use convention for separating log and error from error stream
			StringBuffer errbuffer = new StringBuffer();
			for (;;) {
				if (serror.ready()) {
					cherror = serror.read();
					errbuffer.append((char) cherror);
					if ((char) cherror == '\n' || (char) cherror == '\r') {
						String line = errbuffer.toString();
						if (line.startsWith("Python ")) {
							logPythonMessage(line);
						} else {
							error.append(line);
						}
						errbuffer = new StringBuffer();
					}
				}

				// Read result
				if (sreader.ready()) {
					chdata = sreader.read();
					result.append((char) chdata);
				}

				if (!pyProcess.isAlive() && !serror.ready() && !sreader.ready())
					break;
			}

			if (errbuffer.length() > 0) {
				error.append(errbuffer.toString());
				String line = errbuffer.toString();
				if (line.startsWith("Python ")) {
					logPythonMessage(line);
				}
			}

		} catch (Exception e) {
			while ((cherror = serror.read()) > -1)
				error.append((char) cherror);
		}

		pyProcess.destroy();
		return new PythonSimpleResult(result.toString(), error.toString());
	}

	private void logPythonMessage(String message) {
		if (message.startsWith("Python INFO"))
			LOGGER.info(message.trim());
		else if (message.startsWith("Python ERROR"))
			LOGGER.error(message.trim());
		else if (message.startsWith("Python WARN"))
			LOGGER.warn(message.trim());
		else
			LOGGER.info(message.trim());
	}
}
