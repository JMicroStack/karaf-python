package org.apache.karaf.python.osgi;

import org.apache.karaf.python.api.PythonResourceActivator;
import org.apache.karaf.util.tracker.BaseActivator;
import org.apache.karaf.util.tracker.annotation.ProvideService;
import org.apache.karaf.util.tracker.annotation.Services;


@Services(provides = { @ProvideService(PythonResourceActivator.class) })
public class PythonBaseActivator extends BaseActivator {

	@Override
	public void doStart() {
		System.out.println("Python Base Activator: [START]\n");
		PythonResourceActivatorImpl pythonResourceActivator = new PythonResourceActivatorImpl();
		register(PythonResourceActivator.class, pythonResourceActivator);
	}
}