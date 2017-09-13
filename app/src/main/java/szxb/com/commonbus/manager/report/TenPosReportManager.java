package szxb.com.commonbus.manager.report;

import android.text.TextUtils;
import android.util.Log;

import com.tencent.wlxsdk.WlxSdk;

import szxb.com.commonbus.db.manager.DBManager;
import szxb.com.commonbus.entity.PosRecord;
import szxb.com.commonbus.entity.QRCode;
import szxb.com.commonbus.entity.QRScanMessage;
import szxb.com.commonbus.task.scan.LoopScanTask;
import szxb.com.commonbus.util.rx.RxBus;

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
                RxBus.getInstance().send(new QRScanMessage(null, QRCode.QR_ERROR));
            } else {
                if (init == 0 && key_id > 0) {
                    //String open_id, String pub_key, int payfee, byte scene, byte scantype, String pos_id, String pos_trx_id, String aes_mac_root
                    verify = wxSdk.verify(open_id
                            , LoopScanTask.getPosManager().getPublicKey(String.valueOf(key_id))
//                            , LoopScanTask.getPosManager().getMarkedPrice()
                            , 1
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
                    posRecord.setTotal_fee(1);
//                    posRecord.setTotal_fee(LoopScanTask.getPosManager().getMarkedPrice());
//                    posRecord.setPay_fee(LoopScanTask.getPosManager().getMarkedPrice());
                    posRecord.setPay_fee(1);
                    posRecord.setCity_code(LoopScanTask.getPosManager().geCityCode());
                    posRecord.setOrder_desc(LoopScanTask.getPosManager().getOrderDesc());
                    posRecord.setIn_station_id(LoopScanTask.getPosManager().getInStationId());
                    posRecord.setIn_station_name(LoopScanTask.getPosManager().getInStationName());
                    posRecord.setRecord(record);
                    posRecord.setBus_no(LoopScanTask.getPosManager().getDriverNo());
                    posRecord.setBus_line_name(LoopScanTask.getPosManager().getLineName());
                    posRecord.setPos_no(LoopScanTask.getPosManager().getDriverNo());

                    RxBus.getInstance().send(new QRScanMessage(posRecord, verify));
                }

            }
        }
    }


}
