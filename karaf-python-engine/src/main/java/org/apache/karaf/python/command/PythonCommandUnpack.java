package org.apache.karaf.python.command;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.karaf.python.PythonResource;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

@Service
@Command(scope = "python", name = "unpack", description = "Unpack python script from jar.")
public class PythonCommandUnpack implements Action {
	
	@Reference
	BundleContext bundleContext;

	@Option(name = "-i", aliases = "--id", description = "Unpack python script from bundle.", required = true, multiValued = false)
	Long bundleId;

	@Override
	public Object execute() throws IOException, URISyntaxException {
		Bundle bundle = bundleContext.getBundle(bundleId);
		PythonResource pyResource = new PythonResource();
		pyResource.unpack(bundle);
		return null;
	}
}
