package org.apache.karaf.python.api;

import java.io.IOException;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public interface PythonResource {
	public void unpack(Bundle bundle) throws IOException;
}
