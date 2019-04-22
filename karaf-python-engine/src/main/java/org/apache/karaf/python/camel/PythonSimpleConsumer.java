package org.apache.karaf.python.camel;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

public class PythonSimpleConsumer extends DefaultConsumer{

	public PythonSimpleConsumer(Endpoint endpoint, Processor processor) {
		super(endpoint, processor);
	}
	
	@Override
	protected void doStart() throws Exception {
		super.doStart();
	}
	
	@Override
	protected void doStop() throws Exception {
		super.doStop();
	}
}
