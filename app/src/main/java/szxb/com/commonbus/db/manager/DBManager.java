package szxb.com.commonbus.db.manager;

import java.util.List;

import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.db.table.ScanInfoEntityDao;
import szxb.com.commonbus.entity.ScanInfoEntity;
import szxb.com.commonbus.util.comm.DateUtil;

/**
 * 作者: Tangren on 2017/8/29
 * 包名：szxb.com.commonbus.db.manager
 * 邮箱：996489865@qq.com
 * TODO:数据库操作类
 */

public class DBManager {

    /**
     * 扫码数据
     *
     * @return 扫码List
     */
    public static List<ScanInfoEntity> getScanEntityList() {
        ScanInfoEntityDao dao = DBCore.getDaoSession().getScanInfoEntityDao();
        String lastTimePushFile = FetchAppConfig.lastTimePushFile();
        if (!lastTimePushFile.equals("")) {
            return dao.queryBuilder().where(ScanInfoEntityDao.Properties.Time.between(lastTimePushFile, DateUtil.getCurrentDate())).build().list();
        } else {
            return dao.queryBuilder().build().list();
        }
    }

}
