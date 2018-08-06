package com.cyl.court.config;

import com.cyl.court.anotation.Bean;

@Bean
public class CourtAutoPropertyConfig {

    private static final String path = "property/tree.json";

    public String getPropPath() {
        return propPath;
    }

    public void setPropPath(String propPath) {
        this.propPath = propPath;
    }

    private String propPath = path;

}
