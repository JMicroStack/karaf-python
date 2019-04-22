package org.apache.karaf.python;

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

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PythonResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PythonResource.class);
	
	public void unpack(Bundle bundle) throws IOException {
		LOGGER.info("Python: Unpack resources from <" + bundle.getSymbolicName() + "/" + bundle.getVersion().toString() + ">");

		String basePath = Paths.get("scripts", bundle.getSymbolicName(), bundle.getVersion().toString()).toString();
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		List<Path> installScripts = new ArrayList<>();
		Collection<String> resources = bundleWiring.listResources("/python", "*", BundleWiring.LISTRESOURCES_RECURSE);
		for (String resource : resources) {
			if (resource.endsWith("\\") || resource.endsWith("/")) {
				String destinationPath = Paths.get(basePath, resource).toString();
				LOGGER.info("MD: " + resource);
				File file = new File(destinationPath);
				file.mkdirs();
			} else {
				LOGGER.info("CP: " + resource);
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
	}

	private boolean postUnpack(List<Path> installScripts) throws IOException {
		for (Path installscript : installScripts) {
			String cmd = "bash " + installscript.toString();
			LOGGER.info("Post install: " + installscript);
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			String line;
			while ((line = reader.readLine()) != null)
				LOGGER.info(line);
			try {
				p.waitFor();
			} catch (InterruptedException e) {
				LOGGER.info(e.getMessage());
			}
			while ((line = error.readLine()) != null)
				LOGGER.info(line);
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
