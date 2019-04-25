package org.jmicrostack.karaf.python.command;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jmicrostack.karaf.python.PythonSimple;
import org.jmicrostack.karaf.python.PythonSimpleResult;
import org.jmicrostack.karaf.python.api.PythonVersion;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.osgi.framework.BundleContext;

@Service
@Command(scope = "python", name = "run", description = "Run python script from jar.")
public class PythonCommandRun implements Action {

	private PythonVersion pyEngine = PythonVersion.PYTHON37;

	@Reference
	BundleContext bundleContext;

	@Option(name = "-i", aliases = "--id", description = "Bundle ID.", required = true, multiValued = false)
	Long bundleId;

	@Option(name = "-s", aliases = "--script", description = "Path to script.", required = true, multiValued = false)
	String pathToScript;
	
	@Option(name = "-p", aliases = "--params", description = "Parameters for script.", required = false, multiValued = false)
	String parameters;
	
	@Option(name = "-d", aliases = "--data", description = "Data for script.", required = false, multiValued = false)
	String data;

	@Override
	public Object execute() throws IOException, URISyntaxException {

		long startTime = System.currentTimeMillis();

		PythonSimple pySimple = new PythonSimple(pyEngine, bundleContext.getBundle(bundleId));
		pySimple.setPythonVersion(pyEngine);
		PythonSimpleResult pyResult = pySimple.run(new URI(pathToScript), data, parameters, null);

		long endTime = System.currentTimeMillis();
		
		if (pyResult.isError()) {
			System.out.println(pyResult.getError());
		} else {
			System.out.println(pyResult.getResult());
		}

		System.out.println("Execute time: " + (endTime - startTime) + "ms");

		return null;
	}
}
