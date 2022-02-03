package top.woodwhale.taobaounion.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.woodwhale.taobaounion.R;
import top.woodwhale.taobaounion.model.domain.HomePagerContent;
import top.woodwhale.taobaounion.utils.UrlUtils;

public class HomePageContentAdapter extends RecyclerView.Adapter<HomePageContentAdapter.InnerHolder> {
    private List<HomePagerContent.DataBean> data = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        // 设置数据
        HomePagerContent.DataBean dataBean = data.get(position);
        holder.setData(dataBean);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    @SuppressLint("NonConstantResourceId")
    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_goods_cover) public ImageView mCover;
        @BindView(R.id.tv_goods_title) public TextView mTitle;
        @BindView(R.id.tv_goods_off_prise) public TextView mOffPrice;
        @BindView(R.id.tv_goods_after_off_price) public TextView mAfterOffPrice;
        @BindView(R.id.tv_goods_original_price) public TextView mOriginalPrice;
        @BindView(R.id.tv_goods_sell_count) public TextView mSellCount;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void setData(HomePagerContent.DataBean dataBean) {
            Context context = itemView.getContext();
            long offPrice = dataBean.getCouponAmount();
            double originalPrice = Double.parseDouble(dataBean.getZkFinalPrice());
            double afterOffPrice = originalPrice - offPrice;

            mTitle.setText(dataBean.getTitle());
            Glide.with(context)
                    .load(UrlUtils.getCoverPath(dataBean.getPictUrl()))
                    .thumbnail(0.2f)
                    .into(mCover);

            mOffPrice.setText("省"+offPrice+"元");

            mOriginalPrice.setText("￥"+String.format("%.2f",originalPrice));
            mOriginalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            mAfterOffPrice.setText(String.format("%.2f",afterOffPrice));
            mSellCount.setText(dataBean.getVolume()+"+购买");
        }
    }
}
