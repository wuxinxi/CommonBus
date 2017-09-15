package szxb.com.commonbus.db.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.db.table.BlackListEntityDao;
import szxb.com.commonbus.db.table.MacKeyEntityDao;
import szxb.com.commonbus.db.table.PublicKeyEntityDao;
import szxb.com.commonbus.db.table.ScanInfoEntityDao;
import szxb.com.commonbus.entity.BlackListEntity;
import szxb.com.commonbus.entity.MacKeyEntity;
import szxb.com.commonbus.entity.PublicKeyEntity;
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


    /**
     * 获取公钥列表
     *
     * @return
     */
    public static List<PublicKeyEntity> getPublicKeylist() {
        PublicKeyEntityDao dao = DBCore.getDaoSession().getPublicKeyEntityDao();
        return dao.queryBuilder().build().list();
    }

    /**
     * 获取公钥
     *
     * @param keyId 公钥ID
     * @return
     */
    public static String getPublicKey(String keyId) {
        PublicKeyEntityDao dao = DBCore.getDaoSession().getPublicKeyEntityDao();
        PublicKeyEntity unique = dao.queryBuilder().where(PublicKeyEntityDao.Properties.Key_id.eq(keyId)).build().unique();
        if (unique != null)
            return unique.getPubkey();
        return "";
    }

    /**
     * 获取mac秘钥列表
     *
     * @return
     */
    public static List<MacKeyEntity> getMacList() {
        MacKeyEntityDao dao = DBCore.getDaoSession().getMacKeyEntityDao();
        return dao.queryBuilder().build().list();
    }

    /**
     * 获取mac秘钥
     *
     * @param keyId
     * @return
     */
    public static String getMac(String keyId) {
        MacKeyEntityDao dao = DBCore.getDaoSession().getMacKeyEntityDao();
        MacKeyEntity unique = dao.queryBuilder().where(MacKeyEntityDao.Properties.Key_id.eq(keyId)).unique();
        if (unique != null)
            return unique.getPubkey();
        return "";

    }

    /**
     * 是否属于黑名单
     *
     * @param openID
     * @return
     */
    public static boolean filterBlackName(String openID) {
        BlackListEntityDao dao = DBCore.getDaoSession().getBlackListEntityDao();
        Query<BlackListEntity> build = dao.queryBuilder().where(BlackListEntityDao.Properties.Open_id.eq(openID),
                BlackListEntityDao.Properties.Time.le(DateUtil.currentLong())).build();
        BlackListEntity blackEntity = build.unique();
        return blackEntity != null;
    }


    /**
     * 验码成功,保存数据
     *
     * @param object
     */
    public static void insert(JSONObject object, String mch_trx_id) {
        ScanInfoEntity infoEntity = new ScanInfoEntity();
        infoEntity.setStatus(false);
        infoEntity.setBiz_data_single(object.toJSONString());
        infoEntity.setMch_trx_id(mch_trx_id);
        infoEntity.setTime(DateUtil.getCurrentDate());
        DBCore.getDaoSession().getScanInfoEntityDao().insert(infoEntity);
    }

    /**
     * @return 得到未支付的数据每次最多25条, 降序排列取得最新的最多25条数据
     */
    public static List<ScanInfoEntity> getSwipeList() {
        ScanInfoEntityDao dao = DBCore.getDaoSession().getScanInfoEntityDao();
        Query<ScanInfoEntity> qb = dao.queryBuilder().where(ScanInfoEntityDao.Properties.Status.eq(false))
                .limit(25).orderDesc(ScanInfoEntityDao.Properties.Id).build();
        return qb.list();
    }


    /**
     * 删除过期的黑名单
     */
    private static void deleteOverDueBlackName() {
        BlackListEntityDao dao = DBCore.getDaoSession().getBlackListEntityDao();
        List<BlackListEntity> list = dao.queryBuilder().where(BlackListEntityDao.Properties.Time
                .le(DateUtil.currentLong())).build().list();
        dao.deleteInTx(list);
    }

    /**
     * 更新黑名单
     *
     * @param memberList
     */
    public static void addBlackList(JSONArray memberList) {
        deleteOverDueBlackName();
        for (int i = 0; i < memberList.size(); i++) {
            JSONObject object = memberList.getJSONObject(i);
            BlackListEntity entity = new BlackListEntity();
            entity.setOpen_id(object.getString("open_id"));
            entity.setTime(object.getLong("time"));
            DBCore.getDaoSession().insert(entity);
        }
    }
}
