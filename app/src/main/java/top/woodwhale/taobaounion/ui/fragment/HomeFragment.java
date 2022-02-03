package top.woodwhale.taobaounion.ui.fragment;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import top.woodwhale.taobaounion.R;
import top.woodwhale.taobaounion.base.BaseFragment;
import top.woodwhale.taobaounion.model.domain.Categories;
import top.woodwhale.taobaounion.presenter.IHomePresenter;
import top.woodwhale.taobaounion.presenter.impl.HomePresenterImpl;
import top.woodwhale.taobaounion.ui.adapter.HomePagerAdapter;
import top.woodwhale.taobaounion.view.IHomeCallback;

@SuppressLint("NonConstantResourceId")
// 最外层的大框架
public class HomeFragment extends BaseFragment implements IHomeCallback {

    @BindView(R.id.home_indicator) public TabLayout mTabLayout;
    @BindView(R.id.home_pager) public ViewPager mHomePager;

    private IHomePresenter mHomePresenter;
    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initPresenter() {
        // 创建Presenter
        mHomePresenter = new HomePresenterImpl();
        mHomePresenter.registerViewCallback(this);
    }

    @Override
    protected void initView(View rootView) {
        // tab设置绑定homePager
        mTabLayout.setupWithViewPager(mHomePager);
        // 给ViewPager创建adapter
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        mHomePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected void loadData() {
        if (mHomePresenter != null) {
            mHomePresenter.getCategories();
        }
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout, container, false);
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        // 加载的数据会从这里回来
        if (mHomePagerAdapter != null) {
            setupState(State.SUCCESS);
            mHomePagerAdapter.setCategories(categories);
        }

    }

    @Override
    public void onNetworkError() {
        setupState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setupState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setupState(State.EMPTY);
    }

    @Override
    protected void release() {
        if (mHomePresenter != null) {
            // 取消注册
            mHomePresenter.unregisterViewCallback(this);
        }
    }

    /**
     * 网络错误重新加载的方法
     */
    @Override
    protected void onNetworkRetryClick() {
        loadData();
    }
}
