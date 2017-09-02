package szxb.com.commonbus;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.rest.Response;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import szxb.com.commonbus.http.CallServer;
import szxb.com.commonbus.http.HttpListener;
import szxb.com.commonbus.http.JsonRequest;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.ParamsUtil;
import szxb.com.commonbus.util.sign.ParamSingUtil;

/**
 * 作者: Tangren on 2017/8/31
 * 包名：szxb.com.commonbus
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

@RunWith(AndroidJUnit4.class)
public class PushInfo {
    String key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJQN0rdvPOak5zx+\n" +
            "Vs1kA90zMWlLiYUQDLgQuCleYDUkG5MMdrALciX2SBoUSHBzXz8XqKI5+o7aBmX+\n" +
            "Bu2vD8Z6OdD7xv62MIwlMxfw5I+fRzFLcSTdfOlP/NNFrwNc555gAfjbWyEkDFkR\n" +
            "ISYkmsu/EladoKD/XK3lOZJ5D+xZAgMBAAECgYA+zzp0vYlNo+nBnSuACQ0mm2A7\n" +
            "9mLKA4wRzm4Chtoz0TSthp0XhFORzVC7V51/r0JaBtRwtj4Yul/6hvEzhpAfE2vl\n" +
            "iR7HvS8Ktqkl9EBlkPzHCMpfSyytX+knK7x0grKsrajNI1toZGWdmaORidZGzY2z\n" +
            "t8btdjgrhEXxOsZhfQJBAMSP5G1OJOD53y8CLk5jSRO3xizpInkro/xK+s/3m64s\n" +
            "PUKfU3+cMC/AjvNHWjRMsBVrDceHem2jc7cqusFPoK8CQQDA0tySaPgSMqJpl3iK\n" +
            "+eptq0fSNvHLbyBJ1BaHkHRVGmO3rF+U2LvwQPrSmrkBCX78sUgRUzY65h3vneNN\n" +
            "uDV3AkBoo1Eu/xKS0XIGTFrqT+BvJr3Q2qsHZjv96sxqOZ4esl5KQRbqL/NW+GMh\n" +
            "DzLt9IUoYb0MIwsBoqnPMGgK0KDxAkBolXXhS2HRrPj+QJO2/VozZYUs9XQsHPfs\n" +
            "U0Zs/OK8DfYr9yhYeT1mUDg65oSVlWr078rg6rstMwblokNZMiJ9AkEAjwZKPB18\n" +
            "wynX1FaKVt0aTRYfTvGdeaN8jbPeJwB35d7RWyR9/o7z/jM6cUn8iIUCO9qoyNs6\n" +
            "FMhpN2NhLYygyQ==";

    //    String url = "http://web.dev.bus.dkh.snsshop.net/api/bus/update";
    String url = "http://139.199.158.253/bipbus/interaction/posinstall";

    @Test
    public void push() {

//        app_id=97263905084014600&biz_data={"bus_no":"粤B12547","is_online":0}&charset=UTF-8&format=json&sign_type=sha1withrsa&timestamp=2017-08-31 16:39:54&version=1.0
//        app_id=97263905084014600&biz_data={"bus_no":"B12547","is_set_pos":1,"pos_no":"3344","is_online":1}&charset=UTF-8&format=json&sign_type=sha1withrsa&timestamp=2017-08-31 16:52:58&version=1.0


        String app_id = "97263905084014600";
        String timestamp = DateUtil.getCurrentDate();
        JSONObject object = new JSONObject();
        object.put("bus_no", "粤B12548");
        object.put("is_set_pos", 1);
        object.put("pos_no", "3344");
        object.put("is_online", 1);

        object.put("bus_line_id ", 111);
        object.put("bus_line_name", "观光4");
        object.put("line_start", "科技园");
        object.put("line_end ", "西乡步行街");
        object.put("total_fee  ", 1);
        object.put("pay_fee ", 1);

        Map<String, Object> map = ParamsUtil.commonMap(app_id, timestamp);

        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, object, key));
        map.put("biz_data", object.toString());

        JsonRequest request = new JsonRequest(url);
        request.add(map);
        request.setRetryCount(3);
        CallServer.getHttpclient().add(0, request, new HttpListener<JSONObject>() {
            @Override
            public void success(int what, Response<JSONObject> response) {
                Log.d("PushInfo",
                        "success(PushInfo.java:85)" + response.get().toJSONString());
            }

            @Override
            public void fail(int what, String e) {
                Log.d("PushInfo",
                        "fail(PushInfo.java:91)" + e);
            }
        });

    }
}

