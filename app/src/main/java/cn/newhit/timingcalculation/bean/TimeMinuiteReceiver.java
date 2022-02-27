package cn.newhit.timingcalculation.bean;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Date;

public class TimeMinuiteReceiver extends BroadcastReceiver {

    private OnTimeChange mOnTimeChange;
    private Context mContext;
    private boolean isRegister;

    public static class TimeMineiteReceiverHolder {
        public static TimeMinuiteReceiver INSTANCE = new TimeMinuiteReceiver();
    }

    public static TimeMinuiteReceiver getInstance() {
        return TimeMineiteReceiverHolder.INSTANCE;
    }
    public void initContext(Context context){
        this.mContext=context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_TIME_TICK)) {
            Date date = new Date(System.currentTimeMillis());
            if (mOnTimeChange != null) {
                mOnTimeChange.onTimeChange(date);
            }
        }

    }

    public boolean isRegister() {
        return isRegister;
    }

    public void setRegister(boolean register) {
        isRegister = register;
    }

    public void setOnTimeChangeListener(OnTimeChange onTimeChangeListener) {
        this.mOnTimeChange = onTimeChangeListener;

    }

    public interface OnTimeChange {
        void onTimeChange(Date date);
    }

    public void unRegisterReceiver() {
        isRegister=false;
        mContext.unregisterReceiver(this);
    }

    public void registerTimeMinuteReceiver() {
        isRegister=true;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        mContext.registerReceiver(this, filter);
    }
}