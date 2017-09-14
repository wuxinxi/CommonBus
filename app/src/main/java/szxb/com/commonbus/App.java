package szxb.com.commonbus;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.szxb.xblog.AndroidLogAdapter;
import com.szxb.xblog.CsvFormatStrategy;
import com.szxb.xblog.DiskLogAdapter;
import com.szxb.xblog.FormatStrategy;
import com.szxb.xblog.PrettyFormatStrategy;
import com.szxb.xblog.XBLog;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;

import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.util.AppUtil;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.Utils;
import szxb.com.commonbus.util.crash.Cockroach;
import szxb.com.commonbus.util.sound.SoundPoolUtil;
import szxb.com.commonbus.util.tip.BusToast;

/**
 * 作者: Tangren on 2017/8/16
 * 包名：szxb.com.commonbus
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class App extends Application {

    private static App instance = null;
    public static final String DB_NAME = "BUS_INFO";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLog();
        DBCore.init(this, DB_NAME);
        NoHttp.initialize(InitializationConfig.newBuilder(this)
                .networkExecutor(new OkHttpNetworkExecutor())
                .build());
        Logger.setDebug(true);
        SoundPoolUtil.init(this);

        Cockroach.install(new Cockroach.ExceptionHandler() {
            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("App",
                                    "run(App.java:56)" + throwable.toString());
                            BusToast.showToast(App.this, "异常错误", true);
                        } catch (Throwable e) {

                        }
                    }
                });
            }
        });
    }

    public static App getInstance() {
        synchronized (App.class) {
            if (instance == null) {
                instance = new App();
            }
        }
        return instance;
    }

    private void initLog() {

        FormatStrategy format = PrettyFormatStrategy.newBuilder()
                .tag("公交日志信息:")
                .build();
        XBLog.addLogAdapter(new AndroidLogAdapter(format));

        FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
                .tag("公交日志信息:")
                .build();
        XBLog.addLogAdapter(new DiskLogAdapter(formatStrategy));

        //检查本地是否存在补丁
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        SophixManager.getInstance().setContext(this)
                .setAppVersion(AppUtil.getVersionName(base))
                .setAesKey(null)
                .setPatchLoadStatusStub(listener)
                .setSecretMetaData(Config.AL_APPID, Config.AL_APPSECRET, Config.AL_RSASECRET)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            Log.d("App",
                                "onLoad(App.java:116)补丁加载成功且立即生效");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                            Log.d("App",
                                "onLoad(App.java:121)补丁加载成功下次重启生效");
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            Log.d("App",
                                "onLoad(App.java:124)补丁加载失败"+code);
                        }
                    }
                }).initialize();

    }


    PatchLoadStatusListener listener = new PatchLoadStatusListener() {
        @Override
        public void onLoad(int mode, int code, String info, int version) {
            Log.d("App",
                "onLoad(App.java:136)"+mode+","+code+","+info);
            Log.d("App",
                    "onLoad(App.java:132)模式:" + Utils.mode(mode) + ",状态:" + Utils.code(code) + ",详情信息:" + info);
        }
    };
}
