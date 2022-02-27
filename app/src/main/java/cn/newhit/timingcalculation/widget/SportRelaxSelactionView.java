package cn.newhit.timingcalculation.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SizeUtils;

import org.jetbrains.annotations.NotNull;

import cn.newhit.timingcalculation.R;
import cn.newhit.timingcalculation.bean.SpotRelaxBean;

public class SportRelaxSelactionView extends ConstraintLayout {

    private boolean mShowSubOrIncrease;
    private TextView mSubTime;
    private TextView mRelaxName;
    private TextView mIncreaseTime;
    private TextView mTvRelaxTime;
    private float mSelectionTextSize;
    private float mSubOrIncreaseTextSize;
    private long mRelaxTime;
    private Context mContext;
    private Handler mHandler;
    private int mTimeUnit;

    public SportRelaxSelactionView(@NonNull @NotNull Context context) {
        this(context, null);
    }

    public SportRelaxSelactionView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SportRelaxSelactionView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mHandler = new Handler(context.getMainLooper());
        loadAttrs(context, attrs);
        initView(context);
        initListener();
    }

    private void initListener() {
        mSubTime.setOnClickListener(v -> {
            updateTime(-1 * mTimeUnit);
        });
        mIncreaseTime.setOnClickListener(v -> {
            updateTime(1 * mTimeUnit);
        });

    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.layout_sport_selaction_view, this, true);
        mSubTime = rootView.findViewById(R.id.tv_sport_selaction_sub);
        mRelaxName = rootView.findViewById(R.id.tv_sport_selaction_name);
        mIncreaseTime = rootView.findViewById(R.id.tv_sport_selaction_increase);
        mTvRelaxTime = rootView.findViewById(R.id.tv_sport_selaction_time);
        mSubTime.setTextSize(mSubOrIncreaseTextSize);
        mIncreaseTime.setTextSize(mSubOrIncreaseTextSize);
        mTvRelaxTime.setTextSize(mSelectionTextSize);
        mRelaxName.setTextSize(mSelectionTextSize);

    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SportRelaxSelactionView);
        mShowSubOrIncrease = a.getBoolean(R.styleable.SportRelaxSelactionView_show_sub_or_incrase, true);
        mSelectionTextSize = a.getDimension(R.styleable.SportRelaxSelactionView_selactionTextSize, SizeUtils.px2sp(SizeUtils.dp2px(14)));
        mSubOrIncreaseTextSize = a.getDimension(R.styleable.SportRelaxSelactionView_sub_or_increase_text_size, SizeUtils.px2sp(SizeUtils.dp2px(14)));
        mTimeUnit = a.getInt(R.styleable.SportRelaxSelactionView_time_unit, 1);
        a.recycle();
    }

    public void setSportData(SpotRelaxBean sportData) {
        mRelaxName.setText(sportData.getReleaxName());
        mRelaxTime = sportData.getRelaxTime();
        mTvRelaxTime.setText(mRelaxTime + "");
    }

    /**
     * 给加减法使用的改变时间方法
     *
     * @param changeTime
     */
    public void updateTime(long changeTime) {
        mHandler.post(() -> {
            if (changeTime < 0 && mRelaxTime + changeTime < 0) {
                return;
            }
            mRelaxTime += changeTime;
            if (mTvRelaxTime != null) {
                mTvRelaxTime.setText(mRelaxTime + "");
            }
        });
    }

    /**
     * 直接进行设置
     *
     * @param realTime 应当设置的时间值
     */
    public void setRealTime(long realTime) {
        mHandler.post(() -> {

            mTvRelaxTime.setText(realTime + "");
        });
    }


}
