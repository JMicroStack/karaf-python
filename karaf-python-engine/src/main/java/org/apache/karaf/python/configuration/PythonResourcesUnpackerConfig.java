package org.apache.karaf.python.configuration;

import org.apache.karaf.python.api.PythonResource;
import org.apache.karaf.util.tracker.BaseActivator;
import org.apache.karaf.util.tracker.annotation.ProvideService;
import org.apache.karaf.util.tracker.annotation.Services;


@Services(provides = { @ProvideService(PythonResource.class) })
public class PythonResourcesUnpackerConfig extends BaseActivator {

	@Override
	public void doStart() {
		//PythonResourcesUnpacker pythonResourceUnpacker = new PythonResourcesUnpacker();
		//register(PythonResource.class, pythonResourceUnpacker);
	}
}