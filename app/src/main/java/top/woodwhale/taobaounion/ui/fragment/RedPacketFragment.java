package top.woodwhale.taobaounion.ui.fragment;

import android.view.View;

import top.woodwhale.taobaounion.R;
import top.woodwhale.taobaounion.base.BaseFragment;

public class RedPacketFragment extends BaseFragment {
    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_red_packet;
    }

    @Override
    protected void initView(View rootView) {
        setupState(State.SUCCESS);
    }
}
