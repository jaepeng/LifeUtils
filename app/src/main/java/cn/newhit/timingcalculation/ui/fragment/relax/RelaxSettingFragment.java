package cn.newhit.timingcalculation.ui.fragment.relax;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.CenterListPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

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
import cn.newhit.timingcalculation.widget.popup.StandardNormalPopup;

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
    private SportRelaxDaoManager mSportRelaxDaoManager;
    private InputPopup mInputPopup;
    private CenterListPopupView mCenterListPopupView;
    private StandardNormalPopup mDeletePopup;

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
    }

    @OnClick({R.id.tv_delete, R.id.tv_update, R.id.tv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                showInputPopup();
                break;
            case R.id.tv_delete:
                showSportListPopup((position, text) -> {
                    if (mDeletePopup == null) {
                        mDeletePopup = new StandardNormalPopup.SetBuilder(mContext)
                                .setMessage("是否删除" + text.substring(0, text.indexOf("（")))
                                .setTitle("删除指定运动?")
                                .setBtnConfirm("确定", () -> {
                                    deleteBean(text.substring(0, text.indexOf("（")));
                                    mCenterListPopupView.dismiss();
                                })
                                .setBtnCancel("取消", null)
                                .build();
                    }
                    if (!mDeletePopup.isShow()) {
                        mDeletePopup.setMessage("是否删除" + text.substring(0, text.indexOf("（")));
                        mDeletePopup.show();
                    }
                });
                getAllBean();
                break;
            case R.id.tv_update:
                showSportListPopup((position, text) -> {
                    showInputPopup();
                    String oldActionName = text.substring(0, text.indexOf("（"));
                    String oldActionTime = text.substring(text.indexOf("（") + 1, text.indexOf("）") - 1);
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

    /**
     * 显示所有放松项目
     *
     * @param onSelectListener
     */
    private void showSportListPopup(OnSelectListener onSelectListener) {
        String[] strSportNames = getLocalSportData(mSportRelaxDaoManager.getAll());
        mCenterListPopupView = new XPopup.Builder(mContext)
                .maxHeight(800)
                .autoDismiss(false)
                .asCenterList("所有活动如下", strSportNames, onSelectListener);
        mCenterListPopupView.show();
    }

    /**
     * 获取本地全部运动数据并返回一个String[]便于操作
     *
     * @param allSport
     * @return
     */
    @NotNull
    private String[] getLocalSportData(List<SportRelaxModel> allSport) {
        List<String> stringLis = new ArrayList<>();
        Stream.of(allSport).forEach(sportRelaxModel -> {
            stringLis.add(sportRelaxModel.getSprotName() + "（" + sportRelaxModel.getInitsprotTime() + "秒）");
        });
        return stringLis.toArray(new String[]{});
    }


    /**
     * 显示运动输入框
     */
    private void showInputPopup() {
        mInputPopup = new InputPopup.SetBuilder(mContext)
                .setBackPressdCancelable(true)
                .setTouchOutsideCancelable(true)
                .setBtnConfirm((actionName, actionTime) -> {
                    SportRelaxModel sportRelaxModel = new SportRelaxModel();
                    sportRelaxModel.setInitsprotTime(actionTime);
                    sportRelaxModel.setSetSportTime(actionTime);
                    sportRelaxModel.setSprotName(actionName);
                    sportRelaxModel.setShowSubIncrease(true);
                    mSportRelaxDaoManager.insertSportRelax(sportRelaxModel);
                    EventBus.getDefault().post(new MessageEvent(MessageCode.CODE_UPDEATE_RELAX_ITEM, null));
                })
                .build();
        mInputPopup.show();

    }

    /**
     * 根据名称删除对应的
     *
     * @param name
     */
    private void deleteBean(String name) {
        mSportRelaxDaoManager.deleteByName(name);
        EventBus.getDefault().post(new MessageEvent(MessageCode.CODE_UPDEATE_RELAX_ITEM, null));
    }

    /**
     * 根据旧的数据获取到数据库中数据后进行更新
     *
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
                sportRelaxModel.setSetSportTime(newTime);
                mSportRelaxDaoManager.updateSportRelax(sportRelaxModel);
            });
        }
        EventBus.getDefault().post(new MessageEvent(MessageCode.CODE_UPDEATE_RELAX_ITEM, null));
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
