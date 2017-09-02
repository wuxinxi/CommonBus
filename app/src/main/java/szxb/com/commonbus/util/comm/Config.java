package szxb.com.commonbus.util.comm;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 作者: Tangren on 2017/7/8
 * 包名：com.szxb.onlinbus.util
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class Config {

    //扣款what
    public static final int FETCH_DEBIT_WHAT = 0;

    //获取公钥的what
    public static final int PUBKEY_WHAT = 1;

    //获取黑名单列表的what
    public static final int FETCH_BLACKLIST_WHAT = 2;

    //后台service扣款
    public static final int ROTATION_DEBIT = 3;

    //安装信息上班what
    public static final int REPORT_WHAT = 4;

    //MAC what
    public static final int MAC_WHAT = 5;

    //黑名单 what
    public static final int BLACK_WHAT = 6;

    //上传流水 what
    public static final int POST_BILL_WHAT = 7;

    //扣款接口
    public static final String URL = "https://open-wlx.tenpay.com/cgi-bin/wlx_order/wlx_order_recv.cgi";

    //黑名单下载接口
    public static final String BLACK_QUERY = "https://open-wlx.tenpay.com/cgi-bin/black/black_query.cgi";

    //对账单
    public static final String BILL_QUERY = "https://open-wlx.tenpay.com/cgi-bin/wlx_order/wlx_bill_query.cgi";

    //公钥接口
    public static final String getPublic_key_url = "https://open-wlx.tenpay.com/cgi-bin/key/pub_key_query.cgi";

    //mac接口
    public static final String getMac_key_url = "https://open-wlx.tenpay.com/cgi-bin/key/mac_key_query.cgi";

    //机具安装上报信息接口--盛灿
    public static final String REPORT_URL = "http://web.dev.bus.dkh.snsshop.net/api/bus/update";

    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm", new Locale("zh", "CN"));

    //私钥
    public static final String private_key = "MIICXAIBAAKBgQCrnFUPueWNY3HLcVf55kXzJDb+ftYINmhde+4EMbKjPY38xaZQ\n" +
            "k+OjeXykbo8XgIi/xBpRvogWyOwZKOr4kdnV/PdLSoXCrr3DoTRU9INFiOKZPxFY" +
            "8nYmH6KI4c/z5ooeats8+1bwN5lZdXwXWL/MJA7JrSSSUt0qCwy9MI7+OQIDAQAB" +
            "AoGAIL37HL0DJy7KD17Ywj1FK1bFh1j7zSVUVEHI79PrmWmtJYUwbj9JN29+cIEH" +
            "nBxR+wSXYPFRVceQBFziN/rb7MAS0qNmBxcSzJfqjenoHPZa9smZXpX6W1zHuFTd" +
            "IloV8juM7ssQyRNRNLSIDs2zZBNXHV6eDqW0cdIJuWaKyYECQQDTkZpgv6531pby" +
            "trtWrdgIIjC55YsLZKWv3VqCfvHbhodETA+1EL9y/BV0F0yXE8oDlMbIR99uuU4X" +
            "24/q93mlAkEAz6Z+1KGqy2twmQ1JRO/8B4zfqgUlitYu41dWu+iHDfTC2ex84BRQ" +
            "dXVND2HGiz/fRB3yubc/WAnToLv/kdTGBQJAcDQnQKpH2CyJj52Ty0uVZ/LiDqUL" +
            "UfaF3LgzWUQD9t3o/TKtneSM9Gl240O8Dd+j4rRTnEJp3+oM3aBHOmEXNQJBAJR5" +
            "K/7FieXhcKU/BsCwB7kuVU6wV2OqOeR8Mpwxaz/jXt+LZM6kN9OEiBETjG9MwEto" +
            "ToHUMQq2HAe15MtVJDECQF7lh83AMlL31AtdmFkaHvqu8vrwYiDwqlam+dGADWPG" +
            "+Cpn7fcXw0wBqRLLMLymz6IAp2mJCN+N7W76j8GP08E=";

    //盛灿私钥
    public static final String SC_KEY  = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJQN0rdvPOak5zx+\n" +
            "Vs1kA90zMWlLiYUQDLgQuCleYDUkG5MMdrALciX2SBoUSHBzXz8XqKI5+o7aBmX+\n" +
            "Bu2vD8Z6OdD7xv62MIwlMxfw5I+fRzFLcSTdfOlP/NNFrwNc555gAfjbWyEkDFkR\n" +
            "ISYkmsu/EladoKD/XK3lOZJ5D+xZAgMBAAECgYA+zzp0vYlNo+nBnSuACQ0mm2A7\n" +
            "9mLKA4wRzm4Chtoz0TSthp0XhFORzVC7V51/r0JaBtRwtj4Yul/6hvEzhpAfE2vl\n" +
            "iR7HvS8Ktqkl9EBlkPzHCMpfSyytX+knK7x0grKsrajNI1toZGWdmaORidZGzY2z\n" +
            "t8btdjgrhEXxOsZhfQJBAMSP5G1OJOD53y8CLk5jSRO3xizpInkro/xK+s/3m64s\n" +
            "PUKfU3+cMC/AjvNHWjRMsBVrDceHem2jc7cqusFPoK8CQQDA0tySaPgSMqJpl3iK\n" +
            "+eptq0fSNvHLbyBJ1BaHkHRVGmO3rF+U2LvwQPrSmrkBCX78sUgRUzY65h3vneNN\n" +
            "uDV3AkBoo1Eu/xKS0XIGTFrqT+BvJr3Q2qsHZjv96sxqOZ4esl5KQRbqL/NW+GMh\n" +
            "DzLt9IUoYb0MIwsBoqnPMGgK0KDxAkBolXXhS2HRrPj+QJO2/VozZYUs9XQsHPfs\n" +
            "U0Zs/OK8DfYr9yhYeT1mUDg65oSVlWr078rg6rstMwblokNZMiJ9AkEAjwZKPB18\n" +
            "wynX1FaKVt0aTRYfTvGdeaN8jbPeJwB35d7RWyR9/o7z/jM6cUn8iIUCO9qoyNs6\n" +
            "FMhpN2NhLYygyQ==";


    //公钥
    public static final String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/yZX6DqUjPUvTptbeqsptfhQI\n" +
            "w/qBKoxFFbd9y9g0nmI3ARKcPTpdoC3oDNeqDMd17fkcG1Vph6b6fdQAGQcyMPZb\n" +
            "t/C8MRMF/PfY/xGHmO48A4HlYCQY+e2RGqtM91961UrWORvQg5+/h5lfEMVgQkbG\n" +
            "xm+qznZXBewUMgUdxQIDAQAB";

    public static final String DES_KEY = "JPEf*7fU*Cal8Zag";


    private static final String IP = "http://139.199.158.253";

    //小兵mac
    public static final String MAC_KEY = IP + "/bipbus/interaction/getmackey";

    //小兵公钥
    public static final String PUBLIC_KEY = IP + "/bipbus/interaction/getpubkey";

    //流水上传
    public static final String POST_BILL = IP + "";

    //安装上报
    public static final String POS_INSTALL = IP + "/bipbus/interaction/posinstall";

}
