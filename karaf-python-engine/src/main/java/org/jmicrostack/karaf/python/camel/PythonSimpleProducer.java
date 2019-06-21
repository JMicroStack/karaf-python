package org.jmicrostack.karaf.python.camel;

import java.net.URI;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.jmicrostack.karaf.python.PythonRuntimeException;
import org.jmicrostack.karaf.python.PythonSimple;
import org.jmicrostack.karaf.python.PythonSimpleResult;
import org.jmicrostack.karaf.python.api.PythonVersion;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PythonSimpleProducer extends DefaultProducer {

	private Map<String, Object> parameters;
	private static final Logger LOGGER = LoggerFactory.getLogger(PythonSimpleProducer.class);

	public PythonSimpleProducer(Endpoint endpoint, Map<String, Object> parameters) {
		super(endpoint);
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		String argv = ((PythonSimpleEndpoint) this.getEndpoint()).getArgv();
		String userName = ((PythonSimpleEndpoint) this.getEndpoint()).getUser();
		
		StringBuffer pythonHeaders = new StringBuffer();
		if ( argv == null) {
			pythonHeaders.append("{");
			for(Map.Entry<String, Object> header : exchange.getIn().getHeaders().entrySet()) {
				exchange.getOut().setHeader(header.getKey(), header.getValue());
				if (header.getKey().startsWith("python_")){
					pythonHeaders.append("\"" + header.getKey().substring(7, header.getKey().length()) + "\":");
					pythonHeaders.append("\"" + header.getValue().toString() + "\"");
					pythonHeaders.append(",");
				}
			}
			pythonHeaders.append("\"_\":\"python\"");
			pythonHeaders.append("}");
			argv = pythonHeaders.toString();
		}

		PythonSimple pySimple = new PythonSimple(PythonVersion.PYTHON3,
				((PythonSimpleEndpoint) this.getEndpoint()).getContext().getBundle());
		pySimple.setSystemUserName(userName);
		pySimple.setPythonVersion(((PythonSimpleComponent) ((PythonSimpleEndpoint) this.getEndpoint()).getComponent())
				.getPythonVersion());
		PythonSimpleResult result = pySimple.run(new URI(((PythonSimpleEndpoint) this.getEndpoint()).getPath()),
				exchange.getIn().getBody(), argv, null);
		exchange.getOut().setBody(result.getResult());
		
		if (result.isError()) {
			throw new PythonRuntimeException(result.getError());
		} 
	}
}
