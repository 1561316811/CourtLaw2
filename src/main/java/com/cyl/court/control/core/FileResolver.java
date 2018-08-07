package com.cyl.court.control.core;

import com.cyl.court.anotation.Bean;
import com.cyl.court.beanfactory.BeanFactory;
import com.cyl.court.config.CourtAutoFileConfig;
import com.cyl.court.event.Callback;
import com.cyl.court.model.ArticleNodeModel;
import com.cyl.court.model.ArticleStructModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;


@Bean
public class FileResolver {

    private StringBuilder stringBuilderArticle;

    public void readFile(String fileName, Callback callback) {

        Objects.requireNonNull(fileName);

        if (!checkFileType(fileName)) {
            callback.fail("Error ! This file type can't readFile");
            return;
        }

        String fileCharSet = getFileEncode(fileName).toLowerCase();

        if (!Arrays.asList(
                BeanFactory.getBean(CourtAutoFileConfig.class)
                        .getCharsets()).contains(getFileEncode(fileName).toLowerCase())) {
            callback.fail("Error ! This file charset is " + fileCharSet + " can't readFile");
            return;
        }

        try {
            callback.success(resolveFile(fileName, fileCharSet));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            callback.fail("Error ! File not found !");
        } catch (IOException e) {
            e.printStackTrace();
            callback.fail("Error ! Read file error !");
        }

    }

    private String resolveFile(String fileName, String fileCharSet) throws FileNotFoundException, IOException {
        FileInputStream fis = null;
        byte[] bs = new byte[1024 * 1024];
        StringBuilder sb = new StringBuilder();
        fis = new FileInputStream(fileName);
        int length = 0;
        while ((length = fis.read(bs)) != -1) {
            sb.append(new String(bs, fileCharSet));
        }
        fis.close();
        //更新 stringBuilder
        stringBuilderArticle = sb;
        return sb.toString();
    }

    private String getFileEncode(String fileName) {
        String charsetName = null;
        try {
            File file = new File(fileName);
            CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
            detector.add(new ParsingDetector(false));
            detector.add(JChardetFacade.getInstance());
            detector.add(ASCIIDetector.getInstance());
            detector.add(UnicodeDetector.getInstance());
            java.nio.charset.Charset charset = null;
            charset = detector.detectCodepage(file.toURI().toURL());
            if (charset != null) {
                charsetName = charset.name();
            } else {
                charsetName = "UTF-8";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return charsetName;
    }

    private boolean checkFileType(String fileName) {
        String[] t = fileName.split("\\.");
        String fileType = t[t.length - 1];
        fileType = "*." + fileType;
        return Arrays.asList(
                BeanFactory.getBean(CourtAutoFileConfig.class)
                        .getType()).contains(fileType.toLowerCase());
    }

    private TreeLevelResolver treeLevelResolver = BeanFactory.getBean(TreeLevelResolver.class);

    /**
     *
     * @return
     */
    public boolean resolveFile(StringBuilder sb , ArticleNodeModel articleNode, Callback callback) {

        Objects.requireNonNull(articleNode);
        Objects.requireNonNull(sb);

        //解析标准
        List<ArticleStructModel> structList = treeLevelResolver.getArticleStructList();

        if(structList == null || structList.size() == 0){
            callback.fail("请先配置筛选树");
            return false;
        }

        if( stringBuilderArticle == null || stringBuilderArticle.length() == 0){
           callback.fail("请先导入文本");
           return false;
        }

        //解析数据
        String article = stringBuilderArticle.toString()/*.trim()*/;

        //装入数据
        sb.append(article);

        articleNode.setParent(null);
        articleNode.setStart(0);
        articleNode.setEndContext(article.length());
        articleNode.setEndAll(article.length());

        splitArticle(structList, 0,
                article, articleNode);

        return true;
    }

    private void splitArticle(List<ArticleStructModel> structList, int index,
                              String article, ArticleNodeModel articleNodeParent) {

        if(structList.size() <= index ) return;

        articleNodeParent.initChildrenList();
        ArticleStructModel articleStruct = structList.get(index);
        Pattern pattern = Pattern.compile(articleStruct.getRegex());

        Matcher matcher = pattern.matcher(article);
        matcher.region(articleNodeParent.getStart(), articleNodeParent.getEndAll());

        while (matcher.find()) {
            ArticleNodeModel articleNodeChild = new ArticleNodeModel();
            articleNodeChild.setParent(articleNodeParent);

            //开始匹配项
            articleNodeChild.setStart(matcher.start());
            //结束匹配项
            articleNodeChild.setEndContext(matcher.end());

            //处理上一个节点末尾
            //偷偷往下再匹配一次
            int start = matcher.end();

            if (matcher.find()) {
                articleNodeChild.setEndAll(matcher.start());
                //调整到上一次的位置
                matcher.region(start, articleNodeParent.getEndAll());
            } else {
                articleNodeChild.setEndAll(articleNodeParent.getEndAll());
            }

            articleNodeParent.getChildren().add(articleNodeChild);
            splitArticle(structList, index + 1, article, articleNodeChild);
        }

    }



}
