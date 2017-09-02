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
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.ParamsUtil;
import szxb.com.commonbus.util.sign.ParamSingUtil;

/**
 * 作者: Tangren on 2017/9/1
 * 包名：szxb.com.commonbus
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
@RunWith(AndroidJUnit4.class)
public class StatusTest {
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
    @Test
    public void status() {
        String url = "http://139.199.158.253//bipbus/interaction/posinfdyn";

        JsonRequest request = new JsonRequest(url);
        request.setRetryCount(3);
        request.add(getKeyRequestParams());
        CallServer.getHttpclient().add(0, request, new HttpListener<JSONObject>() {
            @Override
            public void success(int what, Response<JSONObject> response) {
                Log.d("PubKeyTest",
                        "success(PubKeyTest.java:42)" + response.get().toJSONString());

//                {"retcode":"0","retmsg":"success","mackey_list":"[{\"key_id\":\"20170902\",\"mackey\":\"nTsuEsxWzPnzp5YDU7S5QQKLNb3N46bwZMdu3UW1+X9C5CNZHfSd103Rp14gLd39gyiPQdlqnJyZA7DI8gO50gkJXCRozL4KpyTrPsrynbMHPk01jhpCPx6ahaum9qp083rxk6DyEveoA6ESlnKZ0lpKa8Geu/osGel4zCjUy9c=\"},{\"key_id\":\"20170829\",\"mackey\":\"mKtePAzTpzOBY1sTnGMu1UvJxq01wd5ab1YOlD0sHJIowNGmTevEunlRDcisRNHeW233HTMpgBLbY+g2vv2TS0bQCCvbeqr1elLgDtYIOAu/JUOK1BrRnCueC3DianQs4yjLaY7+nYOL3wTHTCyV7g4q/COdPYXRetZmQ/zA2aI=\"},{\"key_id\":\"20170903\",\"mackey\":\"BdtDuED8NkhgdoltmnDHOtDQB92J9JzZetEcM5lpo6nnyZwO+u2B42aRRj8D8Kv2wOPZiNqR7606ljIxzIfgc4PQlPT/Pz6LzDCdPXCvD9lbeS8zosax3J/9WD8mQeVOI/0KnqSRrYvtdlC+gKLFi747unWwV1mHgK9u9ddSMH0=\"},{\"key_id\":\"20170905\",\"mackey\":\"MszDNiNGsvIAbpmFpbFd88oSiLkjGc2KE6NTB5kUgN8voAyjE8/POI1e1w+muxCum0ua/EknnptgmUocAWFk2Mqso1N3nMRDlAJOnQPUVYNfxyhABtWSRyxUCYkuS7TOVhXGqBgvIKMUBYxJyGcnF2Ga+E+LP9VOKf19g1+hcKs=\"},{\"key_id\":\"20170904\",\"mackey\":\"lLm2THGYfp8r4UFCUJdVOj9WGNUq1RwljTPzVxFGyX2nvAP40DZZpHN9pVNgHCm0ZD0gLjP+LllTEY1n/sBS2SSaS4oa1jL+DPKp/80NmXPUf/g/ELYvsN53bood30AerE/cr+oiZ0G3A4TqKXpo5t9KJ7lmnxBfQ9PZ/tIAG8g=\"},{\"key_id\":\"20170906\",\"mackey\":\"Qyux2hhu9edVzfhC+ivVv9yfex06seM5gTd+HUEhM7XbAicWY6C8YV0ZgiQGVjMpc7omIU+M1ZZlGLm7kHpxmlCqziLXvEUaym3Dcj8x7VT8CLoBD5GCVYpS90NwAiTN6m+n4Bnib5tCA40bRQe3ZHZ4uVZIwC+Gl8lsUrMfKqU=\"},{\"key_id\":\"20170827\",\"mackey\":\"BDXGuTNxgHYdp2vhyKcWuCO4mXLWuu2TCBq5owb72QZ3vHNWMD8uOlhDStDzq9wjHZCVMSBZHeNaKcrAXdDKqKnU8TcX1ZfLzcsf4Q0oc2dW34WXQlZvMuTLlSP49zV1MTC7DA11ViWlgnm7dRRotIclOio38ghDYKDTB2Rcnrs=\"},{\"key_id\":\"20170828\",\"mackey\":\"Mq9BWzoXX7dC4YT3ki9PspFTxUI4mQpeNaR8Pu2AkvtsuFPwuCPTuzCAUho+SL4IefbEIXtC4R628qjMtZ6TIHbfXPBvR/McZM96hLOwBloBK73Xs/zR/MdZHYH3XIk0pv9p9jBhXxMYHgo0dV7w6eh+Q2Qv/WlfxR5DV7S4Ut4=\"},{\"key_id\":\"20170830\",\"mackey\":\"iuXp+jOd2vGRX7HZQ4cAFjrP8r1SwSXByq4MI608KRZShFrVAv0FMi2344tpGRMuv/76Nw1YqB9bJl6HVPQawEpxWH/0cZjNtlvjREifIHsuV0JPGgOAY0CGJNyPTUB9Ro6r3BW3snmZfDCCb6eTDjBFsHa62D03JF92339G5Cg=\"},{\"key_id\":\"20170901\",\"mackey\":\"OPhEX7zPoX2yJbfukMH7w4e9L4eQZ7HRHSScKDS2tr7af4Afu+9FV7+zBq2oZZMhdSMT/lUCFwUix2ChjngIyaY9HxhVPg7RWSVVJViSL5JBEgzNijYQQ+pBOUymtYXBqi3fJCNc7fHyib1WZ/UXrZTxujN3Y29+obyGCRescdI=\"},{\"key_id\":\"20170831\",\"mackey\":\"KL1zsYEdZ5BX2UD6c8qzWdeioBY1jlekVxHtBpogKwfRK8PMExtBfxQnrzvioL7CFqBzQeAkzYA5NNjS0Y8B0YDqPIVHQZ1bVVSZDqEtTPEdFflIu5diSL3ddBSQ+RDnYvZej0KAOdFvf8TfcUuLDnCGT4JrgzluW9lVlZ7zkiY=\"}]"}

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
        JSONObject object=new JSONObject();
        object.put("bus_no ","BC111");
        object.put("pos_no","001");
        object.put("net_state",1);
        object.put("run_stat",1);
        object.put("price","3");
        object.put("start_station","科技园");
        object.put("end_station","西乡");
        object.put("line_name","观光4");
        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, object, key));
        map.put("biz_data",object.toJSONString());
        return map;
    }

}
