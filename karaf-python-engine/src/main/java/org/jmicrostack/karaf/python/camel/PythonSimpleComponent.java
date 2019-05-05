package org.jmicrostack.karaf.python.camel;


import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.jmicrostack.karaf.python.api.PythonVersion;
import org.osgi.framework.BundleContext;


public class PythonSimpleComponent extends DefaultComponent{

	private BundleContext context;
	private PythonVersion pyVersion = PythonVersion.PYTHON3;
	
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
	
	public void setPythonVersion(PythonVersion pyVersion) {
		this.pyVersion = pyVersion;
	}
	
	public PythonVersion getPythonVersion() {
		return this.pyVersion;
	}
	
	public void setPythonEngineVersion(String pythonVersion) {
		this.pyVersion = PythonVersion.valueOf(pythonVersion);
	}
	
	public String getPythonEngineVersion() {
		return this.pyVersion.toString();
	}
}
