package com.cyl.court.control.basic;

import java.io.IOException;
import java.sql.SQLException;

public interface BasicResolver {

  void refresh() throws IOException, SQLException;

}
