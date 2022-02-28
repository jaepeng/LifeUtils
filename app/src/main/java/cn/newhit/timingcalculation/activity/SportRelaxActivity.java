package cn.newhit.timingcalculation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fondesa.recyclerviewdivider.DividerBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.newhit.timingcalculation.R;
import cn.newhit.timingcalculation.adapter.RelaxTimeSelectionAdapter;
import cn.newhit.timingcalculation.base.BaseActivity;
import cn.newhit.timingcalculation.bean.SportRelaxBean;

public class SportRelaxActivity extends BaseActivity {
    @BindView(R.id.rv_select_relax_item)
    RecyclerView rvSelectRelaxItem;
    @BindView(R.id.rv_selected_relax)
    RecyclerView rvSelected;
    private List<SportRelaxBean> selectionSportBeans;
    private List<SportRelaxBean> selectedSportBeans;
    private RelaxTimeSelectionAdapter mRelaxTimeSelectionAdapter;

    @Override
    public int getLayouId() {
        return R.layout.activity_sport_relax;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void initData() {
        selectedSportBeans = new ArrayList<>();
        selectionSportBeans = new ArrayList<>();
        selectionSportBeans.add(new SportRelaxBean(25, "左腿", true));
        selectionSportBeans.add(new SportRelaxBean(15, "左手", false));
        selectionSportBeans.add(new SportRelaxBean(25, "右腿", true));
        selectionSportBeans.add(new SportRelaxBean(15, "右手", false));

        mRelaxTimeSelectionAdapter = new RelaxTimeSelectionAdapter(this, selectionSportBeans);
        mRelaxTimeSelectionAdapter.setOnItemClickListener((position, view) -> {
            // TODO: 2022/2/28 点击了选择放松内容的Item
        });
    }

    public static void startSportRelaxActivity(Context context) {
        Intent intent = new Intent(context, SportRelaxActivity.class);
        context.startActivity(intent);
    }
}