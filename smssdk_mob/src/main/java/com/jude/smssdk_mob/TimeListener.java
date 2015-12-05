package com.jude.smssdk_mob;

/**
 * Created by Mr.Jude on 2015/7/6.
 */
public interface TimeListener {
    void onLastTimeNotify(int lastSecond);
    void onAbleNotify(boolean valuable);
}
