package szxb.com.commonbus.interfaces;

import szxb.com.commonbus.entity.PosRecord;

/**
 * 作者: Tangren on 2017-09-08
 * 包名：szxb.com.commonbus.interfaces
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public interface IPosManage {

    void loadFromPrefs();

    String getLineName();

    void setLineName(String var1);

    String getLineStart();

    void setLineStart(String var1);

    String getLineEnd();

    void setLineEnd(String var1);

    String getDriverNo();

    void setDriverNo(String var1);

    int getMarkedPrice();

    void setMarkedPrice(int var1);

    String getMac(String keyId);

    String getPublicKey(String keyId);


    long getOrderTime();

    String getmchTrxId();

    String geCityCode();

    int getExpType();

    int getChargeType();

    int getInStationId();

    String getInStationName();

    String getOrderDesc();

    void setOrderDesc(String var1);

    byte[] getKey();

    void setKey(String privateKey);

    String getBusNo();

    void setBusNo(String bus_no);

    String getAppId();

    void setAppId(String app_id);


    void dowithPosRecordReply(PosRecord var1, int var2);
}
