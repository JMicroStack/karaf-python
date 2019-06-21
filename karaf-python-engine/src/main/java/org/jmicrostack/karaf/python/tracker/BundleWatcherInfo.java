package org.jmicrostack.karaf.python.tracker;

import org.osgi.framework.Bundle;

public class BundleWatcherInfo {
	private static String stateAsString(Bundle bundle) {
		if (bundle == null) {
			return "null";
		}
		int state = bundle.getState();
		switch (state) {
		case Bundle.ACTIVE:
			return "ACTIVE";
		case Bundle.INSTALLED:
			return "INSTALLED";
		case Bundle.RESOLVED:
			return "RESOLVED";
		case Bundle.STARTING:
			return "STARTING";
		case Bundle.STOPPING:
			return "STOPPING";
		case Bundle.UNINSTALLED:
			return "UNINSTALLED";
		default:
			return "unknown bundle state: " + state;
		}
	}
/**
	private static String typeAsString(int eventType) {
		switch (eventType) {
		case START_INSTALLING:
			return "START_INSTALLING";
		case END_INSTALLING:
			return "END_INSTALLING";
		case START_ACTIVATION:
			return "START_ACTIVATION";
		case END_ACTIVATION:
			return "END_ACTIVATION";
		case START_DEACTIVATION:
			return "START_DEACTIVATION";
		case END_DEACTIVATION:
			return "END_DEACTIVATION";
		case START_UNINSTALLING:
			return "START_UNINSTALLING";
		case END_UNINSTALLING:
			return "END_UNINSTALLING";
		default:
			return "unknown bundle watcher event type: " + eventType;
		}
	}**/
}
