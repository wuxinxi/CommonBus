package szxb.com.commonbus.util.crash;

/**
 * 作者：Tangren on 2017/5/11 17:50
 * 邮箱：wu_tangren@163.com
 * TODO:一句话描述
 */
public class QuitCockroachException extends RuntimeException {
    public QuitCockroachException(String message) {
        super(message);
    }
}
