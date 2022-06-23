package com.qfsoft.app.atev.core.testfun.X0101;

import com.google.gson.JsonObject;

import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.framework.ate.instance.TestExecObject;
import qfsoft.framework.ate.instance.TestFun;
import qfsoft.framework.ate.util.FrameworkMethod;
import qfsoft.library.common.method.DealJson;

public class TestFunF11001 extends TestFun {
	
	public TestFunF11001(TestExecDetail ted, JsonObject params) {
		super(ted, params);
	}
	
	@Override
	public JsonObject exec() throws Exception {
		DealJson.setValue(ted.params, "X0101.A11001",
				FrameworkMethod.runApiThread(ted, new TestExecObject("X0101.A11001,function,1,1"), DealJson.cvtJson("{\"data_param\": {\"city\": \"" + DealJson.getString(params, "城市编码") + "\"}}")));
		JsonObject result = DealJson.cvtJson(String.valueOf(DealJson.getPath(DealJson.getJsonArray(ted.params, "X0101.A11001"), "$.[0].[0].response")));
		return result;
	}
	
}