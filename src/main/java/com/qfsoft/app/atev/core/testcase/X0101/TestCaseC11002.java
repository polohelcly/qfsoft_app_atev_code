package com.qfsoft.app.atev.core.testcase.X0101;

import com.google.gson.JsonObject;

import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.framework.ate.instance.TestCase;
import qfsoft.framework.ate.plugin.SeleniumMethod;
import qfsoft.framework.ate.util.FrameworkMethod;

public class TestCaseC11002 extends TestCase {
	
	// GUI测试：测试百度搜索功能
	public TestCaseC11002(TestExecDetail ted) {
		super(ted);
	}
	
	@Override
	public JsonObject exec() throws Exception {
		SeleniumMethod.visit(ted, FrameworkMethod.getObjectUrl("百度首页"));
		SeleniumMethod.setText(ted, FrameworkMethod.getObjectDetailLocator("百度首页", "搜索输入"), "test");
		SeleniumMethod.click(ted, FrameworkMethod.getObjectDetailLocator("百度首页", "搜索按钮"));
		boolean result = SeleniumMethod.isWaitforExistShort(ted, FrameworkMethod.getObjectDetailLocator("百度首页", "搜索结果"));
		FrameworkMethod.checkTrue(ted, "检查搜索结果显示", result);
		return null;
	}
	
}