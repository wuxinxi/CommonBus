package szxb.com.commonbus.module.init;

import android.content.Intent;
import android.view.View;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import szxb.com.commonbus.R;
import szxb.com.commonbus.base.BaseActivity;
import szxb.com.commonbus.interfaces.InitOnListener;
import szxb.com.commonbus.module.home.HomeActivity;

/**
 * 作者: Tangren on 2017/8/31
 * 包名：szxb.com.commonbus.module.init
 * 邮箱：996489865@qq.com
 * TODO:初始化
 */

public class InitZipActivity extends BaseActivity implements InitOnListener {

    private CircleProgressBar progressBar;
    private PosScanInit posScanInit;

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

    @Override
    protected void initData() {
        posScanInit = new PosScanInit();
        posScanInit.setOnCallBack(this);
        posScanInit.init(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressBar.setVisibility(View.GONE);
        if (posScanInit != null)
            posScanInit.unsubscribe();
    }

    @Override
    public void onCallBack(boolean isOk) {
        startActivity(new Intent(InitZipActivity.this, HomeActivity.class));
        finish();
    }
}