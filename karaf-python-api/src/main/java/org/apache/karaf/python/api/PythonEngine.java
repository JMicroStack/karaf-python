package org.apache.karaf.python.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.osgi.framework.Bundle;


public interface PythonEngine {
	public InputStreamReader run(Bundle bundle) throws IOException; 
	public InputStreamReader run(Bundle bundle, URI path) throws IOException;
}
