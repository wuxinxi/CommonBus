package szxb.com.commonbus.task.post;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import szxb.com.commonbus.base.BaseView;
import szxb.com.commonbus.db.manager.DBManager;
import szxb.com.commonbus.db.sp.CommonSharedPreferences;
import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.entity.ScanInfoEntity;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.schedule.ThreadScheduledExecutorUtil;

/**
 * 作者: Tangren on 2017/9/1
 * 包名：szxb.com.commonbus.task
 * 邮箱：996489865@qq.com
 * TODO:定时上送流水，10分钟如果有数据就上传一次
 */

public class TimePostBillTask extends Service implements BaseView {

    private String app_id;
    private PostBIllPresenter presenter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app_id = FetchAppConfig.appId();
        presenter = new PostBIllPresenter(this);
        ThreadScheduledExecutorUtil.getInstance().getService().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                List<ScanInfoEntity> swipeList = DBManager.getScanEntityList();
                Log.d("TimePostBillTask",
                        "run(TimePostBillTask.java:55)" + swipeList.size());
                if (swipeList.size() == 0) {
                    Log.d("TimePostBillTask",
                            "run(TimePostBillTask.java:58)" + swipeList.size());
                    return;
                }
                JSONObject order_list = new JSONObject();
                JSONArray array = new JSONArray();
                for (int i = 0; i < swipeList.size(); i++) {
                    array.add(JSON.parse(swipeList.get(i).getBiz_data_single()));
                }
                order_list.put("order_list", array);
                Map<String, Object> map = commMap();
                map.put("order_data", order_list.toJSONString());
                presenter.requestPost(Config.POST_BILL_WHAT, map, Config.POST_BILL);

            }
        }, 10, 10, TimeUnit.MINUTES);
    }

    private Map<String, Object> commMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("app_id", FetchAppConfig.appId());
        map.put("sn_no", FetchAppConfig.snNo());
        map.put("bus_no", FetchAppConfig.busNo());
        map.put("bus_time", DateUtil.getCurrentDate());
        return map;
    }

    @Override
    public void onSuccess(int what, String str) {
        //上传成功
        CommonSharedPreferences.put("lastTimePushFile", DateUtil.getCurrentDate());
    }

    @Override
    public void onFail(int what, String str) {
        //上传失败
    }
}
