package com.qfsoft.app.atev.core.other.soccer.object;

import javax.swing.table.DefaultTableModel;

import com.qfsoft.app.atev.core.other.soccer.util.SoccerMethod;

import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.library.common.method.DealTable;

public class SoccerLeague {
	
	public String league_id;
	public String league_code;
	public String league_name;
	public int match_num;
	public String last_status;
	
	public SoccerLeague(String league_id, String league_code, String league_name, int match_num, String last_status) {
		this.league_id = league_id;
		this.league_code = league_code;
		this.league_name = league_name;
		this.match_num = match_num;
		this.last_status = last_status;
	}
	
	public SoccerLeague(TestExecDetail ted, String key, String value) throws Exception {
		DefaultTableModel league = SoccerMethod.getLeague(ted, key, value);
		if (league.getRowCount() == 1) {
			this.league_id = DealTable.getString(league, 0, "league_id");
			this.league_code = DealTable.getString(league, 0, "league_code");
			this.league_name = DealTable.getString(league, 0, "league_name");
			this.match_num = DealTable.getInt(league, 0, "match_num");
			this.last_status = DealTable.getString(league, 0, "last_status");
		}
	}
	
	public int roundTotal() {
		int total = (match_num * 2 - 1) * 2;
		return total;
	}
	
}