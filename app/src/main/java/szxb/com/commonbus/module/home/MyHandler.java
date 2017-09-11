package szxb.com.commonbus.module.home;

import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;

import java.lang.ref.WeakReference;

import szxb.com.commonbus.App;
import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.entity.QRCode;
import szxb.com.commonbus.util.comm.Utils;
import szxb.com.commonbus.util.tip.BusToast;

/**
 * 作者: Tangren on 2017-09-11
 * 包名：szxb.com.commonbus.module.home
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class MyHandler extends Handler {

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
                case QRCode.TIMER:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd HH:mm:ss", sysTime);
                    activity.currentTime.setText(sysTimeStr);
                    break;
                case QRCode.EC_FORMAT://二维码格式错误、
                    BusToast.showToast(App.getInstance(), "二维码格式错误", false);
                    break;
                case QRCode.EC_CARD_PUBLIC_KEY://卡证书公钥错误
                    BusToast.showToast(App.getInstance(), "卡证书公钥错误", false);
                    break;
                case QRCode.EC_CARD_CERT://卡证书签名错误
                    BusToast.showToast(App.getInstance(), "卡证书签名错误", false);
                    break;
                case QRCode.EC_USER_PUBLIC_KEY://卡证书用户公钥错误
                    BusToast.showToast(App.getInstance(), "卡证书用户公钥错误", false);
                    break;
                case QRCode.EC_USER_SIGN://二维码签名错误
                    BusToast.showToast(App.getInstance(), "二维码签名错误", false);
                    break;
                case QRCode.EC_CARD_CERT_TIME://卡证书过期
                    BusToast.showToast(App.getInstance(), "卡证书过期", false);
                    break;
                case QRCode.EC_CODE_TIME://二维码过期
                    BusToast.showToast(App.getInstance(), "二维码过期", false);
                    break;
                case QRCode.EC_FEE://超出最大金额
                    BusToast.showToast(App.getInstance(), "超出最大金额", false);
                    break;
                case QRCode.EC_BALANCE://余额不足
                    BusToast.showToast(App.getInstance(), "余额不足", false);
                    break;
                case QRCode.EC_OPEN_ID://输入的openid不符
                    BusToast.showToast(App.getInstance(), "输入的openid不符", false);
                    break;
                case QRCode.EC_PARAM_ERR://参数错误
                    BusToast.showToast(App.getInstance(), "参数错误", false);
                    break;
                case QRCode.EC_MEM_ERR://申请内存错误
                    BusToast.showToast(App.getInstance(), "申请内存错误", false);
                    break;
                case QRCode.EC_CARD_CERT_SIGN_ALG_NOT_SUPPORT://卡证书签名算法不支持
                    BusToast.showToast(App.getInstance(), "卡证书签名算法不支持", false);
                    break;
                case QRCode.EC_MAC_ROOT_KEY_DECRYPT_ERR://加密的mac根密钥解密失败
                    BusToast.showToast(App.getInstance(), "加密的mac根密钥解密失败", false);
                    break;
                case QRCode.EC_MAC_SIGN_ERR://mac校验失败
                    BusToast.showToast(App.getInstance(), "mac校验失败", false);
                    break;
                case QRCode.EC_QRCODE_SIGN_ALG_NOT_SUPPORT://二维码签名算法不支持
                    BusToast.showToast(App.getInstance(), "二维码签名算法不支持", false);
                    break;
                case QRCode.EC_SCAN_RECORD_ECRYPT_ERR://扫码记录加密失败
                    BusToast.showToast(App.getInstance(), "扫码记录加密失败", false);
                    break;
                case QRCode.EC_SCAN_RECORD_ECODE_ERR://扫码记录编码失败
                    BusToast.showToast(App.getInstance(), "扫码记录编码失败", false);
                    break;
                case QRCode.MY_QR_INSTALL_SUCCESS://小兵二维码安装上报成功
                    activity.station_name.setText(FetchAppConfig.startStationName() + "————" + FetchAppConfig.endStationName());
                    activity.prices.setText(Utils.fen2Yuan(FetchAppConfig.ticketPrice()) + "元");
                    activity.bus_line_name.setText(FetchAppConfig.lineName());
                    break;
                case QRCode.EC_FAIL://系统异常
                    BusToast.showToast(App.getInstance(), "系统异常", false);
                    break;
                default:

                    break;
            }
        }
    }
}
