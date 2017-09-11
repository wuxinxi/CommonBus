package szxb.com.commonbus.manager.report;

import android.text.TextUtils;
import android.util.Log;

import com.tencent.wlxsdk.WlxSdk;

import szxb.com.commonbus.db.manager.DBManager;
import szxb.com.commonbus.entity.PosRecord;
import szxb.com.commonbus.entity.QRScanMessage;
import szxb.com.commonbus.task.scan.LoopScanTask;
import szxb.com.commonbus.util.rx.RxBus;
import szxb.com.commonbus.util.sound.SoundPoolUtil;

/**
 * 作者: Tangren on 2017-09-08
 * 包名：szxb.com.commonbus.util.report
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class TenPosReportManager {

    private static TenPosReportManager instance = null;
    private WlxSdk wxSdk;

    private TenPosReportManager() {
        wxSdk = new WlxSdk();
    }

    public static TenPosReportManager getInstance() {
        synchronized (TenPosReportManager.class) {
            if (instance == null) {
                instance = new TenPosReportManager();
            }
        }
        return instance;
    }

    public void posScan(String qrcode) {
        if (wxSdk == null) wxSdk = new WlxSdk();
        int init = wxSdk.init(qrcode);
        int key_id = wxSdk.get_key_id();
        String open_id = wxSdk.get_open_id();
        String mac_root_id = wxSdk.get_mac_root_id();
        Log.d("TenPosReportManager",
                "posScan(TenPosReportManager.java:37)init=" + init + "key_id=" + key_id + "open_id=" + open_id + "mac_root_id=" + mac_root_id);
        int verify = 0;
        if (!TextUtils.isEmpty(open_id)) {
            if (DBManager.filterBlackName(open_id)) {
                //是黑名单里面的成员
                SoundPoolUtil.play(4);
//                RxBus.getInstance().send(new SendInfo(null, PosMessage.QR_INVALID));
            } else {
                if (init == 0 && key_id > 0) {
                    //String open_id, String pub_key, int payfee, byte scene, byte scantype, String pos_id, String pos_trx_id, String aes_mac_root
                    verify = wxSdk.verify(open_id
                            , LoopScanTask.getPosManager().getPublicKey(String.valueOf(key_id))
                            , LoopScanTask.getPosManager().getMarkedPrice()
                            , (byte) 1
                            , (byte) 1
                            , LoopScanTask.getPosManager().getDriverNo()
                            , LoopScanTask.getPosManager().getmchTrxId()
                            , LoopScanTask.getPosManager().getMac(mac_root_id));
                    Log.d("TenPosReportManager",
                            "posScan(TenPosReportManager.java:63)verify=" + verify);
                    Log.d("TenPosReportManager",
                            "posScan(TenPosReportManager.java:65)getPublicKey=" + LoopScanTask.getPosManager().getPublicKey(String.valueOf(key_id)));
                    Log.d("TenPosReportManager",
                            "posScan(TenPosReportManager.java:67)getmchTrxId=" + LoopScanTask.getPosManager().getmchTrxId());
                    Log.d("TenPosReportManager",
                            "posScan(TenPosReportManager.java:69)getMac=" + LoopScanTask.getPosManager().getMac(mac_root_id));

                    String record = wxSdk.get_record();
                    PosRecord posRecord = new PosRecord();
                    posRecord.setOpen_id(open_id);
                    posRecord.setMch_trx_id(LoopScanTask.getPosManager().getmchTrxId());
                    posRecord.setOrder_time(LoopScanTask.getPosManager().getOrderTime());
                    posRecord.setTotal_fee(LoopScanTask.getPosManager().getMarkedPrice());
                    posRecord.setPay_fee(LoopScanTask.getPosManager().getMarkedPrice());
                    posRecord.setCity_code(LoopScanTask.getPosManager().geCityCode());
                    posRecord.setOrder_desc(LoopScanTask.getPosManager().getOrderDesc());
                    posRecord.setIn_station_id(LoopScanTask.getPosManager().getInStationId());
                    posRecord.setIn_station_name(LoopScanTask.getPosManager().getInStationName());
                    posRecord.setRecord(record);
                    posRecord.setBus_no(LoopScanTask.getPosManager().getBusNo());
                    posRecord.setBus_line_name(LoopScanTask.getPosManager().getLineName());
                    posRecord.setPos_no(LoopScanTask.getPosManager().getDriverNo());

                    RxBus.getInstance().send(new QRScanMessage(posRecord, verify));
                }
//
//                    //验码通过
//                    if (verify == 0) {
//                        Log.d("TenPosReportManager",
//                                "posScan(TenPosReportManager.java:72)验码通过");
////                        String record = wxSdk.get_record();
//                        //转换成JSONObject
//                        JSONObject object = new JSONObject();
//                        object.put("open_id", open_id);
//                        object.put("mch_trx_id", Utils.Random(10));
//                        object.put("order_time", DateUtil.currentLong());
//                        object.put("order_desc", "扫码乘车");
//                        object.put("total_fee", TestConstant.tickPrice);
//                        object.put("pay_fee", TestConstant.tickPrice);
//                        object.put("city_code", TestConstant.city_code);
//                        object.put("exp_type", 0);
//                        object.put("charge_type", 0);
//
//                        object.put("bus_no", TestConstant.bus_no);
//                        object.put("bus_line_name", TestConstant.bus_line_name);
//                        object.put("pos_no", TestConstant.pos_no);
//
//                        JSONObject ext = new JSONObject();
//                        ext.put("in_station_id", TestConstant.in_station_id);
//                        ext.put("in_station_name", TestConstant.in_station_name);
//                        object.put("ext", ext);
//
//                        JSONArray cord = new JSONArray();
//                        cord.add(record);
//                        object.put("record", cord);
////                        DBManager.insert(object);//存储乘车记录
//                        SoundPoolUtil.play(1);
////                        RxBus.getInstance().send(new SendInfo(object, PosMessage.VERIFY_CODE_SUCCESS));
//
//                    } else {
////                        SoundPoolUtil.play(5);
////                        RxBus.getInstance().send(new SendInfo(null, PosMessage.VERIFY_CODE_FAIL));
//                    }
//                } else {
////                    SoundPoolUtil.play(5);
////                    RxBus.getInstance().send(new SendInfo(null, PosMessage.VERIFY_CODE_FAIL));
//                }

            }
        }
    }


}
