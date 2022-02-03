package top.woodwhale.taobaounion.model.domain;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface API {
    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);
}
