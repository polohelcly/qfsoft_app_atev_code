package com.qfsoft.app.atev.core.other.soccer.util;

import java.util.LinkedHashMap;

import javax.swing.table.DefaultTableModel;

import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.framework.ate.plugin.DatabaseMethod;
import qfsoft.library.common.method.DealDatabase;
import qfsoft.library.common.method.DealString;
import qfsoft.library.common.method.DealTable;

public class SoccerMethod {
	
	public final static DefaultTableModel getLeague(TestExecDetail ted, String key, String value) throws Exception {
		String sql = "SELECT * FROM t_invest_soccer_league WHERE " + DealDatabase.cvtString(key) + "='" + DealDatabase.cvtString(value) + "'";
		DefaultTableModel dtm = DatabaseMethod.query(ted, "qfsoft_db_prmp", sql);
		return dtm;
	}
	
	public final static DefaultTableModel getSeason(TestExecDetail ted, String key, String value) throws Exception {
		String sql = "SELECT * FROM t_invest_soccer_season WHERE " + DealDatabase.cvtString(key) + "='" + DealDatabase.cvtString(value) + "'";
		DefaultTableModel dtm = DatabaseMethod.query(ted, "qfsoft_db_prmp", sql);
		return dtm;
	}
	
	public final static String getSoccerId(TestExecDetail ted, String league_id, String soccer_code, String soccer_name) throws Exception {
		LinkedHashMap hm = new LinkedHashMap();
		DealDatabase.set_field(hm, "soccer_code", "value", soccer_code);
		DealDatabase.set_field(hm, "last_status", "value", "enabled");
		String sql = "SELECT * FROM t_invest_soccer WHERE league_id='" + DealDatabase.cvtString(league_id) + "' AND soccer_name='" + DealDatabase.cvtString(soccer_name) + "'";
		DefaultTableModel dtm = DatabaseMethod.query(ted, "qfsoft_db_prmp", sql);
		if (dtm.getRowCount() == 0) {
			String soccer_id = DealString.getUnikey();
			DealDatabase.set_field(hm, "soccer_id", "value", soccer_id);
			DealDatabase.set_field(hm, "league_id", "value", league_id);
			DealDatabase.set_field(hm, "soccer_name", "value", soccer_name);
			String sqlv = DealDatabase.getAddSql("t_invest_soccer", hm);
			DatabaseMethod.edit(ted, "qfsoft_db_prmp", sqlv);
			return soccer_id;
		} else {
			String soccer_id = DealTable.getString(dtm, 0, "soccer_id");
			String sqlv = DealDatabase.getModifySql("t_invest_soccer", hm, "soccer_id='" + DealDatabase.cvtString(soccer_id) + "'");
			DatabaseMethod.edit(ted, "qfsoft_db_prmp", sqlv);
			return soccer_id;
		}
	}
	
}
