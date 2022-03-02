package cn.newhit.timingcalculation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.newhit.timingcalculation.R;
import cn.newhit.timingcalculation.adapter.RelaxNavigationAdapter;
import cn.newhit.timingcalculation.adapter.SportRelaxContainerPagerAdapter;
import cn.newhit.timingcalculation.base.BaseActivity;
import cn.newhit.timingcalculation.bean.RelaxMenuBean;
import cn.newhit.timingcalculation.ui.fragment.relax.RelaxMainFragment;

public class SportRelaxActivity extends BaseActivity {
    private static final String TAG = "SportRelaxActivity";
    private Handler mHandler;
    @BindView(R.id.vp_relax)
    ViewPager2 vpRelax;
    @BindView(R.id.rv_relax_navigation)
    RecyclerView rvNavigation;
    private List<Fragment> mFragmentList;
    private List<RelaxMenuBean> mRelaxMenuBeans;
    private SportRelaxContainerPagerAdapter mSportRelaxContainerPagerAdapter;
    private RelaxNavigationAdapter mRelaxNavigationAdapter;


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
        mSportRelaxContainerPagerAdapter = new SportRelaxContainerPagerAdapter(this, mFragmentList);
        vpRelax.setAdapter(mSportRelaxContainerPagerAdapter);
        vpRelax.setCurrentItem(0);
        vpRelax.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: position==>"+position);
                super.onPageSelected(position);
                updateSelectNav(position);
            }
        });
        mRelaxNavigationAdapter=new RelaxNavigationAdapter(mContext,mRelaxMenuBeans);
        mRelaxNavigationAdapter.setOnItemClick((position, view) -> {
            vpRelax.setCurrentItem(position);
            updateSelectNav(position);
        });
        rvNavigation.setAdapter(mRelaxNavigationAdapter);
        rvNavigation.setLayoutManager(new GridLayoutManager(mContext,2));

    }

    private void updateSelectNav(int position) {
        Stream.of(mRelaxMenuBeans).forEach(relaxMenuBean -> relaxMenuBean.setSelected(false));
        mRelaxMenuBeans.get(position).setSelected(true);
        mRelaxNavigationAdapter.notifyDataSetChanged();
    }

    private void initData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new RelaxMainFragment());
        mFragmentList.add(new RelaxMainFragment());
        mRelaxMenuBeans=new ArrayList<>();
        mRelaxMenuBeans.add(new RelaxMenuBean("放松",R.mipmap.icon_sport_relax,true));
        mRelaxMenuBeans.add(new RelaxMenuBean("设置",R.mipmap.icon_sport_relax));
    }

    public static void startSportRelaxActivity(Context context) {
        Intent intent = new Intent(context, SportRelaxActivity.class);
        context.startActivity(intent);
    }
}