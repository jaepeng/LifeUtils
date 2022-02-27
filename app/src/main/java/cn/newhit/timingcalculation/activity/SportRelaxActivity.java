package cn.newhit.timingcalculation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import cn.newhit.timingcalculation.R;
import cn.newhit.timingcalculation.base.BaseActivity;

public class SportRelaxActivity extends BaseActivity {
    @BindView(R.id.rv_select_relax_item)
    RecyclerView rvSelectRelaxItem;
    @BindView(R.id.rv_selected_relax)
    RecyclerView rvSelected;

    @Override
    public int getLayouId() {
        return R.layout.activity_sport_relax;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static void startSportRelaxActivity(Context context){
        Intent intent=new Intent(context,SportRelaxActivity.class);
        context.startActivity(intent);
    }
}