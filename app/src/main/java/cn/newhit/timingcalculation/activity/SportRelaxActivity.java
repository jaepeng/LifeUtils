package cn.newhit.timingcalculation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fondesa.recyclerviewdivider.DividerBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.newhit.timingcalculation.R;
import cn.newhit.timingcalculation.adapter.RelaxSelectedAdapter;
import cn.newhit.timingcalculation.adapter.RelaxTimeSelectionAdapter;
import cn.newhit.timingcalculation.base.BaseActivity;
import cn.newhit.timingcalculation.bean.SportRelaxBean;

public class SportRelaxActivity extends BaseActivity {
    private static final String TAG = "SportRelaxActivity";
    @BindView(R.id.rv_select_relax_item)
    RecyclerView rvSelectRelaxItem;
    @BindView(R.id.rv_selected_relax)
    RecyclerView rvSelected;
    @BindView(R.id.tv_start_relax)
    TextView tvStartRelax;
    private List<SportRelaxBean> selectionSportBeans;
    private List<SportRelaxBean> selectedSportBeans;
    private RelaxTimeSelectionAdapter mRelaxTimeSelectionAdapter;
    private RelaxSelectedAdapter mRelaxSelectedAdapter;
    private Handler mHandler;
    private CountDownTimer mTimer;
    private RelaxSelectedAdapter.OnItemLongClick mOnItemLongClick;

    @Override
    public int getLayouId() {
        return R.layout.activity_sport_relax;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(getMainLooper());
        initData();
        initView();
    }

    private void initView() {
        rvSelectRelaxItem.setAdapter(mRelaxTimeSelectionAdapter);
        rvSelectRelaxItem.setLayoutManager(new GridLayoutManager(this, 4, RecyclerView.HORIZONTAL, false));
        new DividerBuilder(mContext)
                .color(Color.TRANSPARENT)
                .showSideDividers()
                .size(10, TypedValue.COMPLEX_UNIT_DIP)
                .build()
                .addTo(rvSelectRelaxItem);

        rvSelected.setAdapter(mRelaxSelectedAdapter);
        rvSelected.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


    }

    private void initData() {
        selectedSportBeans = new ArrayList<>();
        selectionSportBeans = new ArrayList<>();
        selectionSportBeans.add(new SportRelaxBean(25, "左腿", true));
        selectionSportBeans.add(new SportRelaxBean(15, "左手", false));
        selectionSportBeans.add(new SportRelaxBean(25, "右腿", true));
        selectionSportBeans.add(new SportRelaxBean(15, "右手", false));
        selectionSportBeans.add(new SportRelaxBean(5, "间隔", true));

        mRelaxTimeSelectionAdapter = new RelaxTimeSelectionAdapter(this, selectionSportBeans);
        mRelaxTimeSelectionAdapter.setOnItemClickListener((position, view) -> {
            // TODO: 2022/2/28 点击了选择放松内容的Item
            SportRelaxBean sportRelaxBean = selectionSportBeans.get(position);
            selectedSportBeans.add(new SportRelaxBean(sportRelaxBean.getRelaxTime(), sportRelaxBean.getReleaxName(), true));
            mRelaxSelectedAdapter.notifyDataSetChanged();
        });
        mRelaxSelectedAdapter = new RelaxSelectedAdapter(this, selectedSportBeans);
        mOnItemLongClick = (position, view) -> {
            // TODO: 2022/2/28 长按已经选进来的东西，可以删除
            selectedSportBeans.remove(position);
            mRelaxSelectedAdapter.notifyDataSetChanged();
        };
        mRelaxSelectedAdapter.setOnItemClickListener(mOnItemLongClick);
    }

    public static void startSportRelaxActivity(Context context) {
        Intent intent = new Intent(context, SportRelaxActivity.class);
        context.startActivity(intent);
    }

    //开始放松的方法
    @OnClick(R.id.tv_start_relax)
    public void startRelax() {
        mRelaxSelectedAdapter.setOnItemClickListener(null);
        mHandler.post(() -> {
            if (mTimer == null) {
                startTimer(selectedSportBeans.get(0));
            }
        });
    }

    private void startTimer(SportRelaxBean sportRelaxBean) {
        long relaxTime = sportRelaxBean.getRelaxTime();
        Log.d(TAG, "startTimer: relaxTime==>" + relaxTime + " ,actionName==>" + sportRelaxBean.getReleaxName());
        mTimer = new CountDownTimer(relaxTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: relaxTime==>" + relaxTime);
                sportRelaxBean.subTime(1);
                mRelaxSelectedAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFinish() {
                selectedSportBeans.remove(sportRelaxBean);
                mRelaxSelectedAdapter.notifyDataSetChanged();
                if (selectedSportBeans.isEmpty()) {
                    mRelaxSelectedAdapter.setOnItemClickListener(mOnItemLongClick);
                    mTimer.cancel();
                    mTimer = null;
                    return;
                }
                startTimer(selectedSportBeans.get(0));

            }
        };
        mTimer.start();
    }
}