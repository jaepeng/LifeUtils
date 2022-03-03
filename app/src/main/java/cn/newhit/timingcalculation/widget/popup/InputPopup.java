package cn.newhit.timingcalculation.widget.popup;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;

import org.jetbrains.annotations.NotNull;

import cn.newhit.timingcalculation.R;

public class InputPopup extends CenterPopupView {
    EditText etActionName;
    EditText etActionTime;
    TextView tvConfirm;
    private SetBuilder mSetBuilder;

    public InputPopup(@NonNull @NotNull Context context, SetBuilder setBuilder) {
        super(context);
        this.mSetBuilder = setBuilder;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initListener();
    }

    private void initListener() {
        tvConfirm.setOnClickListener(v -> {
            if (mSetBuilder.mConfirm != null) {
                String actionTime = etActionTime.getText().toString();
                String actionName = etActionName.getText().toString();
                if (TextUtils.isEmpty(actionName)) {
                    ToastUtils.showShort("请输入放松部位名称！");
                    return;
                }
                if (TextUtils.isEmpty(actionTime)) {
                    ToastUtils.showShort("请输入放松时长！");
                    return;
                }
                dismiss();
                mSetBuilder.mConfirm.onClickConfirm(actionName, Long.parseLong(actionTime));
            }
        });

    }

    private void initView() {
        etActionName = findViewById(R.id.edt_relax_action_name);
        etActionTime = findViewById(R.id.edt_relax_action_time);
        tvConfirm = findViewById(R.id.tv_input_confirm);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_popup_input_popup;
    }

    //对按钮的字体进行设置
    public static class SetBuilder {

        private final Context mContext;
        public OnClickConfirm mConfirm = null;
        //默认不能点击外部后被取消
        public boolean canTouchCancel = true;
        public boolean canBackCancel = true;

        public SetBuilder(Context context) {
            this.mContext = context;
        }


        public SetBuilder setBtnConfirm(OnClickConfirm clickconfirm) {
            if (clickconfirm != null) {
                this.mConfirm = clickconfirm;
            }
            return this;
        }

        public SetBuilder setTouchOutsideCancelable(boolean cancelable) {
            this.canTouchCancel = cancelable;
            return this;
        }

        public SetBuilder setBackPressdCancelable(boolean cancelable) {
            this.canBackCancel = cancelable;
            return this;
        }

        /**
         * 用create函数返回一个弹窗实例
         */
        public InputPopup build() {
            XPopup.Builder builder = new XPopup.Builder(this.mContext);
            builder.dismissOnTouchOutside(this.canTouchCancel);
            builder.dismissOnBackPressed(this.canBackCancel);
            return (InputPopup) builder.asCustom(new InputPopup(mContext, this));
        }

    }

    public interface OnClickConfirm {
        void onClickConfirm(String actionName, long actionTime);
    }
}
