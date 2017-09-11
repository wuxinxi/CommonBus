package szxb.com.commonbus.manager.report;

/**
 * 作者: Tangren on 2017-09-08
 * 包名：szxb.com.commonbus.util.comm
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class PosScanManager {

    private static PosScanManager instance = null;

    private PosScanManager() {
    }

    public static PosScanManager getInstance() {
        synchronized (PosScanManager.class) {
            if (instance == null) {
                instance = new PosScanManager();
            }
        }
        return instance;
    }


    public static boolean isMyQRcode(String qrcode) {
        return qrcode != null && qrcode.indexOf("Sszxbzn") == 0;
    }

    public static boolean isTenQRcode(String qrcode) {
        return qrcode != null && qrcode.indexOf("TX") == 0;
    }


    public void txposScan(String qrcode) {
        TenPosReportManager.getInstance().posScan(qrcode);
    }

    public void xbposScan(String qrcode) {
        XBPosReportManager.getInstance().posScan(qrcode);
    }

}
