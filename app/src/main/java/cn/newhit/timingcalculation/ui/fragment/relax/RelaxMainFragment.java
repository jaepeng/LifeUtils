package cn.newhit.timingcalculation.ui.fragment.relax;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annimon.stream.Stream;
import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.newhit.timingcalculation.R;
import cn.newhit.timingcalculation.adapter.RelaxSelectedAdapter;
import cn.newhit.timingcalculation.adapter.RelaxTimeSelectionAdapter;
import cn.newhit.timingcalculation.base.BaseFragment;
import cn.newhit.timingcalculation.bean.SportRelaxBean;
import cn.newhit.timingcalculation.constants.MessageCode;
import cn.newhit.timingcalculation.greendao.SportRelaxDaoManager;
import cn.newhit.timingcalculation.greendao.SportRelaxModel;
import cn.newhit.timingcalculation.message.MessageEvent;

import static android.os.Looper.getMainLooper;

public class RelaxMainFragment extends BaseFragment {
    private static final String TAG = "RelaxMainFragment";
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

    //可选的放松列表
    private List<SportRelaxBean> selectionSportBeans;
    //已选择的放松列表
    private List<SportRelaxBean> selectedSportBeans;
    //上层 可选 的放松列表的适配器
    private RelaxTimeSelectionAdapter mRelaxTimeSelectionAdapter;
    //下层 已选 的放松列表适配器
    private RelaxSelectedAdapter mRelaxSelectedAdapter;
    private Handler mHandler;
    //计时器
    private CountDownTimer mTimer;
    private RelaxSelectedAdapter.OnItemLongClick mOnItemLongClick;
    private SportRelaxDaoManager mSportRelaxDaoManager;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_relax_main;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        mSportRelaxDaoManager = SportRelaxDaoManager.getInstance();
        mHandler = new Handler(getMainLooper());
        selectedSportBeans = new ArrayList<>();
        selectionSportBeans = new ArrayList<>();
        selectionSportBeans.add(new SportRelaxBean(25, "左腿", false));
        selectionSportBeans.add(new SportRelaxBean(15, "左手", false));
        selectionSportBeans.add(new SportRelaxBean(25, "右腿", false));
        selectionSportBeans.add(new SportRelaxBean(15, "右手", false));
        selectionSportBeans.add(new SportRelaxBean(5, "间隔", false));
        // TODO: 2022/3/1 使用数据库存储设置中的自定义放松数据，并且在这个时候进行添加以及删除
        mRelaxTimeSelectionAdapter = new RelaxTimeSelectionAdapter(mContext, selectionSportBeans);
        mRelaxTimeSelectionAdapter.setOnItemClickListener((position, view) -> {
            SportRelaxBean sportRelaxBean = selectionSportBeans.get(position);
            selectedSportBeans.add(new SportRelaxBean(sportRelaxBean.getRelaxTime(), sportRelaxBean.getReleaxName(), true));
            mRelaxSelectedAdapter.notifyDataSetChanged();
        });
        mRelaxSelectedAdapter = new RelaxSelectedAdapter(mContext, selectedSportBeans);
        mOnItemLongClick = (position, view) -> {
            selectedSportBeans.remove(position);
            mRelaxSelectedAdapter.notifyDataSetChanged();
        };
        mRelaxSelectedAdapter.setOnItemClickListener(mOnItemLongClick);
    }

    @Override
    protected void initView() {
        rvSelectRelaxItem.setAdapter(mRelaxTimeSelectionAdapter);
        rvSelectRelaxItem.setLayoutManager(new GridLayoutManager(mContext, 4, RecyclerView.HORIZONTAL, false));

        rvSelected.setAdapter(mRelaxSelectedAdapter);
        rvSelected.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
    }

    //开始放松的方法
    @OnClick(R.id.tv_start_relax)
    public void startRelax() {
        if (selectedSportBeans.isEmpty()) {
            //如果没有已经选中的放松列表则直接返回不进行倒计时
            ToastUtils.showShort("请先选择放松运动！");
            return;
        }
        updateUI(false);
        mHandler.post(() -> {
            if (mTimer == null) {
                startTimer(selectedSportBeans.get(0));
            }
        });
    }

    /**
     * 开始放松进行倒计时
     *
     * @param sportRelaxBean 放松运动包装类
     */
    private void startTimer(SportRelaxBean sportRelaxBean) {
        if (sportRelaxBean == null) {
            return;
        }
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

    /**
     * 是显示倒计时界面还是显示选择放松运动界面
     *
     * @param needShowMain true：显示放松选择界面
     *                     false：显示倒计时界面
     */
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

    @Subscribe
    public void OnMessageEvent(MessageEvent messageEvent) {
        int messageCode = messageEvent.getMessageCode();
        switch (messageCode) {
            case MessageCode.CODE_UPDEATE_RELAX_ITEM:
                //更新数据库数据通知到达后，更新主界面的数据
                List<SportRelaxModel> all = mSportRelaxDaoManager.getAll();
                selectionSportBeans.clear();
                Stream.of(all).forEach(sportRelaxModel -> {
                    selectionSportBeans.add(new SportRelaxBean(sportRelaxModel.getSetSportTime(), sportRelaxModel.getSprotName(), sportRelaxModel.getShowSubIncrease()));
                });
                Log.d(TAG, "OnMessageEvent: all==>" + all);
                break;
            default:
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //界面销毁时取消计时器
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}