package szxb.com.commonbus;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Response;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import szxb.com.commonbus.db.manager.DBManager;
import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.entity.ScanInfoEntity;
import szxb.com.commonbus.http.CallServer;
import szxb.com.commonbus.http.HttpListener;
import szxb.com.commonbus.http.JsonRequest;
import szxb.com.commonbus.util.comm.DateUtil;

/**
 * 作者: Tangren on 2017/9/2
 * 包名：szxb.com.commonbus
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
@RunWith(AndroidJUnit4.class)
public class BillPostTest {

    @Test
    public void billPost() throws Exception {

        String url = "http://139.199.158.253/bipbus/interaction/posjour";

        final JsonRequest request = new JsonRequest(url, RequestMethod.POST);
        request.add(getKeyRequestParams());
        CallServer.getHttpclient().add(0, request, new HttpListener<JSONObject>() {
            @Override
            public void success(int what, Response<JSONObject> response) {
                Log.d("BillPostTest",
                        "success(BillPostTest.java:47)" + response.get().toJSONString());
            }

            @Override
            public void fail(int what, String e) {
                Log.d("BillPostTest",
                        "fail(BillPostTest.java:53)" + e);
            }
        });
    }


    public Map<String, Object> getKeyRequestParams() {
        List<ScanInfoEntity> swipeList = DBManager.getScanEntityList();
        Log.d("BillPostTest",
            "getKeyRequestParams(BillPostTest.java:62)"+swipeList.size());
        if (swipeList == null || swipeList.size() == 0) {
            Log.d("TimePostBillTask",
                    "run(TimePostBillTask.java:39)" + swipeList.size());
            return null;
        }
        JSONObject order_list = new JSONObject();
        JSONArray array = new JSONArray();
        for (int i = 0; i < swipeList.size(); i++) {
            array.add(JSON.parse(swipeList.get(i).getBiz_data_single()));
        }
        order_list.put("order_list", array);

        Log.d("BillPostTest",
                "getKeyRequestParams(BillPostTest.java:74)" + order_list.toJSONString());

        Map<String, Object> map = new HashMap<>();
        map.put("app_id", FetchAppConfig.appId());
        map.put("sn_no", "001");
        map.put("bus_no", "001");
        map.put("bus_time", DateUtil.getCurrentDate());
        map.put("order_data", order_list.toJSONString());
        return map;
    }


}


