package com.cyl.court.config;

import com.cyl.court.anotation.Resolver;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 *外部资源文件配置，
 * 比如可读写的配置文件
  */
@Resolver
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

    private static final String treePathS = basicPath + "property"+ File.separator +"tree.json";

    private String propPath = treePathS;

    private static final String sqlConnDataPathS = basicPath + "property/sql";

    private String sqlConnDataPath = sqlConnDataPathS;

    public String getSqlConnDataPath() {
        return sqlConnDataPath;
    }

    public void setSqlConnDataPath(String sqlConnDataPath) {
        this.sqlConnDataPath = sqlConnDataPath;
    }

    public String getPropPath() {
        return propPath;
    }

    public void setPropPath(String propPath) {
        this.propPath = propPath;
    }

}
