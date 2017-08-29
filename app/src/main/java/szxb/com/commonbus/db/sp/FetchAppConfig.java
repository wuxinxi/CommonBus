package szxb.com.commonbus.db.sp;


/**
 * 作者: Tangren on 2017/7/12
 * 包名：com.szxb.onlinbus.util
 * 邮箱：996489865@qq.com
 * TODO:获取全局的SP数据
 */

public class FetchAppConfig {

    //获取posId
    public static String posId() {
        return (String) CommonSharedPreferences.get("posId", "10001");
    }

    //获取appId
    public static String appId() {
        return (String) CommonSharedPreferences.get("appId", "10000002");
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

    //获取城市编码
    public static String cityCode() {
        return (String) CommonSharedPreferences.get("cityCode", "440300");
    }

    //获取计费类型0或1
    public static int chargeType() {
        return (Integer) CommonSharedPreferences.get("chargeType", 0);
    }

    //获取公钥1
    public static String key_1() {
        return (String) CommonSharedPreferences.get("key_0", "02C5F34D7F24886F657E7FE728D795D75A2632F2B79091B517BD79787E");
    }

    //获取公钥2
    public static String key_2() {
        return (String) CommonSharedPreferences.get("key_1", "03AE275B0BB13A9F189A2B0A93CD25A70B69737717428AEA35EC3A026F");
    }

    //获取保存公钥时间
    public static String saveKeyTime() {
        return (String) CommonSharedPreferences.get("save_key_time", "2017-07-13");
    }

    //获取公钥保存状态
    public static boolean saveState() {
        return (boolean) CommonSharedPreferences.get("save_state", true);
    }

    //司机卡号
    public static String saveDriver() {
        return (String) CommonSharedPreferences.get("DriverCard", "");
    }

    //连接蓝牙设备号
    public static String BluetoothDevice() {
        return (String) CommonSharedPreferences.get("BluetoothDevice", "Xperia Z5 Compact");
    }

    //票价
    public static float BusPrice() {
        return (float) CommonSharedPreferences.get("BusPrice", "2.00");
    }

    //线路号
    public static String LineName() {
        return (String) CommonSharedPreferences.get("LineName", "");
    }

    //车牌号
    public static String Plate() {
        return (String) CommonSharedPreferences.get("Plate", "");
    }

    //上次提交文件到服务器的时间，格式：yyyy-MM-dd HH:mm:ss
    public static String lastTimePushFile() {
        return (String) CommonSharedPreferences.get("lastTimePushFile", "");
    }

    //sn号
    public static String snNo() {
        return (String) CommonSharedPreferences.get("SnNo", "");
    }

}
