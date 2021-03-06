package org.jmicrostack.karaf.python.command;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.support.table.ShellTable;

@Service
@Command(scope = "python", name = "list", description = "List python modules.")
public class PythonCommandList implements Action {
	@Override
	public Object execute() throws Exception {
		ShellTable table = new ShellTable();
		table.column("ID");
		table.column("Bundle");
		table.column("Name");
		table.print(System.out);
		return null;
	}
}
