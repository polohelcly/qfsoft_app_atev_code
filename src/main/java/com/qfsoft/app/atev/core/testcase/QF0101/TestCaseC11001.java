package com.qfsoft.app.atev.core.testcase.QF0101;

import java.util.LinkedHashMap;

import javax.swing.table.DefaultTableModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qfsoft.app.atev.core.other.soccer.object.SoccerLeague;
import com.qfsoft.app.atev.core.other.soccer.object.SoccerSeason;
import com.qfsoft.app.atev.core.other.soccer.util.SoccerConstant;
import com.qfsoft.app.atev.core.other.soccer.util.SoccerMethod;

import qfsoft.framework.ate.instance.TestCase;
import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.framework.ate.plugin.DatabaseMethod;
import qfsoft.framework.ate.plugin.HttpMethod;
import qfsoft.framework.ate.util.FrameworkCache;
import qfsoft.framework.ate.util.FrameworkMethod;
import qfsoft.library.common.method.DealDatabase;
import qfsoft.library.common.method.DealDate;
import qfsoft.library.common.method.DealJson;
import qfsoft.library.common.method.DealNumber;
import qfsoft.library.common.method.DealTable;

public class TestCaseC11001 extends TestCase {
	
	// 彩票投资: 足球-抓取比赛数据
	public TestCaseC11001(TestExecDetail ted) {
		super(ted);
	}
	
	@Override
	public JsonObject exec() throws Exception {
		boolean debug_mode = DealJson.getBool(FrameworkCache.params, "debug_mode");
		String sql = "SELECT * FROM t_invest_soccer_league WHERE 1=1 ORDER BY league_code";
		DefaultTableModel dtm = DatabaseMethod.query(ted, "qfsoft_db_prmp", sql);
		for (int i = 0; i < dtm.getRowCount(); i++) {
			String league_idv = DealTable.getString(dtm, i, "league_id");
			SoccerLeague leaguev = new SoccerLeague(ted, "league_id", league_idv);
			String sqlv = "SELECT * FROM t_invest_soccer_season WHERE 1=1 ORDER BY season_name";
			DefaultTableModel dtmv = DatabaseMethod.query(ted, "qfsoft_db_prmp", sqlv);
			for (int j = 0; j < dtmv.getRowCount(); j++) {
				String season_idv = DealTable.getString(dtmv, j, "season_id");
				SoccerSeason seasonv = new SoccerSeason(ted, "season_id", season_idv);
				if ((!debug_mode) && (!seasonv.season_name.equals(SoccerConstant.SEASON))) {
					continue;
				}
				setSoccer(leaguev, seasonv);
			}
		}
		return null;
	}
	
	private void setSoccer(SoccerLeague league, SoccerSeason season) throws Exception {
		int roundstart = 1;
		int roundend = league.roundTotal();
		for (int i = roundstart; i <= roundend; i++) {
			int roll_num = i;
//			if (!(league.league_name.equals("德甲") && season.season_name.equals("2019-2020") && roll_num >= 1)) {
//				continue;
//			}
			String sqlv1 = "SELECT * FROM t_invest_soccer_history WHERE league_id='" + DealDatabase.cvtString(league.league_id) + "' AND season_id='" + DealDatabase.cvtString(season.season_id)
					+ "' AND roll_num='" + DealDatabase.cvtString(roll_num) + "'";
			DefaultTableModel dtmv1 = DatabaseMethod.query(ted, "qfsoft_db_prmp", sqlv1);
			if (dtmv1.getRowCount() > league.match_num) {
				String sqlv = DealDatabase.getDeleteSql("t_invest_soccer_history", "league_id='" + DealDatabase.cvtString(league.league_id) + "' AND season_id='"
						+ DealDatabase.cvtString(season.season_id) + "' AND roll_num='" + DealDatabase.cvtString(roll_num) + "'");
				DatabaseMethod.edit(ted, "qfsoft_db_prmp", sqlv);
				FrameworkMethod.waitfor(ted, "1000");
			}
			JsonObject params = new JsonObject();
			DealJson.setValue(params, "apiName", "getLeagueMatchList");
			DealJson.setValue(params, "leagueId", league.league_code);
			DealJson.setValue(params, "seasonName", season.season_name);
			DealJson.setValue(params, "round", roll_num);
			DealJson.setValue(params, "seasonFlag", 0);
			DealJson.setValue(params, "pageSize", 100);
			DealJson.setValue(params, "pageNo", 1);
			HttpMethod.sendJson(ted, "http://live.aicai.com/sportdata/f", params.toString());
			if (DealJson.getInt(ted.response, "http_code") != 200) {
				continue;
			}
			JsonObject match_jsonv = DealJson.cvtJson(DealJson.getString(ted.response, "http_context"));
			if (!DealJson.getString(match_jsonv, "code").equals("1")) {
				continue;
			}
			JsonArray match_listv = DealJson.getJsonArray(match_jsonv, "matchList");
			for (int j = 0; j < match_listv.size(); j++) {
				JsonObject matchv = (JsonObject) match_listv.get(j);
				String match_date = DealJson.getString(matchv, "matchDate");
				if (DealDate.parseDate(match_date).after(DealDate.getDateDiff(10))) {
					continue;
				}
				String match_time = DealJson.getString(matchv, "matchTime") + ":00";
				String host_id = SoccerMethod.getSoccerId(ted, league.league_id, String.valueOf(DealJson.getInt(matchv, "homeId")), DealJson.getString(matchv, "homeName"));
				String guest_id = SoccerMethod.getSoccerId(ted, league.league_id, String.valueOf(DealJson.getInt(matchv, "awayId")), DealJson.getString(matchv, "awayName"));
				JsonArray scores = DealJson.getJsonArray(matchv, "score");
				int match_status = DealJson.getInt(matchv, "status");
				String odds_europe = DealJson.getString(matchv, "oddsEurope");
				String odds_asia = DealJson.getString(matchv, "oddsAsia");
				LinkedHashMap hmv = new LinkedHashMap();
				DealDatabase.set_field(hmv, "match_date", "value", match_date + " " + match_time);
				setOuzhi(hmv, odds_europe);
				setYazhi(hmv, odds_asia);
				if (match_status == 2) {
					String[] vs = DealJson.getString(scores, 1).split(":");
					DealDatabase.set_field(hmv, "host_goal", "value", vs[0]);
					DealDatabase.set_field(hmv, "guest_goal", "value", vs[1]);
				} else {
					DealDatabase.set_field(hmv, "host_goal", "function", "NULL");
					DealDatabase.set_field(hmv, "guest_goal", "function", "NULL");
				}
				DealDatabase.set_field(hmv, "last_status", "value", "going");
				String sqlv3 = "SELECT * FROM t_invest_soccer_history WHERE league_id='" + DealDatabase.cvtString(league.league_id) + "' AND season_id='" + DealDatabase.cvtString(season.season_id)
						+ "' AND roll_num='" + DealDatabase.cvtString(roll_num) + "' AND host_id='" + DealDatabase.cvtString(host_id) + "' AND guest_id='" + DealDatabase.cvtString(guest_id) + "'";
				DefaultTableModel dtmv3 = DatabaseMethod.query(ted, "qfsoft_db_prmp", sqlv3);
				int n = dtmv3.getRowCount();
				if (n <= 0) {
					DealDatabase.set_field(hmv, "history_id", "function", "UUID()");
					DealDatabase.set_field(hmv, "league_id", "value", league.league_id);
					DealDatabase.set_field(hmv, "season_id", "value", season.season_id);
					DealDatabase.set_field(hmv, "host_id", "value", host_id);
					DealDatabase.set_field(hmv, "guest_id", "value", guest_id);
					DealDatabase.set_field(hmv, "roll_num", "value", roll_num);
					String sqlv = DealDatabase.getAddSql("t_invest_soccer_history", hmv);
					DatabaseMethod.edit(ted, "qfsoft_db_prmp", sqlv);
				} else if (n == 1) {
					String history_id = DealTable.getString(dtmv3, 0, "history_id");
					String sqlv = DealDatabase.getModifySql("t_invest_soccer_history", hmv, "history_id='" + DealDatabase.cvtString(history_id) + "'");
					DatabaseMethod.edit(ted, "qfsoft_db_prmp", sqlv);
				} else if (n > 1) {
					for (int k = 1; k < n; k++) {
						String history_id = DealTable.getString(dtmv3, k, "history_id");
						String sqlv = DealDatabase.getDeleteSql("t_invest_soccer_history", "history_id='" + DealDatabase.cvtString(history_id) + "'");
						DatabaseMethod.edit(ted, "qfsoft_db_prmp", sqlv);
					}
				}
			}
		}
	}
	
	private void setOuzhi(LinkedHashMap hmv, String rate) throws Exception {
		String[] rates = rate.split(";");
		if (rates.length >= 3 && !rates[0].equals("") && !rates[1].equals("") && !rates[2].equals("")) {
			DealDatabase.set_field(hmv, "europe_win", "value", rates[0]);
			DealDatabase.set_field(hmv, "europe_equal", "value", rates[1]);
			DealDatabase.set_field(hmv, "europe_lose", "value", rates[2]);
		}
	}
	
	private void setYazhi(LinkedHashMap hmv, String rate) throws Exception {
		String[] rates = rate.split(";");
		if (rates.length >= 3) {
			DealDatabase.set_field(hmv, "asia_win", "value", rates[0]);
			DealDatabase.set_field(hmv, "asia_tape", "value", cvtTape(rates[1]));
			DealDatabase.set_field(hmv, "asia_lose", "value", rates[2]);
		}
	}
	
	private double cvtTape(String tape) throws Exception {
		if (tape.contains("/")) {
			String[] tapes = tape.split("/");
			double t1 = DealNumber.cvtDouble(tapes[0]);
			double t2 = DealNumber.cvtDouble(tapes[1]);
			return (t1 + t2) / 2;
		}
		return DealNumber.cvtDouble(tape);
	}
	
}