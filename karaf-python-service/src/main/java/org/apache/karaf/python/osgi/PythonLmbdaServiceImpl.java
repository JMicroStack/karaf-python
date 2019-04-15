package org.apache.karaf.python.osgi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Paths;

import org.apache.karaf.python.api.PythonLambda;
import org.apache.karaf.python.api.PythonVersion;
import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = PythonLambda.class)
public class PythonLmbdaServiceImpl implements PythonLambda {

	private PythonVersion pyEngine = PythonVersion.PYTHON3;

	@Override
	public String exec(Bundle bundle, URI path) throws IOException {
		String basePath = Paths
				.get("scripts", bundle.getSymbolicName(), bundle.getVersion().toString(), "python", path.getPath())
				.toString();

		String cmd = pyEngine.getEngineName() + " " + basePath;
		Process p = Runtime.getRuntime().exec(cmd);
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		StringBuffer result = new StringBuffer();
		String line;
		while ((line = reader.readLine()) != null) {
			result.append(line);
			result.append("\n");
		}
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		p.destroy();
		return result.toString();
	}

	@Override
	public Process install(Bundle bundle, URI path) throws IOException {
		String basePath = Paths
				.get("scripts", bundle.getSymbolicName(), bundle.getVersion().toString(), "python", path.getPath())
				.toString();

		String cmd = pyEngine.getEngineName() + " " + basePath;
		Process process = Runtime.getRuntime().exec(cmd);

		return process;
	}

}
