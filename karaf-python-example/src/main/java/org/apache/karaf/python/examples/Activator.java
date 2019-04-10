package org.apache.karaf.python.examples;


import java.io.IOException;
import org.apache.karaf.python.api.PythonResourceActivator;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;


@Component(immediate = true, service = Activator.class)
public class Activator implements BundleActivator {
	
	@Reference
	PythonResourceActivator resourceActivator;

    @Override
    public void start(BundleContext bundleContext) throws IOException {
        Bundle bundle = bundleContext.getBundle();
        System.out.print("Wypakowywuje");
        System.out.print(resourceActivator);
        resourceActivator.unpack(bundle);
        System.out.print("Wypakowywuje ok");
    }

    @Override
    public void stop(BundleContext bundleContext) {
    	System.out.println("Python: [STOP]");
    }

}