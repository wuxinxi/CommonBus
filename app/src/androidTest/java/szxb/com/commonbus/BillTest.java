package szxb.com.commonbus;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Response;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import szxb.com.commonbus.http.CallServer;
import szxb.com.commonbus.http.HttpListener;
import szxb.com.commonbus.http.JsonRequest;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.ParamsUtil;
import szxb.com.commonbus.util.sign.ParamSingUtil;

/**
 * 作者: Tangren on 2017/8/2
 * 包名：com.szxb.buspay
 * 邮箱：996489865@qq.com
 * TODO:对账单:
 */
@RunWith(AndroidJUnit4.class)
public class BillTest {
    @Test
    public void fetchBill() throws Exception {
        Map<String, Object> map = new HashMap<>();
        JSONObject object = new JSONObject();
        String app_id = "10000002";
//        String time = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
        String time = "2017-08-08 09:39:28";

//
        object.put("page_index", 1);
        object.put("page_size", 99);

        object.put("date", "20170807");
        map = ParamsUtil.commonMap(app_id, time);
        map.put("sign", ParamSingUtil.getSign(app_id, time, object, Config.private_key));
        map.put("biz_data", object.toString());

        String url = "https://open-wlx.tenpay.com/cgi-bin/wlx_order/wlx_bill_query.cgi";

        JsonRequest request = new JsonRequest(url, RequestMethod.POST);
        request.add(map);
        CallServer.getHttpclient().add(0, request, new HttpListener<JSONObject>() {
            @Override
            public void success(int what, Response<JSONObject> response) {
                System.out.println(response.get().toString());
                Log.d("BillTest",
                        "success(BillTest.java:58)" + response.get().toJSONString());
            }

            @Override
            public void fail(int what, String e) {
                System.out.println("-----------------------------------失败---------");
                Log.d("BillTest",
                        "fail(BillTest.java:64)失败");
            }
        });
    }
}
