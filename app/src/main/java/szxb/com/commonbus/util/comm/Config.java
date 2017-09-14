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

    //后台service扣款
    public static final int ROTATION_DEBIT = 3;

    //安装信息上班what
    public static final int REPORT_WHAT = 4;

    //上传流水 what
    public static final int POST_BILL_WHAT = 7;

    //黑名单下载接口
    public static final String BLACK_QUERY = "https://open-wlx.tenpay.com/cgi-bin/black/black_query.cgi";

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
    public static final String SC_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJQN0rdvPOak5zx+\n" +
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


    public static final String DES_KEY = "JPEf*7fU*Cal8Zag";


    //    private static final String IP = "http://120.24.212.72:8080";//139.199.158.253
    private static final String IP = "http://120.24.212.72:8080";//139.199.158.253

    //小兵mac
    public static final String MAC_KEY = IP + "/bipbus/interaction/getmackey";

    //小兵公钥
    public static final String PUBLIC_KEY = IP + "/bipbus/interaction/getpubkey";

    //小兵黑名单
    public static final String BLACK_LIST=IP +"/bipbus/interaction/blacklist";

    //流水上传
    public static final String POST_BILL = IP + "/bipbus/interaction/posjour";

    //安装上报
    public static final String POS_INSTALL = IP + "/bipbus/interaction/posinstall";

    public static final String XBPAY = "http://120.24.212.72:8080/bipbus/interaction/posrecv";

    public static final String AL_APPID = "24608754-1";

    public static final String AL_APPSECRET = "befbfc2db56404334af5081eeb5b26ca";

    public static final String AL_RSASECRET = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCgNEbwKe8fGWWczBPgLDwUH1mc24Y12w0rrmnNGWktOHy0Zew82Jv+/yuX/rsiJ/HkvoI7e57QsDNbGtKbjD5HosipG8qgWti1P4Cppi+mO82WlO2nkzAXz/TAuRdCUAPlvidFnD5ipT3agnlRpie46wu6ftWUNiEW53fqmp9EbwgVA0EcHakcZv4ApeT66zu+z5DkMBY5tmm4IMaD2AB59pZ5kydMvOL342MAfbpXvMhSzJC7m4yYbByzUmunV48Rk4r+Su25U4Ox9cZbza7CShP1hFV4cZwWjEI0Gyn97KPUTEgQt8rVNlGu8/+jGwk8y/d+bG37iTM8UebaonS5AgMBAAECggEAAjPVydV5wR6zQ+2WVF2nUcuAiuSFutXRH4mdUMGJraJ6LGy+S7PaqS3O5p8M6ZJNBA0Oc0zmyQ2zFwyOWq8Z6Bg31ANazMuS8SL3XN25B/sIf4p6nfQrFBi6z697v2CkPRNZLN7SCL8m04s5qcK0BxnMJns56ni4Cb2S63nGuMUccK5bTknynwUmFI0K09xeEPzB98ahdfbm0MKGbwaeRIKEiQ2pAzJm0vLOFI1TTzxhI1URUqnpyGJdMULFZ82+QuPHWQZONrQBRdZBGGcDmNHxn9Y0A1a5YOJZy25PcpKIe8mmX2vJxSUnZuMkNXWphBIehyTTa0QrR76LPhoqRQKBgQDNWmAESZWn851f6B5jGVGCbLMjjxPSMRFvIkn5JQd5T3KjIMghl0QfFpDurKrBNmmnYw3lsePJ3+xDpSIwQYJaD+T3T/qK5Vi/Z4ajarEkucKyvKnP6UQ4Uf6k5WwYxmjmzZdnse/WCbPkm07cB6bheieMOVK/gvpLJYcCB3q5XwKBgQDHt0ksKNWY5Kl7hHai3iKx1ST2svypA7YWz2uhdT74fz8ttxF3r2MqWeXouAGcD4NTBmqp+k7KTsMFxS18TMmKELq6GBvBKAUAkis/vIQ/EgkZrxmkF1mCkKJyd0jQwMEYI+8TxwK3cS2ZrFoaD2o3v1YdD0yqLs1Cd3I/2LjQ5wKBgQDLLOHXHB8QHz7UB0sZbZFx466SPhu0WYwQUvKxqOtuISchM2wPoCRsRd176CbCJ939GUpEuu7Pa9fNTfM3n3kjNpqp/t260xQtvHY/9W3zEkAKrBOve/JdbvLtn3iGQrjDsyJcHHdZZBwy0V8C4CdC66N5X7X5edO+yhB79357LQKBgQC57TQ93DYeBKKTzxnzPgHmtJuY38DASCg4zBlTUqexCCV1Fb05Qxp6zv5uzP8Sno6PuKWMYO8BAJomwQ47bfEii7iKju8nv24IvgttZr9nSA8Yzh582RAHsUbKcapkTU52ft2P9/HbfY4KRp4LLNah14r/usKQnMmSW0kNj2FfeQKBgF78kh4P8AsIlLSpLLyihXju4B2yO+mzbl9k3goReg9cBqVzoyS1ve863D/Vq48QMyV9NaHz64CY9ggXLB1aRc2W/aNdcx40V1C5dRZ1sawqjIOF+Rk8zVrBX9FtgIqq7wTSX1hVMz7M+djg1Xv+TJXZgkIhFL2l/XEMkxAvBlR0";

}
