package szxb.com.commonbus;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.rest.Response;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.http.CallServer;
import szxb.com.commonbus.http.HttpListener;
import szxb.com.commonbus.http.JsonRequest;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.sign.ParamSingUtil;

import static szxb.com.commonbus.util.comm.ParamsUtil.commonMap;

/**
 * 作者: Tangren on 2017/8/31
 * 包名：szxb.com.commonbus
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
@RunWith(AndroidJUnit4.class)
public class BlackListTest {

    @Test
    public void fet() {
        JsonRequest request = new JsonRequest(Config.BLACK_QUERY);
        request.set(getBlackListRequestParams());
        CallServer.getHttpclient().add(0, request, new HttpListener<JSONObject>() {
            @Override
            public void success(int what, Response<JSONObject> response) {
                Log.d("BlackListTest",
                        "success(BlackListTest.java:41)" + response.get().toJSONString());
            }

            @Override
            public void fail(int what, String e) {

                Log.d("BlackListTest",
                        "fail(BlackListTest.java:49)" + e);
            }
        });
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
        object.put("page_size", 100);

        object.put("begin_time", DateUtil.getLastDate("yyyy-MM-dd HH:mm:ss"));
        object.put("end_time", DateUtil.getCurrentDate());

        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, object, Config.private_key));
        map.put("biz_data", object.toString());
        return map;
    }
}
