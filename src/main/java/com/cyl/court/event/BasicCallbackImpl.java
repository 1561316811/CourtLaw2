package com.cyl.court.event;

import com.cyl.court.util.ViewUtil;

public class BasicCallbackImpl implements Callback{

    @Override
    public <T> void success(T t) {
        ViewUtil.alertInfoDialog(t.toString());
    }

    @Override
    public <T> void fail(T t) {
        ViewUtil.alertInfoDialog(t.toString());
    }

}
