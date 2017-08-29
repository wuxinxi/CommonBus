package szxb.com.commonbus.task;

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
import szxb.com.commonbus.db.table.BlackListEntityDao;
import szxb.com.commonbus.entity.BlackListEntity;
import szxb.com.commonbus.entity.ScanInfoEntity;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.Utils;
import szxb.com.commonbus.util.rx.RxBus;
import szxb.com.commonbus.util.schedule.ThreadScheduledExecutorUtil;
import szxb.com.commonbus.util.test.TestConstant;

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
    private static SoundPool soundPool;
    private static int music;

    @Override
    public void onCreate() {
        super.onCreate();
        wxSdk = new WlxSdk();
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        music = soundPool.load(App.getInstance(), R.raw.beep2, 1);
        ThreadScheduledExecutorUtil.getInstance().getService().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //循环扫码
                String result = libtest.mytestbarcode();
                if (!TextUtils.isEmpty(result) && result.length() > 7) {
                    String msgStr = result.substring(1, 7);
                    //判断是否是iPhone,如果是则截取字符串
                    if (msgStr.equals("000026"))
                        result = result.substring(7, result.length());
                    if (tem.equals(result)) return;
                    Logger.d("二维码数据：" + result);
                    //进行验证二位的有效性、是否属于黑名单
                    verifyCode(result);
                    tem = result;
                }
            }
        }, 500, 500, TimeUnit.MILLISECONDS);
    }


    //过滤黑名单
    private boolean filterBlackName(String openID) {
        BlackListEntityDao dao = DBCore.getDaoSession().getBlackListEntityDao();
        Query<BlackListEntity> build = dao.queryBuilder().where(BlackListEntityDao.Properties.Open_id.eq(openID)).build();
        BlackListEntity blackEntity = build.unique();
        return blackEntity != null;
    }


    private void verifyCode(String codeResult) {
        int init = wxSdk.init(codeResult);
        int key_id = wxSdk.get_key_id();
        String open_id = wxSdk.get_open_id();
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
//                        verify = wxSdk.verify(open_id, TestConstant.key_1, TestConstant.tickPrice, TestConstant.scane, TestConstant.scaneType, pos_id, Utils.Random(10));
                    } else {
//                        verify = wxSdk.verify(open_id, TestConstant.key_2, TestConstant.tickPrice, TestConstant.scane, TestConstant.scaneType, TestConstant.pos_id, Utils.Random(10));
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

                        JSONObject ext = new JSONObject();
                        ext.put("in_station_id", TestConstant.in_station_id);
                        ext.put("in_station_name", TestConstant.in_station_name);
                        object.put("ext", ext);

                        JSONArray cord = new JSONArray();
                        cord.add(record);
                        object.put("record", cord);
                        insert(object);//存储乘车记录
                        RxBus.getInstance().send(object);
                    } else {
                        //验码失败
                        Log.d("LoopScanTask",
                                "verifyCode(LoopScanTask.java:139)验码失败");
                        RxBus.getInstance().send(null);
                    }

                } else {
                    Log.d("LoopScanTask",
                            "verifyCode(LoopScanTask.java:146)验码失败");
                    RxBus.getInstance().send(null);
                }
            }

        } else RxBus.getInstance().send(false);
    }

    private void insert(JSONObject object) {
        ScanInfoEntity infoEntity = new ScanInfoEntity();
        infoEntity.setStatus(false);
        infoEntity.setBiz_data_single(object.toJSONString());
        DBCore.getDaoSession().getScanInfoEntityDao().insert(infoEntity);
    }

}
