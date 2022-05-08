package cn.newhit.timingcalculation.ui.fragment.relax;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.CenterListPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
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

/**
 * 放松运动设置界面
 */
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
    private CenterListPopupView mCenterListPopupView;

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
                showInputPopup();
                break;
            case R.id.tv_delete:
                showSportListPopup((position, text) -> {
                    // TODO: 2022/3/4 标准弹窗进行提示，是否确认删除
                    deleteBean(text.substring(0, text.indexOf("（")));
                });
                getAllBean();
                break;
            case R.id.tv_update:
                showSportListPopup((position, text) -> {
                    showInputPopup();
                    String oldActionName = text.substring(0, text.indexOf("（"));
                    String oldActionTime = text.substring(text.indexOf("（")+1, text.indexOf("）")-1);
                    mInputPopup.setActionName(oldActionName);
                    mInputPopup.setActionTime(oldActionTime);
                    mInputPopup.setOnClickConfirm((actionName, actionTime) -> {
                        getAndUpdateBean(oldActionName, Long.valueOf(oldActionTime).longValue(), actionName, actionTime);
                        mCenterListPopupView.dismiss();
                    });

                });
                getAllBean();
                break;
            default:
                break;
        }

    }

    private void showSportListPopup(OnSelectListener onSelectListener) {
        List<SportRelaxModel> allSport = mSportRelaxDaoManager.getAll();
        String[] strSportName = new String[allSport.size()];
        List<String> stringLis = new ArrayList<>();
        Stream.of(allSport).forEach(sportRelaxModel -> {
            stringLis.add(sportRelaxModel.getSprotName() + "（" + sportRelaxModel.getInitsprotTime() + "秒）");
        });
        stringLis.toArray(strSportName);
        mCenterListPopupView = new XPopup.Builder(mContext)
                .maxHeight(800)
                .autoDismiss(false)
                .asCenterList("所有活动如下", strSportName, onSelectListener);
        mCenterListPopupView.show();
    }


    private void showInputPopup() {
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
            mInputPopup.show();
        }
    }

    /**
     * 根据名称删除对应的
     * @param name
     */
    private void deleteBean(String name) {
        mSportRelaxDaoManager.deleteByName(name);
        EventBus.getDefault().post(new MessageEvent(MessageCode.CODE_UPDEATE_RELAX_ITEM, null));
    }

    /**
     * 根据旧的数据获取到数据库中数据后进行更新
     * @param oldName
     * @param oldNewTime
     * @param newName
     * @param newTime
     */
    private void getAndUpdateBean(String oldName, long oldNewTime, String newName, long newTime) {
        List<SportRelaxModel> modelByRelaxName = mSportRelaxDaoManager.getModelByRelaxName(oldName, oldNewTime);
        if (modelByRelaxName != null && !modelByRelaxName.isEmpty()) {
            Stream.of(modelByRelaxName).forEach(sportRelaxModel -> {
                sportRelaxModel.setSprotName(newName);
                sportRelaxModel.setInitsprotTime(newTime);
                mSportRelaxDaoManager.updateSportRelax(sportRelaxModel);
            });
        }
    }

    /**
     * 获取全部放松选项
     */
    private void getAllBean() {
        List<SportRelaxModel> all = mSportRelaxDaoManager.getAll();
        Stream.of(all).forEach(sportRelaxModel -> {
            Log.d(TAG, "getAllBean: name==>" + sportRelaxModel.getSprotName() + " initTime==>" + sportRelaxModel.getInitsprotTime() + " ,setTime==>" + sportRelaxModel.getSetSportTime());
        });
    }
}
