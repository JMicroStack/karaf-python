package org.apache.karaf.python.examples.camel;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.apache.karaf.python.PythonResource;

public class Activator implements BundleActivator{

	@Override
	public void start(BundleContext context) throws Exception {
		PythonResource pyResource = new PythonResource();
		pyResource.unpack(context.getBundle());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}

}
