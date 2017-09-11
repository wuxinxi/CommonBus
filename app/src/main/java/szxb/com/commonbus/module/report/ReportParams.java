package szxb.com.commonbus.module.report;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.Map;

import szxb.com.commonbus.App;
import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.entity.QRCode;
import szxb.com.commonbus.entity.QRScanMessage;
import szxb.com.commonbus.http.CallServer;
import szxb.com.commonbus.http.HttpListener;
import szxb.com.commonbus.http.JsonRequest;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.ParamsUtil;
import szxb.com.commonbus.util.rx.RxBus;
import szxb.com.commonbus.util.sign.ParamSingUtil;
import szxb.com.commonbus.util.sound.SoundPoolUtil;
import szxb.com.commonbus.util.tip.BusToast;

/**
 * 作者: Tangren on 2017/8/29
 * 包名：szxb.com.commonbus.module.report
 * 邮箱：996489865@qq.com
 * TODO:机具接收到
 */

public class ReportParams {

    public ReportParams() {
    }

    public void report(JSONObject object) {
        String timestamp = DateUtil.getCurrentDate();
        Map<String, Object> map = ParamsUtil.commonMap("97263905084014600", timestamp);
        map.put("sign", ParamSingUtil.getSign(FetchAppConfig.appId(), timestamp, object, Config.SC_KEY));
        map.put("biz_data", object.toString());
        JsonRequest request = new JsonRequest(Config.POS_INSTALL);
        request.set(map);
        request.setRetryCount(3);
        CallServer.getHttpclient().add(Config.REPORT_WHAT, request, new HttpListener<JSONObject>() {
            @Override
            public void success(int what, Response<JSONObject> response) {
                Log.d("ReportParams",
                        "success(ReportParams.java:42)" + response.get().toJSONString());
                if (response.get() != null) {
                    String retcode = response.get().getString("retcode");
                    if (retcode.equals("0")) {
                        RxBus.getInstance().send(new QRScanMessage(null, QRCode.MY_QR_INSTALL_SUCCESS));
                        SoundPoolUtil.play(2);
                        BusToast.showToast(App.getInstance(), "机具信息上报成功!", true);
                    } else {
                        BusToast.showToast(App.getInstance(), "机具信息上报失败!", false);
                        SoundPoolUtil.play(3);
                    }
                }
            }

            @Override
            public void fail(int what, String e) {
                Log.d("ReportParams",
                        "fail(ReportParams.java:62)" + e);
                SoundPoolUtil.play(3);
                BusToast.showToast(App.getInstance(), "机具信息上报错误!\n" + e, false);
            }
        });

    }
}
