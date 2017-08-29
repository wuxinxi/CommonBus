package szxb.com.poslibrary.base;

import com.alibaba.fastjson.JSONObject;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.CacheMode;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.Map;

import szxb.com.poslibrary.http.CallServer;
import szxb.com.poslibrary.http.HttpListener;
import szxb.com.poslibrary.http.JsonRequest;

/**
 * 作者：Tangren on 2017/6/9 13:16
 * 邮箱：wu_tangren@163.com
 * TODO:一句话描述
 */
public abstract class BasePresenter {

    private JsonRequest request;

    private void requestPost(int what, Map<String, Object> map, String url) {
        request = new JsonRequest(url, RequestMethod.POST);
        request.setCancelSign(what);
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        request.set(map);
        CallServer.getHttpclient().add(what, request, new HttpListener<JSONObject>() {
            @Override
            public void success(int what, Response<JSONObject> response) {
                if (response.get() != null)
                    onAllSuccess(what, response.get());
            }

            @Override
            public void fail(int what, String e) {
                onFail("网络或服务器异常!" + e);
            }
        });

    }

    protected abstract void onAllSuccess(int what, JSONObject result);

    protected abstract void onFail(String failStr);

    protected void cancel(int what) {
        if (request != null)
            request.cancelBySign(what);
    }

}
