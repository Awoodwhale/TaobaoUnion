package top.woodwhale.taobaounion.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import top.woodwhale.taobaounion.model.domain.Categories;
import top.woodwhale.taobaounion.ui.fragment.HomePagerFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<Categories.DataBean> mCategoryList = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mCategoryList.get(position).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return HomePagerFragment.newInstance(mCategoryList.get(position));
    }

    @Override
    public int getCount() {
        return mCategoryList.size();
    }

    public void setCategories(Categories categories) {
        mCategoryList.clear();
        mCategoryList.addAll(categories.getData());
        notifyDataSetChanged();
    }
}
