package com.qfsoft.app.atev.core.testcase.QF0101;

import java.util.LinkedHashMap;

import javax.swing.table.DefaultTableModel;

import com.google.gson.JsonObject;

import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.framework.ate.instance.TestCase;
import qfsoft.framework.ate.plugin.DatabaseMethod;
import qfsoft.framework.ate.plugin.SeleniumMethod;
import qfsoft.framework.ate.util.FrameworkMethod;
import qfsoft.library.common.method.DealDatabase;
import qfsoft.library.common.method.DealTable;

public class TestCaseC22006 extends TestCase {
	
	// 数据抓取: 设置我的自选股
	public TestCaseC22006(TestExecDetail ted) {
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
		FrameworkMethod.waitfor(ted, "3000");
		SeleniumMethod.goMainframe(ted);
		SeleniumMethod.visit(ted, "http://i.eastmoney.com/stock");
		String sqlv1 = "SELECT * FROM t_invest_stock WHERE guess_batch IS NOT NULL AND last_status='enabled'";
		DefaultTableModel dtmv1 = DatabaseMethod.query(ted, "qfsoft_db_prmp", sqlv1);
		for (int i = 0; i < dtmv1.getRowCount(); i++) {
			String stock_idv = DealTable.getString(dtmv1, i, "stock_id");
			String stock_codev = DealTable.getString(dtmv1, i, "stock_code");
			SeleniumMethod.setText(ted, ".//input[@id='addfavstockinput']", stock_codev.replace("sh", "").replace("sz", ""));
			SeleniumMethod.click(ted, ".//input[@value='添加']");
			LinkedHashMap hmv = new LinkedHashMap();
			DealDatabase.set_field(hmv, "last_status", "value", "enabled");
			String sqlv = DealDatabase.getModifySql("t_invest_stock", hmv, "stock_id='" + DealDatabase.cvtString(stock_idv) + "'");
			DatabaseMethod.edit(ted, "qfsoft_db_prmp", sqlv);
		}
		return null;
	}
	
}