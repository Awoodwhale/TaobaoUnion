package top.woodwhale.taobaounion.presenter;

import top.woodwhale.taobaounion.base.IBasePresenter;
import top.woodwhale.taobaounion.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    /**
     * 获取商品分类
     */
    void getCategories();
}
