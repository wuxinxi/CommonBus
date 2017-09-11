package szxb.com.commonbus.util.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import szxb.com.commonbus.R;

/**
 * 作者: Tangren on 2017-09-05
 * 包名：szxb.com.commonbus.util.sound
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class SoundPoolUtil {

    public static SoundPool mSoundPlayer = new SoundPool(10,
            AudioManager.STREAM_MUSIC, 5);
    public static SoundPoolUtil soundPlayUtils;

    static Context mContext;

    /**
     * 初始化
     *
     * @param context
     */
    public static SoundPoolUtil init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPoolUtil();
        }
        mContext = context;

        mSoundPlayer.load(mContext, R.raw.scan_success, 1);// 1,刷码成功
        mSoundPlayer.load(mContext, R.raw.install_success, 1);// 2,安装上报成功
        mSoundPlayer.load(mContext, R.raw.install_fail, 1);// 3，安装上报失败
        mSoundPlayer.load(mContext, R.raw.qr_error, 1);// 4,二维码错误
        mSoundPlayer.load(mContext, R.raw.verify_fail, 1);// 5,效验失败
        mSoundPlayer.load(mContext, R.raw.e, 1);// 6,软件异常
        mSoundPlayer.load(mContext, R.raw.ec_format, 1);// 7,二维码格式错误
        mSoundPlayer.load(mContext, R.raw.ec_card_public_key, 1);// 8,卡证书公钥错误
        mSoundPlayer.load(mContext, R.raw.ec_card_cert, 1);// 9,卡证书签名错误
        mSoundPlayer.load(mContext, R.raw.ec_user_public_key, 1);// 10,卡证书用户公钥错误
        mSoundPlayer.load(mContext, R.raw.ec_user_sign, 1);// 11,二维码签名错误
        mSoundPlayer.load(mContext, R.raw.ec_card_cert_time, 1);// 12,卡证书过期
        mSoundPlayer.load(mContext, R.raw.ec_code_time, 1);// 13,二维码过期
        mSoundPlayer.load(mContext, R.raw.ec_fee, 1);// 14,超出最大金额
        mSoundPlayer.load(mContext, R.raw.ec_balance, 1);// 15,余额不足
        mSoundPlayer.load(mContext, R.raw.ec_open_id, 1);// 16,输入的openid不符
        mSoundPlayer.load(mContext, R.raw.ec_param_err, 1);// 17,参数错误
        mSoundPlayer.load(mContext, R.raw.ec_mem_err, 1);// 18,申请内存错误
        mSoundPlayer.load(mContext, R.raw.ec_card_cert_sign_alg_not_support, 1);// 19,卡证书签名算法不支持
        mSoundPlayer.load(mContext, R.raw.ec_mac_root_key_decrypt_err, 1);// 20,加密的mac根密钥解密失败
        mSoundPlayer.load(mContext, R.raw.ec_mac_sign_err, 1);// 21,mac校验失败
        mSoundPlayer.load(mContext, R.raw.ec_qrcode_sign_alg_not_support, 1);// 22,二维码签名算法不支持
        mSoundPlayer.load(mContext, R.raw.ec_scan_record_ecrypt_err, 1);// 23,扫码记录加密失败
        mSoundPlayer.load(mContext, R.raw.ec_scan_record_ecode_err, 1);// 24,扫码记录编码失败
        mSoundPlayer.load(mContext, R.raw.ec_fail, 1);// 25,系统错误
        return soundPlayUtils;
    }

    /**
     * 播放声音
     *
     * @param soundID
     */
    public static void play(int soundID) {
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }

    public static void release() {
        if (mSoundPlayer != null)
            mSoundPlayer.release();
    }
}
