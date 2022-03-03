package cn.newhit.timingcalculation.ui.fragment.relax;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.Stream;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.newhit.timingcalculation.R;
import cn.newhit.timingcalculation.base.BaseFragment;
import cn.newhit.timingcalculation.constants.MessageCode;
import cn.newhit.timingcalculation.greendao.SportRelaxDaoManager;
import cn.newhit.timingcalculation.greendao.SportRelaxModel;
import cn.newhit.timingcalculation.message.MessageEvent;
import cn.newhit.timingcalculation.widget.popup.InputPopup;

public class RelaxSettingFragment extends BaseFragment {
    private static final String TAG = "RelaxSettingFragment";
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    private SportRelaxModel mSportRelaxModel;
    private SportRelaxDaoManager mSportRelaxDaoManager;
    private InputPopup mInputPopup;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_relax_setting;
    }

    @Override
    protected void initView() {
        mSportRelaxDaoManager = SportRelaxDaoManager.getInstance();
    }

    @Override
    protected void initData() {
        mSportRelaxModel = new SportRelaxModel();
        mSportRelaxModel.setInitsprotTime(15);
        mSportRelaxModel.setSprotName("颈椎");
        mSportRelaxModel.setShowSubIncrease(true);
    }

    @OnClick({R.id.tv_delete, R.id.tv_update, R.id.tv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                addBean();
                getAllBean();
                break;
            case R.id.tv_delete:
                deleteBean("颈椎");
                getAllBean();
                break;
            case R.id.tv_update:
                updateBean("颈椎");
                getAllBean();
                break;
            default:
                break;
        }

    }

    private void addBean() {
        if (mInputPopup != null) {
            mInputPopup.show();
        } else {
            mInputPopup = new InputPopup.SetBuilder(mContext)
                    .setBackPressdCancelable(true)
                    .setTouchOutsideCancelable(true)
                    .setBtnConfirm((actionName, actionTime) -> {
                        SportRelaxModel sportRelaxModel = new SportRelaxModel();
                        sportRelaxModel.setInitsprotTime(actionTime);
                        sportRelaxModel.setSprotName(actionName);
                        sportRelaxModel.setShowSubIncrease(true);
                        mSportRelaxDaoManager.insertSportRelax(sportRelaxModel);
                        EventBus.getDefault().post(new MessageEvent(MessageCode.CODE_UPDEATE_RELAX_ITEM, null));
                    })
                    .build();
        }
    }

    private void deleteBean(String name) {
        mSportRelaxDaoManager.deleteByName(name);
    }

    private void updateBean(String name) {
        List<SportRelaxModel> modelByRelaxName = mSportRelaxDaoManager.getModelByRelaxName(name);
        if (modelByRelaxName != null && !modelByRelaxName.isEmpty()) {
            SportRelaxModel sportRelaxModel = modelByRelaxName.get(0);
            sportRelaxModel.setSetSportTime(100);
            mSportRelaxDaoManager.updateSportRelax(sportRelaxModel);
        }
    }

    private void getAllBean() {
        List<SportRelaxModel> all = mSportRelaxDaoManager.getAll();
        Stream.of(all).forEach(sportRelaxModel -> {
            Log.d(TAG, "getAllBean: name==>" + sportRelaxModel.getSprotName() + " initTime==>" + sportRelaxModel.getInitsprotTime() + " ,setTime==>" + sportRelaxModel.getSetSportTime());
        });
    }
}
