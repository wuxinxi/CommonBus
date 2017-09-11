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
        mSoundPlayer.load(mContext, R.raw.e, 1);// 6,异常
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
