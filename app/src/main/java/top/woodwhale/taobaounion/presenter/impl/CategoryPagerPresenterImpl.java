package top.woodwhale.taobaounion.presenter.impl;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import top.woodwhale.taobaounion.model.domain.API;
import top.woodwhale.taobaounion.model.domain.HomePagerContent;
import top.woodwhale.taobaounion.presenter.ICategoryPagerPresenter;
import top.woodwhale.taobaounion.utils.LogUtils;
import top.woodwhale.taobaounion.utils.RetrofitManager;
import top.woodwhale.taobaounion.utils.UrlUtils;
import top.woodwhale.taobaounion.view.ICategoryPagerCallback;

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {
    // 单例，根据ID判断是哪个
    private CategoryPagerPresenterImpl(){}
    private static ICategoryPagerPresenter sInstance = null;
    public static ICategoryPagerPresenter getInstance() {
        if (sInstance == null) {
            sInstance = new CategoryPagerPresenterImpl();
        }
        return sInstance;
    }
    private static final int DEFAULT_PAGE = 1;

    private final Map<Integer,Integer> pagesInfo = new HashMap<>();
    // 回调集合

    private final ArrayList<ICategoryPagerCallback> mCallbacks = new ArrayList<>();

    @Override
    public void getContentByCategoryId(int categoryId) {
        // 刚进来就加载
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryID() == categoryId) {
                callback.onLoading();
            }
        }

        // 根据分类ID加载内容
        Call<HomePagerContent> task = createCategoriesTask(categoryId);

        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(@NonNull Call<HomePagerContent> call, @NonNull Response<HomePagerContent> response) {
                LogUtils.d(CategoryPagerPresenterImpl.this,"category content code --> " + response.code());
                if (response.code() == 200) {
                    HomePagerContent homePagerContent = response.body();
                    // 把数据给到UI
                    handleHomePagerContentResult(homePagerContent,categoryId,false);
                } else {
                    handleNetworkError(categoryId,false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomePagerContent> call, @NonNull Throwable t) {
                LogUtils.d(CategoryPagerPresenterImpl.this,"onFailure --> " + t.toString());
                handleNetworkError(categoryId,false);
            }
        });
    }

    // 构建网络请求
    private Call<HomePagerContent> createCategoriesTask(int categoryId) {
        /*
         1. 拿到当前页码
         2. 页码++后存回map
         3. 加载数据
         4. 处理数据
         */
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        Integer curPage = pagesInfo.get(categoryId);
        if (curPage == null) {
            curPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId,curPage);
        }
        LogUtils.d(this,"category --> "+ categoryId +" curPage --> " + curPage);
        String url = UrlUtils.createHomePagerUrl(categoryId, curPage);
        return api.getHomePagerContent(url);
    }

    // 网络错误处理，分两种情况，一种是第一次加载，另一种是加载更多
    private void handleNetworkError(int categoryId, boolean isLoadMore) {
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryID() == categoryId) {
                if (isLoadMore) {
                    // 失败了，curPage不变
                    LogUtils.d(this,"failed curPage --> " + pagesInfo.get(categoryId));
                    callback.onLoadMoreError();
                } else {
                    callback.onNetworkError();
                }
            }
        }
    }

    // 处理首页内容和轮播图内容，判断是否是loadMore
    private void handleHomePagerContentResult(HomePagerContent homePagerContent, int categoryId, boolean isLoadMore) {
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryID() == categoryId) {
                if (homePagerContent == null || homePagerContent.getData().size() == 0) {
                    if (isLoadMore) {
                        // 内容为空，同样是page不变
                        callback.onLoadMoreEmpty();
                    } else {
                        callback.onEmpty();
                    }
                } else {
                    List<HomePagerContent.DataBean> data = homePagerContent.getData();
                    if (isLoadMore) {
                        // 如果loadMore成功，那就让当前的page+1
                        Integer curPage = pagesInfo.get(categoryId);
                        if (curPage != null) {
                            curPage = curPage + 1;
                        }
                        LogUtils.d(this,"cateID --> " + categoryId+" success curPage --> " +curPage);
                        pagesInfo.put(categoryId,curPage);
                        callback.onLoadMoreLoaded(data);
                    } else {
                        List<HomePagerContent.DataBean> looperList = data.subList(data.size() - 5, data.size());
                        callback.onContentLoaded(data);
                        callback.onLooperListLoaded(looperList);
                    }
                }
            }
        }
    }

    // 加载更多内容
    @Override
    public void loadMore(int categoryID) {
        Call<HomePagerContent> task = createCategoriesTask(categoryID);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(@NonNull Call<HomePagerContent> call, @NonNull Response<HomePagerContent> response) {
                LogUtils.d(CategoryPagerPresenterImpl.this,"load more code --> " + response.code());
                if (response.code() == 200) {
                    HomePagerContent body = response.body();
                    handleHomePagerContentResult(body,categoryID,true);
                } else {
                    handleNetworkError(categoryID,true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomePagerContent> call, @NonNull Throwable t) {
                LogUtils.d(CategoryPagerPresenterImpl.this,"onFailure --> " + t.toString());
                handleNetworkError(categoryID,true);
            }
        });

    }

    @Override
    public void reload(int categoryID) {

    }

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        mCallbacks.remove(callback);
    }
}
