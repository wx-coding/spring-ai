package com.example.wx.entiy.tools;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ToolCallResp {

	/**
	 * Tool 的执行状态
	 */
	private ToolState status;

	/**
	 * Tool Name
	 */
	private String toolName;

	/**
	 * Tool 执行参数
	 */
	private String toolParameters;

	/**
	 * Tool 执行结果
	 */
	private String toolResult;

	/**
	 * 工具执行开始的时间戳
	 */
	private LocalDateTime toolStartTime;

	/**
	 * 工具执行完成的时间戳
	 */
	private LocalDateTime toolEndTime;

	/**
	 * 工具执行的错误信息
	 */
	private String errorMessage;

	/**
	 * 工具执行输入
	 */
	private String toolInput;

	/**
	 * 工具执行耗时
	 */
	private Long toolCostTime;
	/**
	 * Tool 记录tool返回的中间结果
	 */
	private String toolResponse;

	public enum ToolState {
		/**
		 * 工具执行成功
		 */
		SUCCESS,
		/**
		 * 工具执行失败
		 */
		FAILURE,
		/**
		 * 工具状态未知
		 */
		UNKNOWN,
		/**
		 * 工具执行中
		 */
		RUNNING,
	}

	// return a null ToolCallResp
	public static ToolCallResp TCR() {
		return new ToolCallResp();
	}

	public static ToolCallResp startExecute(String toolInput, String toolName, String toolParameters) {

		var res = new ToolCallResp();

		res.setToolName(toolName);
		res.setToolParameters(toolParameters);
		res.setToolInput(toolInput);
		res.setToolStartTime(LocalDateTime.now());
		res.setStatus(ToolState.RUNNING);
		return res;
	}

	public static ToolCallResp endExecute(ToolState status, LocalDateTime toolStartTime, String toolResult) {

		var res = new ToolCallResp();
		res.setToolResult(toolResult);
		res.setToolEndTime(LocalDateTime.now());
		res.setStatus(status);
		res.setToolCostTime(
				(long) (res.getToolEndTime().getNano() - toolStartTime.getNano())
		);

		return res;
	}

}