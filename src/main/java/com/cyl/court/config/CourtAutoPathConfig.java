package com.cyl.court.config;

import com.cyl.court.anotation.Bean;

@Bean
public class CourtAutoPathConfig {

    private static  final String pathName = "com/cyl/court/resource/";

    private String path = pathName;

    private static final String postfix = ".fxml";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPostfix() {
        return postfix;
    }
}
