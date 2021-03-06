package szxb.com.commonbus.util.comm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import szxb.com.commonbus.App;

/**
 * 作者: Tangren on 2017/7/8
 * 包名：com.szxb.onlinbus.util
 * 邮箱：996489865@qq.com
 * TODO:通用的工具类
 */

public class Utils {

    /**
     * 随机字符串
     *
     * @param length
     * @return
     */
    public static String Random(int length) {
        char[] ss = new char[length];
        int i = 0;
        while (i < length) {
            int f = (int) (Math.random() * 5);
            if (f == 0)
                ss[i] = (char) ('A' + Math.random() * 26);
            else if (f == 1)
                ss[i] = (char) ('a' + Math.random() * 26);
            else
                ss[i] = (char) ('0' + Math.random() * 10);
            i++;
        }
        String is = new String(ss);
        return is;
    }

    /**
     * 检查字符串是否可以转化成数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        } else {
            char[] chars = str.toCharArray();
            int sz = chars.length;
            boolean hasExp = false;
            boolean hasDecPoint = false;
            boolean allowSigns = false;
            boolean foundDigit = false;
            int start = chars[0] == 45 ? 1 : 0;
            int i;
            if (sz > start + 1 && chars[start] == 48 && chars[start + 1] == 120) {
                i = start + 2;
                if (i == sz) {
                    return false;
                } else {
                    while (i < chars.length) {
                        if ((chars[i] < 48 || chars[i] > 57) && (chars[i] < 97 || chars[i] > 102) && (chars[i] < 65 || chars[i] > 70)) {
                            return false;
                        }

                        ++i;
                    }

                    return true;
                }
            } else {
                --sz;

                for (i = start; i < sz || i < sz + 1 && allowSigns && !foundDigit; ++i) {
                    if (chars[i] >= 48 && chars[i] <= 57) {
                        foundDigit = true;
                        allowSigns = false;
                    } else if (chars[i] == 46) {
                        if (hasDecPoint || hasExp) {
                            return false;
                        }

                        hasDecPoint = true;
                    } else if (chars[i] != 101 && chars[i] != 69) {
                        if (chars[i] != 43 && chars[i] != 45) {
                            return false;
                        }

                        if (!allowSigns) {
                            return false;
                        }

                        allowSigns = false;
                        foundDigit = false;
                    } else {
                        if (hasExp) {
                            return false;
                        }

                        if (!foundDigit) {
                            return false;
                        }

                        hasExp = true;
                        allowSigns = true;
                    }
                }

                return i < chars.length ? (chars[i] >= 48 && chars[i] <= 57 ? true : (chars[i] != 101 && chars[i] != 69 ? (chars[i] == 46 ? (!hasDecPoint && !hasExp ? foundDigit : false) : (allowSigns || chars[i] != 100 && chars[i] != 68 && chars[i] != 102 && chars[i] != 70 ? (chars[i] != 108 && chars[i] != 76 ? false : foundDigit && !hasExp && !hasDecPoint) : foundDigit)) : false)) : !allowSigns && foundDigit;
            }
        }
    }

    /**
     * 是否有网络
     *
     * @return boolean
     */
    public static boolean checkNetStatus() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        return current != null && current.isAvailable();
    }


    /**
     * 根据byte数组，生成文件
     *
     * @param bfile    文件数组
     * @param filePath 文件存放路径
     * @param fileName 文件名称
     */
    public static void byte2File(byte[] bfile, String filePath, String fileName) {

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && !dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }


    //打开目录文件
    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {

            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();

            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static String fen2Yuan(int prices) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format((float) prices / (float) 100);
    }


    public static int string2Integer(String var) {
        if (isNumber(var))
            return new Integer(var);
        return 0;
    }


    public static String bytesToHexString(byte[] src, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || len <= 0) {
            return null;
        }
        for (int i = 0; i < len; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String hex2IntString(String hex) {
        int i = Integer.parseInt(hex, 16);
        return String.valueOf(i);
    }


    public static String getResult2String(byte[] resultByte) {
        return hex2IntString(bytesToHexString(resultByte, resultByte.length));
    }


    public static String mode(int mode) {
        switch (mode) {
            case 0:
                return "正常请求模式";
            case 1:
                return "扫码模式";
            case 2:
                return ":本地补丁模式";
            default:
                return "未知模式";
        }
    }

    public static String code(int code) {
        switch (code) {
            case 1:
                return "补丁加载成功";
            case 6:
                return "服务端没有最新可用的补丁";
            case 11:
                return ":RSASECRET错误，官网中的密钥是否正确请检查";
            case 12:
                return ":当前应用已经存在一个旧补丁, 应用重启尝试加载新补丁";
            case 13:
                return ":补丁加载失败, 导致的原因很多种, 比如UnsatisfiedLinkError等异常, 此时应该严格检查logcat异常日志";
            case 16:
                return ":APPSECRET错误，官网中的密钥是否正确请检查";
            case 18:
                return ":一键清除补丁";
            case 19:
                return ":连续两次queryAndLoadNewPatch()方法调用不能短于3s";
            default:
                return "未知错误";
        }
    }
}
