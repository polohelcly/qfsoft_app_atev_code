package com.qfsoft.app.atev.core.testcase.QF0101;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.swing.table.DefaultTableModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qfsoft.app.atev.core.other.stock.util.StockMethod;

import qfsoft.framework.ate.instance.TestCase;
import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.framework.ate.plugin.DatabaseMethod;
import qfsoft.library.common.method.DealArray;
import qfsoft.library.common.method.DealDatabase;
import qfsoft.library.common.method.DealDate;
import qfsoft.library.common.method.DealJson;
import qfsoft.library.common.method.DealNumber;
import qfsoft.library.common.method.DealTable;

public class TestCaseC22001 extends TestCase {
	
	// 数据抓取: 获取股票数据
	public TestCaseC22001(TestExecDetail ted) {
		super(ted);
	}
	
	@Override
	public JsonObject exec() throws Exception {
		Date last_date = DealDate.getDateDiff(-1);
		String sql = "SELECT * FROM t_invest_stock WHERE 1=1";
		sql = sql + " AND (happen_date IS NULL OR happen_date<'" + DealDatabase.cvtString(DealDate.getDateStr(last_date)) + "')";
//		sql = sql + " AND stock_id='221272f7-330e-11ec-9f94-7c8ae16a303e'";
		sql = sql + " ORDER BY stock_code";
		DefaultTableModel dtm = DatabaseMethod.query(ted, "qfsoft_db_prmp", sql);
		for (int i = 0; i < dtm.getRowCount(); i++) {
			String stock_idv = DealTable.getString(dtm, i, "stock_id");
			String stock_codev = DealTable.getString(dtm, i, "stock_code");
			Date open_datev = DealTable.getDate(dtm, i, "open_date");
			double history_lowv = DealTable.getDouble(dtm, i, "history_low");
			double history_highv = DealTable.getDouble(dtm, i, "history_high");
			Date happen_datev = DealTable.getDate(dtm, i, "happen_date");
			double happen_pricev = DealTable.getDouble(dtm, i, "happen_price");
			Date start_datev = open_datev;
			if (happen_datev != null) {
				start_datev = DealDate.getDateDiff(happen_datev, Calendar.DATE, 1);
			}
			Date end_datev = last_date;
			if (start_datev.after(end_datev) || start_datev.equals(end_datev)) {
				continue;
			}
			ArrayList<JsonArray> list = getTushare(stock_codev, start_datev, end_datev);
			if (list == null) {
				continue;
			}
			for (int j = 0; j < list.size(); j++) {
				JsonArray historyvs = list.get(j);
				Date trade_datevv = DealDate.parseDateFormat(DealJson.getString(historyvs, 0), "yyyyMMdd");
				double history_lowvv = DealJson.getDouble(historyvs, 3);
				double history_highvv = DealJson.getDouble(historyvs, 2);
				double history_closevv = DealJson.getDouble(historyvs, 4);
				if (history_lowv == 0 || history_lowv > history_lowvv) {
					history_lowv = history_lowvv;
				}
				if (history_highv == 0 || history_highv < history_highvv) {
					history_highv = history_highvv;
				}
				if (j == list.size() - 1) {
					happen_datev = trade_datevv;
					happen_pricev = history_closevv;
				}
			}
			if (happen_datev != null) {
				LinkedHashMap hmv = new LinkedHashMap();
				DealDatabase.set_field(hmv, "history_low", "value", DealNumber.decFormat(history_lowv, 2));
				DealDatabase.set_field(hmv, "history_high", "value", DealNumber.decFormat(history_highv, 2));
				DealDatabase.set_field(hmv, "happen_date", "value", DealDate.getDateStr(happen_datev));
				DealDatabase.set_field(hmv, "happen_price", "value", DealNumber.decFormat(happen_pricev, 2));
				String sqlv = DealDatabase.getModifySql("t_invest_stock", hmv, "stock_id='" + DealDatabase.cvtString(stock_idv) + "'");
				DatabaseMethod.edit(ted, "qfsoft_db_prmp", sqlv);
			}
		}
		return null;
		
	}
	
	private ArrayList<JsonArray> getTushare(String stock_code, Date start_date, Date end_date) throws Exception {
		String api_name = "daily";
		String fields = "trade_date,open,low,high,close";
		JsonObject params = new JsonObject();
		DealJson.setValue(params, "ts_code", stock_code);
		DealJson.setValue(params, "start_date", DealDate.parseDateStr(start_date, "yyyyMMdd"));
		DealJson.setValue(params, "end_date", DealDate.parseDateStr(end_date, "yyyyMMdd"));
		StockMethod.tushare(ted, api_name, params, fields);
		if (DealJson.getInt(ted.response, "http_code") != 200) {
			return null;
		}
		JsonObject context = DealJson.cvtJson(DealJson.getString(ted.response, "http_context"));
		if (DealJson.getInt(context, "code") != 0) {
			return null;
		}
		JsonArray historys = DealJson.getJsonArray(DealJson.getJson(context, "data"), "items");
		ArrayList<JsonArray> list = new ArrayList<JsonArray>();
		for (int j = 0; j < historys.size(); j++) {
			JsonArray historyvs = DealJson.getJsonArray(historys, j);
			list.add(historyvs);
		}
		DealArray.sort(list, "name", "asc");
		return list;
	}
	
}