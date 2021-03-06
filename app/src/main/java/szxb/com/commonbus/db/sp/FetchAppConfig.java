package szxb.com.commonbus.db.sp;


/**
 * 作者: Tangren on 2017/7/12
 * 包名：com.szxb.onlinbus.util
 * 邮箱：996489865@qq.com
 * TODO:获取全局的SP数据
 */

public class FetchAppConfig {

    //获取appId
    public static String appId() {
        return (String) CommonSharedPreferences.get("appId", "20000007");
    }

    //获取起始站
    public static String startStationName() {
        return (String) CommonSharedPreferences.get("startStationName", "未设置");
    }

    //获取终点站
    public static String endStationName() {
        return (String) CommonSharedPreferences.get("endStationName", "未设置");
    }

    //获取票价
    public static int ticketPrice() {
        return (Integer) CommonSharedPreferences.get("ticketPrice", 0);
    }

    //车牌号
    public static String busNo() {
        return (String) CommonSharedPreferences.get("busNo", "00");
    }

    //获取城市编码
    public static String cityCode() {
        return (String) CommonSharedPreferences.get("cityCode", "440300");
    }

    //获取计费类型0或1
    public static int chargeType() {
        return (Integer) CommonSharedPreferences.get("chargeType", 0);
    }

    //获取保存公钥时间
    public static String saveKeyTime() {
        return (String) CommonSharedPreferences.get("save_key_time", "2017-08-31");
    }

    //获取公钥保存状态
    public static boolean saveState() {
        return (boolean) CommonSharedPreferences.get("save_state", true);
    }


    //线路号
    public static String lineName() {
        return (String) CommonSharedPreferences.get("lineName", "未设置");
    }

    //线路号
    public static String orderDesc() {
        return (String) CommonSharedPreferences.get("orderDesc", "扫码乘车");
    }

    //线路ID
    public static String busLineId() {
        return (String) CommonSharedPreferences.get("busLineId", "未设置");
    }

    //上次提交文件到服务器的时间，格式：yyyy-MM-dd HH:mm:ss
    public static String lastTimePushFile() {
        return (String) CommonSharedPreferences.get("lastTimePushFile", "");
    }

    //sn号
    public static String snNo() {
        return (String) CommonSharedPreferences.get("snNo", "");
    }

}
