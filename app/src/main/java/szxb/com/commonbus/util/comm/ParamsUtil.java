package szxb.com.commonbus.util.comm;

import com.alibaba.fastjson.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.util.sign.ParamSingUtil;

/**
 * 作者: Tangren on 2017/7/20
 * 包名：com.szxb.buspay.util
 * 邮箱：996489865@qq.com
 * TODO:参数工具类
 */

public class ParamsUtil {

    /**
     * @param app_id    商户id
     * @param timestamp 请求时间
     * @return 通用map
     */
    public static Map<String, Object> commonMap(String app_id, String timestamp) {
        Map<String, Object> map = new HashMap<>();
        map.put("app_id", app_id);
        map.put("charset", "UTF-8");
        map.put("format", "json");
        map.put("timestamp", timestamp);
        map.put("version", "1.0");
        map.put("sign_type", "sha1withrsa");
        return map;
    }

    /**
     * @return 黑名单下载列表map
     */
    public static Map<String, Object> getBlackListRequestParams() {
        String timestamp = DateUtil.getCurrentDate();
        String app_id = FetchAppConfig.appId();
        Map<String, Object> map = commonMap(app_id, timestamp);
        JSONObject object = new JSONObject();
        object.put("page_index", 1);
        object.put("page_size", 5);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        object.put("begin_time", "2017-04-06");
        object.put("end_time", "2017-05-16");

        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, object, Config.private_key));
        map.put("biz_data", object.toString());
        return map;
    }


    /**
     * @return 公钥列表下载map
     */
    public static Map<String, Object> getKeyRequestParams() {
        String timestamp = DateUtil.getCurrentDate();
        String app_id = FetchAppConfig.appId();
        Map<String, Object> map = commonMap(app_id, timestamp);
        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, null, Config.private_key));
        map.put("biz_data", "");
        return map;
    }

}
