package top.woodwhale.taobaounion.presenter;

import top.woodwhale.taobaounion.base.IBasePresenter;
import top.woodwhale.taobaounion.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {
    /**
     * 根据分类ID获取分类页面内容
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    /**
     * 加载更多
     * @param categoryID
     */
    void loadMore(int categoryID);

    /**
     * 重新加载（刷新)
     * @param categoryID
     */
    void reload(int categoryID);

}
