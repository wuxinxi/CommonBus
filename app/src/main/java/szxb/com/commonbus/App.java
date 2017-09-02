package szxb.com.commonbus;

import android.app.Application;

import com.szxb.xblog.AndroidLogAdapter;
import com.szxb.xblog.CsvFormatStrategy;
import com.szxb.xblog.DiskLogAdapter;
import com.szxb.xblog.FormatStrategy;
import com.szxb.xblog.PrettyFormatStrategy;
import com.szxb.xblog.XBLog;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;

import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.manager.MyActivityLifecycleCallbacks;

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
        this.registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());

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

    }

}
