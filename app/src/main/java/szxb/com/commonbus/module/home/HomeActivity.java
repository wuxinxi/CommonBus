package szxb.com.commonbus.module.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.rest.Response;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import szxb.com.commonbus.R;
import szxb.com.commonbus.base.BaseActivity;
import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.db.sp.CommonSharedPreferences;
import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.db.table.ScanInfoEntityDao;
import szxb.com.commonbus.entity.MacKeyEntity;
import szxb.com.commonbus.entity.PosMessage;
import szxb.com.commonbus.entity.ScanInfoEntity;
import szxb.com.commonbus.entity.SendInfo;
import szxb.com.commonbus.http.CallServer;
import szxb.com.commonbus.http.HttpListener;
import szxb.com.commonbus.http.JsonRequest;
import szxb.com.commonbus.task.scan.LoopScanTask;
import szxb.com.commonbus.task.settle.TimeSettleTask;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.Utils;
import szxb.com.commonbus.util.rx.RxBus;
import szxb.com.commonbus.util.schedule.ThreadScheduledExecutorUtil;
import szxb.com.commonbus.util.sign.ParamSingUtil;
import szxb.com.commonbus.util.sound.SoundPoolUtil;
import szxb.com.commonbus.util.tip.BusToast;

import static szxb.com.commonbus.util.comm.ParamsUtil.commonMap;

public class HomeActivity extends BaseActivity {

    private Intent loopScanTaskIntent;
    private Intent timeSettleTaskIntent;
    private Subscription sub;
    private JsonRequest request;

    private TextView currentTime;
    private TextView station_name;
    private TextView bus_line_name;
    private TextView prices;

    private MyHandler mHandler;
    private MyBroadcastReceiver mReceiver;

    LinearLayout layout;

    @Override
    protected int rootView() {
        return R.layout.bus_view;
    }


    @Override
    protected void initView() {
        currentTime = (TextView) findViewById(R.id.currentTime);
        station_name = (TextView) findViewById(R.id.station_name);
        bus_line_name = (TextView) findViewById(R.id.bus_line_name);
        prices = (TextView) findViewById(R.id.prices);
        layout = (LinearLayout) findViewById(R.id.layout);
    }

    @Override
    protected void initData() {
        //初始化数据
        station_name.setText(FetchAppConfig.startStationName() + "————" + FetchAppConfig.endStationName());
        bus_line_name.setText(FetchAppConfig.lineName());
        prices.setText(Utils.fen2Yuan(FetchAppConfig.ticketPrice()) + "元");
        currentTime.setText(DateUtil.getCurrentDate());

        loopScanTaskIntent = new Intent(this, LoopScanTask.class);
        timeSettleTaskIntent = new Intent(this, TimeSettleTask.class);
        startService(loopScanTaskIntent);
        startService(timeSettleTaskIntent);
        request = new JsonRequest("");
        receiveNews();

        mHandler = new MyHandler(this);
        mReceiver = new MyBroadcastReceiver(this);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(11);
            }
        }, 0, 1000);

        registerReceiver(mReceiver, new IntentFilter("com.szxb.bus.notice"));


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //实时扣款
    private void receiveNews() {
        sub = RxBus.getInstance().toObservable(SendInfo.class)
                .filter(new Func1<SendInfo, Boolean>() {
                    @Override
                    public Boolean call(SendInfo sendInfo) {

                        switch (sendInfo.getType()) {
                            case PosMessage.MY_QR_INSTALL_SUCCESS:
                                mHandler.sendEmptyMessage(PosMessage.MY_QR_INSTALL_SUCCESS);
                                break;
                            case PosMessage.VERIFY_CODE_FAIL:
                                mHandler.sendEmptyMessage(PosMessage.VERIFY_CODE_FAIL);
                                break;
                            case PosMessage.QR_INVALID:
                                mHandler.sendEmptyMessage(PosMessage.QR_INVALID);
                                break;
                        }

                        return sendInfo.getObject() != null;//过滤非法
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<SendInfo>() {
                    @Override
                    public void call(SendInfo sendInfo) {
                        final JSONObject jsonObject = sendInfo.getObject();
                        JSONObject object = new JSONObject();
                        JSONArray order_list = new JSONArray();
                        order_list.add(jsonObject);
                        object.put("order_list", order_list);

                        String timestamp = DateUtil.getCurrentDate();
                        String app_id = FetchAppConfig.appId();
                        Map<String, Object> map = commonMap(app_id, timestamp);
                        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, object, ""));
                        map.put("biz_data", object.toString());

                        request.add(map);
                        CallServer.getHttpclient().add(0, request, new HttpListener<JSONObject>() {
                            @Override
                            public void success(int what, Response<JSONObject> response) {

                                String retcode = response.get().getString("retcode");
                                if (retcode.equals("0")) {
                                    String retmsg = response.get().getString("retmsg");
                                    if (retmsg.equals("ok")) {
                                        JSONArray result_list = response.get().getJSONArray("result_list");
                                        JSONObject resultObject = result_list.getJSONObject(0);
                                        if (resultObject.getString("status").equals("00") || resultObject.getString("status").equals("91")) {

                                            ScanInfoEntityDao dao = DBCore.getDaoSession().getScanInfoEntityDao();
                                            ScanInfoEntity entity = dao.queryBuilder().where(ScanInfoEntityDao.Properties.Biz_data_single.eq(jsonObject.toJSONString())).build().unique();
                                            if (entity != null) {
                                                entity.setStatus(true);
                                                dao.update(entity);
                                            }
                                            Log.d("HomeActivity",
                                                    "success(HomeActivity.java:108)扣款成功-修改成功!");
                                        }
                                    } else {
                                        //准实时扣款失败
                                    }
                                }
                            }

                            @Override
                            public void fail(int what, String e) {

                            }
                        });

                    }
                });
    }


    static class MyHandler extends Handler {
        private WeakReference<HomeActivity> weakReference;

        public MyHandler(HomeActivity activity) {
            weakReference = new WeakReference<HomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HomeActivity activity = weakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 11:
                        long sysTime = System.currentTimeMillis();
                        CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd HH:mm:ss", sysTime);
                        activity.currentTime.setText(sysTimeStr);
                        break;
                    case PosMessage.MY_QR_INSTALL_SUCCESS:
                        activity.station_name.setText(FetchAppConfig.startStationName() + "————" + FetchAppConfig.endStationName());
                        activity.prices.setText(Utils.fen2Yuan(FetchAppConfig.ticketPrice()) + "元");
                        activity.bus_line_name.setText(FetchAppConfig.lineName());
                        break;
                    case PosMessage.VERIFY_CODE_FAIL:
                        BusToast.showToast(activity, "验码失败", false);
                        break;
                    case PosMessage.QR_INVALID:
                        BusToast.showToast(activity, "无效二维码", false);
                        break;
                    default:

                        break;
                }

            }

        }
    }

    static class MyBroadcastReceiver extends BroadcastReceiver {

        private WeakReference<HomeActivity> weakReference;

        public MyBroadcastReceiver(HomeActivity activity) {
            weakReference = new WeakReference<HomeActivity>(activity);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            HomeActivity activity = weakReference.get();
            if (activity != null) {
                if (intent.getAction().equals("com.szxb.bus.notice")) {
                    String noticeJsonText = intent.getStringExtra("noticeJson");
                    Log.d("MyBroadcastReceiver",
                            "onReceive(MyBroadcastReceiver.java:223)" + noticeJsonText);
                    if (!TextUtils.isEmpty(noticeJsonText)) {
                        JSONObject noticeJosnObject = JSONObject.parseObject(noticeJsonText);
                        String flag = noticeJosnObject.getString("flag");
                        switch (flag) {
                            case "6"://mac  根秘钥推送
                                final JSONArray array = noticeJosnObject.getJSONArray("mackey_list");
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
                                            macKeyEntity.setPubkey(object.getString("mac_key"));
                                            DBCore.getDaoSession().insert(macKeyEntity);
                                        }
                                    }
                                });
                                async.deleteAll(MacKeyEntity.class);
                                BusToast.showToast(activity, "MAC更新成功", true);
                                break;
                            case "5"://线路信息更新推送
//                            {“flag”:”5”,” bus_no”：”粤 B123456”，” pos_no ”:” SN001”, ” price ”:”2” , ” start_station ”:” 深大” , ” end_station ”:”蛇口” , ” line_name ”:”深蛇线”,”remark”:”备注”}

                                String ticketPrice = noticeJosnObject.getString("price");
                                String start_station = noticeJosnObject.getString("start_station");
                                String end_station = noticeJosnObject.getString("end_station");
                                String line_name = noticeJosnObject.getString("line_name");

                                activity.prices.setText(Utils.fen2Yuan(Integer.valueOf(ticketPrice)) + "元");
                                activity.station_name.setText(start_station + "————" + end_station);
                                activity.bus_line_name.setText(line_name);

                                CommonSharedPreferences.put("busNo", noticeJosnObject.getString("bus_no"));
                                CommonSharedPreferences.put("snNo", noticeJosnObject.getString("pos_no"));
                                CommonSharedPreferences.put("ticketPrice", ticketPrice);
                                CommonSharedPreferences.put("startStationName", start_station);
                                CommonSharedPreferences.put("endStationName", end_station);
                                CommonSharedPreferences.put("lineName", line_name);

                                BusToast.showToast(activity, "线路信息更新成功", true);
                                break;
                            default:

                                break;
                        }
                    }
                }
            }
        }
    }

    boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
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
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);

        SoundPoolUtil.release();
    }

}
