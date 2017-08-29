package szxb.com.commonbus.module;

import android.content.Intent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.Map;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import szxb.com.commonbus.R;
import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.task.LoopScanTask;
import szxb.com.commonbus.task.TimeSettleTask;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.rx.RxBus;
import szxb.com.commonbus.util.schedule.ThreadScheduledExecutorUtil;
import szxb.com.poslibrary.base.BaseActivity;
import szxb.com.poslibrary.http.CallServer;
import szxb.com.poslibrary.http.HttpListener;
import szxb.com.poslibrary.http.JsonRequest;
import szxb.com.poslibrary.util.sign.ParamSingUtil;

import static szxb.com.commonbus.util.comm.ParamsUtil.commonMap;

public class HomeActivity extends BaseActivity {

    private Intent loopScanTaskIntent;
    private Intent timeSettleTaskIntent;
    private Subscription sub;
    private JsonRequest request;

    @Override
    protected int rootView() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {

        loopScanTaskIntent = new Intent(this, LoopScanTask.class);
        timeSettleTaskIntent = new Intent(this, TimeSettleTask.class);
        request = new JsonRequest("");
        receiveNews();
    }

    //实时扣款
    private void receiveNews() {
        sub = RxBus.getInstance().toObservable(JSONObject.class)
                .filter(new Func1<JSONObject, Boolean>() {
                    @Override
                    public Boolean call(JSONObject jsonObject) {
                        return jsonObject != null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        JSONObject object = new JSONObject();
                        JSONArray order_list = new JSONArray();
                        order_list.add(object);
                        object.put("order_list", order_list);

                        String timestamp = DateUtil.getCurrentDate();
                        String app_id = FetchAppConfig.appId();
                        Map<String, Object> map = commonMap(app_id, timestamp);
                        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, jsonObject, ""));
                        map.put("biz_data", object.toString());

                        request.add(map);
                        CallServer.getHttpclient().add(0, request, new HttpListener<JSONObject>() {
                            @Override
                            public void success(int what, Response<JSONObject> response) {

                            }

                            @Override
                            public void fail(int what, String e) {

                            }
                        });
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadScheduledExecutorUtil.shutdown();
        stopService(loopScanTaskIntent);
        stopService(timeSettleTaskIntent);
        if (sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }
}
