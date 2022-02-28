package cn.newhit.timingcalculation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.newhit.timingcalculation.R;
import cn.newhit.timingcalculation.bean.SportRelaxBean;
import cn.newhit.timingcalculation.widget.SportRelaxSelactionView;

public class RelaxSelectedAdapter extends RecyclerView.Adapter<RelaxSelectedAdapter.ViewHolder> {
    private static final String TAG = "RelaxTimeSelectionAdapt";
    private Context mContext;
    private List<SportRelaxBean> mSportRelaxBeans;
    private OnItemLongClick mOnItemClick;

    public RelaxSelectedAdapter(Context context, List<SportRelaxBean> sportRelaxBeans) {
        mContext = context;
        mSportRelaxBeans = sportRelaxBeans;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sport_selection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        SportRelaxBean sportRelaxBean = mSportRelaxBeans.get(position);
        holder.mSportRelaxSelactionView.setSportData(sportRelaxBean);
        holder.mSportRelaxSelactionView.setShowSubOrIncrease(sportRelaxBean.isShowSubIncrease());
    }

    @Override
    public int getItemCount() {
        return mSportRelaxBeans.size();
    }

    public void setOnItemClickListener(OnItemLongClick onItemClickListener) {
        this.mOnItemClick = onItemClickListener;
    }

    public interface OnItemLongClick {
        void onItemLongClick(int position, View view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.srv_action)
        SportRelaxSelactionView mSportRelaxSelactionView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mSportRelaxSelactionView.setOnItemLongClick(new SportRelaxSelactionView.OnItemLongClickListener() {
                @Override
                public void onActionLongClick(long realTime, View view) {
                    if (mOnItemClick!=null){
                        mOnItemClick.onItemLongClick(getLayoutPosition(),view);
                    }
                }
            });
        }
    }
}
