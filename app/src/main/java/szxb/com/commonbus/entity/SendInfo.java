package szxb.com.commonbus.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 作者: Tangren on 2017/9/1
 * 包名：szxb.com.commonbus.entity
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class SendInfo {

    private JSONObject object;
    private boolean transaction;

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    public boolean isTransaction() {
        return transaction;
    }

    public void setTransaction(boolean transaction) {
        this.transaction = transaction;
    }

    public SendInfo(JSONObject object, boolean transaction) {
        this.object = object;
        this.transaction = transaction;
    }
}
