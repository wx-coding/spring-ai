package com.example.wx.service.impl;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.chat.MessageFormat;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.example.wx.service.ImageService;
import com.example.wx.utils.FilesUtils;
import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static com.alibaba.cloud.ai.dashscope.common.DashScopeApiConstants.MESSAGE_FORMAT;

/**
 * @author wangxiang
 * @description
 * @create 2025/7/26 21:43
 */
@Service
public class ImageServiceImpl implements ImageService {

    private static final String DEFAULT_TEXT2IMAGE_MODEL = "qwen-vl-max-latest";

    private static final String DEFAULT_IMAGE_MODEL = "wanx2.1-t2i-turbo";

    /**
     * Images generate text
     */
    private final ImageModel imageModel;

    /**
     * Multimodal support for parsing images
     */
    private final ChatClient daschScopeChatClient;

    public ImageServiceImpl(
            @Qualifier("dashscopeChatModel") ChatModel chatModel,
            @Qualifier("dashScopeImageModel") ImageModel imageModel) {
        this.imageModel = imageModel;
        this.daschScopeChatClient = ChatClient
                .builder(chatModel)
                .build();
    }

    @Override
    public Flux<String> image2Text(String prompt, MultipartFile file) throws IOException {
        String filePath = FilesUtils.saveTempFile(file, "/tmp/image/");
        List<Media> mediaList = Lists.newArrayList(new Media(
                MimeTypeUtils.IMAGE_PNG,
                new FileSystemResource(filePath)
        ));

        UserMessage message = UserMessage.builder()
                .text(prompt)
                .media(mediaList)
                .metadata(new HashMap<>()).build();
        message.getMetadata().put(MESSAGE_FORMAT, MessageFormat.IMAGE);

        // todo
        List<ChatResponse> response = daschScopeChatClient.prompt(
                        new Prompt(
                                message,
                                DashScopeChatOptions.builder()
                                        .withModel(DEFAULT_TEXT2IMAGE_MODEL)
                                        .withMultiModel(true)
                                        .build())
                ).stream()
                .chatResponse()
                .collectList()
                .block();
        // .map(chatResponse -> {
        //     // 提取每个响应中的 text 内容
        //     return chatResponse.getResult().getOutput().getText();
        // });
        StringBuilder result = new StringBuilder();
        if (response != null) {
            for (ChatResponse chatResponse : response) {
                String outputContent = chatResponse.getResult().getOutput().getText();
                result.append(outputContent);
            }
        }
        return Flux.just(result.toString());
    }

    /**
     * 可以基于此接口扩展更多参数，调用不同模型来实现
     * 图像扩展，反向 Prompt，图像增强，抠图等功能。
     * 此示例中不做演示。
     * 文档参考：<a href="https://help.aliyun.com/zh/model-studio/developer-reference/text-to-image-v2-api-reference">...</a>
     */
    @Override
    public void text2Image(String prompt, String resolution, String style, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Security-Policy", "img-src 'self' data:;");

        ImageGeneration result = imageModel.call(
                new ImagePrompt(
                        prompt,
                        DashScopeImageOptions.builder()
                                .withHeight(Integer.valueOf(resolution.split("\\*")[0]))
                                .withWidth(Integer.valueOf(resolution.split("\\*")[1]))
                                .withStyle(style)
                                .withBaseImageUrl("")
                                .withModel(DEFAULT_IMAGE_MODEL)
                                .build())
        ).getResult();

        String imageUrl = result.getOutput().getUrl();

        try {
            URL url = URI.create(imageUrl).toURL();
            InputStream in = url.openStream();

            response.setHeader("Content-Security-Policy", "img-src 'self' data:;");
            response.setHeader("Content-Type", MediaType.IMAGE_PNG_VALUE);
            response.getOutputStream().write(in.readAllBytes());
            response.getOutputStream().flush();
        }
        catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
