package org.apache.karaf.python.api;

import java.io.IOException;
import java.net.URI;

import org.osgi.framework.Bundle;

public interface PythonLambda {
	public Process install(Bundle bundle, URI path) throws IOException;
	public String exec(Bundle bundle, URI path) throws IOException;
}
