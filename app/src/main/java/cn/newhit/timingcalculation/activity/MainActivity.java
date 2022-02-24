package cn.newhit.timingcalculation.activity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fondesa.recyclerviewdivider.DividerBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.newhit.timingcalculation.LifeUtilAdapter;
import cn.newhit.timingcalculation.LifeUtilBean;
import cn.newhit.timingcalculation.R;
import cn.newhit.timingcalculation.constants.LifeUtilConstants;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.rv_life_utlis)
    RecyclerView rvLifeUtil;
    private LifeUtilAdapter mLifeUtilAdapter;
    private List<LifeUtilBean> mLifeUtilBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mLifeUtilBeans = new ArrayList<>();
        mLifeUtilBeans.add(new LifeUtilBean(LifeUtilConstants.LIFE_UTIL_RESERVATION, R.mipmap.icon_life_util_clock));
        mLifeUtilAdapter = new LifeUtilAdapter(this, mLifeUtilBeans);
        mLifeUtilAdapter.setOnItemClickListener((position, view) -> {
            handleItemAction(position, view);
        });
        rvLifeUtil.setAdapter(mLifeUtilAdapter);
        rvLifeUtil.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        new DividerBuilder(this)
                .color(getResources().getColor(R.color.c_EEEEEE))
                .size(1, TypedValue.COMPLEX_UNIT_DIP)
                .build()
                .addTo(rvLifeUtil);

    }

    private void handleItemAction(int position, View view) {
        LifeUtilBean lifeUtilBean = mLifeUtilBeans.get(position);
        switch (lifeUtilBean.getLifeUtilName()) {
            case LifeUtilConstants.LIFE_UTIL_RESERVATION:
                //预约时间计算
                ReservationTimeActivity.startRevervationTimeActivity(this);
                break;
            default:
                break;
        }
    }
}