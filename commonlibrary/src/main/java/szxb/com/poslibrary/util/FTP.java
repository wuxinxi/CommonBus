package szxb.com.poslibrary.util;

import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 作者: Tangren on 2017/7/27
 * 包名：com.szxb.ftp
 * 邮箱：996489865@qq.com
 * TODO:FTP工具类
 */

public class FTP {

    private String url;
    private int port = 21;
    private String username;
    private String password;
    private String path;
    private String[] fileName;
    private List<InputStream> input;

    public FTP builder(String url) {
        this.url = url;
        return this;
    }

    public FTP setPort(int port) {
        this.port = port;
        return this;
    }

    public FTP setLogin(String username, String psw) {
        this.username = username;
        this.password = psw;
        return this;
    }

    public FTP setPath(String path) {
        this.path = path;
        return this;
    }

    public FTP setFileName(String[] fileName) {
        this.fileName = fileName;
        return this;
    }

    public FTP setInput(List<InputStream> input) {
        this.input = input;
        return this;
    }

    public boolean build() {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        int reply;
        ftp.setConnectTimeout(3000);
        try {
            ftp.connect(url, port);// 连接FTP服务器

            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            //连接的状态码
            reply = ftp.getReplyCode();
            ftp.setDataTimeout(12000);
            //判断是否连接上ftp
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return false;
            }
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            //对传入的路径进行拆分，ftp里创建路径要一层一层的创建。
            String[] argPath = path.split("/");
            System.out.println("path:" + path);
            String pathName = null;
            //一层一层的开始创建路径
            for (int i = 0; i < argPath.length; i++) {
                pathName = argPath[i];
                ftp.makeDirectory(pathName);
                //创建一层就切换到该目录下
                ftp.changeWorkingDirectory(pathName);
            }

            Log.d("FTP",
                "build(FTP.java:96)FTP创建目录成功");
            //保存文件到路径下。
            for (int i = 0; i < input.size(); i++) {
                ftp.storeFile(fileName[i], input.get(i));
                input.get(i).close();
                Log.d("FTP",
                        "build(FTP.java:100)提交第" + (i + 1) + "个");
            }
            Log.i(TAG, "FTP上传文件成功");
            //关闭流，退出ftp
            ftp.logout();
            success = true;
            //判断是否退出成功，不成功就再断开连接。
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    throw new RuntimeException("FTP disconnect fail!", ioe);
                }
            }
        } catch (IOException e) {
            Log.d("FTP",
                "build(FTP.java:117)"+e.getMessage());
            e.printStackTrace();
        }
        return success;
    }

}
