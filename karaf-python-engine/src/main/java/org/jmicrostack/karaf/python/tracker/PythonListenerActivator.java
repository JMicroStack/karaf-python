package org.jmicrostack.karaf.python.tracker;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import org.apache.camel.util.FileUtil;
import org.jmicrostack.karaf.python.PythonResource;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PythonListenerActivator implements BundleActivator, BundleListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(PythonListenerActivator.class);
	private HashMap pythonBundle = new HashMap<>();

	@Override
	public void bundleChanged(BundleEvent event) {
		String bundleKey = event.getBundle().getSymbolicName() + ":" + event.getBundle().getVersion().toString();
		LOGGER.info("Process module <" + BundleListenerInfo.typeAsString(event) + ">: " + bundleKey);
		if (event.getBundle() != null && (event.getType() == event.INSTALLED || event.getType() == event.UPDATED)) {
			URL pythonResource = event.getBundle().getResource("python");
			if (pythonResource != null) {
				if (event.getType() == event.INSTALLED)
					LOGGER.info("Python install module: " + bundleKey);
				else
					LOGGER.info("Python update module: " + bundleKey);
				pythonBundle.put(bundleKey, event.getBundle());
				installPythonResource(event.getBundle());
			}
		} else if (event.getType() == event.UNINSTALLED) {
			LOGGER.info("Python uninstall module: " + bundleKey);
			if (pythonBundle.containsKey(bundleKey))
				pythonBundle.remove(bundleKey);
			uninstallPythonResource(event.getBundle());
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		LOGGER.info("Python listener START");
		context.addBundleListener(this);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		LOGGER.info("Python listener STOP");
		context.removeBundleListener(this);
	}

	private void installPythonResource(Bundle bundle) {
		PythonResource pyRes = new PythonResource();
		try {
			pyRes.unpack(bundle);
		} catch (IOException e) {
			LOGGER.error(e.toString());
			e.printStackTrace();
			try {
				bundle.stop();
			} catch (BundleException e1) {
				LOGGER.error(e.toString());
				e.printStackTrace();
			}
		}
	}

	private void uninstallPythonResource(Bundle bundle) {
		PythonResource pyRes = new PythonResource();
		pyRes.remove(bundle);
	}
}
