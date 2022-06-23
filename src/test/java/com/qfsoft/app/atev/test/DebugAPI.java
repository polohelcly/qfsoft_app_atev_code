package com.qfsoft.app.atev.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import junit.framework.TestCase;
import qfsoft.framework.ate.MainFramework;
import qfsoft.library.common.method.DealJson;
import qfsoft.library.common.util.CommonMethod;

public class DebugAPI extends TestCase {
	
	protected void setUp() throws Exception {
	}
	
	protected void tearDown() throws Exception {
	}
	
	public void testApp() throws Exception {
		String server_id = "737e1eb2-518d-11e7-9ab3-000c29ce8469";
		String listv = "X0101.A11001,function,1,1";
		JsonObject params = DealJson.cvtJson("{\"data_param\": {\"city\": \"440300\"}}");
		JsonArray results = MainFramework.executeApi(server_id, listv, params);
		CommonMethod.log(results);
	}
	
}
