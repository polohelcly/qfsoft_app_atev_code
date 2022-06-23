package com.qfsoft.app.atev.core.other.stock.object;

import javax.swing.table.DefaultTableModel;

import com.qfsoft.app.atev.core.other.stock.util.StockMethod;

import qfsoft.framework.ate.instance.TestExecDetail;
import qfsoft.library.common.method.DealTable;

public class StockIndustry {
	
	public String industry_id = null;
	public String industry_name = null;
	public String last_status = null;
	
	public StockIndustry(String industry_id, String industry_name, String last_status) {
		this.industry_id = industry_id;
		this.industry_name = industry_name;
		this.last_status = last_status;
	}
	
	public StockIndustry(TestExecDetail ted, String key, String value) throws Exception {
		DefaultTableModel Industry = StockMethod.getIndustry(ted, key, value);
		if (Industry.getRowCount() == 1) {
			this.industry_id = DealTable.getString(Industry, 0, "industry_id");
			this.industry_name = DealTable.getString(Industry, 0, "industry_name");
			this.last_status = DealTable.getString(Industry, 0, "last_status");
		}
	}
	
}
