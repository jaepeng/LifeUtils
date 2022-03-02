package cn.newhit.timingcalculation.bean;

public class RelaxMenuBean {
    String menuName;
    int localId;
    String menuIconUrl;
    boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public RelaxMenuBean(String menuName, int localId) {
        this.menuName = menuName;
        this.localId = localId;
    }

    public RelaxMenuBean(String menuName, int localId, boolean isSelected) {
        this.menuName = menuName;
        this.localId = localId;
        this.isSelected = isSelected;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getMenuIconUrl() {
        return menuIconUrl;
    }

    public void setMenuIconUrl(String menuIconUrl) {
        this.menuIconUrl = menuIconUrl;
    }
}
