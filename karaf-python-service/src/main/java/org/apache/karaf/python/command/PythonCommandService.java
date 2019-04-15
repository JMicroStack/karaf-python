package org.apache.karaf.python.command;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.apache.karaf.python.api.PythonVersion;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

@Service
@Command(scope = "python", name = "service", description = "Install python service script from jar.")
public class PythonCommandService implements Action {
	
	private PythonVersion pyEngine = PythonVersion.PYTHON3;

	@Reference
	BundleContext bundleContext;

	@Option(name = "-i", aliases = "--id", description = "Bundle ID.", required = true, multiValued = false)
	Long bundleId;

	@Option(name = "-s", aliases = "--script", description = "Path to script.", required = true, multiValued = false)
	String pathToScript;

	@Override
	public Object execute() throws IOException, URISyntaxException {

		long startTime = System.currentTimeMillis();

		Bundle bundle = bundleContext.getBundle(bundleId);
		String basePath = Paths
				.get("scripts", bundle.getSymbolicName(), bundle.getVersion().toString(), "python", pathToScript)
				.toString();

		String cmd = pyEngine.getEngineName() + " " + basePath;
		Process p = Runtime.getRuntime().exec(cmd);
		return null;
	}
}