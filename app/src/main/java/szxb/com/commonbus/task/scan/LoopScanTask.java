package szxb.com.commonbus.task.scan;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.szxb.jni.libtest;
import com.szxb.xblog.XBLog;
import com.tencent.wlxsdk.WlxSdk;
import com.yanzhenjie.nohttp.Logger;

import org.greenrobot.greendao.query.Query;

import java.util.concurrent.TimeUnit;

import szxb.com.commonbus.App;
import szxb.com.commonbus.R;
import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.db.sp.CommonSharedPreferences;
import szxb.com.commonbus.db.table.BlackListEntityDao;
import szxb.com.commonbus.entity.BlackListEntity;
import szxb.com.commonbus.entity.ScanInfoEntity;
import szxb.com.commonbus.entity.SendInfo;
import szxb.com.commonbus.module.report.ReportParams;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.Des;
import szxb.com.commonbus.util.comm.Utils;
import szxb.com.commonbus.util.rx.RxBus;
import szxb.com.commonbus.util.schedule.ThreadScheduledExecutorUtil;
import szxb.com.commonbus.util.test.TestConstant;

import static szxb.com.commonbus.util.test.TestConstant.pos_id;

/**
 * 作者: Tangren on 2017/7/31
 * 包名：com.szxb.task
 * 邮箱：996489865@qq.com
 * TODO:轮训扫码
 */

public class LoopScanTask extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //临时变量存储上次刷卡记录,为了防止重复刷卡
    private String tem = "0";
    private WlxSdk wxSdk;
    private Des des;
    private static SoundPool soundPool;
    private static int music;
    ReportParams reportParams = new ReportParams();

    @Override
    public void onCreate() {
        super.onCreate();
        wxSdk = new WlxSdk();
        des = new Des();
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        music = soundPool.load(App.getInstance(), R.raw.beep2, 1);
        ThreadScheduledExecutorUtil.getInstance().getService().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //循环扫码
                String result = libtest.mytestbarcode();
                Log.d("LoopScanTask",
                        "run(LoopScanTask.java:76)" + result);
                if (!TextUtils.isEmpty(result) && result.length() > 10) {
                    String szxbStr = result.substring(1, 7);//前1-7位判断二维码
                    if (szxbStr.equals("000026")) {
                        //iPhone手机
                        result = result.substring(7, result.length());
                        if (tem.equals(result)) return;
                        Logger.d("二维码数据：" + result);
                        //进行验证二位的有效性、是否属于黑名单
                        verifyCode(result);

                    } else if (szxbStr.equals("szxbzn")) {
                        //下发参数二维码
                        if (tem.equals(result)) return;
                        if (result.length() < 56) return;//防止解析出现越界错误，所以多加一层判断
                        String sign = result.substring(7, 55);
                        if (TextUtils.equals(des.strDec(sign, Config.DES_KEY), DateUtil.getCurrentDate("yyyy-MM-dd"))) {
                            try {
                                String date = des.strDec(result.substring(55, result.length()), Config.DES_KEY);
                                String params[] = date.split(",");
                                JSONObject object = new JSONObject();

                                String bus_no = params[0];
                                String prices = params[1];
                                String start_station = params[2];
                                String end_station = params[3];
                                String line_name = params[4];

                                Log.d("LoopScanTask",
                                        "run(LoopScanTask.java:106)bus_no=" + bus_no);

                                Log.d("LoopScanTask",
                                        "run(LoopScanTask.java:108)prices=" + prices);

                                Log.d("LoopScanTask",
                                        "run(LoopScanTask.java:111)start_station=" + start_station);

                                Log.d("LoopScanTask",
                                        "run(LoopScanTask.java:114)end_station=" + end_station);

                                Log.d("LoopScanTask",
                                        "run(LoopScanTask.java:117)line_name=" + line_name);

                                //配置参数
                                CommonSharedPreferences.put("busNo", bus_no);
                                CommonSharedPreferences.put("ticketPrice", prices);
                                CommonSharedPreferences.put("startStationName", start_station);
                                CommonSharedPreferences.put("endStationName", end_station);
                                CommonSharedPreferences.put("lineName", line_name);

                                object.put("bus_no", params[0]);
                                object.put("is_set_pos", "1");
                                object.put("pos_no", "SN001");
                                object.put("is_online", "1");

                                object.put("bus_line_id ", 111);
                                object.put("bus_line_name", line_name);
                                object.put("line_start", start_station);
                                object.put("line_end ", end_station);
                                object.put("total_fee  ", 1);
                                object.put("pay_fee ", 1);

                                reportParams.report(object);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        //Android手机二维码
                        if (tem.equals(result)) return;
                        verifyCode(result);
                    }

                    tem = result;

                }

//
//                if (!TextUtils.isEmpty(result) && result.length() > 7) {
//                    String msgStr = result.substring(1, 7);
//                    //判断是否是iPhone,如果是则截取字符串
//                    if (msgStr.equals("000026"))
//                        result = result.substring(7, result.length());
//                    if (tem.equals(result)) return;
//                    Logger.d("二维码数据：" + result);
//                    //进行验证二位的有效性、是否属于黑名单
//                    verifyCode(result);
//                    tem = result;
//                }
            }
        }, 500, 200, TimeUnit.MILLISECONDS);
    }


    //过滤黑名单
    private boolean filterBlackName(String openID) {
        BlackListEntityDao dao = DBCore.getDaoSession().getBlackListEntityDao();
        Query<BlackListEntity> build = dao.queryBuilder().where(BlackListEntityDao.Properties.Open_id.eq(openID),
                BlackListEntityDao.Properties.Time.le(DateUtil.getCurrentDate())).build();
        BlackListEntity blackEntity = build.unique();
        return blackEntity != null;
    }


    private void verifyCode(String codeResult) {
        int init = wxSdk.init(codeResult);
        int key_id = wxSdk.get_key_id();
        String open_id = wxSdk.get_open_id();
        String mac_root_id = wxSdk.get_mac_root_id();

        Log.d("LoopScanTask",
                "verifyCode(LoopScanTask.java:102)" + init);
        Log.d("LoopScanTask",
                "verifyCode(LoopScanTask.java:101)mac_root_key=" + mac_root_id);
        Log.d("LoopScanTask",
                "verifyCode(LoopScanTask.java:103)open_id=" + open_id);
        Log.d("LoopScanTask",
                "verifyCode(LoopScanTask.java:105)key_id=" + key_id);

        //安全起见
        int verify = 0;
        if (!TextUtils.isEmpty(open_id)) {
            if (filterBlackName(open_id)) {
                //是黑名单里面的成员

            } else {
                //不是黑名单成员
                if (init == 0 && key_id > 0) {
                    if (key_id == 1) {
                        //String open_id, String pub_key, int payfee, byte scene, byte scantype, String pos_id, String pos_trx_id, String aes_mac_root
                        verify = wxSdk.verify(open_id, TestConstant.key_1, TestConstant.tickPrice, TestConstant.scane, TestConstant.scaneType, pos_id, Utils.Random(10), mac_root_id);
                    } else {
                        verify = wxSdk.verify(open_id, TestConstant.key_2, TestConstant.tickPrice, TestConstant.scane, TestConstant.scaneType, TestConstant.pos_id, Utils.Random(10), mac_root_id);
                    }
                    if (verify == 0) {
                        //验证成功
                        XBLog.d("verifyCode(LoopScanTask.java:111)验码成功!");
                        soundPool.play(music, 1, 1, 0, 0, 1);
                        String record = wxSdk.get_record();

                        //转换成JSONObject
                        JSONObject object = new JSONObject();
                        object.put("open_id", open_id);
                        object.put("mch_trx_id", Utils.Random(10));
                        object.put("order_time", DateUtil.currentLong());
                        object.put("order_desc", "扫码乘车");
                        object.put("total_fee", TestConstant.tickPrice);
                        object.put("pay_fee", TestConstant.tickPrice);
                        object.put("city_code", TestConstant.city_code);
                        object.put("exp_type", 0);
                        object.put("charge_type", 0);

                        object.put("bus_no", TestConstant.bus_no);
                        object.put("bus_line_name", TestConstant.bus_line_name);
                        object.put("pos_no", TestConstant.pos_no);

                        JSONObject ext = new JSONObject();
                        ext.put("in_station_id", TestConstant.in_station_id);
                        ext.put("in_station_name", TestConstant.in_station_name);
                        object.put("ext", ext);

                        JSONArray cord = new JSONArray();
                        cord.add(record);
                        object.put("record", cord);
                        insert(object);//存储乘车记录
                        RxBus.getInstance().send(new SendInfo(object, true));
                    } else {
                        //验码失败
                        Log.d("LoopScanTask",
                                "verifyCode(LoopScanTask.java:139)验码失败");
                        RxBus.getInstance().send(new SendInfo(null, true));
                    }

                } else {
                    Log.d("LoopScanTask",
                            "verifyCode(LoopScanTask.java:146)验码失败");
                    RxBus.getInstance().send(new SendInfo(null, true));
                }
            }

        } else RxBus.getInstance().send(null);
    }

    private void insert(JSONObject object) {
        ScanInfoEntity infoEntity = new ScanInfoEntity();
        infoEntity.setStatus(false);
        infoEntity.setBiz_data_single(object.toJSONString());
        DBCore.getDaoSession().getScanInfoEntityDao().insert(infoEntity);
    }

}
