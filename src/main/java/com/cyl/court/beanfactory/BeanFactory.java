package com.cyl.court.beanfactory;

import com.cyl.court.anotation.Bean;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.jws.Oneway;

public class BeanFactory {

    private static final BeanFactory instance = new BeanFactory();

    private BeanFactory(){

    }

    public static BeanFactory newInstance(){
        return instance;
    }

    public static  <T> T getBean(Class<T> beanClass){
        return instance.getMapBean(beanClass);
    }

    public static void hostBean(Object obj){
        instance.hostMapBean(obj);
    }

    private Map<String ,Object> beanMaps = new ConcurrentHashMap<>();

    public <T> T getMapBean(Class<T> beanClass){

        if(beanClass.getAnnotation(Bean.class) == null){
            throw new RuntimeException(beanClass.getName() + " is not a bean !");
        }

        Object bean = beanMaps.get(beanClass.getName());
        if(bean != null){
            return (T)bean;
        }
        try {
            String t = beanClass.getName();
            Object o = Class.forName(t).newInstance();
            beanMaps.put(beanClass.getName(), o);
            return (T) o;
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {

        }  catch (InstantiationException e) {
            new RuntimeException(beanClass.getName() + " need a non parameter constructor");
        }
        throw new RuntimeException(beanClass.getName() + " init Error !");
    }

    public void hostMapBean(Object bean){
        if(bean == null){
            throw  new RuntimeException("bean can not be null");
        }
        if(bean.getClass().getAnnotation(Bean.class) == null){
            throw  new RuntimeException(bean.getClass().getName() + " is not a bean !");
        }
        beanMaps.put(bean.getClass().getName(), bean);
    }
}
