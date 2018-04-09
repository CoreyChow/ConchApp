package com.isoftston.issuser.conchapp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.common.AppManager;
import com.corelibs.views.tab.InterceptedFragmentTabHost;
import com.corelibs.views.tab.TabNavigator;
import com.isoftston.issuser.conchapp.views.check.CheckFragment;
import com.isoftston.issuser.conchapp.views.message.MessageFragment;
import com.isoftston.issuser.conchapp.views.mine.MineFragment;
import com.isoftston.issuser.conchapp.views.security.SecurityFragment;
import com.isoftston.issuser.conchapp.views.work.WorkFragment;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements TabNavigator.TabNavigatorContent{

    @Bind(android.R.id.tabhost)
    InterceptedFragmentTabHost tabHost;

    private TabNavigator navigator = new TabNavigator();
    private String[] tabTags;
    private Context context=MainActivity.this;
    private int bgRecourse[] = new int[]{
//            R.drawable.tab_home,
//            R.drawable.tab_msg,
//            R.drawable.tab_order,
//            R.drawable.tab_mine
    };

    public static Intent getLauncher(Context context){
        return new Intent(context, MainActivity.class);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
            tabTags = new String[]{getString(R.string.home_message), getString(R.string.home_security),getString(R.string.home_work),
                    getString(R.string.home_check),getString(R.string.home_mine)};
            navigator.setup(this, tabHost, this, getSupportFragmentManager(), R.id.real_tab_content);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public View getTabView(int position) {
        View view = getLayoutInflater().inflate(R.layout.view_tab_content, null);

        ImageView icon = (ImageView) view.findViewById(R.id.iv_tab_icon);
        TextView text = (TextView) view.findViewById(R.id.tv_tab_text);

//        icon.setImageResource(bgRecourse[position]);
        text.setText(tabTags[position]);
        return view;
    }

    @Override
    public Bundle getArgs(int position) {
        return null;
    }

    @Override
    public Class[] getFragmentClasses() {
        return new Class[]{MessageFragment.class, SecurityFragment.class, WorkFragment.class,CheckFragment.class, MineFragment.class};
    }

    @Override
    public String[] getTabTags() {
        return tabTags;
    }

    /**
     * 物理返回键拦截
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doublePressBackToast();
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    /**
     * 双击两次返回键退出应用
     */
    private boolean isBackPressed = false;//判断是否已经点击过一次回退键

    private void doublePressBackToast() {
        if (!isBackPressed) {
            isBackPressed = true;
            showToast("再次点击返回退出程序");
        } else {
            AppManager.getAppManager().finishAllActivity();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        }, 2000);
    }
}
