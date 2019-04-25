package org.jmicrostack.karaf.python.api;

import java.io.IOException;
import java.net.URI;

public interface PythonEngine {
	public PythonProcess run(URI path, String [] env) throws IOException;
}
