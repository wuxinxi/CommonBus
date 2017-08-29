package szxb.com.commonbus;

import com.szxb.xblog.AndroidLogAdapter;
import com.szxb.xblog.CsvFormatStrategy;
import com.szxb.xblog.DiskLogAdapter;
import com.szxb.xblog.FormatStrategy;
import com.szxb.xblog.PrettyFormatStrategy;
import com.szxb.xblog.XBLog;

import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.poslibrary.LibApp;

/**
 * 作者: Tangren on 2017/8/16
 * 包名：szxb.com.commonbus
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class App extends LibApp {

    private static App instance = null;
    public static final String DB_NAME = "BUS_INFO";

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        initLog();
        DBCore.init(this, DB_NAME);

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
