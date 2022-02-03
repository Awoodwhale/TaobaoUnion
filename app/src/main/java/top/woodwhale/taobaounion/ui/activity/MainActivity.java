package top.woodwhale.taobaounion.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vondear.rxtool.RxTool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import top.woodwhale.taobaounion.R;
import top.woodwhale.taobaounion.base.BaseFragment;
import top.woodwhale.taobaounion.ui.fragment.HomeFragment;
import top.woodwhale.taobaounion.ui.fragment.RedPacketFragment;
import top.woodwhale.taobaounion.ui.fragment.SearchFragment;
import top.woodwhale.taobaounion.ui.fragment.SelectedFragment;
import top.woodwhale.taobaounion.utils.LogUtils;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_navigation_bar) public BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private SelectedFragment mSelectedFragment;
    private SearchFragment mSearchFragment;
    private RedPacketFragment mRedPacketFragment;
    private FragmentManager mFragmentManager;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBind = ButterKnife.bind(this);
        RxTool.init(this);
        initFragments();
        initEvent();    // 设置监听
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
    }

    private void initFragments() {
        mHomeFragment = new HomeFragment();
        mSelectedFragment = new SelectedFragment();
        mSearchFragment = new SearchFragment();
        mRedPacketFragment = new RedPacketFragment();
        mFragmentManager = getSupportFragmentManager();
        // 默认首页
        switchFragment(mHomeFragment);
    }

    private void initEvent() {
        mNavigationView.setOnItemSelectedListener(item -> {
            LogUtils.d(this,"item Title --> " + item.getTitle() + "| item id -->" + item.getItemId());
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.home:
                    switchFragment(mHomeFragment);
                    break;
                case R.id.search:
                    switchFragment(mSearchFragment);
                    break;
                case R.id.red_packet:
                    switchFragment(mRedPacketFragment);
                    break;
                case R.id.selected:
                    switchFragment(mSelectedFragment);
                    break;
            }
            return true;
        });
    }

    private void switchFragment(BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_page_container,fragment);
        fragmentTransaction.commit();
    }

}