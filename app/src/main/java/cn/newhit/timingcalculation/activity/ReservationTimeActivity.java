package cn.newhit.timingcalculation.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.mmkv.MMKV;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.newhit.timingcalculation.R;
import cn.newhit.timingcalculation.TimeMinuiteReceiver;
import cn.newhit.timingcalculation.constants.Constants;
import cn.newhit.timingcalculation.utils.back.BackHandlerHelper;

/**
 * 1. 可以自定义预约时间单位
 * 2. 可以自定义操作时间单位，是小时还是分
 */

/**
 * 时间预定计算
 */

public class ReservationTimeActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Unbinder mBind;
    @BindView(R.id.edt_nowtime)
    EditText edtNowTime;
    @BindView(R.id.edt_handleTime)
    EditText edtHandleTime;
    @BindView(R.id.edt_targetTime)
    EditText edtTargetTime;
    @BindView(R.id.tv_timeresult)
    TextView tvTimeResult;
    @BindView(R.id.tv_cal)
    TextView tv_cal;
    @BindView(R.id.tv_ck_tip)
    TextView tvCktip;
    @BindView(R.id.ck_now_time_auto)
    CheckBox ckGetTimeAuto;
    private TimeMinuiteReceiver mTimeMinuiteReceiver;
    private String mNowTime;
    private Handler mHandler;

    private boolean isAutoGetTime;
    private long length;
    private int mSelectHour;
    private int mSelectMinute;

    public static void startRevervationTimeActivity(Context context) {
        Intent intent = new Intent(context, ReservationTimeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_time);
        mBind = ButterKnife.bind(this);
        MMKV.initialize(this);
        mHandler = new Handler(Looper.getMainLooper());
        initTimeReceiver();
        initView();
        initListenre();
    }

    private void initListenre() {
        edtTargetTime.setOnClickListener(v -> showTimePickerDialog(3, edtTargetTime));
        edtNowTime.setOnClickListener(v -> showTimePickerDialog(3, edtNowTime));
        tvCktip.setOnClickListener(v -> {
            ckGetTimeAuto.setChecked(!ckGetTimeAuto.isChecked());
        });
        ckGetTimeAuto.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d(TAG, "initListenre: onCheckChange==>" + isChecked);
            MMKV.defaultMMKV().encode(Constants.MMKV_CODE_LAST_AUTO_GET_TIME, isChecked);
            if (isChecked) {
                //自动更新时间
                isAutoGetTime = true;
                if (!mTimeMinuiteReceiver.isRegister()) {
                    mTimeMinuiteReceiver.registerTimeMinuteReceiver();
                }
            } else {
                //不自动更新时间
                isAutoGetTime = false;
                if (mTimeMinuiteReceiver.isRegister()) {
                    mTimeMinuiteReceiver.unRegisterReceiver();
                }
            }
        });
        edtNowTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: s==>" + s + " ,start==>" + start + " ,count==>" + count + " ,after==>" + after);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != length) {
                    ckGetTimeAuto.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initView() {
        edtNowTime.setText(TimeUtils.millis2String(System.currentTimeMillis(), "HH:mm"));
        length = edtNowTime.getText().toString().length();
        isAutoGetTime = MMKV.defaultMMKV().decodeBool(Constants.MMKV_CODE_LAST_AUTO_GET_TIME, true);
        ckGetTimeAuto.setChecked(isAutoGetTime);
        edtHandleTime.setText(MMKV.defaultMMKV().decodeString(Constants.MMKV_CODE_LAST_HANDLE_TIME, ""));
        edtTargetTime.setText(MMKV.defaultMMKV().decodeString(Constants.MMKV_CODE_LAST_TARGET_TIME, ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAutoGetTime) {
            edtNowTime.setText(TimeUtils.millis2String(System.currentTimeMillis(), "HH:mm"));
            if (!mTimeMinuiteReceiver.isRegister()) {
                mTimeMinuiteReceiver.registerTimeMinuteReceiver();
            }
        }
    }

    /**
     * 计算预约时间
     */
    @OnClick({R.id.tv_cal})
    public void calTimeClick() {
        String nowTime = edtNowTime.getText().toString();
        String targetTime = edtTargetTime.getText().toString();
        String handleTime = edtHandleTime.getText().toString();
        if (TextUtils.isEmpty(nowTime) || TextUtils.isEmpty(targetTime) || TextUtils.isEmpty(handleTime)) {
            ToastUtils.make().setGravity(Gravity.CENTER, 0, 0).setTextSize(18).show("有未填数据！");
            return;
        }
        String resultTime = calTime(nowTime, targetTime, handleTime);
        tvTimeResult.setText("所需时间 " + resultTime);
    }

    /**
     * 计算预约时间
     *
     * @param nowTimeStr
     * @param targetTimeStr
     * @param handleTimeStr
     */
    private String calTime(String nowTimeStr, String targetTimeStr, String handleTimeStr) {
        long now = TimeUtils.string2Millis(nowTimeStr, "HH:mm");
        long target = TimeUtils.string2Millis(targetTimeStr, "HH:mm");
        long handle = Long.valueOf(handleTimeStr).longValue();
        //距离目标时间应该预约的分钟数
        long millis2TimeSpan;
        String resultStr = "";
        if (now > target) {
            millis2TimeSpan = 24 * 60 + ConvertUtils.millis2TimeSpan(target - now, TimeConstants.MIN);
        } else {
            millis2TimeSpan = ConvertUtils.millis2TimeSpan(target - now, TimeConstants.MIN);
        }
        millis2TimeSpan -= handle;
        if (millis2TimeSpan > 0) {
            if (millis2TimeSpan / 60 == 0) {
                if (millis2TimeSpan % 60 < 10) {
                    resultStr = "00:" + "0" + millis2TimeSpan % 60;
                } else {
                    resultStr = "00:" + millis2TimeSpan % 60;
                }

            } else {
                if (millis2TimeSpan % 60 == 0) {
                    if (millis2TimeSpan / 60 < 10) {
                        resultStr = "0" + millis2TimeSpan / 60 + ":00";
                    } else {
                        resultStr = millis2TimeSpan / 60 + ":00";
                    }
                } else {
                    String hourStr, minuStr = "00";
                    if (millis2TimeSpan / 60 < 10) {
                        hourStr = "0" + millis2TimeSpan / 60;
                    } else {
                        hourStr = millis2TimeSpan / 60 + "";
                    }
                    if (millis2TimeSpan % 60 < 10) {
                        minuStr = "0" + millis2TimeSpan % 60;
                    } else {
                        minuStr = "" + millis2TimeSpan % 60;
                    }
                    resultStr = hourStr + ":" + minuStr;
                }
            }
        } else {
            resultStr = "00:00";
        }
        MMKV.defaultMMKV().encode(Constants.MMKV_CODE_LAST_HANDLE_TIME, handleTimeStr);
        MMKV.defaultMMKV().encode(Constants.MMKV_CODE_LAST_TARGET_TIME, targetTimeStr);
        return resultStr;
    }

    private void initTimeReceiver() {
        mTimeMinuiteReceiver = TimeMinuiteReceiver.getInstance();
        mTimeMinuiteReceiver.initContext(this);
        mTimeMinuiteReceiver.setOnTimeChangeListener(date -> {
            mNowTime = TimeUtils.date2String(date, "HH:mm");
            mHandler.post(() -> {
                edtNowTime.setText(mNowTime);
            });
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        mTimeMinuiteReceiver.unRegisterReceiver();
    }

    long lastBackPress;

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {

            if (System.currentTimeMillis() - lastBackPress < 1000) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            } else {
                lastBackPress = System.currentTimeMillis();
                ToastUtils.make()
                        .setBgColor(getResources().getColor(R.color.color_toast_bg))
                        .setTextColor(Color.WHITE)
                        .show("再按一次退出程序");
            }
        }
    }

    /**
     * 时间选择
     *
     * @param themeResId
     */
    public void showTimePickerDialog(int themeResId, EditText editText) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        String editTimeStr = editText.getText().toString();
        String[] split = editTimeStr.trim().split(":");
        Log.d(TAG, "showTimePickerDialog: split==>" + split[0] + split[1]);
        new TimePickerDialog(this, themeResId,
                // 绑定监听器
                (view, hourOfDay, minute) -> {
                    int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                    int currentMinute = calendar.get(Calendar.MINUTE);

                    mSelectHour = hourOfDay;
                    mSelectMinute = minute;
                    String time = hourOfDay + ":" + minute;
                    editText.setText(time);
                }, split.length > 1 ? Integer.parseInt(split[0]) : calendar.get(Calendar.HOUR_OF_DAY)
                , split.length > 1 ? Integer.parseInt(split[1]) : calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
    }
}