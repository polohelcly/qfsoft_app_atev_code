package com.qfsoft.app.atev.test;

import junit.framework.TestCase;
import qfsoft.framework.ate.MainFramework;

public class DebugTaskCase extends TestCase {
	
	protected void setUp() throws Exception {
	}
	
	protected void tearDown() throws Exception {
	}
	
	public void testApp() throws Exception {
		String server_id = "737e1eb2-518d-11e7-9ab3-000c29ce8469";
		String task_id = "d44d940a-ace8-4992-98f9-03e278b6cc29";
		String listv = "X0101.C11001,function,1,1";
		MainFramework.executeTaskCase(server_id, task_id, listv);
	}
	
}