package cn.newhit.timingcalculation.constants;

public enum TimeUnit {

    MINUTE("分"), SECOND("秒"), HOUR("小时");
    private String timeUnit;

    TimeUnit(String time) {
        timeUnit = time;
    }

    public String getTimeUnit() {
        return timeUnit;
    }
}
