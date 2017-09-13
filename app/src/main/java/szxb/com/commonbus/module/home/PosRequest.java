package szxb.com.commonbus.module.home;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

import szxb.com.commonbus.db.manager.DBManager;
import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.entity.PosRecord;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.sign.ParamSingUtil;

import static szxb.com.commonbus.util.comm.ParamsUtil.commonMap;

/**
 * 作者: Tangren on 2017-09-11
 * 包名：szxb.com.commonbus.module.home
 * 邮箱：996489865@qq.com
 * TODO:组包
 */
public class PosRequest {

    //扣款
    public static Map<String, Object> requestMap(PosRecord record) {
        JSONObject object = new JSONObject();
        object.put("open_id", record.getOpen_id());
        object.put("mch_trx_id", record.getMch_trx_id());
        object.put("order_time", record.getOrder_time());
        object.put("order_desc", record.getOrder_desc());
        object.put("total_fee", record.getTotal_fee());
        object.put("pay_fee", record.getPay_fee());
        object.put("city_code", record.getCity_code());
        object.put("exp_type", 0);
        object.put("charge_type", 0);

        object.put("bus_no", record.getBus_no());
        object.put("bus_line_name", record.getBus_line_name());
        object.put("pos_no", record.getPos_no());

        JSONObject ext = new JSONObject();
        ext.put("in_station_id", record.getIn_station_id());
        ext.put("in_station_name", record.getIn_station_name());
        object.put("ext", ext);

        JSONArray cord = new JSONArray();
        cord.add(record.getRecord());
        object.put("record", cord);
        DBManager.insert(object, record.getMch_trx_id());//存储乘车记录


        JSONObject jsonObject = new JSONObject();
        JSONArray order_list = new JSONArray();
        order_list.add(object);
        jsonObject.put("order_list", order_list);

        String timestamp = DateUtil.getCurrentDate();
        String app_id = FetchAppConfig.appId();
        Map<String, Object> map = commonMap(app_id, timestamp);
        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, jsonObject, Config.private_key));
        map.put("biz_data", jsonObject.toString());
        return map;
    }

    /**
     * 公钥/MAC下载
     *
     * @return map
     */
    public static Map<String, Object> getkeyMap() {
        String timestamp = DateUtil.getCurrentDate();
        String app_id = FetchAppConfig.appId();
        Map<String, Object> map = commonMap(app_id, timestamp);
        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, null, Config.private_key));
        map.put("biz_data", "");
        return map;
    }


    /**
     * @return 黑名单下载列表map
     */
    public static Map<String, Object> getBlackListMap() {
        String timestamp = DateUtil.getCurrentDate();
        String app_id = FetchAppConfig.appId();
        Map<String, Object> map = commonMap(app_id, timestamp);
        JSONObject object = new JSONObject();
        object.put("page_index", 1);
        object.put("page_size", 100);

        object.put("begin_time", DateUtil.getLastDate("yyyy-MM-dd HH:mm:ss"));
        object.put("end_time", DateUtil.getCurrentDate());

        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, object, Config.private_key));
        map.put("biz_data", object.toString());
        return map;
    }

}
