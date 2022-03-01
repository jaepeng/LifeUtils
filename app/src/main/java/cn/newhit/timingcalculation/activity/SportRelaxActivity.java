package cn.newhit.timingcalculation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    @BindView(R.id.tv_relax_show_name)
    TextView tvRelaxShowName;
    @BindView(R.id.tv_relax_show_time)
    TextView tvRelaxShowTime;
    @BindView(R.id.cl_relax_time_show)
    ConstraintLayout clRelaxTimeShow;

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

        rvSelected.setAdapter(mRelaxSelectedAdapter);
        rvSelected.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


    }

    private void initData() {
        selectedSportBeans = new ArrayList<>();
        selectionSportBeans = new ArrayList<>();
        selectionSportBeans.add(new SportRelaxBean(25, "左腿", false));
        selectionSportBeans.add(new SportRelaxBean(15, "左手", false));
        selectionSportBeans.add(new SportRelaxBean(25, "右腿", false));
        selectionSportBeans.add(new SportRelaxBean(15, "右手", false));
        selectionSportBeans.add(new SportRelaxBean(5, "间隔", false));
        // TODO: 2022/3/1 使用数据库存储设置中的自定义放松数据，并且在这个时候进行添加以及删除
        mRelaxTimeSelectionAdapter = new RelaxTimeSelectionAdapter(this, selectionSportBeans);
        mRelaxTimeSelectionAdapter.setOnItemClickListener((position, view) -> {
            SportRelaxBean sportRelaxBean = selectionSportBeans.get(position);
            selectedSportBeans.add(new SportRelaxBean(sportRelaxBean.getRelaxTime(), sportRelaxBean.getReleaxName(), true));
            mRelaxSelectedAdapter.notifyDataSetChanged();
        });
        mRelaxSelectedAdapter = new RelaxSelectedAdapter(this, selectedSportBeans);
        mOnItemLongClick = (position, view) -> {
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
        updateUI(false);
        mHandler.post(() -> {
            if (mTimer == null) {
                startTimer(selectedSportBeans.get(0));
            }
        });
    }

    private void startTimer(SportRelaxBean sportRelaxBean) {
        long relaxTime = sportRelaxBean.getRelaxTime();
        Log.d(TAG, "startTimer: relaxTime==>" + relaxTime + " ,actionName==>" + sportRelaxBean.getReleaxName());
        tvRelaxShowName.setText(sportRelaxBean.getReleaxName());
        mTimer = new CountDownTimer(relaxTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: relaxTime==>" + relaxTime);

                mHandler.post(() -> {
                    sportRelaxBean.subTime(1);
                    tvRelaxShowTime.setText(sportRelaxBean.getRelaxTime() + sportRelaxBean.getTimeUnit().getTimeUnit());
                });
                mRelaxSelectedAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFinish() {
                selectedSportBeans.remove(sportRelaxBean);
                mRelaxSelectedAdapter.notifyDataSetChanged();
                if (selectedSportBeans.isEmpty()) {
                    updateUI(true);
                    mTimer.cancel();
                    mTimer = null;
                    return;
                }
                startTimer(selectedSportBeans.get(0));

            }
        };
        mTimer.start();
    }

    private void updateUI(boolean needShowMain) {
        if (needShowMain) {
            rvSelectRelaxItem.setVisibility(View.VISIBLE);
            rvSelected.setVisibility(View.VISIBLE);
            clRelaxTimeShow.setVisibility(View.INVISIBLE);
            tvStartRelax.setVisibility(View.VISIBLE);
            mRelaxSelectedAdapter.setOnItemClickListener(mOnItemLongClick);
        } else {
            rvSelectRelaxItem.setVisibility(View.INVISIBLE);
            rvSelected.setVisibility(View.INVISIBLE);
            clRelaxTimeShow.setVisibility(View.VISIBLE);
            tvStartRelax.setVisibility(View.INVISIBLE);
            mRelaxSelectedAdapter.setOnItemClickListener(null);
        }
    }
}