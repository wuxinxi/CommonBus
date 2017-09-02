package szxb.com.commonbus.module.init;

import android.content.Intent;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.Map;

import szxb.com.commonbus.R;
import szxb.com.commonbus.base.BaseMvpActivity;
import szxb.com.commonbus.db.sp.FetchAppConfig;
import szxb.com.commonbus.module.home.HomeActivity;
import szxb.com.commonbus.util.comm.Config;
import szxb.com.commonbus.util.comm.DateUtil;
import szxb.com.commonbus.util.sign.ParamSingUtil;
import szxb.com.commonbus.util.tip.BusToast;

import static szxb.com.commonbus.util.comm.ParamsUtil.commonMap;

/**
 * 作者: Tangren on 2017/8/31
 * 包名：szxb.com.commonbus.module.init
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */

public class InitActivity extends BaseMvpActivity<InitPresenter> {


    private boolean initKeySuccess = false;//初始化公钥是否完成

    private boolean initBlackListSuccess = false;//初始化黑名单是否完成

    private boolean initMacKeySuccess = false;//初始化Mac是否完成

    private CircleProgressBar progressBar;

    @Override
    protected InitPresenter getChildPresenter() {
        return new InitPresenter(this);
    }

    @Override
    protected int rootView() {
        return R.layout.activity_init;
    }

    @Override
    protected void initView() {
        super.initView();
        progressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 公钥/MAC下载
     *
     * @return map
     */
    private Map<String, Object> getkeyMap() {
        String timestamp = DateUtil.getCurrentDate();
        String app_id = FetchAppConfig.appId();
        Map<String, Object> map = commonMap(app_id, timestamp);
        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, null, Config.private_key));
        map.put("biz_data", "");
        return map;
    }


    /**
     * @return 黑名单下载列表map
     */
    public static Map<String, Object> getBlackListMap() {
        String timestamp = DateUtil.getCurrentDate();
        String app_id = FetchAppConfig.appId();
        Map<String, Object> map = commonMap(app_id, timestamp);
        JSONObject object = new JSONObject();
        object.put("page_index", 1);
        object.put("page_size", 100);

        object.put("begin_time", DateUtil.getLastDate("yyyy-MM-dd HH:mm:ss"));
        object.put("end_time", DateUtil.getCurrentDate());

        map.put("sign", ParamSingUtil.getSign(app_id, timestamp, object, Config.private_key));
        map.put("biz_data", object.toString());
        return map;
    }

    @Override
    protected void initData() {
        //判断保存状态,根据保存状态判断是不是第一次进入程序
        if (FetchAppConfig.saveState()) {
            mPresenter.requestPost(Config.MAC_WHAT, getkeyMap(), Config.MAC_KEY);//获取mac秘钥
            request();
        } else {
            //每次开机请求公钥、下载黑名单
            request();
        }

    }

    private void request() {
        mPresenter.requestPost(Config.PUBKEY_WHAT, getkeyMap(), Config.PUBLIC_KEY);//获取公钥
        mPresenter.requestPost(Config.BLACK_WHAT, getBlackListMap(), Config.BLACK_QUERY);//黑名单下载
    }

    @Override
    public void onSuccess(int what, String str) {
        if (what == Config.PUBKEY_WHAT) {
            initKeySuccess = true;
            BusToast.showToast(getApplicationContext(), str, true);
        } else if (what == Config.MAC_WHAT) {
            initMacKeySuccess = true;
            BusToast.showToast(getApplicationContext(), str, true);
        } else if (what == Config.BLACK_WHAT) {
            initBlackListSuccess = true;
            BusToast.showToast(getApplicationContext(), str, true);
        }
        if (initBlackListSuccess && initMacKeySuccess && initKeySuccess) {
            startActivity(new Intent(InitActivity.this, HomeActivity.class));
            finish();
        }

    }

    @Override
    public void onFail(int what, String str) {

        BusToast.showToast(getApplicationContext(), str, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressBar.setVisibility(View.GONE);
    }
}