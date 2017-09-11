package szxb.com.commonbus.module.home;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.ref.WeakReference;

import szxb.com.commonbus.base.BasePresenter;
import szxb.com.commonbus.db.manager.DBCore;
import szxb.com.commonbus.db.table.ScanInfoEntityDao;
import szxb.com.commonbus.entity.ScanInfoEntity;


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
            String retcode = result.getString("retcode");
            if (retcode.equals("0")) {
                String retmsg = result.getString("retmsg");
                if (retmsg.equals("ok")) {
                    JSONArray result_list = result.getJSONArray("result_list");
                    JSONObject resultObject = result_list.getJSONObject(0);
                    if (resultObject.getString("status").equals("00") || resultObject.getString("status").equals("91")) {
                        String mch_trx_id = resultObject.getString("mch_trx_id");
                        ScanInfoEntityDao dao = DBCore.getDaoSession().getScanInfoEntityDao();
                        ScanInfoEntity entity = dao.queryBuilder().where(ScanInfoEntityDao.Properties.Mch_trx_id.eq(mch_trx_id)).build().unique();
                        if (entity != null) {
                            entity.setStatus(true);
                            dao.update(entity);
                        }
                        activity.onSuccess(what, "实时扣款成功");
                        Log.d("HomeActivity",
                                "success(HomeActivity.java:108)扣款成功-修改成功!");
                    }
                } else {
                    //准实时扣款失败
                    activity.onFail(what, "实时扣款失败");
                }
            }
        }
    }

    @Override
    protected void onFail(int what, String failStr) {
        HomeActivity activity = weakReference.get();
        if (activity != null)
            activity.onFail(what, "网络或服务器异常/n实时扣款失败");
    }
}
