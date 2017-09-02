package szxb.com.commonbus.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import szxb.com.commonbus.module.home.HomeActivity;


/**
 * 作者: Tangren on 2017/7/12
 * 包名：com.szxb.onlinbus.task
 * 邮箱：996489865@qq.com
 * TODO:开机自起
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent mainActivityIntent = new Intent(context, HomeActivity.class);
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }
    }
}
