package cn.newhit.timingcalculation.message;

import android.content.Intent;

public class MessageEvent {
    private int messageCode;
    private Intent mIntent;

    public MessageEvent(int messageCode, Intent intent) {
        this.messageCode = messageCode;
        mIntent = intent;
    }

    public int getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(int messageCode) {
        this.messageCode = messageCode;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent intent) {
        mIntent = intent;
    }
}
