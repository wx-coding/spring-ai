package com.example.wx.entiy.mcp;

import java.util.List;
import java.util.Map;

public class McpServer {

	private String id;

	private String name;

	private String desc;

	private Map<String, String> env;

	private List<Tools> toolList;

	public static class Tools {

		private String name;

		private String params;

		private String desc;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getParams() {
			return params;
		}

		public void setParams(String params) {
			this.params = params;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getEnv() {
		return env;
	}

	public void setEnv(Map<String, String> env) {
		this.env = env;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Tools> getToolList() {
		return toolList;
	}

	public void setToolList(List<Tools> toolList) {
		this.toolList = toolList;
	}

	@Override
	public String toString() {

		final StringBuilder sb = new StringBuilder("McpServer{");

		sb.append("id='").append(id).append('\'');
		sb.append(", name='").append(name).append('\'');
		sb.append(", desc='").append(desc).append('\'');
		sb.append(", toolList=").append(toolList);
		sb.append('}');

		return sb.toString();
	}

	public static McpServerBuilder builder() {
		return new McpServerBuilder();
	}

	public static class McpServerBuilder {

		private McpServer mcpServer = new McpServer();

		public McpServerBuilder() {}

		public McpServerBuilder id(String id) {
			this.mcpServer.id = id;
			return this;
		}

		public McpServerBuilder env(Map<String, String> env) {
			this.mcpServer.env = env;
			return this;
		}

		public McpServerBuilder name(String name) {
			this.mcpServer.name = name;
			return this;
		}

		public McpServerBuilder desc(String desc) {
			this.mcpServer.desc = desc;
			return this;
		}

		public McpServerBuilder toolList(List<Tools> toolList) {
			this.mcpServer.toolList = toolList;
			return this;
		}

		public McpServer build() {
			return this.mcpServer;
		}
	}

}