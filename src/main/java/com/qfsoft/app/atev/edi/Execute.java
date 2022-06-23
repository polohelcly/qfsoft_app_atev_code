package com.qfsoft.app.atev.edi;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.swagger.annotations.ApiOperation;
import qfsoft.framework.ate.MainFramework;
import qfsoft.framework.ate.util.FrameworkCache;
import qfsoft.library.common.method.DealException;
import qfsoft.library.common.method.DealJson;

@RestController
@RequestMapping(value = "/execute")
public class Execute {
	
	@ApiOperation(value = "执行任务", notes = "执行任务")
	@RequestMapping(value = "/task", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String executeTask(@RequestBody String body) throws Exception {
		try {
			if (FrameworkCache.running_task_flag == true) {
				JsonObject error = new JsonObject();
				DealJson.setValue(error, "code", 1);
				DealJson.setValue(error, "message", "已有测试正在执行...");
				return error.toString();
			}
			JsonObject request = DealJson.cvtJson(body);
			String server_id = DealJson.getString(request, "server_id");
			String task_id = DealJson.getString(request, "task_id");
			MainFramework.executeTask(server_id, task_id);
			JsonObject response = new JsonObject();
			DealJson.setValue(response, "code", 0);
			DealJson.setValue(response, "message", "测试执行完毕...");
			return response.toString();
		} catch (Exception e) {
			FrameworkCache.running_task_flag = false;
			e.printStackTrace();
			JsonObject error = new JsonObject();
			DealJson.setValue(error, "code", 1);
			DealJson.setValue(error, "message", "测试执行错误：" + DealException.getDetail(e));
			return error.toString();
		}
	}
	
	@ApiOperation(value = "执行任务用例", notes = "执行任务用例")
	@RequestMapping(value = "/task_case", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String executeTaskCase(@RequestBody String body) throws Exception {
		try {
			if (FrameworkCache.running_case_flag == true) {
				JsonObject error = new JsonObject();
				DealJson.setValue(error, "code", 1);
				DealJson.setValue(error, "message", "已有测试正在执行...");
				return error.toString();
			}
			JsonObject request = DealJson.cvtJson(body);
			String server_id = DealJson.getString(request, "server_id");
			String task_id = DealJson.getString(request, "task_id");
			String listv = DealJson.getString(request, "listv");
			JsonArray results = MainFramework.executeTaskCase(server_id, task_id, listv);
			JsonObject response = new JsonObject();
			DealJson.setValue(response, "code", 0);
			DealJson.setValue(response, "message", "测试执行完毕...");
			DealJson.setValue(response, "data", results);
			return response.toString();
		} catch (Exception e) {
			FrameworkCache.running_case_flag = false;
			e.printStackTrace();
			JsonObject error = new JsonObject();
			DealJson.setValue(error, "code", 1);
			DealJson.setValue(error, "message", "测试执行错误：" + DealException.getDetail(e));
			return error.toString();
		}
	}
	
	@ApiOperation(value = "执行用例", notes = "执行用例")
	@RequestMapping(value = "/case", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String executeCase(@RequestBody String body) throws Exception {
		try {
			if (FrameworkCache.running_case_flag == true) {
				JsonObject error = new JsonObject();
				DealJson.setValue(error, "code", 1);
				DealJson.setValue(error, "message", "已有测试正在执行...");
				return error.toString();
			}
			JsonObject request = DealJson.cvtJson(body);
			String server_id = DealJson.getString(request, "server_id");
			String listv = DealJson.getString(request, "listv");
			JsonArray results = MainFramework.executeCase(server_id, listv);
			JsonObject response = new JsonObject();
			DealJson.setValue(response, "code", 0);
			DealJson.setValue(response, "message", "测试执行完毕...");
			DealJson.setValue(response, "data", results);
			return response.toString();
		} catch (Exception e) {
			FrameworkCache.running_case_flag = false;
			e.printStackTrace();
			JsonObject error = new JsonObject();
			DealJson.setValue(error, "code", 1);
			DealJson.setValue(error, "message", "测试执行错误：" + DealException.getDetail(e));
			return error.toString();
		}
	}
	
	@ApiOperation(value = "执行功能", notes = "执行功能")
	@RequestMapping(value = "/fun", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String executeFun(@RequestBody String body) throws Exception {
		try {
			if (FrameworkCache.running_case_flag == true) {
				JsonObject error = new JsonObject();
				DealJson.setValue(error, "code", 1);
				DealJson.setValue(error, "message", "已有测试正在执行...");
				return error.toString();
			}
			JsonObject request = DealJson.cvtJson(body);
			String server_id = DealJson.getString(request, "server_id");
			String listv = DealJson.getString(request, "listv");
			JsonObject params = DealJson.getJson(request, "params");
			JsonArray results = MainFramework.executeFun(server_id, listv, params);
			JsonObject response = new JsonObject();
			DealJson.setValue(response, "code", 0);
			DealJson.setValue(response, "message", "测试执行完毕...");
			DealJson.setValue(response, "data", results);
			return response.toString();
		} catch (Exception e) {
			FrameworkCache.running_case_flag = false;
			e.printStackTrace();
			JsonObject error = new JsonObject();
			DealJson.setValue(error, "code", 1);
			DealJson.setValue(error, "message", "测试执行错误：" + DealException.getDetail(e));
			return error.toString();
		}
	}
	
	@ApiOperation(value = "执行接口", notes = "执行接口")
	@RequestMapping(value = "/api", method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String executeApi(@RequestBody String body) throws Exception {
		try {
			if (FrameworkCache.running_case_flag == true) {
				JsonObject error = new JsonObject();
				DealJson.setValue(error, "code", 1);
				DealJson.setValue(error, "message", "已有测试正在执行...");
				return error.toString();
			}
			JsonObject request = DealJson.cvtJson(body);
			String server_id = DealJson.getString(request, "server_id");
			String listv = DealJson.getString(request, "listv");
			JsonObject params = DealJson.getJson(request, "params");
			JsonArray results = MainFramework.executeApi(server_id, listv, params);
			JsonObject response = new JsonObject();
			DealJson.setValue(response, "code", 0);
			DealJson.setValue(response, "message", "测试执行完毕...");
			DealJson.setValue(response, "data", results);
			return response.toString();
		} catch (Exception e) {
			FrameworkCache.running_case_flag = false;
			e.printStackTrace();
			JsonObject error = new JsonObject();
			DealJson.setValue(error, "code", 1);
			DealJson.setValue(error, "message", "测试执行错误：" + DealException.getDetail(e));
			return error.toString();
		}
	}
	
}