package szxb.com.commonbus.interfaces;

import java.util.List;

import szxb.com.commonbus.entity.MacKeyEntity;
import szxb.com.commonbus.entity.PosRecord;
import szxb.com.commonbus.entity.PublicKeyEntity;

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

    List<MacKeyEntity> getMacList();

    String getMac(String keyId);

    void setMacList(List<MacKeyEntity> list);

    List<PublicKeyEntity> getPublicKeyList();

    String getPublicKey(String keyId);

    void setPublicKeyList(List<PublicKeyEntity> list);

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


    void dowithPosRecordReply(PosRecord var1, int var2);
}
