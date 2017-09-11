package szxb.com.commonbus.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 作者: Tangren on 2017/9/1
 * 包名：szxb.com.commonbus.entity
 * 邮箱：996489865@qq.com
 * TODO:type 1:验码通过交易/2：安装上报/4：验码失败
 *
 */

public class SendInfo {

    private JSONObject object;
    private int type;

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SendInfo(JSONObject object, int type) {
        this.object = object;
        this.type = type;
    }
}
