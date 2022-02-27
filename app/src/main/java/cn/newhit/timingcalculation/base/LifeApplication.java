package cn.newhit.timingcalculation.base;

import android.app.Application;

import com.tencent.mmkv.MMKV;

public class LifeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);
    }
}
