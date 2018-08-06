package com.cyl.court.control;

import com.cyl.court.anotation.Bean;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.config.CourtAutoPropertyConfig;
import com.cyl.court.event.Callback;
import com.cyl.court.model.ArticleStructModel;
import com.cyl.court.util.JsonIO;
import com.cyl.court.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Bean
public class TreeLevelResolver {

    List<ArticleStructModel> articleStructList;

    public TreeLevelResolver() {
    }

    public List<ArticleStructModel> getArticleStructList() {
        if(this.articleStructList == null){
            this.articleStructList = readProperty();
        }
        return articleStructList;
    }

    public void uploadTreeStruct(List<ArticleStructModel> articleStructList) {
        this.articleStructList = articleStructList;
    }

    private CourtAutoPropertyConfig courtAutoPropertyConfig = BeanFactory.getBean(CourtAutoPropertyConfig.class);

    public void saveProperty(List<ArticleStructModel> articleStructList, Callback callback) {

        this.articleStructList = articleStructList;

        Objects.requireNonNull(callback);
        String data = JsonUtil.toJsonDisableHtmlEscaping(articleStructList);
        try {
            JsonIO.write(data, courtAutoPropertyConfig.getPropPath());
        } catch (IOException e) {
            e.printStackTrace();
            callback.fail("保存失败");
            return;
        }
        callback.success("保存成功");
    }

    public List<ArticleStructModel> readProperty() {

        String data = JsonIO.read(courtAutoPropertyConfig.getPropPath());
        return JsonUtil.fromJson( new TypeToken<ArrayList<ArticleStructModel>>(){}.getType(), data);

    }


}
