package com.spring.food.util;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ResourceUtils {

    public static String getContentFromResource(String resourceName) {
        try {
            InputStream stream = ResourceUtils.class.getResourceAsStream(resourceName);
            String teste = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
            return teste;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
