package org.apache.karaf.python.camel;

import java.util.Map;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriPath;
import org.osgi.framework.BundleContext;

public class PythonSimpleEndpoint extends DefaultEndpoint {

	private Map<String, Object> parameters;
	private String endpointUri;
	private String path;
	private BundleContext context;
	
	 @UriPath(name = "argv")
	 String argv;

	public PythonSimpleEndpoint(final Component component, final String endpointUri, final String path,
			Map<String, Object> parameters) {
		super(endpointUri, component);
		this.parameters = parameters;
		this.endpointUri = endpointUri;
		this.path = path;
		this.context = ((PythonSimpleComponent) component).getContext();
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public String getEndpointUri() {
		return endpointUri;
	}

	public String getPath() {
		return path;
	}

	public BundleContext getContext() {
		return context;
	}
	
	public String getArgv() {
		return this.argv;
	}
	
	public void setArgv(String argv) {
		this.argv = argv;
	}

	@Override
	public Producer createProducer() throws Exception {
		return new PythonSimpleProducer(this, parameters);
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		return new PythonSimpleConsumer(this, processor);
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
