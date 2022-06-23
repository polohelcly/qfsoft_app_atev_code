package com.qfsoft.app.atev.edi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

import io.swagger.annotations.ApiOperation;
import qfsoft.library.common.method.DealJson;

@RestController
@RequestMapping(value = "/")
public class Home {
	
	@ApiOperation(value = "框架", notes = "主页")
	@RequestMapping(value = "/index", method = { RequestMethod.GET }, produces = "application/json;charset=UTF-8")
	public String index() throws Exception {
		JsonObject data = new JsonObject();
		DealJson.setValue(data, "code", "0");
		DealJson.setValue(data, "message", "系统运行正常！");
		return data.toString();
	}
	
}