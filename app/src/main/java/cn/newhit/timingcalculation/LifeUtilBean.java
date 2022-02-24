package cn.newhit.timingcalculation;

public class LifeUtilBean {
    String lifeUtilName;
    int lifeUtilResId;
    String lifeUtilUrl;

    public LifeUtilBean(String lifeUtilName, int lifeUtilResId) {
        this.lifeUtilName = lifeUtilName;
        this.lifeUtilResId = lifeUtilResId;
    }

    public String getLifeUtilName() {
        return lifeUtilName;
    }

    public void setLifeUtilName(String lifeUtilName) {
        this.lifeUtilName = lifeUtilName;
    }

    public int getLifeUtilResId() {
        return lifeUtilResId;
    }

    public void setLifeUtilResId(int lifeUtilResId) {
        this.lifeUtilResId = lifeUtilResId;
    }

    public String getLifeUtilUrl() {
        return lifeUtilUrl;
    }

    public void setLifeUtilUrl(String lifeUtilUrl) {
        this.lifeUtilUrl = lifeUtilUrl;
    }
}
