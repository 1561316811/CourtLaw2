package com.cyl.court.event;

public interface Callback {

    <T> void success(T t);

    <T> void fail(T t);

}
