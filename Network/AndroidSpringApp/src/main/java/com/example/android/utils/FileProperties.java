package com.example.android.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "appconf")
@Getter
@Setter
public class FileProperties {

    private String uploadDir;

    private String thumbnailDir;
}
