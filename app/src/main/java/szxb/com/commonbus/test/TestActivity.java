package szxb.com.commonbus.test;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SyncRequestExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import szxb.com.commonbus.R;
import szxb.com.commonbus.base.BaseActivity;
import szxb.com.commonbus.http.CallServer;
import szxb.com.commonbus.http.HttpListener;
import szxb.com.commonbus.http.JsonRequest;
import szxb.com.commonbus.module.init.InitActivity;
import szxb.com.commonbus.util.comm.Config;

import static szxb.com.commonbus.util.comm.ParamsUtil.getKeyRequestParams;

/**
 * 作者: Tangren on 2017/8/26
 * 包名：szxb.com.commonbus.test
 * 邮箱：996489865@qq.com
 * TODO:车辆信息录入
 */

public class TestActivity extends BaseActivity {

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

    String url = "http://web.dev.bus.dkh.snsshop.net/api/bus/update";

    @Override
    protected int rootView() {
        return R.layout.bus_view;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_view);

        String url = Config.getMac_key_url;

        final JsonRequest request = new JsonRequest(url);
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


//        String app_id = "97263905084014600";
//        String timestamp = DateUtil.getCurrentDate();
//        JSONObject object = new JSONObject();
//        object.put("bus_no", "粤B12548");
//        object.put("is_set_pos", "2");
//        object.put("pos_no", "001");
//        object.put("is_online", "2");
//        Map<String, Object> map = ParamsUtil.commonMap(app_id, timestamp);
//        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, object, key));
//        map.put("biz_data", object.toString());
//        JsonRequest request = new JsonRequest(url);
//        request.add(map);
//        CallServer.getHttpclient().add(0, request, new HttpListener<JSONObject>() {
//            @Override
//            public void success(int what, Response<JSONObject> response) {
//                Log.d("TestActivity",
//                        "success(TestActivity.java:66)" + response.get().toJSONString());
//            }
//
//            @Override
//            public void fail(int what, String e) {
//                Log.d("TestActivity",
//                        "fail(TestActivity.java:73)" + e);
//            }
//        });

        Observable<JSONObject> macKey = Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                JsonRequest request1 = new JsonRequest(Config.MAC_KEY);
                request1.set(InitActivity.getkeyMap());
                Response<JSONObject> execute = SyncRequestExecutor.INSTANCE.execute(request1);
                if (execute.isSucceed()) {
                    subscriber.onNext(execute.get());
                } else subscriber.onError(execute.getException());

            }
        });

        Observable<JSONObject> publicKey = Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                JsonRequest request1 = new JsonRequest(Config.PUBLIC_KEY);
                request1.set(InitActivity.getkeyMap());
                Response<JSONObject> execute = SyncRequestExecutor.INSTANCE.execute(request1);
                if (execute.isSucceed()) {
                    subscriber.onNext(execute.get());
                } else subscriber.onError(execute.getException());
            }
        });
        Observable<JSONObject> blackList = Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                JsonRequest request1 = new JsonRequest(Config.BLACK_QUERY);
                request1.set(InitActivity.getBlackListMap());
                Response<JSONObject> execute = SyncRequestExecutor.INSTANCE.execute(request1);
                if (execute.isSucceed()) {
                    subscriber.onNext(execute.get());
                } else subscriber.onError(execute.getException());
            }
        });


        Observable.zip(macKey, publicKey, blackList, new Func3<JSONObject, JSONObject, JSONObject, Boolean>() {
            @Override
            public Boolean call(JSONObject object, JSONObject object2, JSONObject object3) {
                Log.d("TestActivity",
                        "call(TestActivity.java:161)mackey=" + object.toJSONString());
                Log.d("TestActivity",
                        "call(TestActivity.java:163)publicKey=" + object2.toJSONString());
                Log.d("TestActivity",
                        "call(TestActivity.java:165)blackList=" + object3.toJSONString());
                Log.d("TestActivity",
                    "call(TestActivity.java:168)是否是主线程："+isMainThread());
                return Boolean.TRUE;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.d("TestActivity",
                                "call(TestActivity.java:174)" + throwable.toString());
                    }
                });
    }

    public boolean isMainThread(){
        return Looper.getMainLooper()==Looper.myLooper();
    }
}
