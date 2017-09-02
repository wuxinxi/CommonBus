package szxb.com.commonbus;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tencent.wlxsdk.WlxSdk;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import szxb.com.commonbus.util.comm.Utils;
import szxb.com.commonbus.util.test.TestConstant;

/**
 * 作者: Tangren on 2017/8/30
 * 包名：szxb.com.commonbus
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
@RunWith(AndroidJUnit4.class)
public class VerifyCodeTest {
    private WlxSdk wxSdk;

    @Before
    public void init() {
        wxSdk = new WlxSdk();
    }

    @Test
    public void verify() {
        String codeResult = "TXAChoODF0bENYZDA4bTJPcjRiTFJHZDljMzU4MHh2Zmk0ZFgwSHE2MHMAAQUAAQExLQUgBWmy7YkWQ1mmeopdaOGKGUAAAAH0d2VhYmNkAgFH/VAbqJt0lIbHokXJJodBVhLYE0qUVNojsjqYR9iSBa1LixPGtiGiwXXE9KzfAq/qFw6DKbptGDBZpnqKdHl1dnZuYm0Vwie3";
        int init = wxSdk.init(codeResult);
        Log.d("VerifyCodeTest",
            "verify(VerifyCodeTest.java:35)init="+init);
        int key_id = wxSdk.get_key_id();
        Log.d("VerifyCodeTest",
                "verify(VerifyCodeTest.java:32)key_id=" + key_id);
        String open_id = wxSdk.get_open_id();
        Log.d("VerifyCodeTest",
                "verify(VerifyCodeTest.java:36)open_id=" + open_id);
        String mac_root_id = wxSdk.get_mac_root_id();
        Log.d("VerifyCodeTest",
                "verify(VerifyCodeTest.java:39)get_mac_root_id=" + mac_root_id);

        int verify = wxSdk.verify("200569B2ED891643", "0452688EA88A887678051E57342EE0422E639803E4673678368400073B5002C465864274D080A777F00C73C6EAE585BD7E2E7A82BB84DCEF33", TestConstant.tickPrice, TestConstant.scane, TestConstant.scaneType, TestConstant.pos_id, Utils.Random(12),"A4E224B9B419422505975EAEA1300212");

        Log.d("VerifyCodeTest",
            "verify(VerifyCodeTest.java:47)verify="+verify);
    }

}
