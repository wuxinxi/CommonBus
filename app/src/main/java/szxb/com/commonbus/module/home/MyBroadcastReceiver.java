package szxb.com.commonbus.module.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;

import java.lang.ref.WeakReference;

import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.db.sp.CommonSharedPreferences;
import szxb.com.commonbus.entity.MacKeyEntity;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.Utils;
import szxb.com.commonbus.util.tip.BusToast;

/**
 * 作者: Tangren on 2017-09-11
 * 包名：szxb.com.commonbus.module.home
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

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
                        "onReceive(MyBroadcastReceiver.java:46)" + noticeJsonText);
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
                            CommonSharedPreferences.put("ticketPrice", Utils.string2Integer(ticketPrice));
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
