package szxb.com.commonbus.db.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import szxb.com.commonbus.entity.BlackListEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BLACK_LIST_ENTITY".
*/
public class BlackListEntityDao extends AbstractDao<BlackListEntity, Long> {

    public static final String TABLENAME = "BLACK_LIST_ENTITY";

    /**
     * Properties of entity BlackListEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Open_id = new Property(1, String.class, "open_id", false, "OPEN_ID");
        public final static Property Time = new Property(2, String.class, "time", false, "TIME");
    }


    public BlackListEntityDao(DaoConfig config) {
        super(config);
    }
    
    public BlackListEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BLACK_LIST_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"OPEN_ID\" TEXT," + // 1: open_id
                "\"TIME\" TEXT);"); // 2: time
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BLACK_LIST_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BlackListEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String open_id = entity.getOpen_id();
        if (open_id != null) {
            stmt.bindString(2, open_id);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(3, time);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BlackListEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String open_id = entity.getOpen_id();
        if (open_id != null) {
            stmt.bindString(2, open_id);
        }
 
        String time = entity.getTime();
        if (time != null) {
            stmt.bindString(3, time);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public BlackListEntity readEntity(Cursor cursor, int offset) {
        BlackListEntity entity = new BlackListEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // open_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // time
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BlackListEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setOpen_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTime(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(BlackListEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(BlackListEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(BlackListEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}