package com.example.wx.tools;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;

/**
 * @author wangxiang
 * @description
 * @create 2025/8/24 17:50
 */
@Slf4j
public class BaiduTranslateTools implements BiFunction<BaiduTranslateTools.BaiduTranslateToolRequest, ToolContext, BaiduTranslateTools.BaiduTranslateToolResponse> {

    private final String appId;

    private final String secretKey;

    private final RestClient restClient;

    public BaiduTranslateTools(String appId, String secretKey, RestClient.Builder restClientBuilder, ResponseErrorHandler responseErrorHandler) {

        this.appId = appId;
        this.secretKey = secretKey;
        this.restClient = restClientBuilder.baseUrl("https://fanyi-api.baidu.com/api/trans/vip/translate")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
                .defaultStatusHandler(responseErrorHandler).build();
    }

    private MultiValueMap<String, String> constructRequestBody(Request request, String salt, String sign) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("q", request.q);
        body.add("from", request.from);
        body.add("to", request.to);
        body.add("appid", this.appId);
        body.add("salt", salt);
        body.add("sign", sign);

        return body;
    }

    private BaiduTranslateToolResponse parseResponse(String responseData) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, String> translations = new HashMap<>();

            TranslationResponse responseList = mapper.readValue(
                    responseData,
                    TranslationResponse.class
            );

            String to = responseList.to;
            List<TranslationResult> translationsList = responseList.trans_result;

            if (translationsList != null) {
                for (TranslationResult translation : translationsList) {
                    String translatedText = translation.dst;
                    translations.put(to, translatedText);
                    log.info("Translated text to {}: {}", to, translatedText);
                }
            }

            return new BaiduTranslateToolResponse(new Response(translations));

        } catch (Exception var11) {
            try {
                Map<String, String> responseList = mapper.readValue(responseData, mapper.getTypeFactory()
                        .constructMapType(Map.class, String.class, String.class));
                log.error("Translation exception, please inquire Baidu translation api documentation to info error_code:{}", responseList);
                return new BaiduTranslateToolResponse(new Response(responseList));
            } catch (Exception var10) {
                log.error("Failed to parse json due to: {}", var10.getMessage());
                return null;
            }
        }
    }

    @Override
    public BaiduTranslateToolResponse apply(BaiduTranslateToolRequest request, ToolContext toolContext) {
        Random random = new Random();
        if (request.input != null && StringUtils.hasText(request.input.q) && StringUtils.hasText(request.input.from) && StringUtils.hasText(request.input.to)) {
            String salt = String.valueOf(random.nextInt(100000));
            String sign = DigestUtils.md5DigestAsHex((this.appId + request.input.q + salt + this.secretKey).getBytes());
            String url = UriComponentsBuilder.fromHttpUrl("https://fanyi-api.baidu.com/api/trans/vip/translate")
                    .toUriString();

            try {
                MultiValueMap<String, String> body = this.constructRequestBody(request.input, salt, sign);
                String respData = this.restClient.post().uri(url).body(body).retrieve().toEntity(String.class).getBody();
                return parseResponse(respData);
            } catch (Exception var9) {
                log.error("Error occurred: {}", var9.getMessage());
            }
        }
        return null;
    }

    public record BaiduTranslateToolRequest(@JsonProperty("Request") Request input) {
    }

    public record BaiduTranslateToolResponse(@JsonProperty("Response") Response output) {
    }

    @JsonClassDescription("Request to translate text to a target language")
    public record Request(
            @JsonProperty(required = true, value = "q")
            @JsonPropertyDescription("Content that needs to be translated") String q,
            @JsonProperty(required = true, value = "from")
            @JsonPropertyDescription("Source language that needs to be translated") String from,
            @JsonProperty(required = true, value = "to")
            @JsonPropertyDescription("Target language to translate into") String to) {
    }

    @JsonClassDescription("Response to translate text to a target language")
    public record Response(Map<String, String> translatedTexts) {
    }

    @JsonClassDescription("complete response")
    public record TranslationResponse(
            @JsonProperty(required = true, value = "from")
            @JsonPropertyDescription("Source language that needs to be translated") String from,
            @JsonProperty(required = true, value = "to")
            @JsonPropertyDescription("Target language to translate into") String to,
            @JsonProperty(required = true, value = "trans_result")
            @JsonPropertyDescription("part of the response") List<TranslationResult> trans_result) {

    }

    @JsonClassDescription("part of the response")
    public record TranslationResult(
            @JsonProperty(required = true, value = "src") @JsonPropertyDescription("Original Content") String src,
            @JsonProperty(required = true, value = "dst") @JsonPropertyDescription("Final Result") String dst) {
    }
}
