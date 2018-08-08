package com.cyl.court.control.basic;

import java.io.IOException;

public interface Persistence {

  String read() throws IOException;

  void write(Object dataModel) throws IOException;

}
