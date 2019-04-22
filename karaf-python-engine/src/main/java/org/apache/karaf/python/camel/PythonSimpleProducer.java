package org.apache.karaf.python.camel;

import java.net.URI;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.karaf.python.PythonSimple;
import org.apache.karaf.python.PythonSimpleResult;
import org.apache.karaf.python.api.PythonVersion;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PythonSimpleProducer extends DefaultProducer {

	private Map<String, Object> parameters;
	private static final Logger LOGGER = LoggerFactory.getLogger(PythonSimple.class);

	public PythonSimpleProducer(Endpoint endpoint, Map<String, Object> parameters) {
		super(endpoint);
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		String argv = ((PythonSimpleEndpoint)this.getEndpoint()).getArgv();
		
		PythonSimple pySimple = new PythonSimple(PythonVersion.PYTHON3,
				((PythonSimpleEndpoint) this.getEndpoint()).getContext().getBundle());
		PythonSimpleResult result = pySimple.run(new URI(((PythonSimpleEndpoint) this.getEndpoint()).getPath()),
				exchange.getIn().getBody().toString(), argv, null);
		exchange.getOut().setBody(result.getResult());
		if (result.isError()) {
			LOGGER.error(result.getError());
		}
	}
}
