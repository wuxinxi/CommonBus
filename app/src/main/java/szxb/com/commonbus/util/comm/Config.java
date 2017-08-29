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
    public static final int FETCH_KEYLIST_WHAT = 1;

    //获取黑名单列表的what
    public static final int FETCH_BLACKLIST_WHAT = 2;

    //后台service扣款
    public static final int ROTATION_DEBIT = 3;

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

    //公钥
    public static final String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/yZX6DqUjPUvTptbeqsptfhQI\n" +
            "w/qBKoxFFbd9y9g0nmI3ARKcPTpdoC3oDNeqDMd17fkcG1Vph6b6fdQAGQcyMPZb\n" +
            "t/C8MRMF/PfY/xGHmO48A4HlYCQY+e2RGqtM91961UrWORvQg5+/h5lfEMVgQkbG\n" +
            "xm+qznZXBewUMgUdxQIDAQAB";

    //扫码成功->发起扣款
    public static final int RESULT_CODE = 0x10;

    //时间
    public static final int TIME_CODE = 0x20;

    public static final String ACTION = "com.szxb.onlineBus";


}
