package com.jude.smssdk_mob;

/**
 * Created by Mr.Jude on 2015/12/5.
 */
public interface Callback {
    void success();
    void error(Throwable error);
}
