package com.qfsoft.app.atev.core.other.soccer.object;

import javax.swing.table.DefaultTableModel;

import com.qfsoft.app.atev.core.other.soccer.util.SoccerMethod;

import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.library.common.method.DealNumber;
import qfsoft.library.common.method.DealTable;

public class SoccerSeason {
	
	public String season_id = null;
	public String season_name = null;
	public String last_status = null;
	
	public SoccerSeason(String season_id, String season_name, String last_status) {
		this.season_id = season_id;
		this.season_name = season_name;
		this.last_status = last_status;
	}
	
	public SoccerSeason(TestExecDetail ted, String key, String value) throws Exception {
		DefaultTableModel season = SoccerMethod.getSeason(ted, key, value);
		if (season.getRowCount() == 1) {
			this.season_id = DealTable.getString(season, 0, "season_id");
			this.season_name = DealTable.getString(season, 0, "season_name");
			this.last_status = DealTable.getString(season, 0, "last_status");
		}
	}
	
	public int seasonStart() {
		int season_start = DealNumber.cvtInt(season_name.split("-")[0]);
		return season_start;
	}
	
	public int seasonEnd() {
		int season_end = DealNumber.cvtInt(season_name.split("-")[1]);
		return season_end;
	}
	
	public String seasonDiff(int year) {
		int season_start = DealNumber.cvtInt(season_name.split("-")[0]);
		int season_end = DealNumber.cvtInt(season_name.split("-")[1]);
		String season = (season_start + year) + "-" + (season_end + year);
		return season;
	}
	
}
