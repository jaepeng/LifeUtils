package cn.newhit.timingcalculation.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SportRelaxContainerPagerAdapter extends FragmentStateAdapter {
    private List<Fragment> mFragmentList;
    public SportRelaxContainerPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity, List<Fragment> fragments) {
        super(fragmentActivity);
        this.mFragmentList=fragments;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }
}
