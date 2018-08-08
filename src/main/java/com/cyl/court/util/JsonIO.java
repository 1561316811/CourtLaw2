package com.cyl.court.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class JsonIO {

  /**
   * 字符串的形式写入
   *
   * @param data
   * @param filePath
   * @throws IOException
   */
  public static void writeString(String data, String filePath) throws IOException {

    Objects.requireNonNull(filePath);
    if (StringUtils.isEmpty(filePath))
      throw new RuntimeException("FilePath can not be empty!");

    FileWriter fw = null;
    fw = new FileWriter(repairNonFile(filePath), false);
    fw.write(data.toCharArray());
    fw.close();

  }

  /**
   * 字符串的形式读取
   *
   * @param filePath
   * @return
   * @throws IOException
   */
  public static String readString(String filePath) throws IOException {
    Objects.requireNonNull(filePath);
    if (StringUtils.isEmpty(filePath))
      throw new RuntimeException("FilePath can not be empty!");

    File file = new File(filePath);
    if (!file.exists()) {
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

  /**
   * 字节流的形式写入文件
   *
   * @param data
   * @param filePath
   * @throws IOException
   */
  public static void writeStream(String data, String filePath) throws IOException {
    StringUtils.requiredNoNullAndEmpty(filePath);

    FileOutputStream fos = new FileOutputStream(repairNonFile(filePath));
    fos.write(data.getBytes());
    fos.close();
  }

  /**
   *字节流读取文件
   */
  public static String readStream(String filePath) throws IOException {

    StringUtils.requiredNoNullAndEmpty(filePath);
    repairNonFile(filePath);
    StringBuilder sb = new StringBuilder();
    FileInputStream fis = new FileInputStream(repairNonFile(filePath));
    byte[] bs = new byte[1024 * 1024];
    int length ;
    while ((length = fis.read(bs)) != -1) {
      String metaData = new String(bs,0,length);
      sb.append(metaData);
    }
    if (fis != null) {
      fis.close();
    }
    return sb.toString();
}

  /**
   * 修复不存在的文件
   *
   * @param filePath
   */
  public static File repairNonFile(String filePath) throws IOException {
    File file = new File(filePath);
    if (!file.exists()) {
      file.getParentFile().mkdirs();
      file.createNewFile();
    }
    return file;
  }

}
