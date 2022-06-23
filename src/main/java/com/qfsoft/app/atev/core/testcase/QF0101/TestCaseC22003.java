package com.qfsoft.app.atev.core.testcase.QF0101;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.swing.table.DefaultTableModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qfsoft.app.atev.core.other.stock.util.StockMethod;

import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.framework.ate.instance.TestCase;
import qfsoft.framework.ate.plugin.DatabaseMethod;
import qfsoft.library.common.method.DealDatabase;
import qfsoft.library.common.method.DealDate;
import qfsoft.library.common.method.DealJson;
import qfsoft.library.common.method.DealTable;

public class TestCaseC22003 extends TestCase {
	
	// 数据抓取: 收集股票数据
	public TestCaseC22003(TestExecDetail ted) {
		super(ted);
	}
	
	@Override
	public JsonObject exec() throws Exception {
		String api_name = "stock_basic";
		String fields = "symbol,name,area,industry,market,list_status,list_date";
		JsonObject params = new JsonObject();
		DealJson.setValue(params, "exchange", "");
		DealJson.setValue(params, "list_status", "L");
		StockMethod.tushare(ted, api_name, params, fields);
		if (DealJson.getInt(ted.response, "http_code") != 200) {
			return null;
		}
		JsonObject context = DealJson.cvtJson(DealJson.getString(ted.response, "http_context"));
		if (DealJson.getInt(context, "code") != 0) {
			return null;
		}
		JsonArray items = DealJson.getJsonArray(DealJson.getJson(context, "data"), "items");
		for (int i = 0; i < items.size(); i++) {
			JsonArray itemvs = DealJson.getJsonArray(items, i);
			String codev = DealJson.getString(itemvs, 0);
			String namev = DealJson.getString(itemvs, 1);
			String industryv = DealJson.getString(itemvs, 3);
			String marketv = DealJson.getString(itemvs, 4);
			Date datev = new Date(Long.parseLong(DealJson.getString(itemvs, 6)));
			String sqlv2 = "SELECT * FROM t_invest_stock WHERE stock_code='" + DealDatabase.cvtString(codev) + "'";
			DefaultTableModel dtmv2 = DatabaseMethod.query(ted, "qfsoft_db_prmp", sqlv2);
			LinkedHashMap hmv3 = new LinkedHashMap();
			DealDatabase.set_field(hmv3, "stock_code", "value", codev);
			DealDatabase.set_field(hmv3, "stock_name", "value", namev);
			DealDatabase.set_field(hmv3, "account_id", "value", "ddaf9019-2682-11e8-af67-000c29786a75");
			DealDatabase.set_field(hmv3, "market_type", "value", "china");
			DealDatabase.set_field(hmv3, "industry", "value", industryv);
			DealDatabase.set_field(hmv3, "currency_type", "value", "standard");
			DealDatabase.set_field(hmv3, "unit_multiple", "value", "100");
			DealDatabase.set_field(hmv3, "unit_price", "value", 0);
			DealDatabase.set_field(hmv3, "update_date", "value", DealDate.getNowDateStr());
			DealDatabase.set_field(hmv3, "last_status", "value", "enabled");
			if (dtmv2.getRowCount() == 0) {
				DealDatabase.set_field(hmv3, "stock_id", "function", "UUID()");
				String sqlv3 = DealDatabase.getAddSql("t_invest_stock", hmv3);
				DatabaseMethod.edit(ted, "qfsoft_db_prmp", sqlv3);
			} else {
				String stock_idv = DealTable.getString(dtmv2, 0, "stock_id");
				String sqlv3 = DealDatabase.getModifySql("t_invest_stock", hmv3, "stock_id='" + stock_idv + "'");
				DatabaseMethod.edit(ted, "qfsoft_db_prmp", sqlv3);
			}
		}
		return null;
	}
	
}