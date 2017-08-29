package szxb.com.commonbus;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.rest.Response;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.ParamsUtil;
import szxb.com.poslibrary.http.CallServer;
import szxb.com.poslibrary.http.HttpListener;
import szxb.com.poslibrary.http.JsonRequest;
import szxb.com.poslibrary.util.sign.ParamSingUtil;

/**
 * 作者: Tangren on 2017/8/27
 * 包名：szxb.com.commonbus
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
@RunWith(AndroidJUnit4.class)
public class MacKeyTest {

    @Test
    public void fetMacKey() throws Exception {

        String url = Config.getMac_key_url;

        JsonRequest request = new JsonRequest(url);
        request.setRetryCount(3);
        request.add(getKeyRequestParams());
        CallServer.getHttpclient().add(0, request, new HttpListener<JSONObject>() {
            @Override
            public void success(int what, Response<JSONObject> response) {
                Log.d("PubKeyTest",
                        "success(PubKeyTest.java:42)" + response.get().toJSONString());
            }

            @Override
            public void fail(int what, String e) {
                Log.d("PubKeyTest",
                        "fail(PubKeyTest.java:49)" + e);
            }
        });
    }

    public Map<String, Object> getKeyRequestParams() {
        String timestamp = DateUtil.getCurrentDate();
        String app_id = FetchAppConfig.appId();
        Map<String, Object> map = ParamsUtil.commonMap(app_id, timestamp);
        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, null, Config.private_key));
        map.put("biz_data", "");
        return map;
    }

}
