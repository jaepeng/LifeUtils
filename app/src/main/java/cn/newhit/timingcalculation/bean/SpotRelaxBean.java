package cn.newhit.timingcalculation.bean;

import java.util.concurrent.TimeUnit;

/**
 * 运动放松的bean
 */
public class SpotRelaxBean {
    //放松时间
    long relaxTime;
    //放松的名称
    String releaxName;
    TimeUnit mTimeUnit;

    public SpotRelaxBean(long relaxTime, String releaxName) {
        this.relaxTime = relaxTime;
        this.releaxName = releaxName;
    }

    public long getRelaxTime() {
        return relaxTime;
    }

    public void setRelaxTime(long relaxTime) {
        this.relaxTime = relaxTime;
    }

    public String getReleaxName() {
        return releaxName;
    }

    public void setReleaxName(String releaxName) {
        this.releaxName = releaxName;
    }
    public void increaseTime(long increaseTime){
        this.relaxTime+=increaseTime;
    }
    public void subTime(long subTime){
        if (relaxTime<=0){
            return;
        }
        increaseTime(-subTime);
    }
}
