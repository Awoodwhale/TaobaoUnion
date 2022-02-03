package top.woodwhale.taobaounion.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.constants.PageStyle;
import com.zhpan.bannerview.utils.BannerUtils;
import com.zhpan.indicator.enums.IndicatorSlideMode;
import com.zhpan.indicator.enums.IndicatorStyle;

import java.util.List;

import butterknife.BindView;
import top.woodwhale.taobaounion.R;
import top.woodwhale.taobaounion.base.BaseFragment;
import top.woodwhale.taobaounion.model.domain.Categories;
import top.woodwhale.taobaounion.model.domain.HomePagerContent;
import top.woodwhale.taobaounion.presenter.ICategoryPagerPresenter;
import top.woodwhale.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import top.woodwhale.taobaounion.ui.adapter.HomeLooperPagerAdapter;
import top.woodwhale.taobaounion.ui.adapter.HomePageContentAdapter;
import top.woodwhale.taobaounion.utils.Constants;
import top.woodwhale.taobaounion.utils.LogUtils;
import top.woodwhale.taobaounion.view.ICategoryPagerCallback;

// HomeFragment中的小fragment，用来显示数据
@SuppressLint("NonConstantResourceId")
public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {

    @BindView(R.id.home_pager_content_list) public RecyclerView mContentList;
    @BindView(R.id.bvp_home_pager_looper_view) public BannerViewPager<HomePagerContent.DataBean> mLooperPager;
    @BindView(R.id.tv_home_pager_title) public TextView mLooperTitleTv;
    @BindView(R.id.srl_home_pager_refresh) public RefreshLayout mRefreshLayout;

    private ICategoryPagerPresenter mCategoryPagerPresenter;
    private int mMaterialID;
    private HomePageContentAdapter mContentAdapter;
    private HomeLooperPagerAdapter mHomeLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID,category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        // 设置布局管理
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 5;
                outRect.bottom = 5;
            }
        });
        // 创建适配器
        mContentAdapter = new HomePageContentAdapter();
        // 设置适配器
        mContentList.setAdapter(mContentAdapter);

        // 设置轮播图的适配器
        mHomeLooperPagerAdapter = new HomeLooperPagerAdapter();
        mLooperPager.setIndicatorSlideMode(IndicatorSlideMode.WORM)
                .setRevealWidth(BannerUtils.dp2px(15))
                .setIndicatorStyle(IndicatorStyle.ROUND_RECT)
                .setPageStyle(PageStyle.MULTI_PAGE_OVERLAP)
                .setLifecycleRegistry(getLifecycle())
                .setAdapter(mHomeLooperPagerAdapter)
                .setIndicatorSliderColor(getResources().getColor(R.color.white)
                        ,getResources().getColor(R.color.selected_pink))
                .create();

        // 设置刷新、加载
        mRefreshLayout.setHeaderMaxDragRate(1);
        mRefreshLayout.setFooterMaxDragRate(1);
        mRefreshLayout.setHeaderTriggerRate(0.6F);
        mRefreshLayout.setFooterTriggerRate(0.8F);
        // 刷新
        mRefreshLayout.setOnRefreshListener(refreshLayout -> {
            LogUtils.d(HomePagerFragment.this,"onRefresh");
            refreshLayout.finishRefresh();
        });
        // 加载更多的监听器，去调用presenter层的loadMore方法
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            LogUtils.d(HomePagerFragment.this,"onLoadMore");
            if (mCategoryPagerPresenter != null) {
                mCategoryPagerPresenter.loadMore(mMaterialID);
            }
        });
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mLooperTitleTv.setText(arguments.getString(Constants.KEY_HOME_PAGER_TITLE));
            mMaterialID = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
            if (mCategoryPagerPresenter != null) {
                mCategoryPagerPresenter.getContentByCategoryId(mMaterialID);
            }
        }
    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = CategoryPagerPresenterImpl.getInstance();
        mCategoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void release() {
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public int getCategoryID() {
        return mMaterialID;
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        // 数据列表加载
        mContentAdapter.setData(contents);
        setupState(State.SUCCESS);
    }

    @Override
    public void onNetworkError() {
        // 网络错误
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
    public void onLoadMoreError() {
        mRefreshLayout.finishLoadMore(false);
    }

    @Override
    public void onLoadMoreEmpty() {
        mRefreshLayout.finishLoadMore(false);
    }

    @Override
    public void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents) {
        mRefreshLayout.finishLoadMore();
    }

    /**
     * 轮播图返回
     * @param contents
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        // 将得到的数据传给轮播图
        mLooperPager.refreshData(contents);
    }
}
