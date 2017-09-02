package szxb.com.commonbus;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.db.table.ScanInfoEntityDao;
import szxb.com.commonbus.entity.ScanInfoEntity;

/**
 * 作者: Tangren on 2017/9/1
 * 包名：szxb.com.commonbus
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
@RunWith(AndroidJUnit4.class)
public class Query {

    @Test
    public void query() {

        ScanInfoEntityDao dao = DBCore.getDaoSession().getScanInfoEntityDao();
        ScanInfoEntity entity = dao.queryBuilder().where(ScanInfoEntityDao.Properties.Biz_data_single.eq("{\"charge_type\":0,\"exp_type\":0,\"open_id\":\"201702020580\",\"pay_fee\":1,\"mch_trx_id\":\"a6dm8o37XJ\",\"ext\":{\"in_station_name\":\"桃源站\",\"in_station_id\":13},\"record\":[\"JDIFJ74854DJFI4df4df\"],\"order_desc\":\"扫码乘车\",\"bus_no\":\"粤B12548\",\"pos_no\":\"001\",\"order_time\":1504248942,\"total_fee\":1,\"city_code\":\"440300\",\"bus_line_name\":\"观光4\"}" )).build().unique();
        if (entity != null){
            entity.setStatus(true);
            Log.d("Query",
                    "query(Query.java:29)" + entity.toString());
        }



    }
}
