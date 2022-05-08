package cn.newhit.timingcalculation.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SizeUtils;

import org.jetbrains.annotations.NotNull;

import cn.newhit.timingcalculation.R;
import cn.newhit.timingcalculation.bean.SportRelaxBean;
import cn.newhit.timingcalculation.constants.TimeUnit;

public class SportRelaxSelactionView extends ConstraintLayout {
    private static final String TAG = "SportRelaxSelactionView";

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
    private TimeUnit mTimeUnit = TimeUnit.SECOND;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClick;
    private int mActionTextColor;
    private int mSubOrIncreaseTextColor;
    private SportRelaxBean mSportRelaxBean;

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
        mIncreaseTime.setOnClickListener(v -> {
            updateTime(1);
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onIncreaseClick(mRelaxTime + 1, v);
            }
        });
        mSubTime.setOnClickListener(v -> {
            updateTime(-1);
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onSubTimeClick(mRelaxTime - 1, v);
            }
        });
        mTvRelaxTime.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onActionClick(mRelaxTime, v);
            }
        });
        mRelaxName.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onActionClick(mRelaxTime, v);
            }
        });
        mRelaxName.setOnLongClickListener(v -> {
            if (mOnItemLongClick != null) {
                mOnItemLongClick.onActionLongClick(mRelaxTime, v);
            }
            return true;
        });

    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.layout_sport_selaction_view, this, true);
        mSubTime = rootView.findViewById(R.id.tv_sport_selaction_sub);
        mRelaxName = rootView.findViewById(R.id.tv_sport_selaction_name);
        mIncreaseTime = rootView.findViewById(R.id.tv_sport_selaction_increase);
        mTvRelaxTime = rootView.findViewById(R.id.tv_sport_selaction_time);
        mSubTime.setTextColor(mSubOrIncreaseTextColor);
        mIncreaseTime.setTextColor(mSubOrIncreaseTextColor);
        mRelaxName.setTextColor(mActionTextColor);
        mTvRelaxTime.setTextColor(mActionTextColor);
        mSubTime.setTextSize(mSubOrIncreaseTextSize);
        mIncreaseTime.setTextSize(mSubOrIncreaseTextSize);
        mSubTime.setVisibility(mShowSubOrIncrease ? VISIBLE : INVISIBLE);
        mIncreaseTime.setVisibility(mShowSubOrIncrease ? VISIBLE : INVISIBLE);
        mTvRelaxTime.setTextSize(mSelectionTextSize);
        mRelaxName.setTextSize(mSelectionTextSize);


    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SportRelaxSelactionView);
        mShowSubOrIncrease = a.getBoolean(R.styleable.SportRelaxSelactionView_show_sub_or_incrase, true);
        mSelectionTextSize = a.getDimension(R.styleable.SportRelaxSelactionView_selactionTextSize, SizeUtils.px2sp(SizeUtils.dp2px(14)));
        mSubOrIncreaseTextSize = a.getDimension(R.styleable.SportRelaxSelactionView_sub_or_increase_text_size, SizeUtils.px2sp(SizeUtils.dp2px(14)));
        mActionTextColor = a.getColor(R.styleable.SportRelaxSelactionView_actionTextColor, Color.WHITE);
        mSubOrIncreaseTextColor = a.getColor(R.styleable.SportRelaxSelactionView_subOrIncreaseColor, Color.WHITE);
        a.recycle();
    }

    public void setSportData(SportRelaxBean sportData) {
        Log.d(TAG, "setSportData: sportDataString==>" + sportData.toString());
        mSportRelaxBean = sportData;
        mRelaxName.setText(mSportRelaxBean.getReleaxName());
        mRelaxTime = mSportRelaxBean.getRelaxTime();
        mTvRelaxTime.setText(mRelaxTime + mTimeUnit.getTimeUnit());
    }

    public void upDateSportData() {

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
            mSportRelaxBean.setRelaxTime(mRelaxTime);
            if (mTvRelaxTime != null) {
                mTvRelaxTime.setText(mRelaxTime + mTimeUnit.getTimeUnit());
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

            mTvRelaxTime.setText(realTime + mTimeUnit.getTimeUnit());
        });
    }

    public void setShowSubOrIncrease(boolean showOrHide) {
        mShowSubOrIncrease = showOrHide;
        mSubTime.setVisibility(mShowSubOrIncrease ? VISIBLE : INVISIBLE);
        mIncreaseTime.setVisibility(mShowSubOrIncrease ? VISIBLE : INVISIBLE);
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.mTimeUnit = timeUnit;

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;

    }

    public interface OnItemClickListener {
        //点击减少
        void onSubTimeClick(long realTime, View view);

        //点击增加
        void onIncreaseClick(long realTime, View view);

        //内容点击
        void onActionClick(long realTime, View view);


    }

    public void setOnItemLongClick(OnItemLongClickListener onItemLongClick) {
        this.mOnItemLongClick = onItemLongClick;
    }

    public interface OnItemLongClickListener {

        void onActionLongClick(long realTime, View view);
    }


}
