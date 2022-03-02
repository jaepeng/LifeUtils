package cn.newhit.timingcalculation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.newhit.timingcalculation.R;
import cn.newhit.timingcalculation.bean.RelaxMenuBean;

public class RelaxNavigationAdapter extends RecyclerView.Adapter<RelaxNavigationAdapter.ViewHolder> {
    private Context mContext;
    private List<RelaxMenuBean> mRelaxMenuBeans;
    private OnItemClick mOnItemClick;

    public RelaxNavigationAdapter(Context context, List<RelaxMenuBean> relaxMenuBeans) {
        mContext = context;
        mRelaxMenuBeans = relaxMenuBeans;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_relax_nav, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        RelaxMenuBean relaxMenuBean = mRelaxMenuBeans.get(position);
        if (relaxMenuBean.isSelected()) {
            holder.tvRelaxItemName.setTextColor(mContext.getResources().getColor(R.color.color_select_text));
        } else {
            holder.tvRelaxItemName.setTextColor(mContext.getResources().getColor(R.color.default_text_color));
        }
        if (TextUtils.isEmpty(relaxMenuBean.getMenuIconUrl())) {
            Glide.with(mContext).load(relaxMenuBean.getLocalId()).into(holder.ivRelaxItemIcon);
        } else {
            Glide.with(mContext).load(relaxMenuBean.getMenuIconUrl()).into(holder.ivRelaxItemIcon);
        }
        holder.tvRelaxItemName.setText(relaxMenuBean.getMenuName());

    }

    @Override
    public int getItemCount() {
        return mRelaxMenuBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_relax_nav_icon)
        ImageView ivRelaxItemIcon;
        @BindView(R.id.tv_item_relax_nav_name)
        TextView tvRelaxItemName;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (mOnItemClick != null) {
                    mOnItemClick.onItemClick(getLayoutPosition(), v);
                }
            });
        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onItemClick(int position, View view);
    }
}
