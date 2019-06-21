package org.jmicrostack.karaf.python.tracker;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

public class PythonTrackerActivator implements BundleActivator, BundleListener {

	private PythonBundleTracker bundleTracker;

	@Override
	public void bundleChanged(BundleEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting Bundle Tracker");
		int trackStates = Bundle.STARTING | Bundle.STOPPING | Bundle.RESOLVED | Bundle.INSTALLED | Bundle.UNINSTALLED;
		bundleTracker = new PythonBundleTracker(context, trackStates, null);
		bundleTracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping Bundle Tracker");
		bundleTracker.close();
		bundleTracker = null;
	}

}
