package com.isoftston.issuser.conchapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.corelibs.base.BaseActivity;
import com.corelibs.common.AppManager;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.views.tab.InterceptedFragmentTabHost;
import com.corelibs.views.tab.TabChangeInterceptor;
import com.corelibs.views.tab.TabNavigator;
import com.isoftston.issuser.conchapp.constants.Constant;
import com.isoftston.issuser.conchapp.model.bean.MessageBean;
import com.isoftston.issuser.conchapp.model.bean.OrgBean;
import com.isoftston.issuser.conchapp.model.bean.SecuritySearchBean;
import com.isoftston.issuser.conchapp.model.bean.YhlxBean;
import com.isoftston.issuser.conchapp.model.bean.YhlyBean;
import com.isoftston.issuser.conchapp.presenter.LoginPresenter;
import com.isoftston.issuser.conchapp.views.LoginActivity;
import com.isoftston.issuser.conchapp.views.check.CheckFragment;
import com.isoftston.issuser.conchapp.views.interfaces.LoginView;
import com.isoftston.issuser.conchapp.views.message.MessageFragment;
import com.isoftston.issuser.conchapp.views.message.utils.PushBroadcastReceiver;
import com.isoftston.issuser.conchapp.views.message.utils.PushCacheUtils;
import com.isoftston.issuser.conchapp.views.mine.MineFragment;
import com.isoftston.issuser.conchapp.views.security.SecurityFragment;
import com.isoftston.issuser.conchapp.views.work.WorkFragment;
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;
import com.isoftston.issuser.conchapp.model.bean.MsgTotalCountBean;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class MainActivity extends BaseActivity<LoginView,LoginPresenter> implements TabNavigator.TabNavigatorContent,LoginView {

    @Bind(android.R.id.tabhost)
    InterceptedFragmentTabHost tabHost;

    private TabNavigator navigator = new TabNavigator();
    private String[] tabTags;
    private Context context=MainActivity.this;
    private PushBroadcastReceiver pushBroadcastReceiver;

    private List<String> findCompanyList=new ArrayList<>();
    private List<String> checkCompanyList=new ArrayList<>();
    private List<String> fromList=new ArrayList<>();
    private List<String> fromListId=new ArrayList<>();
    public static Map<String,String> fromLYMap=new HashMap<String, String>();
    public static Map<String,String> fromLXMap=new HashMap<String, String>();

    private List<OrgBean> org=new ArrayList<>();

    private int bgRecourse[] = new int[]{
            R.drawable.tab_msg,
            R.drawable.tab_trouble,
            R.drawable.tab_work,
            R.drawable.tab_check,
            R.drawable.tab_mine
    };
    private List<View> naviList = new ArrayList<>();

    private Handler mhander = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == PushBroadcastReceiver.MESS_PUSH_CODE){
                updateNavTab();
            }
        }
    };



    public static Intent getLauncher(Context context){
        Intent intent=new Intent(context,MainActivity.class);
        return intent;
    }


    @Override
    protected int getLayoutId() {
        if(TextUtils.isEmpty(PreferencesHelper.getData(Constant.LOGIN_STATUE))){
            startActivity(LoginActivity.getLauncher(context));
        }
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
            tabTags = new String[]{getString(R.string.home_message), getString(R.string.home_security),getString(R.string.home_work),
                    getString(R.string.home_check),getString(R.string.home_mine)};
            navigator.setup(this, tabHost, this, getSupportFragmentManager(), R.id.real_tab_content);
        navigator.setTabChangeInterceptor(new TabChangeInterceptor() {
            @Override
            public boolean canTab(String tabId) {
                if(tabId.equals(tabTags[3])) {
                    setBarColor(getResources().getColor(R.color.transparent_black));
                }else{
                    setBarColor(getResources().getColor(R.color.transparent));
                }
                return true;
            }

            @Override
            public void onTabIntercepted(String tabId) {
            }
        });

        //presenter = createPresenter();
        if(presenter!=null){
            presenter.getCompanyChoiceList();
        }
        registerBroadcast();
    }


    /**
     * 更新首页底部tab未读数量角标
     * */
    public void updateNavTab(){
        for(int position =0 ; position < naviList.size() ; position++){
            TextView tv_msg_count=naviList.get(position).findViewById(R.id.tv_msg_count);
            compareCornerMark(position, tv_msg_count);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateNavTab();
        if(presenter!=null){
            presenter.getPushTag();
        }
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public View getTabView(int position) {
        View view = getLayoutInflater().inflate(R.layout.view_tab_content, null);

        ImageView icon = (ImageView) view.findViewById(R.id.iv_tab_icon);
        TextView text = (TextView) view.findViewById(R.id.tv_tab_text);
        TextView tv_msg_count=view.findViewById(R.id.tv_msg_count);
        compareCornerMark(position, tv_msg_count);
        icon.setImageResource(bgRecourse[position]);
        text.setText(tabTags[position]);
        naviList.add(view);
        return view;
    }

    private void registerBroadcast() {
        pushBroadcastReceiver = new PushBroadcastReceiver(mhander);
        IntentFilter intentFilter = new IntentFilter("getThumbService");
        registerReceiver(pushBroadcastReceiver, intentFilter);
    }

    private void compareCornerMark(int position, TextView tv_msg_count) {
        tv_msg_count.setVisibility(View.GONE);
        List<MessageBean> list = PushCacheUtils.getInstance().readPushLocalCache(this);
        switch (position) {
            case 0: //信息tab未读角标
                int allCount = PushCacheUtils.getInstance().getTypeMessageCount(list,"all");
                if (allCount > 0) {
                    tv_msg_count.setVisibility(View.VISIBLE);
                    tv_msg_count.setText(allCount + "");
                }
                break;
            case 1://安全tab未读角标
                int wzCount = PushCacheUtils.getInstance().getTypeMessageCount(list,"2");
                int yhCount = PushCacheUtils.getInstance().getTypeMessageCount(list,"1");
                if (wzCount + yhCount > 0) {
                    tv_msg_count.setVisibility(View.VISIBLE);
                    tv_msg_count.setText((yhCount + wzCount) + "");
                }
                break;
        }
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

    @Override
    public void loginSuccess(String data) {

    }

    @Override
    public void changePwdSuccess() {

    }

    @Override
    public void getCodeSuccess() {

    }

    @Override
    public void returnTag(boolean isSuccess,String tag) {
        if(isSuccess){
            addTag(tag);
        }else{
            deleteTag(tag);
        }
    }

    @Override
    public void getSafeChoiceList(SecuritySearchBean bean) {
        checkCompanyList.clear();
        findCompanyList.clear();
        org = bean.ORG;
        for (OrgBean orgBean: org){
            checkCompanyList.add(orgBean.getORG_NAME_());
            findCompanyList.add(orgBean.getORG_NAME_());
        }
        List<YhlyBean> YHLY=bean.YHLY;

        for (YhlyBean yhlyBean:YHLY){
            //fromList.add(yhlyBean.getNAME_());
            //fromListId.add(yhlyBean.getCODE_());
            fromLYMap.put(yhlyBean.getCODE_(),yhlyBean.getNAME_());
        }

        List<YhlxBean> YHLX=bean.YHLX;

        for (YhlxBean yhlyBean:YHLX){
            //fromList.add(yhlyBean.getNAME_());
            //fromListId.add(yhlyBean.getCODE_());
            fromLXMap.put(yhlyBean.getCODE_(),yhlyBean.getNAME_());
        }

    }


    private void addTag(String username) {
        //final String tag = "2c9af58150f5a3e80150f5c6d51e000b";
        final String tag = username;

        if (TextUtils.isEmpty(tag)) {
            //Toast.makeText(this, "请先输入tag", Toast.LENGTH_SHORT).show();
            return;
        }
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
        mPushAgent.getTagManager().addTags(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {

                Log.i("isSuccess", String.valueOf(isSuccess));
            /*handler.post(new Runnable() {
                @Override
                public void run() {
                    inputTag.setText("");
                    if (isSuccess) {
                        sharedPref.edit().putInt(TAG_REMAIN, result.remain).apply();
                        tagRemain.setText(String.valueOf(result.remain));
                        PushDialogFragment.newInstance(0, 1, getString(R.string.push_add_success), tag).show(
                                getFragmentManager(), "addTag");
                    } else {
                        PushDialogFragment.newInstance(0, 0, getString(R.string.push_add_fail), tag).show(
                                getFragmentManager(), "addTag");
                    }
                }
            });*/
            }
        }, tag);
    }


    private void deleteTag(String tag) {
        // final String tag = inputTag.getText().toString();
        if (TextUtils.isEmpty(tag)) {
            //Toast.makeText(this, "请先输入tag", Toast.LENGTH_SHORT).show();
            return;
        }
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();
        mPushAgent.getTagManager().deleteTags(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
             /*   handler.post(new Runnable() {
                    @Override
                    public void run() {
                        inputTag.setText("");
                        if (isSuccess) {
                            sharedPref.edit().putInt(TAG_REMAIN, result.remain).apply();
                            tagRemain.setText(String.valueOf(result.remain));
                            PushDialogFragment.newInstance(0, 1,
                                    getString(R.string.push_delete_success), tag).show(getFragmentManager(), "deleteTag");
                        } else {
                            PushDialogFragment.newInstance(0, 0,
                                    getString(R.string.push_delete_fail), tag).show(getFragmentManager(), "deleteTag");
                        }
                    }
                });*/
            }
        }, tag);
    }

    @Override
    protected void onDestroy() {
        if(pushBroadcastReceiver != null){
            unregisterReceiver(pushBroadcastReceiver);
        }
        super.onDestroy();
    }
}
