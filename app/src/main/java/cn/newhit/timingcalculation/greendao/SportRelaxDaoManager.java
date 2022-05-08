package cn.newhit.timingcalculation.greendao;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import cn.newhit.timingcalculation.base.LifeApplication;

public class SportRelaxDaoManager {
    private static final String TAG = "GreenDaoManager";
    private Context mContext;
    private SportRelaxModelDao mSportRelaxModelDao;

    public static class SportRelaxDaoManagerHolder {
        private static SportRelaxDaoManager INSTANCE = new SportRelaxDaoManager();
    }

    public static SportRelaxDaoManager getInstance() {
        return SportRelaxDaoManagerHolder.INSTANCE;
    }

    public void initContext(Context context) {
        mContext = context;
        mSportRelaxModelDao = LifeApplication.mDaoSession.getSportRelaxModelDao();
    }

    private SportRelaxDaoManager() {
    }

    public boolean insertSportRelax(SportRelaxModel sportRelaxModel) {
        try {
            List<SportRelaxModel> modelByRelaxName = getModelByRelaxName(sportRelaxModel.sprotName,sportRelaxModel.initsprotTime);
            if (modelByRelaxName == null || modelByRelaxName.isEmpty()) {
                mSportRelaxModelDao.insertOrReplace(sportRelaxModel);
                return true;
            } else {
                updateSportRelax(sportRelaxModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "insertSportRelax: msg==>" + e.getMessage(), e);

        }
        return false;
    }

    public boolean updateSportRelax(SportRelaxModel sportRelaxModel) {
        try {
            mSportRelaxModelDao.update(sportRelaxModel);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "updateSportRelax: msg==>" + e.getMessage(), e);
        }
        return false;
    }

    public boolean deleteAll() {
        try {
            mSportRelaxModelDao.deleteAll();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "deleteAll: msg==>" + e.getMessage(), e);
        }
        return false;
    }
    public List<SportRelaxModel> getAll(){
        List<SportRelaxModel> sportRelaxModels = mSportRelaxModelDao.loadAll();
        return sportRelaxModels;
    }

    public SportRelaxModel getSporRelaxInfo() {
        List<SportRelaxModel> sportRelaxModels = mSportRelaxModelDao.loadAll();
        return sportRelaxModels == null || sportRelaxModels.isEmpty() ? null : sportRelaxModels.get(0);
    }

    public List<SportRelaxModel> getModelByRelaxName(String sportName,long actionTime) {
        try {
            QueryBuilder<SportRelaxModel> qb = mSportRelaxModelDao.queryBuilder();
            qb.where(SportRelaxModelDao.Properties.SprotName.eq(sportName),SportRelaxModelDao.Properties.InitsprotTime.eq(actionTime));
            List<SportRelaxModel> list = qb.list();
            return list;
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "getModelByRelaxName: msg==>"+e.getMessage(),e );
        }
        return null;

    }

    public boolean deleteByName(String name) {
        try {
            DeleteQuery<SportRelaxModel> sportRelaxModelDeleteQuery = mSportRelaxModelDao.queryBuilder().where(SportRelaxModelDao.Properties.SprotName.eq(name)).buildDelete();
            sportRelaxModelDeleteQuery.executeDeleteWithoutDetachingEntities();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "deleteByName: msg==>" + e.getMessage(), e);
        }
        return false;
    }
    public boolean deleteByNameAndTime(String name,long time) {
        try {
            DeleteQuery<SportRelaxModel> sportRelaxModelDeleteQuery = mSportRelaxModelDao.queryBuilder().where(SportRelaxModelDao.Properties.SprotName.eq(name)).buildDelete();
            sportRelaxModelDeleteQuery.executeDeleteWithoutDetachingEntities();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "deleteByName: msg==>" + e.getMessage(), e);
        }
        return false;
    }
}
