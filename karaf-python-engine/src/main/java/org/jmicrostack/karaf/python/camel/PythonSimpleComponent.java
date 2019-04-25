package org.jmicrostack.karaf.python.camel;


import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.osgi.framework.BundleContext;


public class PythonSimpleComponent extends DefaultComponent{

	private BundleContext context;
	
	@Override
	protected Endpoint createEndpoint(String uri, String path, Map<String, Object> parameters) throws Exception {
		return new PythonSimpleEndpoint(this, uri, path, parameters);
	}
	
	public void setContext(BundleContext context) {
		this.context = context;
	}
	
	public BundleContext getContext() {
		return this.context;
	}
}
