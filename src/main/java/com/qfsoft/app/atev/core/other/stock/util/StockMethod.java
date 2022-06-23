package com.qfsoft.app.atev.core.other.stock.util;

import java.util.LinkedHashMap;

import javax.swing.table.DefaultTableModel;

import com.google.gson.JsonObject;

import qfsoft.framework.ate.exception.FunctionException;
import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.framework.ate.plugin.DatabaseMethod;
import qfsoft.framework.ate.plugin.HttpMethod;
import qfsoft.framework.ate.util.FrameworkMethod;
import qfsoft.library.common.method.DealDatabase;
import qfsoft.library.common.method.DealJson;
import qfsoft.library.common.method.DealString;
import qfsoft.library.common.method.DealTable;

public class StockMethod {
	
	public final static DefaultTableModel getIndustry(TestExecDetail ted, String key, String value) throws Exception {
		String sql = "SELECT * FROM t_invest_stock_industry WHERE " + DealDatabase.cvtString(key) + "='" + DealDatabase.cvtString(value) + "'";
		DefaultTableModel dtm = DatabaseMethod.query(ted, "qfsoft_db_prmp", sql);
		return dtm;
	}
	
	public final static void tushare(TestExecDetail ted, String api_name, JsonObject params, String fields) throws Exception {
		for (int i = 0; i < 3; i++) {
			try {
				JsonObject data = new JsonObject();
				DealJson.setValue(data, "api_name", api_name);
				DealJson.setValue(data, "token", "b1d80d2a460ec4bea319b34334fe1829d70b864b7ffc14207aee4aef");
				DealJson.setValue(data, "params", params);
				DealJson.setValue(data, "fields", fields);
				HttpMethod.sendJson(ted, "https://api.waditu.com", data.toString());
				if (DealJson.getInt(ted.response, "http_code") != 200) {
					throw new FunctionException("请求响应错误！");
				}
				JsonObject context = DealJson.cvtJson(DealJson.getString(ted.response, "http_context"));
				if (DealJson.getInt(context, "code") != 0) {
					throw new FunctionException("请求响应错误！");
				}
				return;
			} catch (Exception e) {
				FrameworkMethod.waitfor(ted, "60000");
			}
		}
	}
	
	public final static String getIndustryId(TestExecDetail ted, String industry_name) throws Exception {
		LinkedHashMap hm = new LinkedHashMap();
		String sql = "SELECT * FROM t_invest_stock_industry WHERE industry_name='" + DealDatabase.cvtString(industry_name) + "'";
		DefaultTableModel dtm = DatabaseMethod.query(ted, "qfsoft_db_prmp", sql);
		if (dtm.getRowCount() == 0) {
			String industry_id = DealString.getUnikey();
			DealDatabase.set_field(hm, "industry_id", "value", industry_id);
			DealDatabase.set_field(hm, "industry_name", "value", industry_name);
			DealDatabase.set_field(hm, "last_status", "value", "enabled");
			String sqlv = DealDatabase.getAddSql("t_invest_stock_industry", hm);
			DatabaseMethod.edit(ted, "qfsoft_db_prmp", sqlv);
			return industry_id;
		} else {
			String industry_id = DealTable.getString(dtm, 0, "industry_id");
			return industry_id;
		}
	}
	
}
