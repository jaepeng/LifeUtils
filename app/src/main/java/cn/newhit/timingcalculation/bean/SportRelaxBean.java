package cn.newhit.timingcalculation.bean;

import java.util.concurrent.TimeUnit;

/**
 * 运动放松的bean
 */
public class SportRelaxBean {
    int localIconId;
    //放松时间
    long relaxTime;
    //放松的名称
    String releaxName;
    TimeUnit mTimeUnit=TimeUnit.SECONDS;
    boolean showSubIncrease=true;

    public SportRelaxBean(long relaxTime, String releaxName, boolean showSubIncrease) {
        this.relaxTime = relaxTime;
        this.releaxName = releaxName;
        this.showSubIncrease = showSubIncrease;
    }

    public SportRelaxBean(int localIconId, long relaxTime, String releaxName, boolean showSubIncrease) {
        this.localIconId = localIconId;
        this.relaxTime = relaxTime;
        this.releaxName = releaxName;
        this.showSubIncrease = showSubIncrease;
    }

    public TimeUnit getTimeUnit() {
        return mTimeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        mTimeUnit = timeUnit;
    }

    public boolean isShowSubIncrease() {
        return showSubIncrease;
    }

    public void setShowSubIncrease(boolean showSubIncrease) {
        this.showSubIncrease = showSubIncrease;
    }

    public SportRelaxBean(long relaxTime, String releaxName) {
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
