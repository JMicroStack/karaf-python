package org.jmicrostack.karaf.python;

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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.camel.util.FileUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleRequirement;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWire;
import org.osgi.framework.wiring.BundleWiring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PythonResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(PythonResource.class); 
	
	public void unpack(Bundle bundle) throws IOException {
		String basePath = Paths.get(System.getProperty("karaf.home"), "scripts", bundle.getSymbolicName(), bundle.getVersion().toString()).toString();
		LOGGER.info("Python install resources to: " + basePath.toString());
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
		List<Path> installScripts = new ArrayList<>();
		Collection<String> resources = bundleWiring.listResources("/python", "*", BundleWiring.LISTRESOURCES_RECURSE);
		for (String resource : resources) {
			if (resource.endsWith("\\") || resource.endsWith("/")) {
				String destinationPath = Paths.get(basePath, resource).toString();
				LOGGER.debug("md: " + resource);
				File file = new File(destinationPath);
				file.mkdirs();
			} else {
				InputStream stream = bundle.getEntry(resource).openStream();
				String result = new BufferedReader(new InputStreamReader(stream)).lines()
						.collect(Collectors.joining("\n"));

				String destinationPath = Paths.get(basePath, resource).toString();
				LOGGER.debug("cp: " + destinationPath);
				
				String dirPaths = Paths.get(basePath, resource).getParent().toString();
				File file = new File(dirPaths);
				file.mkdirs();
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(destinationPath));
				writer.write(result);
				writer.close();
				if (resource.endsWith("install.sh"))
					installScripts.add(Paths.get(basePath, resource).toAbsolutePath());
			}
		}
		if (installScripts.size() > 0) {
			Set<Bundle> dependency = dependencyResolver(bundle);
			postUnpack(basePath, installScripts, dependency);
		}
	}
	
	public void remove(Bundle bundle) {
		String modulePath = Paths.get(System.getProperty("karaf.home"), "scripts", bundle.getSymbolicName(), bundle.getVersion().toString()).toString();
		LOGGER.info("Python uninstall resources from: " + modulePath);
		FileUtil.removeDir(new File(modulePath));
	}

	/**
	 * Imported module must be set convention:
	 * <Bundle-SymbolicName>python-${project.artifactId}</Bundle-SymbolicName>
	 * From the package which us use this module must be import by example:
	 * <Import-Package>*, org.python.module</Import-Package>
	 * @param bundle
	 * @return
	 */
	private Set<Bundle> dependencyResolver(Bundle bundle) {
		BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);

		if (bundleWiring == null) {
			return Collections.emptySet();
		}

		List<BundleWire> bundleWires = bundleWiring.getRequiredWires(BundleRevision.PACKAGE_NAMESPACE);

		if (bundleWires == null) {
			return Collections.emptySet();
		}

		Set<Bundle> bundleDependencies = new HashSet<Bundle>();

		for (BundleWire bundleWire : bundleWires) {

			BundleRevision provider = bundleWire.getProvider();

			if (provider == null) {
				continue;
			}

			Bundle providerBundle = provider.getBundle();
			BundleRequirement requirement = bundleWire.getRequirement();

			if (requirement != null) {

				Map<String, String> directives = requirement.getDirectives();
				String resolution = directives.get("resolution");

				if ("dynamic".equalsIgnoreCase(resolution)) {
					continue;
				}
			}

			if (providerBundle.getSymbolicName().startsWith("python-")) {
				LOGGER.info("Python modules dependency: " + providerBundle.getSymbolicName() + " (" + providerBundle.getVersion().toString() + ")");
				bundleDependencies.add(providerBundle);
			}
		}
		return Collections.unmodifiableSet(bundleDependencies);
	}

	private boolean postUnpack(String basePath, List<Path> installScripts, Set<Bundle> pythonDependency) throws IOException {
		String pathsToPythonModules = String.join(" ", getPathsForPythonModules(pythonDependency));
		for (Path installscript : installScripts) {
			LOGGER.info("Python module post install: " + installscript);
			
			Process p = Runtime.getRuntime().exec(
					new String[] { "bash", installscript.toString(), pathsToPythonModules },
					new String[] { "PYWORK_HOME=" + basePath, "KARAF_HOME=" + System.getProperty("karaf.home")});
			
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
	
	private List<String> getPathsForPythonModules(Set<Bundle> pythonDependency){
		List<String> pathToPythonModules = new ArrayList<String>();
		for(Bundle bundle : pythonDependency) {
			pathToPythonModules.add(Paths.get("scripts", bundle.getSymbolicName(), bundle.getVersion().toString(), "python").toString());
		}
		return pathToPythonModules;
	}
}
