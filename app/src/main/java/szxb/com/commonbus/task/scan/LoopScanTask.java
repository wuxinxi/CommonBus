package szxb.com.commonbus.task.scan;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.szxb.jni.libszxb;

import java.util.concurrent.TimeUnit;

import szxb.com.commonbus.entity.QRCode;
import szxb.com.commonbus.entity.QRScanMessage;
import szxb.com.commonbus.interfaces.IPosManage;
import szxb.com.commonbus.manager.PosManager;
import szxb.com.commonbus.manager.report.PosScanManager;
import szxb.com.commonbus.util.rx.RxBus;
import szxb.com.commonbus.util.schedule.ThreadScheduledExecutorUtil;

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
    private static PosManager manager;

    @Override
    public void onCreate() {
        super.onCreate();

        manager = new PosManager();
        manager.loadFromPrefs();

        ThreadScheduledExecutorUtil.getInstance().getService().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    //循环扫码
                    byte[] recv = new byte[1024];
                    int barcode = libszxb.getBarcode(recv);
                    if (barcode > 0) {
                        String result = new String(recv, 0, barcode);
                        if (PosScanManager.isMyQRcode(result)) {
                            if (TextUtils.equals(result, tem)) {
                                RxBus.getInstance().send(new QRScanMessage(null, QRCode.REFRESH_QR_CODE));
                                return;
                            }
                            PosScanManager.getInstance().xbposScan(result);
                        } else if (PosScanManager.isTenQRcode(result)) {
                            if (TextUtils.equals(result, tem)) {
                                RxBus.getInstance().send(new QRScanMessage(null, QRCode.REFRESH_QR_CODE));
                                return;
                            }
                            PosScanManager.getInstance().txposScan(result);
                        } else {
                            RxBus.getInstance().send(new QRScanMessage(null, QRCode.QR_ERROR));
                        }
                        tem = result;
                    }
                } catch (Exception e) {
                    RxBus.getInstance().send(new QRScanMessage(null, QRCode.SOFTWARE_EXCEPTION));
                    e.printStackTrace();
                }

            }
        }, 500, 200, TimeUnit.MILLISECONDS);
    }

    public static IPosManage getPosManager() {
        return manager;
    }


}
