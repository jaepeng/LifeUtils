package cn.newhit.timingcalculation;

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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LifeUtilAdapter extends RecyclerView.Adapter<LifeUtilAdapter.ViewHolder> {
    private Context mContext;
    private List<LifeUtilBean> mLifeUtilBeans;
    private OnItemClickListener mOnItemClickListener;

    public LifeUtilAdapter(Context context, List<LifeUtilBean> lifeUtilBeans) {
        mContext = context;
        mLifeUtilBeans = lifeUtilBeans;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_life_util, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        LifeUtilBean lifeUtilBean = mLifeUtilBeans.get(position);
        String imageIcon = "";
        String lifeUtilUrl = lifeUtilBean.lifeUtilUrl;
        if (lifeUtilUrl != null) {
            imageIcon = lifeUtilUrl.trim();
        }
        Glide.with(mContext).load(TextUtils.isEmpty(imageIcon) ? lifeUtilBean.getLifeUtilResId() : imageIcon).apply(RequestOptions.bitmapTransform(new RoundedCorners(14))).into(holder.ivLifeUtilIcon);
        holder.tvLifeUtilName.setText(lifeUtilBean.getLifeUtilName());
    }

    @Override
    public int getItemCount() {
        return mLifeUtilBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_life_util_icon)
        ImageView ivLifeUtilIcon;
        @BindView(R.id.tv_life_util_name)
        TextView tvLifeUtilName;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(getLayoutPosition(), v);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
