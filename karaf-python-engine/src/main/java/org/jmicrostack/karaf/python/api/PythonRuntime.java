package org.jmicrostack.karaf.python.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.osgi.framework.Bundle;


public interface PythonRuntime {
	public PythonProcess run(URI path, String [] env) throws IOException;
}
