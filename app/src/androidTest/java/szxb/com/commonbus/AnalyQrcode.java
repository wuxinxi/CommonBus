package szxb.com.commonbus;

import android.support.test.runner.AndroidJUnit4;
import android.util.Base64;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 作者: Tangren on 2017-09-09
 * 包名：szxb.com.commonbus
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
@RunWith(AndroidJUnit4.class)
public class AnalyQrcode {
    @Test
    public void analy() {

        String qrcode = "AChPN3NNSTdDT3ZkVk4xT2IyVTZoZzA3cU1wdENTODU1VVk5NmRSWDkAAQUAAQExLQUgBWmy7YkWQ1mzetBddeHQGUAAAAH0d2VhYmNkAgH2dM2g0Nk1G7fe4SM+H3SGcq9jvHK857ZOrDDLy3tSZXoalMbSn4qvekAPourTrjztgpt2zbhhHjBZs3rQdHl1dnZuYm3tu0Fg";
        byte[] data = Base64.decode(qrcode, Base64.NO_WRAP);
        String string = new String(data);
        Log.d("AnalyQrcode",
                "analy(AnalyQrcode.java:24)" + string);

        byte[] bytes = qrcode.getBytes();
        byte[] appid = new byte[16];
        System.arraycopy(bytes, 10, appid, 0, 16);
        Log.d("AnalyQrcode",
            "analy(AnalyQrcode.java:31)"+appid.length);
        Log.d("AnalyQrcode",
            "analy(AnalyQrcode.java:33)"+new String(appid));

    }

}
