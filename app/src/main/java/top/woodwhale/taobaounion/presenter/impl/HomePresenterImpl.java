package top.woodwhale.taobaounion.presenter.impl;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import top.woodwhale.taobaounion.model.domain.API;
import top.woodwhale.taobaounion.model.domain.Categories;
import top.woodwhale.taobaounion.presenter.IHomePresenter;
import top.woodwhale.taobaounion.utils.LogUtils;
import top.woodwhale.taobaounion.utils.RetrofitManager;
import top.woodwhale.taobaounion.view.IHomeCallback;

public class HomePresenterImpl implements IHomePresenter {
    private IHomeCallback mCallback = null;

    @Override
    public void getCategories() {
        // 刚开始，得加载界面
        if (mCallback != null) {
            mCallback.onLoading();
        }
        // 加载分类数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(@NonNull Call<Categories> call, @NonNull Response<Categories> response) {
                LogUtils.d(HomePresenterImpl.this,"code --> " + response.code());
                if (response.code() == 200 && mCallback != null) {
                    // success
                    Categories categories = response.body();
                    if (categories == null || categories.getData().size() == 0) {
                        mCallback.onEmpty();
                    } else {
                        mCallback.onCategoriesLoaded(categories);
                    }
                } else {
                    if (mCallback != null) {
                        mCallback.onNetworkError();
                    }
                    LogUtils.e(HomePresenterImpl.this,"请求失败！");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Categories> call, @NonNull Throwable t) {
                if (mCallback != null) {
                    mCallback.onNetworkError();
                }
                LogUtils.e(HomePresenterImpl.this,"请求错误！" + t.toString());
                // TODO:处理数据
            }
        });
    }

    @Override
    public void registerViewCallback(IHomeCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IHomeCallback callback) {
        mCallback = null;
    }
}
