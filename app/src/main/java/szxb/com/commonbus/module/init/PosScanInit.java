package szxb.com.commonbus.module.init;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SyncRequestExecutor;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.db.manager.DBManager;
import szxb.com.commonbus.db.sp.CommonSharedPreferences;
import szxb.com.commonbus.entity.MacKeyEntity;
import szxb.com.commonbus.entity.PublicKeyEntity;
import szxb.com.commonbus.http.JsonRequest;
import szxb.com.commonbus.interfaces.InitOnListener;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.ParamsUtil;
import szxb.com.commonbus.util.tip.BusToast;

/**
 * 作者: Tangren on 2017-09-12
 * 包名：szxb.com.commonbus.module.init
 * 邮箱：996489865@qq.com
 * TODO:扫码初始化数据
 */

public class PosScanInit {

    private Subscription subscribe;

    private InitOnListener listener;

    public void init(final Context context) {

        Observable<Boolean> macKey = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                JsonRequest macRequest = new JsonRequest(Config.MAC_KEY);
                macRequest.set(ParamsUtil.getkeyMap());
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
                                    MacKeyEntity macKeyEntity = new MacKeyEntity();
                                    macKeyEntity.setTime(DateUtil.getCurrentDate());
                                    macKeyEntity.setKey_id(object.getString("key_id"));
                                    macKeyEntity.setPubkey(object.getString("mackey"));
                                    Log.d("InitZipActivity",
                                            "onAsyncOperationCompleted(InitZipActivity.java:137)" + object.getString("mackey"));
                                    DBCore.getDaoSession().insert(macKeyEntity);
                                }
                            }
                        });
                        async.deleteAll(MacKeyEntity.class);
                        subscriber.onNext(true);
                    } else subscriber.onNext(false);

                } else subscriber.onError(execute.getException());
            }
        });

        Observable<Boolean> publicKey = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                JsonRequest publicKeyRequest = new JsonRequest(Config.PUBLIC_KEY);
                publicKeyRequest.set(ParamsUtil.getkeyMap());
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
                        subscriber.onNext(true);
                    } else subscriber.onNext(false);

                } else subscriber.onError(execute.getException());
            }
        });
        Observable<Boolean> blackList = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                JsonRequest blackListRequest = new JsonRequest(Config.BLACK_LIST);
                blackListRequest.set(ParamsUtil.getBlackListMap());
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
                        DBManager.addBlackList(array);
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
                            BusToast.showToast(context, "初始化成功", true);
                        } else {
                            BusToast.showToast(context, "部分初始化失败", false);
                        }
                        listener.onCallBack(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        BusToast.showToast(context, "初始化失败\n" + throwable.toString(), false);
                        listener.onCallBack(false);
                        subscribe.unsubscribe();
                    }
                });

    }


    public void setOnCallBack(InitOnListener listener) {
        this.listener = listener;
    }

    //取消订阅
    public void unsubscribe() {
        if (subscribe != null && subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

}
