package com.qfsoft.app.atev.core.testcase.X0101;

import com.google.gson.JsonObject;

import qfsoft.framework.ate.instance.TestCase;
import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.framework.ate.instance.TestExecObject;
import qfsoft.framework.ate.util.FrameworkMethod;
import qfsoft.library.common.method.DealJson;

public class TestCaseC11001 extends TestCase {
	
	// API测试：测试高德搜索天气功能
	public TestCaseC11001(TestExecDetail ted) {
		super(ted);
	}
	
	@Override
	public JsonObject exec() throws Exception {
		DealJson.setValue(ted.params, "X0101.F11001", FrameworkMethod.runFunThread(ted, new TestExecObject("X0101.F11001,function,1,1"), DealJson.cvtJson("{\"城市编码\": \"440300\"}")));
		FrameworkMethod.checkEquals(ted, "检查接口返回码是否正确", "200", "${response.code(X0101.F11001)}");
		FrameworkMethod.checkEquals(ted, "检查接口返回值是否正确", "1", "${response.json(X0101.F11001,$.status)}");
		return null;
	}
	
}