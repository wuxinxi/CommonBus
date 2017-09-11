package szxb.com.commonbus.module.init;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SyncRequestExecutor;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import szxb.com.commonbus.R;
import szxb.com.commonbus.base.BaseActivity;
import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.db.sp.CommonSharedPreferences;
import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.entity.BlackListEntity;
import szxb.com.commonbus.entity.MacKeyEntity;
import szxb.com.commonbus.entity.PublicKeyEntity;
import szxb.com.commonbus.http.JsonRequest;
import szxb.com.commonbus.module.home.HomeActivity;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.sign.ParamSingUtil;
import szxb.com.commonbus.util.tip.BusToast;

import static szxb.com.commonbus.util.comm.ParamsUtil.commonMap;

/**
 * 作者: Tangren on 2017/8/31
 * 包名：szxb.com.commonbus.module.init
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class InitZipActivity extends BaseActivity {


    private boolean initKeySuccess = false;//初始化公钥是否完成

    private boolean initBlackListSuccess = false;//初始化黑名单是否完成

    private boolean initMacKeySuccess = false;//初始化Mac是否完成

    private CircleProgressBar progressBar;

    private Subscription subscribe;


    @Override
    protected int rootView() {
        return R.layout.activity_init;
    }

    @Override
    protected void initView() {
        super.initView();
        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 公钥/MAC下载
     *
     * @return map
     */
    private Map<String, Object> getkeyMap() {
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
    private Map<String, Object> getBlackListMap() {
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

    @Override
    protected void initData() {

        Observable<Boolean> macKey = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                JsonRequest macRequest = new JsonRequest(Config.MAC_KEY);
                macRequest.set(getkeyMap());
                Response<JSONObject> execute = SyncRequestExecutor.INSTANCE.execute(macRequest);
                if (execute.isSucceed()) {
                    Log.d("InitZipActivity",
                            "call(InitZipActivity.java:120)" + execute.get().toJSONString());
                    String macMsg = execute.get().getString("retmsg");
                    if (!TextUtils.isEmpty(macMsg) && TextUtils.equals(macMsg, "success")) {
                        final JSONArray array = execute.get().getJSONArray("mackey_list");
                        AsyncSession async = DBCore.getASyncDaoSession();
                        async.setListener(new AsyncOperationListener() {
                            @Override
                            public void onAsyncOperationCompleted(AsyncOperation operation) {
                                for (int i = 0; i < array.size(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    object.getString("pubkey");
                                    MacKeyEntity macKeyEntity = new MacKeyEntity();
                                    macKeyEntity.setTime(DateUtil.getCurrentDate());
                                    macKeyEntity.setKey_id(object.getString("key_id"));
                                    macKeyEntity.setPubkey(object.getString("pubkey"));
                                    DBCore.getDaoSession().insert(macKeyEntity);
                                }
                            }
                        });
                        async.deleteAll(MacKeyEntity.class);
                        initMacKeySuccess = true;
                        subscriber.onNext(true);
                    } else subscriber.onNext(false);

                } else subscriber.onError(execute.getException());
            }
        });

        Observable<Boolean> publicKey = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                JsonRequest publicKeyRequest = new JsonRequest(Config.PUBLIC_KEY);
                publicKeyRequest.set(getkeyMap());
                Response<JSONObject> execute = SyncRequestExecutor.INSTANCE.execute(publicKeyRequest);
                if (execute.isSucceed()) {
                    Log.d("InitZipActivity",
                            "call(InitZipActivity.java:157)" + execute.get().toJSONString());
                    String pubMsg = execute.get().getString("retmsg");
                    if (!TextUtils.isEmpty(pubMsg) && TextUtils.equals(pubMsg, "success")) {
                        JSONArray pKeyarray = execute.get().getJSONArray("pubkey_list");
                        for (int i = 0; i < pKeyarray.size(); i++) {
                            JSONObject object = pKeyarray.getJSONObject(i);
                            PublicKeyEntity entity = new PublicKeyEntity();
                            entity.setKey_id(object.getString("key_id"));
                            entity.setPubkey(object.getString("pubkey"));
                            DBCore.getDaoSession().insertOrReplace(entity);
                            CommonSharedPreferences.put("key_" + i, object.getString("pubkey"));
                        }
                        initKeySuccess = true;
                        subscriber.onNext(true);
                    } else subscriber.onNext(false);

                } else subscriber.onError(execute.getException());
            }
        });
        Observable<Boolean> blackList = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                JsonRequest blackListRequest = new JsonRequest(Config.BLACK_QUERY);
                blackListRequest.set(getBlackListMap());
                Response<JSONObject> execute = SyncRequestExecutor.INSTANCE.execute(blackListRequest);
                if (execute.isSucceed()) {
                    Log.d("InitZipActivity",
                            "call(InitZipActivity.java:180)" + execute.get().toJSONString());
                    String bLMsg = execute.get().getString("retmsg");
                    if (!TextUtils.isEmpty(bLMsg) && TextUtils.equals(bLMsg, "ok")) {
                        final JSONArray array = execute.get().getJSONArray("black_list");
                        if (array == null || array.isEmpty()) {
                            subscriber.onNext(true);
                            return;
                        }
                        //删除所有
                        AsyncSession async = DBCore.getASyncDaoSession();
                        async.setListener(new AsyncOperationListener() {
                            @Override
                            public void onAsyncOperationCompleted(AsyncOperation operation) {
                                for (int i = 0; i < array.size(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    BlackListEntity entity = new BlackListEntity();
                                    entity.setOpen_id(object.getString("open_id"));
                                    entity.setTime(DateUtil.long2String(object.getLong("time")));
                                    DBCore.getDaoSession().insert(entity);
                                }
                            }
                        });
                        async.deleteAll(BlackListEntity.class);
                        initBlackListSuccess = true;
                        subscriber.onNext(true);
                    } else subscriber.onNext(false);
                } else subscriber.onError(execute.getException());
            }
        });

        subscribe = Observable.zip(macKey, publicKey, blackList, new Func3<Boolean, Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean aBoolean, Boolean aBoolean2, Boolean aBoolean3) {
                if (aBoolean && aBoolean2 && aBoolean3)
                    return true;
                return false;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            BusToast.showToast(getApplicationContext(), "初始化成功", true);

                        } else {
                            BusToast.showToast(getApplicationContext(), "部分初始化失败", false);
                        }
                        startActivity(new Intent(InitZipActivity.this, HomeActivity.class));
                        finish();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        BusToast.showToast(getApplicationContext(), "初始化失败\n" + throwable.toString(), false);
                        finish();
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressBar.setVisibility(View.GONE);
        if (subscribe != null && subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }
}