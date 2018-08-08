package com.cyl.court.control.sql;

import java.util.ArrayList;
import java.util.List;

public class TableDesc {

  private String name;

  private List<FieldDesc> fieldDescList = new ArrayList<>();

  public List<FieldDesc> getFieldDescList() {
    return fieldDescList;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
