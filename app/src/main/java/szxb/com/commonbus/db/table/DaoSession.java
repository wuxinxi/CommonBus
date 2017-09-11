package szxb.com.commonbus.db.table;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import szxb.com.commonbus.entity.BlackListEntity;
import szxb.com.commonbus.entity.MacKeyEntity;
import szxb.com.commonbus.entity.PublicKeyEntity;
import szxb.com.commonbus.entity.ScanEntity;
import szxb.com.commonbus.entity.ScanInfoEntity;

import szxb.com.commonbus.db.table.BlackListEntityDao;
import szxb.com.commonbus.db.table.MacKeyEntityDao;
import szxb.com.commonbus.db.table.PublicKeyEntityDao;
import szxb.com.commonbus.db.table.ScanEntityDao;
import szxb.com.commonbus.db.table.ScanInfoEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig blackListEntityDaoConfig;
    private final DaoConfig macKeyEntityDaoConfig;
    private final DaoConfig publicKeyEntityDaoConfig;
    private final DaoConfig scanEntityDaoConfig;
    private final DaoConfig scanInfoEntityDaoConfig;

    private final BlackListEntityDao blackListEntityDao;
    private final MacKeyEntityDao macKeyEntityDao;
    private final PublicKeyEntityDao publicKeyEntityDao;
    private final ScanEntityDao scanEntityDao;
    private final ScanInfoEntityDao scanInfoEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        blackListEntityDaoConfig = daoConfigMap.get(BlackListEntityDao.class).clone();
        blackListEntityDaoConfig.initIdentityScope(type);

        macKeyEntityDaoConfig = daoConfigMap.get(MacKeyEntityDao.class).clone();
        macKeyEntityDaoConfig.initIdentityScope(type);

        publicKeyEntityDaoConfig = daoConfigMap.get(PublicKeyEntityDao.class).clone();
        publicKeyEntityDaoConfig.initIdentityScope(type);

        scanEntityDaoConfig = daoConfigMap.get(ScanEntityDao.class).clone();
        scanEntityDaoConfig.initIdentityScope(type);

        scanInfoEntityDaoConfig = daoConfigMap.get(ScanInfoEntityDao.class).clone();
        scanInfoEntityDaoConfig.initIdentityScope(type);

        blackListEntityDao = new BlackListEntityDao(blackListEntityDaoConfig, this);
        macKeyEntityDao = new MacKeyEntityDao(macKeyEntityDaoConfig, this);
        publicKeyEntityDao = new PublicKeyEntityDao(publicKeyEntityDaoConfig, this);
        scanEntityDao = new ScanEntityDao(scanEntityDaoConfig, this);
        scanInfoEntityDao = new ScanInfoEntityDao(scanInfoEntityDaoConfig, this);

        registerDao(BlackListEntity.class, blackListEntityDao);
        registerDao(MacKeyEntity.class, macKeyEntityDao);
        registerDao(PublicKeyEntity.class, publicKeyEntityDao);
        registerDao(ScanEntity.class, scanEntityDao);
        registerDao(ScanInfoEntity.class, scanInfoEntityDao);
    }
    
    public void clear() {
        blackListEntityDaoConfig.clearIdentityScope();
        macKeyEntityDaoConfig.clearIdentityScope();
        publicKeyEntityDaoConfig.clearIdentityScope();
        scanEntityDaoConfig.clearIdentityScope();
        scanInfoEntityDaoConfig.clearIdentityScope();
    }

    public BlackListEntityDao getBlackListEntityDao() {
        return blackListEntityDao;
    }

    public MacKeyEntityDao getMacKeyEntityDao() {
        return macKeyEntityDao;
    }

    public PublicKeyEntityDao getPublicKeyEntityDao() {
        return publicKeyEntityDao;
    }

    public ScanEntityDao getScanEntityDao() {
        return scanEntityDao;
    }

    public ScanInfoEntityDao getScanInfoEntityDao() {
        return scanInfoEntityDao;
    }

}
