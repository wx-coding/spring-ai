package com.example.wx.controller;

import com.example.wx.annotation.UserIp;
import com.example.wx.service.SummarizerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Docs Summarize APIs")
public class SummarizerController {

	private final SummarizerService summarizerService;

	public SummarizerController(SummarizerService summarizerService) {
		this.summarizerService = summarizerService;
	}

	@UserIp
	@Operation(summary = "Docs summary")
	@PostMapping("/summarizer")
	public Flux<String> summary(
			HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "url", required = false) String url
	) {

		if (file == null && (url == null || url.isEmpty())) {
			return Flux.just("Either 'file' or 'url' must be provided.");
		}

		response.setCharacterEncoding("UTF-8");
		return summarizerService.summary(file, url);
	}

}