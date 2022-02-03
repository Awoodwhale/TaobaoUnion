package top.woodwhale.taobaounion.ui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vondear.rxtool.RxTool;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

import top.woodwhale.taobaounion.R;
import top.woodwhale.taobaounion.model.domain.HomePagerContent;
import top.woodwhale.taobaounion.utils.UrlUtils;

public class HomeLooperPagerAdapter extends BaseBannerAdapter<HomePagerContent.DataBean> {

    @Override
    protected void bindData(BaseViewHolder<HomePagerContent.DataBean> holder, HomePagerContent.DataBean data, int position, int pageSize) {
        Glide.with(RxTool.getContext())
                .load(UrlUtils.getCoverPath(data.getPictUrl()))
                .thumbnail(0.5f)
                .into((ImageView) holder.findViewById(R.id.iv_home_pager_looper_image));
    }


    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_home_looper_content;
    }
}
