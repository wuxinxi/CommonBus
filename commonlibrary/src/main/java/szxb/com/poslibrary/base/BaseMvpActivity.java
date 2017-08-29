package szxb.com.poslibrary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 作者：Tangren on 2017/6/9 13:15
 * 邮箱：wu_tangren@163.com
 * TODO:一句话描述
 */
public abstract class BaseMvpActivity<T extends BasePresenter> extends BaseActivity implements BaseView {

    protected T mPresenter;

    protected T getChildPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (null != getChildPresenter()) {
            mPresenter = getChildPresenter();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSuccess(Object str) {

    }

    @Override
    public void onFail(Object str) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter = null;
        }
    }
}
