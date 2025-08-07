package com.example.wx.config.rag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wangxiang
 * @description
 * @create 2025/7/20 17:35
 */
public class VectorStoreInitializer {
    private static final Logger logger = LoggerFactory.getLogger(VectorStoreInitializer.class);


    public void init(VectorStore vectorStore) throws IOException, URISyntaxException {
        List<MarkdownDocumentReader> markdownDocumentReaderList = loadMarkdownDocuments();

        int size = 0;
        if (markdownDocumentReaderList.isEmpty()) {
            logger.warn("No markdown documents found in the directory.");
            return;
        }
        for (MarkdownDocumentReader markdownDocumentReader : markdownDocumentReaderList) {
            List<Document> documents = new TokenTextSplitter(2000, 1024, 10, 10000, true).transform(markdownDocumentReader.get());
            size += documents.size();

            // 拆分 documents 列表为最大 25 个元素的子列表
            for (int i = 0; i < documents.size(); i += 25) {
                int end = Math.min(i + 25, documents.size());
                List<Document> subList = documents.subList(i, end);
                vectorStore.add(subList);
            }
        }
        logger.debug("Load markdown documents into vector store successfully. Load {} documents.", size);
    }

    private List<MarkdownDocumentReader> loadMarkdownDocuments() throws IOException, URISyntaxException {
        List<MarkdownDocumentReader> readers;

        // 首先检查jar包当前运行目录是否存在markdown文件
        Path currentDirPath = Paths.get(System.getProperty("user.dir"), "rag", "markdown");
        if (Files.exists(currentDirPath) && Files.isDirectory(currentDirPath)) {
            // 存在
            logger.debug("Found markdown directory in current running directory: {}", currentDirPath);
            try (Stream<Path> paths = Files.walk(currentDirPath)) {
                List<Path> markdownFiles = paths.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".md"))
                        .toList();

                if (!markdownFiles.isEmpty()) {
                    logger.debug("Loading {} markdown files from current directory", markdownFiles.size());
                    readers = markdownFiles.stream()
                            .map(path -> {
                                String filePath = path.toAbsolutePath().toString();
                                return new MarkdownDocumentReader("file:" + filePath);
                            })
                            .collect(Collectors.toList());
                    return readers;
                } else {
                    logger.debug("No markdown files found in current directory, falling back to resources");
                }
            }
        } else {
            logger.debug("Markdown directory not found in current directory, falling back to resources");
        }

        // 如果当前运行目录没有找到，则从resources目录加载
        Path markdownDir = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("rag/markdown")).toURI());
        logger.debug("Loading markdown files from resources directory: {}", markdownDir);
        try (Stream<Path> paths = Files.walk(markdownDir)) {
            readers = paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".md"))
                    .map(path -> {
                        String fileName = path.getFileName().toString();
                        String classpathPath = "classpath:rag/markdown/" + fileName;
                        return new MarkdownDocumentReader(classpathPath);
                    })
                    .collect(Collectors.toList());
        }
        return readers;
    }
}
