package cn.newhit.timingcalculation.widget.popup;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;

import cn.newhit.timingcalculation.R;


public class StandardNormalPopup extends CenterPopupView {

    public Confirm mConfirm = null;
    public Cancel mCancel = null;

    private TextView mTitle;
    private TextView mMessage;
    private Button mBtnConfirm;
    private Button mBtnCancel;
    private SetBuilder setBuilder = null;
    private Context mContext;


    public StandardNormalPopup(@NonNull Context context, SetBuilder setBuilder) {
        super(context);
        this.mContext = context;
        this.setBuilder = setBuilder;
    }

    public void setMessage(String message) {
        if (mMessage != null) {
            ((Activity) mContext).runOnUiThread(() -> {
                mMessage.setText(message);
            });
        } else {
            if (setBuilder != null) {
                setBuilder.message = message;
            }
        }
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        ImageView mIvCancel = findViewById(R.id.iv_logout_cancel);
        mTitle = findViewById(R.id.tv_logout_title);
        mMessage = findViewById(R.id.tv_logout_message);
        mBtnConfirm = findViewById(R.id.btn_logout_confirm);
        mBtnCancel = findViewById(R.id.btn_logout_cancel);

        //如果数据非空则写上去
        if (!TextUtils.isEmpty(setBuilder.title)) {
            mTitle.setVisibility(VISIBLE);
            mTitle.setText(setBuilder.title);
        } else {
            mTitle.setVisibility(INVISIBLE);
        }
        if (!TextUtils.isEmpty(setBuilder.message)) {
            mMessage.setVisibility(VISIBLE);
            mMessage.setText(setBuilder.message);
        } else {
            mMessage.setVisibility(INVISIBLE);
        }

        if (!TextUtils.isEmpty(setBuilder.confirm)) {
            mBtnConfirm.setVisibility(VISIBLE);
            mBtnConfirm.setText(setBuilder.confirm);
        } else {
            mBtnConfirm.setVisibility(INVISIBLE);
        }
        if (!TextUtils.isEmpty(setBuilder.cancel)) {
            mBtnCancel.setVisibility(VISIBLE);
            mBtnCancel.setText(setBuilder.cancel);
        } else {
            mBtnCancel.setVisibility(INVISIBLE);
        }

        mIvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (setBuilder.mCancel != null) {
                    setBuilder.mCancel.onClickCancel();
                }
            }
        });

        mBtnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (setBuilder.mCancel != null) {
                    setBuilder.mCancel.onClickCancel();
                }

            }
        });

        mBtnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (setBuilder.mConfirm != null) {
                    setBuilder.mConfirm.onClickConfrim();

                }
            }
        });

    }


    /**
     * 点击确定按钮的接口回调
     */
    public interface Confirm {
        void onClickConfrim();

    }

    /**
     * 点击取消按钮的接口回调
     */
    public interface Cancel {
        void onClickCancel();
    }

    //对按钮的字体进行设置
    public static class SetBuilder {

        private final Context mContext;
        String title;
        String message;
        String confirm;
        String cancel;
        public Confirm mConfirm = null;
        public Cancel mCancel = null;
        //默认不能点击外部后被取消
        public boolean canTouchCancel = true;
        public boolean canBackCancel = true;

        public SetBuilder(Context context) {
            this.mContext = context;
        }

        public SetBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public SetBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public SetBuilder setBtnConfirm(String confrimString, Confirm clickconfirm) {
            this.confirm = confrimString;
            if (clickconfirm != null) {
                this.mConfirm = clickconfirm;
            }
            return this;
        }

        public SetBuilder setBtnCancel(String cancelString, Cancel clickcancel) {
            this.cancel = cancelString;
            if (clickcancel != null) {
                this.mCancel = clickcancel;
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
        public StandardNormalPopup build() {
            XPopup.Builder builder = new XPopup.Builder(this.mContext);
            builder.dismissOnTouchOutside(this.canTouchCancel);
            builder.dismissOnBackPressed(this.canBackCancel);
            return (StandardNormalPopup) builder.asCustom(new StandardNormalPopup(mContext, this));
        }

    }


    @Override
    protected void addInnerContent() {
        super.addInnerContent();
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_standard_normal;
    }

    public void setConfirm(Confirm callBack) {
        this.mConfirm = callBack;
    }

    public void setCancel(Cancel callback) {
        this.mCancel = callback;
    }

    public Button getBtnConfirm() {
        return mBtnConfirm;
    }


}
