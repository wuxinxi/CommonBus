package szxb.com.commonbus;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;

import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.entity.ScanInfoEntity;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.Utils;
import szxb.com.commonbus.util.test.TestConstant;

/**
 * 作者: Tangren on 2017/9/1
 * 包名：szxb.com.commonbus
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
@RunWith(AndroidJUnit4.class)
public class InsertRideTest {
    @Test
    public void insert() {

        for (int i = 0; i < 50; i++) {
            //转换成JSONObject
            JSONObject object = new JSONObject();
            object.put("open_id", "201702020580");
            object.put("mch_trx_id", Utils.Random(10));
            object.put("order_time", DateUtil.currentLong());
            object.put("order_desc", "扫码乘车");
            object.put("total_fee", TestConstant.tickPrice);
            object.put("pay_fee", TestConstant.tickPrice);
            object.put("city_code", TestConstant.city_code);
            object.put("exp_type", 0);
            object.put("charge_type", 0);


            object.put("bus_no", TestConstant.bus_no);
            object.put("bus_line_name", TestConstant.bus_line_name);
            object.put("pos_no", TestConstant.pos_no);


            JSONObject ext = new JSONObject();
            ext.put("in_station_id", TestConstant.in_station_id);
            ext.put("in_station_name", TestConstant.in_station_name);
            object.put("ext", ext);

            JSONArray cord = new JSONArray();
            String record = "JDIFJ74854DJFI4df4df";
            cord.add(record);
            object.put("record", cord);

            insert(object);

            JSONObject jsonObject = new JSONObject();
            JSONArray order_list = new JSONArray();
            order_list.add(object);
            jsonObject.put("order_list", order_list);

            Log.d("InsertRideTest",
                    "insert(InsertRideTest.java:65)" + object.toJSONString());

        }




    }


    private void insert(JSONObject object) {
        ScanInfoEntity infoEntity = new ScanInfoEntity();
        infoEntity.setStatus(false);
        infoEntity.setBiz_data_single(object.toJSONString());
        DBCore.getDaoSession().getScanInfoEntityDao().insert(infoEntity);
    }

}
