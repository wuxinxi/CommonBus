package szxb.com.commonbus.manager.report;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import szxb.com.commonbus.App;
import szxb.com.commonbus.db.sp.CommonSharedPreferences;
import szxb.com.commonbus.module.report.ReportParams;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.comm.Des;
import szxb.com.commonbus.util.comm.Utils;
import szxb.com.commonbus.util.sound.SoundPoolUtil;

/**
 * 作者: Tangren on 2017-09-08
 * 包名：szxb.com.commonbus.util.report
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class XBPosReportManager {

    private Des des;
    private ReportParams reportParam;

    private static XBPosReportManager instance = null;

    private XBPosReportManager() {
        des = new Des();
        reportParam = new ReportParams();
    }

    public static XBPosReportManager getInstance() {
        synchronized (XBPosReportManager.class) {
            if (instance == null) {
                instance = new XBPosReportManager();
            }
        }
        return instance;
    }

    public void posScan(String qrcode) {
        if (qrcode.length() < 56) return;
        String date = qrcode.substring(7, 17);
        Log.d("XBPosReportManager",
                "posScan(XBPosReportManager.java:48)" + date);
        Log.d("XBPosReportManager",
                "posScan(XBPosReportManager.java:48)" + DateUtil.times(date));
        if (TextUtils.equals(DateUtil.times(date), DateUtil.getCurrentDate("yyyy-MM-dd"))) {
            SoundPoolUtil.play(1);
            JSONObject object = new JSONObject();
            String data = des.strDec(qrcode.substring(17, qrcode.length()), Config.DES_KEY);
            String params[] = data.split(",");
            String bus_no = params[0];
            String prices = params[1];
            String start_station = params[2];
            String end_station = params[3];
            String line_name = params[4];

            Log.d("XBPosReportManager",
                    "posScan(XBPosReportManager.java:58)bus_no=" + bus_no);
            Log.d("XBPosReportManager",
                    "posScan(XBPosReportManager.java:60)prices=" + prices);
            Log.d("XBPosReportManager",
                    "posScan(XBPosReportManager.java:62)start_station=" + start_station);
            Log.d("XBPosReportManager",
                    "posScan(XBPosReportManager.java:64)end_station=" + end_station);
            Log.d("XBPosReportManager",
                    "posScan(XBPosReportManager.java:67)line_name=" + line_name);

            //配置参数
            CommonSharedPreferences.put("busNo", bus_no);
            CommonSharedPreferences.put("ticketPrice", Utils.string2Integer(prices));
            CommonSharedPreferences.put("startStationName", start_station);
            CommonSharedPreferences.put("endStationName", end_station);
            CommonSharedPreferences.put("lineName", line_name);


            App.getPosManager().setBusNo(bus_no);
            App.getPosManager().setLineStart(start_station);
            App.getPosManager().setLineEnd(end_station);
            App.getPosManager().setLineName(line_name);
            App.getPosManager().setMarkedPrice(Utils.string2Integer(prices));


            object.put("bus_no", params[0]);
            object.put("is_set_pos", "1");
            object.put("pos_no", App.getPosManager().getDriverNo());
            object.put("is_online", "1");

            object.put("bus_line_name", line_name);
            object.put("line_start", start_station);
            object.put("line_end ", end_station);
            object.put("total_fee  ", prices);
            object.put("pay_fee ", prices);
            reportParam.report(object);
        }

    }

}
