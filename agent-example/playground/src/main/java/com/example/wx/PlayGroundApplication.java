package com.example.wx;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Set;

/**
 * @author wangxiang
 * @description
 * @create 2025/7/20 15:19
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class PlayGroundApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().filename(".env").load();
        Set<String> set = Set.of("AI_DASH_SCOPE_API_KEY", "DEEPSEEK_API_KEY");
        for (String item : set) {
            System.setProperty(item, dotenv.get(item));
        }
        System.setProperty("VECTOR_STORE_TYPE", "pgvector");
        SpringApplication.run(PlayGroundApplication.class, args);
    }
}