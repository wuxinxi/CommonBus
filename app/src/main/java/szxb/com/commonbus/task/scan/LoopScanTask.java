package szxb.com.commonbus.task.scan;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.szxb.jni.libszxb;
import com.szxb.xblog.XBLog;
import com.tencent.wlxsdk.WlxSdk;

import org.greenrobot.greendao.query.Query;

import java.util.concurrent.TimeUnit;

import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.db.table.BlackListEntityDao;
import szxb.com.commonbus.entity.BlackListEntity;
import szxb.com.commonbus.entity.PosMessage;
import szxb.com.commonbus.entity.ScanInfoEntity;
import szxb.com.commonbus.entity.SendInfo;
import szxb.com.commonbus.interfaces.IPosManage;
import szxb.com.commonbus.manager.PosManager;
import szxb.com.commonbus.manager.report.PosScanManager;
import szxb.com.commonbus.module.report.ReportParams;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.Des;
import szxb.com.commonbus.util.comm.Utils;
import szxb.com.commonbus.util.rx.RxBus;
import szxb.com.commonbus.util.schedule.ThreadScheduledExecutorUtil;
import szxb.com.commonbus.util.sound.SoundPoolUtil;
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
    ReportParams reportParams = new ReportParams();
    private static PosManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        wxSdk = new WlxSdk();
        des = new Des();
        manager = new PosManager();
        manager.loadFromPrefs();
        ThreadScheduledExecutorUtil.getInstance().getService().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //循环扫码
                byte[] recv = new byte[1024];
                int barcode = libszxb.getBarcode(recv);
                if (barcode > 0) {
                    String result = new String(recv, 0, barcode);
                    if (TextUtils.equals(result, tem)) return;
                    try {
                        if (PosScanManager.isMyQRcode(result)) {
                            PosScanManager.getInstance().xbposScan(result);
                        } else if (PosScanManager.isTenQRcode(result)) {
                            PosScanManager.getInstance().txposScan(result);
                        } else {
                            SoundPoolUtil.play(4);
                            RxBus.getInstance().send(new SendInfo(null, PosMessage.QR_INVALID));
                            Log.d("LoopScanTask",
                                    "run(LoopScanTask.java:83)" + result);
                        }
                        tem = result;
                    } catch (Exception e) {
                        SoundPoolUtil.play(6);
                        Log.d("LoopScanTask",
                                "run(LoopScanTask.java:98)" + e.toString());
                        e.printStackTrace();
                    }
                }
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
                        SoundPoolUtil.play(1);
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
                        RxBus.getInstance().send(new SendInfo(object, 0));
                    } else {
                        //验码失败
                        Log.d("LoopScanTask",
                                "verifyCode(LoopScanTask.java:139)验码失败");
                        RxBus.getInstance().send(new SendInfo(null, 4));
                    }

                } else {
                    Log.d("LoopScanTask",
                            "verifyCode(LoopScanTask.java:146)验码失败");
                    RxBus.getInstance().send(new SendInfo(null, 4));
                }
            }

        } else RxBus.getInstance().send(new SendInfo(null, 4));
    }

    private void insert(JSONObject object) {
        ScanInfoEntity infoEntity = new ScanInfoEntity();
        infoEntity.setStatus(false);
        infoEntity.setBiz_data_single(object.toJSONString());
        DBCore.getDaoSession().getScanInfoEntityDao().insert(infoEntity);
    }


    public static IPosManage getPosManager() {
        return manager;
    }

}
