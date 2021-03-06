package szxb.com.commonbus.task.settle;

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

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.db.manager.DBManager;
import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.db.table.ScanInfoEntityDao;
import szxb.com.commonbus.entity.ScanInfoEntity;
import szxb.com.commonbus.http.CallServer;
import szxb.com.commonbus.http.HttpListener;
import szxb.com.commonbus.http.JsonRequest;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.ParamsUtil;
import szxb.com.commonbus.util.comm.Utils;
import szxb.com.commonbus.util.schedule.ThreadScheduledExecutorUtil;
import szxb.com.commonbus.util.sign.ParamSingUtil;

/**
 * 作者: Tangren on 2017/8/16
 * 包名：szxb.com.commonbus.task
 * 邮箱：996489865@qq.com
 * TODO:定时处理未按时结算的订单
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
                try {
                    final List<ScanInfoEntity> swipeList = DBManager.getSwipeList();
                    if (swipeList == null || swipeList.size() == 0 || !Utils.checkNetStatus()) {
                        Log.d("TaskRotationService",
                                "run(TaskRotationService.java:63)" + "swipeList.size()=" + swipeList.size());
                        return;
                    }
                    request = new JsonRequest(Config.XBPAY, RequestMethod.POST);
                    JSONObject order_list = new JSONObject();
                    final JSONArray array = new JSONArray();

                    for (int i = 0; i < swipeList.size(); i++) {
                        array.add(JSON.parse(swipeList.get(i).getBiz_data_single()));
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
                                                    "success(TaskRotationService.java:102)扣款成功-修改成功!");
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

                } catch (Exception e) {
                    Log.d("TimeSettleTask",
                            "run(TimeSettleTask.java:118)" + e.toString());
                    e.printStackTrace();
                }
            }

        }, 1, 1, TimeUnit.MINUTES);
    }


}
