package com.cyl.court.control.core;

import com.cyl.court.anotation.Resolver;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.config.CourtAutoPropertyConfig;
import com.cyl.court.control.basic.BasicResolver;
import com.cyl.court.control.basic.Persistence;
import com.cyl.court.event.Callback;
import com.cyl.court.model.ArticleStructModel;
import com.cyl.court.util.JsonIO;
import com.cyl.court.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Resolver
public class ArticlePropResolver implements Persistence, BasicResolver {

  List<ArticleStructModel> articleStructList;

  private int mergeLevel;

  public void uploadMergeLevel(int mergeLevel) {
    this.mergeLevel = mergeLevel;
  }

  public int getMergeLevel() {
    return mergeLevel;
  }

  public ArticlePropResolver() {
  }

  public List<ArticleStructModel> getArticleStructList(Callback callback) {
    if (this.articleStructList == null) {
      try {
        this.articleStructList = readProperty();
      } catch (IOException e) {
        e.printStackTrace();
        if (callback != null)
          callback.fail("读取配置文件失败！");

      }
    }
    return articleStructList;
  }

  /**
   * 上传数据的时候，自动保存
   *
   * @param articleStructList
   * @param callback
   */
  public void uploadTreeStruct(List<ArticleStructModel> articleStructList, Callback callback) {
    saveProperty(articleStructList, callback);
  }

  private CourtAutoPropertyConfig courtAutoPropertyConfig = BeanFactory.getBean(CourtAutoPropertyConfig.class);

  private void saveProperty(List<ArticleStructModel> articleStructList, Callback callback) {

    this.articleStructList = articleStructList;

    Objects.requireNonNull(callback);
    String data = JsonUtil.toJsonDisableHtmlEscaping(articleStructList);
    try {
      JsonIO.writeString(data, courtAutoPropertyConfig.getPropPath());
    } catch (IOException e) {
      e.printStackTrace();
      callback.fail("保存失败");
      return;
    }
    callback.success("保存成功");

  }

  private List<ArticleStructModel> readProperty() throws IOException {

    return JsonUtil.fromJson(new TypeToken<ArrayList<ArticleStructModel>>() {
    }.getType(), read());

  }

  @Override
  public String read() throws IOException {
    String data = null;
    data = JsonIO.readString(courtAutoPropertyConfig.getPropPath());
    return data;
  }

  @Override
  public void write(Object dataObj) throws IOException {
    JsonIO.writeString(JsonUtil.toJsonDisableHtmlEscaping(dataObj), courtAutoPropertyConfig.getPropPath());
  }

  @Override
  public void refresh() throws IOException {
    this.articleStructList = readProperty();
  }

}
