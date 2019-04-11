package org.apache.karaf.python.osgi;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Paths;

import org.apache.karaf.python.api.PythonEngine;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = PythonEngine.class)
public class PythonEngineServiceImpl implements PythonEngine{
	
	private PythonVersion pyEngine = PythonVersion.PYTHON3;

	@Override
	public InputStreamReader run(Bundle bundle) throws IOException {
		String basePath = Paths.get("scripts", bundle.getSymbolicName(), bundle.getVersion().toString(), "python",
				"python-simple", "main.py").toString();
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		String cmd = pyEngine.getEngineName() + " " + basePath;
		Process p = Runtime.getRuntime().exec(cmd);
		return new InputStreamReader(p.getInputStream());
	}

	@Override
	public InputStreamReader run(Bundle bundle, URI path) throws IOException {
		String basePath = Paths.get("scripts", bundle.getSymbolicName(), bundle.getVersion().toString(), "python",
				"python-simple", path.getPath()).toString();
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		String cmd = "python3 " + basePath;
		Process p = Runtime.getRuntime().exec(cmd);
		return new InputStreamReader(p.getInputStream());
	}

}
