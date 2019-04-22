package org.apache.karaf.python.examples;

import java.io.IOException;

import org.apache.karaf.python.api.PythonResource;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ExampleActivator implements BundleActivator {
	
    @Override
    public void start(BundleContext bundleContext) throws IOException {
    	System.out.println("Python install source example.");
        Bundle bundle = bundleContext.getBundle();
        ServiceReference reference = bundleContext.getServiceReference(PythonResource.class.getName());
        PythonResource pythonResource = (PythonResource) bundleContext.getService(reference);
        pythonResource.unpack(bundle);
    }

    @Override
    public void stop(BundleContext bundleContext) {
    	System.out.println("Python uninstall source example.");
    }

}