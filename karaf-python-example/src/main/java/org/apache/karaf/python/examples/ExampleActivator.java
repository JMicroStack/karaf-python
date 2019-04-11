package org.apache.karaf.python.examples;

import java.io.IOException;
import org.apache.karaf.python.api.PythonResourceActivator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ExampleActivator implements BundleActivator {
	
    @Override
    public void start(BundleContext bundleContext) throws IOException {
    	System.out.println("Python install example.");
        Bundle bundle = bundleContext.getBundle();
        
        ServiceReference reference = bundleContext.getServiceReference(PythonResourceActivator.class.getName());
        PythonResourceActivator pythonResource = (PythonResourceActivator) bundleContext.getService(reference);
        pythonResource.unpack(bundle);
    }

    @Override
    public void stop(BundleContext bundleContext) {
    	System.out.println("Python uninstall example.");
    }

}