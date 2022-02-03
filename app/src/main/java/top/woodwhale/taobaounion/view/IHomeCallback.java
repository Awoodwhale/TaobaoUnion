package top.woodwhale.taobaounion.view;

import top.woodwhale.taobaounion.base.IBaseCallback;
import top.woodwhale.taobaounion.model.domain.Categories;

public interface IHomeCallback extends IBaseCallback {
    /**
     * 加载主页的分类
     * @param categories
     */
    void onCategoriesLoaded(Categories categories);
}
