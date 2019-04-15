package org.apache.karaf.python.examples;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.karaf.python.api.PythonEngine;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate=true)
public class ExampleRun {

	@Reference
	PythonEngine pythonEngine;
	
	@Activate
	public void start(BundleContext bundleContext) throws IOException, URISyntaxException {
		System.out.println("Start execute python script:");
		String result = pythonEngine.exec(bundleContext.getBundle(), new URI("python-simple/main.py"));
		System.out.println(result);
	}
}
