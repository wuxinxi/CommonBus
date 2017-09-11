package szxb.com.commonbus.entity;

/**
 * 作者: Tangren on 2017-09-08
 * 包名：szxb.com.commonbus.entity
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class PosMessage {
    //验证二维码失败
    public static final int VERIFY_CODE_FAIL = -1;
    //验证成功
     public static final  int VERIFY_CODE_SUCCESS = 1;
    //二维码格式错误
     public static final  int QE_FORMAT_ERROR = 2;
    //二维码过期
     public static final  int QR_OVERDUE = 3;
    //无效二维码
     public static final  int QR_INVALID = 4;
    //小兵二维码验证失败
     public static final  int MY_QR_INSTALL_FAIL = 5;
    //小兵二维码验证成功
     public static final  int MY_QR_INSTALL_SUCCESS = 6;

}
