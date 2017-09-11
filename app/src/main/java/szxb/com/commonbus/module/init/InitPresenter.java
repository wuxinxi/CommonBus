package szxb.com.commonbus.module.init;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.Logger;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.lang.ref.WeakReference;

import szxb.com.commonbus.base.BasePresenter;
import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.db.sp.CommonSharedPreferences;
import szxb.com.commonbus.entity.BlackListEntity;
import szxb.com.commonbus.entity.MacKeyEntity;
import szxb.com.commonbus.entity.PublicKeyEntity;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;

/**
 * 作者: Tangren on 2017/8/31
 * 包名：szxb.com.commonbus.module.init
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class InitPresenter extends BasePresenter {

    private WeakReference<InitActivity> weakReference;

    public InitPresenter(InitActivity activity) {
        weakReference = new WeakReference<InitActivity>(activity);
    }

    @Override
    protected void onAllSuccess(int what, JSONObject result) {
        Log.d("InitPresenter",
                "onAllSuccess(InitPresenter.java:41)" + result.toJSONString());
        InitActivity activity = weakReference.get();
        if (activity != null) {
            String retmsg = result.getString("retmsg");
            switch (what) {
                case Config.PUBKEY_WHAT:
                    Logger.d("更新公钥返回的数据:" + result.toString());
                    //更新公钥类别成功,做一些保存操作
                    if (!TextUtils.isEmpty(retmsg) && TextUtils.equals(retmsg, "success")) {
                        JSONArray array = result.getJSONArray("pubkey_list");
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            PublicKeyEntity entity = new PublicKeyEntity();
                            entity.setKey_id(object.getString("key_id"));
                            entity.setPubkey(object.getString("pubkey"));
                            DBCore.getDaoSession().insertOrReplace(entity);
                            Log.d("InitPresenter",
                                "onAllSuccess(InitPresenter.java:60)"+entity.getKey_id()+","+entity.getPubkey());
                            CommonSharedPreferences.put("key_" + i, object.getString("pubkey"));
                        }
                        activity.onSuccess(what, "获取公钥成功");
                    } else activity.onFail(what, "更新公钥失败");

                    break;
                case Config.MAC_WHAT:
                    if (!TextUtils.isEmpty(retmsg) && TextUtils.equals(retmsg, "success")) {
                        final JSONArray array = result.getJSONArray("mackey_list");
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

                        activity.onSuccess(what, "获取MAC成功");
                    } else activity.onFail(what, "更新MAC失败");
                    break;
                case Config.BLACK_WHAT:
                    Logger.d("更新黑名单返回的数据:" + result.toString());
                    if (!TextUtils.isEmpty(retmsg) && TextUtils.equals(retmsg, "ok")) {
                        final JSONArray array = result.getJSONArray("black_list");
                        if (array == null || array.isEmpty()) {
                            activity.onSuccess(what, "更新黑名单成功");
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

                        activity.onSuccess(what, "黑名单更新成功");
                    } else activity.onFail(what, "黑名单更新失败");
                    break;
                default:

                    break;
            }
        }
    }

    @Override
    protected void onFail(int what, String failStr) {
        InitActivity activity = weakReference.get();
        if (activity != null) {
            activity.onFail(what, failStr);
        }
    }

}
