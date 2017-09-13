package szxb.com.commonbus.module.home;

import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import szxb.com.commonbus.App;
import szxb.com.commonbus.R;
import szxb.com.commonbus.base.BaseMvpActivity;
import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.entity.QRCode;
import szxb.com.commonbus.entity.QRScanMessage;
import szxb.com.commonbus.task.scan.LoopScanTask;
import szxb.com.commonbus.task.settle.TimeSettleTask;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.Utils;
import szxb.com.commonbus.util.rx.RxBus;
import szxb.com.commonbus.util.schedule.ThreadScheduledExecutorUtil;
import szxb.com.commonbus.util.sound.SoundPoolUtil;
import szxb.com.commonbus.util.tip.BusToast;

public class HomeActivity extends BaseMvpActivity<HomePresenter> {

    private Intent loopScanTaskIntent;
    private Intent timeSettleTaskIntent;
    private Subscription sub;

    public TextView currentTime;
    public TextView station_name;
    public TextView bus_line_name;
    public TextView prices;

    private MyHandler mHandler;
    private MyBroadcastReceiver mReceiver;

    LinearLayout layout;

    @Override
    protected int rootView() {
        return R.layout.bus_view;
    }

    @Override
    protected HomePresenter getChildPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected void initView() {
        currentTime = (TextView) findViewById(R.id.currentTime);
        station_name = (TextView) findViewById(R.id.station_name);
        bus_line_name = (TextView) findViewById(R.id.bus_line_name);
        prices = (TextView) findViewById(R.id.prices);
        layout = (LinearLayout) findViewById(R.id.layout);
    }

    @Override
    protected void initData() {
        //初始化数据
        station_name.setText(FetchAppConfig.startStationName() + "————" + FetchAppConfig.endStationName());
        bus_line_name.setText(FetchAppConfig.lineName());
        prices.setText(Utils.fen2Yuan(FetchAppConfig.ticketPrice()) + "元");
        currentTime.setText(DateUtil.getCurrentDate());

        loopScanTaskIntent = new Intent(this, LoopScanTask.class);
        timeSettleTaskIntent = new Intent(this, TimeSettleTask.class);
        startService(loopScanTaskIntent);
        startService(timeSettleTaskIntent);

        receiverNews();

        mHandler = new MyHandler(this);
        mReceiver = new MyBroadcastReceiver(this);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(QRCode.TIMER);
            }
        }, 0, 1000);

        registerReceiver(mReceiver, new IntentFilter("com.szxb.bus.notice"));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });
    }

    //实时扣款
    //可直接复制到项目中
    private void receiverNews() {
        sub = RxBus.getInstance().toObservable(QRScanMessage.class)
                .filter(new Func1<QRScanMessage, Boolean>() {
                    @Override
                    public Boolean call(QRScanMessage qrScanMessage) {
                        Log.d("HomeActivity",
                                "call(HomeActivity.java:117)" + qrScanMessage.toString());
                        switch (qrScanMessage.getResult()) {
                            case QRCode.EC_SUCCESS://验码成功
                                SoundPoolUtil.play(1);
                                BusToast.showToast(App.getInstance(), "刷码成功", true);
                                return true;
                            case QRCode.QR_ERROR://非腾讯或者小兵二维码
                                SoundPoolUtil.play(4);
                                BusToast.showToast(App.getInstance(), "二维码有误", false);
                                break;
                            case QRCode.SOFTWARE_EXCEPTION:
                                SoundPoolUtil.play(6);
                                BusToast.showToast(App.getInstance(), "软件出现异常", false);
                                break;
                            case QRCode.EC_FORMAT://二维码格式错误
                                SoundPoolUtil.play(7);
                                BusToast.showToast(App.getInstance(), "二维码格式错误", false);
                                break;
                            case QRCode.EC_CARD_PUBLIC_KEY://卡证书公钥错误
                                SoundPoolUtil.play(8);
                                BusToast.showToast(App.getInstance(), "卡证书公钥错误", false);
                                break;
                            case QRCode.EC_CARD_CERT://卡证书签名错误
                                SoundPoolUtil.play(9);
                                BusToast.showToast(App.getInstance(), "卡证书签名错误", false);
                                break;
                            case QRCode.EC_USER_PUBLIC_KEY://卡证书用户公钥错误
                                SoundPoolUtil.play(10);
                                BusToast.showToast(App.getInstance(), "卡证书用户公钥错误", false);
                                break;
                            case QRCode.EC_USER_SIGN://二维码签名错误
                                SoundPoolUtil.play(11);
                                BusToast.showToast(App.getInstance(), "二维码签名错误", false);
                                break;
                            case QRCode.EC_CARD_CERT_TIME://卡证书过期
                                SoundPoolUtil.play(12);
                                BusToast.showToast(App.getInstance(), "卡证书过期", false);
                                break;
                            case QRCode.EC_CODE_TIME://二维码过期
                                SoundPoolUtil.play(13);
                                BusToast.showToast(App.getInstance(), "二维码过期", false);
                                break;
                            case QRCode.EC_FEE://超出最大金额
                                SoundPoolUtil.play(14);
                                BusToast.showToast(App.getInstance(), "超出最大金额", false);
                                break;
                            case QRCode.EC_BALANCE://余额不足
                                SoundPoolUtil.play(15);
                                BusToast.showToast(App.getInstance(), "余额不足", false);
                                break;
                            case QRCode.EC_OPEN_ID://输入的openid不符
                                SoundPoolUtil.play(16);
                                BusToast.showToast(App.getInstance(), "输入的openid不符", false);
                                break;
                            case QRCode.EC_PARAM_ERR://参数错误
                                SoundPoolUtil.play(17);
                                BusToast.showToast(App.getInstance(), "参数错误", false);
                                break;
                            case QRCode.EC_MEM_ERR://申请内存错误
                                SoundPoolUtil.play(18);
                                BusToast.showToast(App.getInstance(), "申请内存错误", false);
                                break;
                            case QRCode.EC_CARD_CERT_SIGN_ALG_NOT_SUPPORT://卡证书签名算法不支持
                                SoundPoolUtil.play(19);
                                BusToast.showToast(App.getInstance(), "卡证书签名算法不支持", false);
                                break;
                            case QRCode.EC_MAC_ROOT_KEY_DECRYPT_ERR://加密的mac根密钥解密失败
                                SoundPoolUtil.play(20);
                                BusToast.showToast(App.getInstance(), "加密的mac根密钥解密失败", false);
                                break;
                            case QRCode.EC_MAC_SIGN_ERR://mac校验失败
                                SoundPoolUtil.play(21);
                                BusToast.showToast(App.getInstance(), "mac校验失败", false);
                                break;
                            case QRCode.EC_QRCODE_SIGN_ALG_NOT_SUPPORT://二维码签名算法不支持
                                SoundPoolUtil.play(22);
                                BusToast.showToast(App.getInstance(), "二维码签名算法不支持", false);
                                break;
                            case QRCode.EC_SCAN_RECORD_ECRYPT_ERR://扫码记录加密失败
                                SoundPoolUtil.play(23);
                                BusToast.showToast(App.getInstance(), "扫码记录加密失败", false);
                                break;
                            case QRCode.EC_SCAN_RECORD_ECODE_ERR://扫码记录编码失败
                                SoundPoolUtil.play(24);
                                BusToast.showToast(App.getInstance(), "扫码记录编码失败", false);
                                break;
                            case QRCode.MY_QR_INSTALL_SUCCESS://小兵二维码验证成功
                                mHandler.sendEmptyMessage(QRCode.MY_QR_INSTALL_SUCCESS);
                                break;
                            case QRCode.EC_FAIL://系统异常
                                SoundPoolUtil.play(25);
                                BusToast.showToast(App.getInstance(), "系统异常", false);
                                break;
                            case QRCode.REFRESH_QR_CODE://请刷新二维码
                                SoundPoolUtil.play(26);
                                BusToast.showToast(App.getInstance(), "请刷新二维码", false);
                                break;

                            default:

                                break;
                        }
                        return false;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<QRScanMessage>() {
                    @Override
                    public void call(QRScanMessage qrScanMessage) {
                        if (qrScanMessage == null) return;
                        Map<String, Object> map = PosRequest.requestMap(qrScanMessage.getPosRecord());
                        mPresenter.requestPost(Config.FETCH_DEBIT_WHAT, map, Config.XBPAY);
                    }
                });

    }

    @Override
    public void onSuccess(int what, String str) {
        super.onSuccess(what, str);
        Log.d("HomeActivity",
                "onSuccess(HomeActivity.java:236)准实时扣款成功");
    }

    @Override
    public void onFail(int what, String str) {
        super.onFail(what, str);
        Log.d("HomeActivity",
                "onFail(HomeActivity.java:243)准实时扣款失败");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadScheduledExecutorUtil.shutdown();
        stopService(loopScanTaskIntent);
        stopService(timeSettleTaskIntent);
        if (sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);

        SoundPoolUtil.release();
    }

}
