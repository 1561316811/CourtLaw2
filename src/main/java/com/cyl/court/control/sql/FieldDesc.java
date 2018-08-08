package com.cyl.court.control.sql;

public class FieldDesc {

  private String name;

  private String type;

  private String desc;

  private Integer dataSize;

  public FieldDesc() {
  }


  public FieldDesc(String name, String type, String desc, Integer dataSize, int nullable) {
    this.name = name;
    this.type = type;
    this.desc = desc;
    this.dataSize = dataSize;
    this.nullable = nullable;
  }

  public int getNullable() {
    return nullable;
  }

  public void setNullable(int nullable) {
    this.nullable = nullable;
  }

  private int nullable;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public Integer getDataSize() {
    return dataSize;
  }

  public void setDataSize(Integer dataSize) {
    this.dataSize = dataSize;
  }
}
