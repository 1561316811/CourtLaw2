package com.cyl.court.config;

import com.cyl.court.anotation.Bean;

@Bean
public class CourtAutoFileConfig {

    private static  final String fileType = "*.txt,*.sql,*.doc,*.pdf";

    private static final String charset = "utf-8,gb2312,us-ascii";

    private String[] type = fileType.split(",");

    public String[] getCharsets() {
        return charsets;
    }

    public void setCharsets(String[] charsets) {
        this.charsets = charsets;
    }

    private String[] charsets = charset.split(",");

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

}
