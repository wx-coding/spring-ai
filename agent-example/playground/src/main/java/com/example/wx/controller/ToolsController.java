package com.example.wx.controller;

import com.example.wx.annotation.UserIp;
import com.example.wx.entiy.result.Result;
import com.example.wx.entiy.tools.ToolCallResp;
import com.example.wx.service.ToolsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @RestController
// @Tag(name = "Tool Calling APIs")
// @RequestMapping("/api/v1")
// public class ToolsController {
//
// 	private final ToolsService toolsService;
//
// 	public ToolsController(ToolsService toolsService) {
// 		this.toolsService = toolsService;
//     }
//
// 	/**
// 	 * http://127.0.0.1:8080/api/v1/tool-call?prompt="使用百度翻译将隐私计算翻译为英文"
// 	 * 触发百度翻译：使用百度翻译将隐私计算翻译为英文
// 	 * 触发百度地图：使用百度地图查找杭州市的银行 ATM 机信息 or 使用百度地图查找杭州的信息
// 	 */
// 	@UserIp
// 	@GetMapping("/tool-call")
// 	@Operation(summary = "DashScope ToolCall Chat")
// 	public Result<ToolCallResp> toolCallChat(
// 			@Validated @RequestParam("prompt") String prompt
// 	) {
//
// 		return Result.success(toolsService.chat(prompt));
// 	}
//
// }