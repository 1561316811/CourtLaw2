package com.cyl.court.config;

import com.cyl.court.anotation.Bean;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 *外部资源文件配置，
 * 比如可读写的配置文件
  */
@Bean
public class CourtAutoPropertyConfig {


    private static String basicPath = null;
    static {
        URL url = CourtAutoPropertyConfig.class.getClassLoader().getResource(
                CourtAutoPropertyConfig.class.getName()
                        .replaceAll("\\.", "/") + ".class");

        if (url.toString().startsWith("jar")) {
            String jarWholePath = CourtAutoPropertyConfig.class.getProtectionDomain()
                .getCodeSource().getLocation().getFile();
            try {
                jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                System.out.println(e.toString());
            }
            basicPath = new File(jarWholePath).getParentFile().getAbsolutePath() + File.separator;
        } else {
            basicPath = "";
        }
    }

    private static final String treePath = basicPath + "property"+ File.separator +"tree.json";

    public String getPropPath() {
        return propPath;
    }

    public void setPropPath(String propPath) {
        this.propPath = propPath;
    }

    private String propPath = treePath;

}
