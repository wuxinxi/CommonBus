package szxb.com.commonbus;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Base64;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import szxb.com.commonbus.util.sign.RSA;

import static szxb.com.commonbus.util.comm.Config.private_key;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        String mac = "O+bM5zNSHR0P1e4WX6lDb4p0Ug/9Mx0U+OjR1DWoXESiPWpf71A4nuhykb8Nvke9tWAVtYecawXM54fLl7cg/Cp/w4uLTeCp07f4O8jACsIQYo+onrsM0G1C++kRuMaMgb7o65xphKiy+K05BKAP6vvlItXFdWzlLaOS6JPMNZs=";
        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey(private_key);
        byte[] key = Base64.decode(private_key, Base64.NO_WRAP);
        String s1 = RSA.RSADecrypt(mac,key);
        Log.d("ExampleInstrumentedTest",
            "useAppContext(ExampleInstrumentedTest.java:46)"+s1);
    }

    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        return keyFactory.generatePrivate(keySpec);
    }


    public static byte[] decryptByPrivateKey(byte[] encryptedData, PrivateKey privateKey) {
        int MAX_ENCRYPT_BLOCK = 128;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int inputLen = encryptedData.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptData = out.toByteArray();
            out.close();
            return encryptData;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("ExampleInstrumentedTest",
                    "decryptByPrivateKey(ExampleInstrumentedTest.java:78)" + e.toString());
            return null;
        }
    }



    public static byte[] hexStringToByte(String hex) {
        hex=hex.toUpperCase();
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
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


    public static String byte2hex(byte[] b) {

        if(b==null) return null;
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }


}
