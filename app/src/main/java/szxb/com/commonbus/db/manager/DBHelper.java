package szxb.com.commonbus.db.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

import szxb.com.commonbus.db.table.BlackListEntityDao;
import szxb.com.commonbus.db.table.DaoMaster;
import szxb.com.commonbus.db.table.MacKeyEntityDao;
import szxb.com.commonbus.db.table.PublicKeyEntityDao;
import szxb.com.commonbus.db.table.ScanEntityDao;
import szxb.com.commonbus.db.table.ScanInfoEntityDao;

/**
 * 作者：Tangren_ on 2017/3/23 0023.
 * 邮箱：wu_tangren@163.com
 * TODO：更新数据库
 */


public class DBHelper extends DaoMaster.OpenHelper {
    DBHelper(Context context, String name) {
        super(context, name);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        MigrationHelper.migrate(db, ScanEntityDao.class, BlackListEntityDao.class, ScanInfoEntityDao.class, MacKeyEntityDao.class, PublicKeyEntityDao.class);
    }
}
