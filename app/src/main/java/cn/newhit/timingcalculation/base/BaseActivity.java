package cn.newhit.timingcalculation.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mBind;

    public abstract int getLayouId();
    protected Context mContext;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayouId());
        mBind = ButterKnife.bind(this);
        mContext=this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();

    }
}
