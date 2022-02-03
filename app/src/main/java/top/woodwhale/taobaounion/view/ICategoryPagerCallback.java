package top.woodwhale.taobaounion.view;

import java.util.List;

import top.woodwhale.taobaounion.base.IBaseCallback;
import top.woodwhale.taobaounion.model.domain.HomePagerContent;

public interface ICategoryPagerCallback extends IBaseCallback {
    /**
     * 获取当前这个callback的id
     * @return
     */
    int getCategoryID();

    /**
     * 数据加载回来
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataBean> contents);


    /**
     * 加载更多 出现错误
     */
    void onLoadMoreError();

    /**
     * 加载更多时为空
     */
    void onLoadMoreEmpty();

    /**
     * 加载更多
     * @param contents
     */
    void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 加载轮播图
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);
}
