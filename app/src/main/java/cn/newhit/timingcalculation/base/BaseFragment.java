package cn.newhit.timingcalculation.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private View mView;
    private Unbinder mBind;
    private EventBus mADefault;
    protected Context mContext;

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false);
        }
        mContext=getContext();
        mBind = ButterKnife.bind(this, mView);
        initData();
        initView();
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        //mADefault.unregister(this);
    }
}
