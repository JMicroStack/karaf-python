package org.jmicrostack.karaf.python.examples.camel;

import org.jmicrostack.karaf.python.PythonResource;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


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
