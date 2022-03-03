package cn.newhit.timingcalculation.base;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.tencent.mmkv.MMKV;

import cn.newhit.timingcalculation.greendao.DaoMaster;
import cn.newhit.timingcalculation.greendao.DaoSession;
import cn.newhit.timingcalculation.greendao.SportRelaxDaoManager;

public class LifeApplication extends Application {

    public static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(this);
        initDb();
        SportRelaxDaoManager.getInstance().initContext(this);
    }

    private void initDb() {
        //1.获取需要连接的数据库
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this,"LifeUtil.db");
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        //2.创建数据库连接
        DaoMaster daoMaster = new DaoMaster(db);
        //3.创建数据库会话
        mDaoSession = daoMaster.newSession();

    }
}
