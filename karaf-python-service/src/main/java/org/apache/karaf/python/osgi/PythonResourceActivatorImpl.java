package org.apache.karaf.python.osgi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.karaf.python.api.PythonResourceActivator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = PythonResourceActivator.class)
public class PythonResourceActivatorImpl implements PythonResourceActivator {

	@Activate
	public void start(BundleContext context) {
		System.out.println("Python Component is active: [OK]");
	}

	public void unpack(Bundle bundle) throws IOException {
		System.out.println("\nPython: Unpack resources from <" + bundle.getSymbolicName() + ">");

		String basePath = Paths.get("scripts", bundle.getSymbolicName(), bundle.getVersion().toString()).toString();
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		List<Path> installScripts = new ArrayList<>();
		Collection<String> resources = bundleWiring.listResources("/python", "*", BundleWiring.LISTRESOURCES_RECURSE);
		for (String resource : resources) {
			if (resource.endsWith("\\") || resource.endsWith("/")) {
				String destinationPath = Paths.get(basePath, resource).toString();
				System.out.println("Create directory: " + resource);
				File file = new File(destinationPath);
				file.mkdirs();
			} else {
				System.out.println("Copy file       : " + resource);
				InputStream stream = bundle.getEntry(resource).openStream();
				String result = new BufferedReader(new InputStreamReader(stream)).lines()
						.collect(Collectors.joining("\n"));

				String destinationPath = Paths.get(basePath, resource).toString();
				BufferedWriter writer = new BufferedWriter(new FileWriter(destinationPath));
				writer.write(result);
				writer.close();
				if (resource.endsWith("install.sh"))
					installScripts.add(Paths.get(basePath, resource).toAbsolutePath());
			}
		}
		if (installScripts.size() > 0) {
			postUnpack(installScripts);
		}
		System.out.println("Python: Unpack resources [FINISH]\n");
	}

	private boolean postUnpack(List<Path> installScripts) throws IOException {
		for (Path installscript : installScripts) {
			String cmd = "bash " + installscript.toString();
			System.out.println("Post install: " + installscript);
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			String line;
			while ((line = reader.readLine()) != null)
				System.out.println(line);
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			while ((line = error.readLine()) != null)
				System.out.println(line);
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			p.destroy();
		}
		return true;
	}
}
