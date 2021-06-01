package org.example.component;

import java.io.InputStream;

public class Resource {
    public static InputStream getInputStream(String path) {
        return Resource.class.getClassLoader().getResourceAsStream(path);
    }
}
