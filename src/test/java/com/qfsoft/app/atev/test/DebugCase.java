package com.qfsoft.app.atev.test;

import com.google.gson.JsonArray;

import junit.framework.TestCase;
import qfsoft.framework.ate.MainFramework;
import qfsoft.library.common.util.CommonMethod;

public class DebugCase extends TestCase {
	
	protected void setUp() throws Exception {
	}
	
	protected void tearDown() throws Exception {
	}
	
	public void testApp() throws Exception {
		String server_id = "737e1eb2-518d-11e7-9ab3-000c29ce8469";
		String listv = "X0101.C11001,function,1,1";
		JsonArray results = MainFramework.executeCase(server_id, listv);
		CommonMethod.log(results);
	}
	
}