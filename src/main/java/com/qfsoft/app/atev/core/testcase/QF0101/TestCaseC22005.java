package com.qfsoft.app.atev.core.testcase.QF0101;

import com.google.gson.JsonObject;

import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.framework.ate.instance.TestCase;
import qfsoft.framework.ate.plugin.SeleniumMethod;

public class TestCaseC22005 extends TestCase {
	
	// 数据抓取: 清除我的自选股
	public TestCaseC22005(TestExecDetail ted) {
		super(ted);
	}
	
	@Override
	public JsonObject exec() throws Exception {
		SeleniumMethod.visit(ted, "https://passport2.eastmoney.com/pub/login");
		SeleniumMethod.goFrame(ted, "frame_login");
		SeleniumMethod.click(ted, ".//span[text()='账号密码登录']");
		SeleniumMethod.setText(ted, ".//input[@id='txt_account']", "18988793306");
		SeleniumMethod.setText(ted, ".//input[@id='txt_pwd']", "polo8010");
		SeleniumMethod.click(ted, ".//button[@id='btn_login']");
		SeleniumMethod.click(ted, ".//div[@class='em_init_icon']");
		SeleniumMethod.goMainframe(ted);
		SeleniumMethod.visit(ted, "http://i.eastmoney.com/stock");
		if (SeleniumMethod.isWaitforExistShort(ted, ".//input[@type='checkbox'][1]")) {
			SeleniumMethod.click(ted, ".//input[@type='checkbox'][1]");
			SeleniumMethod.click(ted, ".//span[@id='plsetting']");
			SeleniumMethod.click(ted, ".//a[@class='deletestock']");
			SeleniumMethod.click(ted, ".//a[@class='modalbtn_default']");
			SeleniumMethod.click(ted, ".//a[@class='modalbtn_default']");
		}
		return null;
	}
	
}