package org.apache.karaf.python.osgi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.karaf.python.api.PythonResourceActivator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;

public class PythonResourceActivatorImpl implements PythonResourceActivator{

    public void unpack(Bundle bundle) throws IOException {
        System.out.println("\nPython: Unpack resources from <" + bundle.getSymbolicName() + "> [START]");
        
		String basePath = Paths.get("scripts", bundle.getSymbolicName(), bundle.getVersion().toString()).toString();
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		Collection<String> resources = bundleWiring.listResources("/python", "*", BundleWiring.LISTRESOURCES_RECURSE);
		for (String resource : resources) {
			if (resource.endsWith("\\") || resource.endsWith("/")) {
				String destinationPath = Paths.get(basePath, resource).toString();
				System.out.print("Create directory: " + resource);
				File file = new File(destinationPath);
				file.mkdirs();
				System.out.println(" [OK]");
			} else {
				System.out.print("Copy file       : " + resource);
				InputStream stream = bundle.getEntry(resource).openStream();
				String result = new BufferedReader(new InputStreamReader(stream)).lines()
						.collect(Collectors.joining("\n"));

				String destinationPath = Paths.get(basePath, resource).toString();
				BufferedWriter writer = new BufferedWriter(new FileWriter(destinationPath));
				writer.write(result);
				writer.close();
				System.out.println(" [OK]");
			}
		}
		System.out.println("Python: Unpack resources [FINISH]\n");
    }
}
