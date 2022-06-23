package com.qfsoft.app.atev.core.testcase.QF0101;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.swing.table.DefaultTableModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qfsoft.app.atev.core.other.stock.util.StockMethod;

import qfsoft.framework.ate.instance.TestCase;
import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.framework.ate.plugin.DatabaseMethod;
import qfsoft.library.common.method.DealDatabase;
import qfsoft.library.common.method.DealDate;
import qfsoft.library.common.method.DealJson;
import qfsoft.library.common.method.DealTable;

public class TestCaseC22002 extends TestCase {
	
	// 数据抓取: 收集股票数据
	public TestCaseC22002(TestExecDetail ted) {
		super(ted);
	}
	
	@Override
	public JsonObject exec() throws Exception {
		DatabaseMethod.edit(ted, "qfsoft_db_prmp", "UPDATE t_invest_stock SET last_status='disabled'");
		String api_name = "stock_basic";
		String fields = "ts_code,name,area,industry,market,list_status,list_date";
		JsonObject params = new JsonObject();
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
		setStocks(items);
		DatabaseMethod.edit(ted, "qfsoft_db_prmp", "DELETE FROM t_invest_stock WHERE last_status='disabled'");
		return null;
	}
	
	public void setStocks(JsonArray stocks) throws Exception {
		ArrayList<String> sqls = new ArrayList<String>();
		for (int i = 0; i < stocks.size(); i++) {
			JsonArray stockvs = DealJson.getJsonArray(stocks, i);
			String stock_codev = DealJson.getString(stockvs, 0);
			String stock_namev = DealJson.getString(stockvs, 1);
			String industry_namev = DealJson.getString(stockvs, 3);
			Date open_datev = DealDate.parseDateFormat(DealJson.getString(stockvs, 6), "yyyyMMdd");
			if (!stock_codev.endsWith("SH") && !stock_codev.endsWith("SZ")) {
				continue;
			}
			String sqlv = "SELECT * FROM t_invest_stock WHERE stock_code='" + DealDatabase.cvtString(stock_codev) + "'";
			DefaultTableModel dtmv = DatabaseMethod.query(ted, "qfsoft_db_prmp", sqlv);
			LinkedHashMap hmv = new LinkedHashMap();
			DealDatabase.set_field(hmv, "industry_id", "value", StockMethod.getIndustryId(ted, industry_namev));
			DealDatabase.set_field(hmv, "stock_code", "value", stock_codev);
			DealDatabase.set_field(hmv, "stock_name", "value", stock_namev);
			DealDatabase.set_field(hmv, "market_type", "value", "china");
			DealDatabase.set_field(hmv, "currency_type", "value", "standard");
			DealDatabase.set_field(hmv, "open_date", "value", DealDate.getDateStr(open_datev));
			DealDatabase.set_field(hmv, "pay_rate", "value", "100");
			DealDatabase.set_field(hmv, "last_status", "value", "enabled");
			if (dtmv.getRowCount() == 0) {
				DealDatabase.set_field(hmv, "stock_id", "function", "UUID()");
				DealDatabase.set_field(hmv, "happen_price", "value", "0");
				String sqlv2 = DealDatabase.getAddSql("t_invest_stock", hmv);
				sqls.add(sqlv2);
			} else {
				String stock_idv = DealTable.getString(dtmv, 0, "stock_id");
				String sqlv2 = DealDatabase.getModifySql("t_invest_stock", hmv, "stock_id='" + stock_idv + "'");
				sqls.add(sqlv2);
			}
		}
		DatabaseMethod.batEdit(ted, "qfsoft_db_prmp", sqls);
	}
	
}