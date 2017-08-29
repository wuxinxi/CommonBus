package szxb.com.commonbus.task;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;

import org.greenrobot.greendao.query.Query;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.db.table.ScanInfoEntityDao;
import szxb.com.commonbus.entity.ScanInfoEntity;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.ParamsUtil;
import szxb.com.commonbus.util.schedule.ThreadScheduledExecutorUtil;
import szxb.com.poslibrary.http.CallServer;
import szxb.com.poslibrary.http.HttpListener;
import szxb.com.poslibrary.http.JsonRequest;
import szxb.com.poslibrary.util.sign.ParamSingUtil;

/**
 * 作者: Tangren on 2017/8/16
 * 包名：szxb.com.commonbus.task
 * 邮箱：996489865@qq.com
 * TODO:定时结算
 */

public class TimeSettleTask extends Service {

    private JsonRequest request;
    private String app_id;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app_id = FetchAppConfig.appId();

        ThreadScheduledExecutorUtil.getInstance().getService().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                request = new JsonRequest(Config.URL, RequestMethod.POST);
                JSONObject order_list = new JSONObject();
                final JSONArray array = new JSONArray();
                final List<ScanInfoEntity> swipeList = getSwipeList();
                if (swipeList == null || swipeList.size() == 0) {
                    Log.d("TaskRotationService",
                            "run(TaskRotationService.java:68)" + "swipeList.size()=" + swipeList.size());
                    return;
                }

                for (int i = 0; i < swipeList.size(); i++) {
                    array.add(JSON.parse(getSwipeList().get(i).getBiz_data_single()));
                }
                order_list.put("order_list", array);

                String timestamp = DateUtil.getCurrentDate();
                Map<String, Object> debitMap = ParamsUtil.commonMap(app_id, timestamp);

                debitMap.put("sign", ParamSingUtil.getSign(app_id, timestamp, order_list, Config.private_key));
                debitMap.put("biz_data", order_list);
                request.add(debitMap);
                request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
                CallServer.getHttpclient().add(Config.ROTATION_DEBIT, request, new HttpListener<JSONObject>() {
                    @Override
                    public void success(int what, Response<JSONObject> response) {
                        Log.d("TaskRotationService",
                                "success(TaskRotationService.java:78)" + response.get().toJSONString());
                        String retcode = response.get().getString("retcode");
                        if (retcode.equals("0")) {
                            ScanInfoEntityDao dao = DBCore.getDaoSession().getScanInfoEntityDao();
                            String retmsg = response.get().getString("retmsg");
                            if (retmsg.equals("ok")) {
                                JSONArray result_list = response.get().getJSONArray("result_list");
                                for (int i = 0; i < result_list.size(); i++) {
                                    JSONObject resultObject = result_list.getJSONObject(i);
                                    if (resultObject.getString("status").equals("00") || resultObject.getString("status").equals("91")) {
                                        ScanInfoEntity entity = swipeList.get(i);
                                        entity.setStatus(true);
                                        dao.update(entity);
                                        Log.d("TaskRotationService",
                                                "success(TaskRotationService.java:104)扣款成功-修改成功!");
                                    }
                                }
                            }
                        }

                    }

                    @Override
                    public void fail(int what, String e) {
                        Log.e("TaskRotationService",
                                "fail(TaskRotationService.java:99)" + e);
                    }
                });


            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * @return 得到未支付的数据每次最多25条
     */
    public List<ScanInfoEntity> getSwipeList() {
        ScanInfoEntityDao dao = DBCore.getDaoSession().getScanInfoEntityDao();
        Query<ScanInfoEntity> qb = dao.queryBuilder().where(ScanInfoEntityDao.Properties.Status.eq(false)).limit(25).build();
        return qb.list();
    }


}
