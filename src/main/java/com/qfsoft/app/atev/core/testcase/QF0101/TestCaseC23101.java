package com.qfsoft.app.atev.core.testcase.QF0101;

import java.util.LinkedHashMap;

import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Level;

import com.google.gson.JsonObject;

import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.framework.ate.instance.TestCase;
import qfsoft.framework.ate.plugin.DatabaseMethod;
import qfsoft.framework.ate.plugin.SeleniumMethod;
import qfsoft.framework.ate.util.FrameworkMethod;
import qfsoft.library.common.method.DealDatabase;
import qfsoft.library.common.method.DealJson;
import qfsoft.library.common.method.DealString;
import qfsoft.library.common.method.DealTable;

public class TestCaseC23101 extends TestCase {
	
	// 数据抓取: 获取合约数据
	public TestCaseC23101(TestExecDetail ted) {
		super(ted);
	}
	
	@Override
	public JsonObject exec() throws Exception {
		for (int n = 0; n < 10; n++) {
			String sql2 = "SELECT * FROM t_invest_contract WHERE 1=1 AND (update_date IS NULL OR update_date<CURDATE())";
			DefaultTableModel dtm2 = DatabaseMethod.query(ted, "qfsoft_db_prmp", sql2);
			if (dtm2.getRowCount() == 0) {
				break;
			}
			for (int i = 0; i < dtm2.getRowCount(); i++) {
				String contract_idv = DealTable.getString(dtm2, i, "contract_id");
				String contract_codev = DealTable.getString(dtm2, i, "contract_code");
				try {
					SeleniumMethod.visit(ted, "http://contract.eastmoney.com/" + contract_codev + ".html");
					JsonObject jsonv1 = new JsonObject();
					DealJson.setValue(jsonv1, "成立日期", SeleniumMethod.getText(ted, ".//div[@class='infoOfcontract']/table/tbody/tr[2]/td[1]").replace("成 立 日：", ""));
					DealJson.setValue(jsonv1, "基金经理", SeleniumMethod.getText(ted, ".//div[@class='infoOfcontract']/table/tbody/tr[1]/td[3]/a"));
					DealJson.setValue(jsonv1, "基金规模", DealString.getParam(SeleniumMethod.getText(ted, ".//div[@class='infoOfcontract']/table/tbody/tr[1]/td[2]"), "基金规模：", "（", 0));
					JsonObject jsonv2 = new JsonObject();
					DealJson.setValue(jsonv2, "单位净值", SeleniumMethod.getText(ted, ".//dl[@class='dataItem02']/dd[1]/span[1]"));
					DealJson.setValue(jsonv2, "累计净值", SeleniumMethod.getText(ted, ".//dl[@class='dataItem03']/dd[1]/span[1]"));
					LinkedHashMap<String, String> hmv = new LinkedHashMap<String, String>();
					DealDatabase.set_field(hmv, "contract_value", "value", jsonv1);
					DealDatabase.set_field(hmv, "current_value", "value", jsonv2);
					DealDatabase.set_field(hmv, "update_date", "function", "CURDATE()");
					DealDatabase.set_field(hmv, "last_status", "value", "enabled");
					String sqlv = DealDatabase.getModifySql("t_invest_contract", hmv, "contract_id='" + DealDatabase.cvtString(contract_idv) + "'");
					DatabaseMethod.edit(ted, "qfsoft_db_prmp", sqlv);
				} catch (Exception e) {
					FrameworkMethod.log(Level.WARN, "取值错误：contract_code=" + contract_codev);
				}
			}
		}
		return null;
	}
	
}