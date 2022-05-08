package cn.newhit.timingcalculation.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SportRelaxModel {
    @Id(autoincrement = true)
    private Long id;
    String sprotName;
    long initsprotTime;
    long setSportTime;
    boolean showSubIncrease;

    @Generated(hash = 1589168351)
    public SportRelaxModel(Long id, String sprotName, long initsprotTime,
                           long setSportTime, boolean showSubIncrease) {
        this.id = id;
        this.sprotName = sprotName;
        this.initsprotTime = initsprotTime;
        this.setSportTime = setSportTime;
        this.showSubIncrease = showSubIncrease;
    }

    @Generated(hash = 1364380480)
    public SportRelaxModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSprotName() {
        return this.sprotName;
    }

    public void setSprotName(String sprotName) {
        this.sprotName = sprotName;
    }

    public long getInitsprotTime() {
        return this.initsprotTime;
    }

    public void setInitsprotTime(long initsprotTime) {
        this.initsprotTime = initsprotTime;
    }

    public long getSetSportTime() {
        return this.setSportTime;
    }

    public void setSetSportTime(long setSportTime) {
        this.setSportTime = setSportTime;
    }

    public boolean getShowSubIncrease() {
        return this.showSubIncrease;
    }

    public void setShowSubIncrease(boolean showSubIncrease) {
        this.showSubIncrease = showSubIncrease;
    }
}
