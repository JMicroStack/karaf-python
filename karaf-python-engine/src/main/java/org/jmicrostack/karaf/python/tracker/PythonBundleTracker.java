package org.jmicrostack.karaf.python.tracker;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

public final class PythonBundleTracker extends BundleTracker {

	public PythonBundleTracker(BundleContext context, int stateMask, BundleTrackerCustomizer customizer) {
		super(context, stateMask, customizer);
	}

	public Object addingBundle(Bundle bundle, BundleEvent event) {
		// Typically we would inspect bundle, to figure out if we want to
		// track it or not. If we don't want to track return null, otherwise
		// return an object.
		print(bundle, event);
		return bundle;
	}

	private void print(Bundle bundle, BundleEvent event) {
		String symbolicName = bundle.getSymbolicName();
		String state = BundleTrackerInfo.stateAsString(bundle);
		String type = BundleTrackerInfo.eventAsString(event);
		System.out.println("[BT] " + symbolicName + ", state: " + state + ", event.type: " + type);
	}

	public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
		print(bundle, event);
	}

	public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
		print(bundle, event);
	}

}