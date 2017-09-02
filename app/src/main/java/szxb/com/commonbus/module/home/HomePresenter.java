package szxb.com.commonbus.module.home;

import com.alibaba.fastjson.JSONObject;

import java.lang.ref.WeakReference;

import szxb.com.commonbus.base.BasePresenter;


/**
 * 作者: Tangren on 2017/9/1
 * 包名：szxb.com.commonbus.module.home
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class HomePresenter extends BasePresenter {

    private WeakReference<HomeActivity> weakReference;

    public HomePresenter(HomeActivity activity) {
        weakReference = new WeakReference<HomeActivity>(activity);
    }

    @Override
    protected void onAllSuccess(int what, JSONObject result) {
        HomeActivity activity = weakReference.get();
        if (activity != null) {

        }
    }

    @Override
    protected void onFail(int what, String failStr) {

    }
}
