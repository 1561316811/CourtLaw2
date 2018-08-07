package com.cyl.court.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class JsonIO {

  public static void write(String data, String filePath) throws IOException {

    Objects.requireNonNull(filePath);
    if (StringUtils.isEmpty(filePath))
      throw new RuntimeException("FilePath can not be empty!");
    FileWriter fw = null;
    File file = new File(filePath);

    file.getParentFile().mkdirs();

    if (!file.exists()) {
      file.createNewFile();
    }

    fw = new FileWriter(file, false);
    fw.write(data.toCharArray());
    if (fw != null) {
      try {
        fw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static String read(String filePath) throws IOException {
    Objects.requireNonNull(filePath);
    if (StringUtils.isEmpty(filePath))
      throw new RuntimeException("FilePath can not be empty!");

    File file = new File(filePath);
    if(!file.exists()){
      file.getParentFile().mkdirs();
      file.createNewFile();
      return null;
    }

    FileReader fr = null;
    char[] chs = new char[1024];
    int length = 0;
    StringBuilder sb = new StringBuilder();
    try {
      fr = new FileReader(filePath);
      while ((length = fr.read(chs)) != -1) {
        sb.append(chs, 0, length);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fr != null) {
        try {
          fr.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return sb.toString();
  }

}
