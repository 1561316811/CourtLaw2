package com.cyl.court.config;

import com.cyl.court.anotation.Resolver;

/**
 * 内部资源文件配置属性，一旦形成jar文件那便不能修改
 */
@Resolver
public class CourtAutoResourcePathConfig {

    private static final String pathName = "static/";

    private static final String postfix = ".fxml";

    private String path = pathName;

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
