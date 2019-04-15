package org.apache.karaf.python.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.osgi.framework.wiring.BundleWiring;

@Service
@Command(scope = "python", name = "run", description = "Run python script from jar.")
public class PythonCommandRun implements Action {
	
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
				.get("scripts", bundle.getSymbolicName(), bundle.getVersion().toString(), "python", pathToScript).toAbsolutePath()
				.toString();

		//String cmd = pyEngine.getEngineName() + " " + basePath;
		String cmd = "/usr/local/lib/python/3.7.1/bin/python3.7" + " " + basePath;
		System.out.println(cmd);
		Process p = Runtime.getRuntime().exec(cmd);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		String line;
		while ((line = reader.readLine()) != null)
			System.out.println(line);
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while ((line = error.readLine()) != null)
			System.out.println(line);
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		p.destroy();

		long endTime = System.currentTimeMillis();

		System.out.println("Execute time: " + (endTime - startTime) + "ms");

		return null;
	}
}
