package com.isoftston.issuser.conchapp.views.work;

import android.content.Intent;

import android.content.res.Resources;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corelibs.base.BaseFragment;
import com.corelibs.base.BasePresenter;
import com.corelibs.utils.PreferencesHelper;
import com.isoftston.issuser.conchapp.R;
import com.isoftston.issuser.conchapp.constants.Constant;
import com.isoftston.issuser.conchapp.utils.ToastUtils;
import com.isoftston.issuser.conchapp.views.seacher.SeacherActivity;
import com.isoftston.issuser.conchapp.views.work.adpter.WorkTypeAdapter;
import com.isoftston.issuser.conchapp.weight.NavBar;

import java.lang.reflect.Field;

import butterknife.Bind;

/**
 * Created by issuser on 2018/4/9.
 */

public class WorkFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.nav)
    NavBar nav;
    @Bind(R.id.iv_add)
    ImageView iv_add;
    @Bind(R.id.iv_seach)
    ImageView iv_seach;
    @Bind((R.id.moretab_viewPager))
    ViewPager viewPager;
    @Bind(R.id.tv_danger_work)
    TextView tv_danger_work;
    @Bind(R.id.tv_common_work)
    TextView tv_common_work;
    @Bind(R.id.tv_mine)
    TextView tv_mine;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_work;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        nav.setColorRes(R.color.app_blue);
        nav.setNavTitle(getString(R.string.home_work));
        nav.hideBack();
        tv_danger_work.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_work_normal));
        iv_seach.setVisibility(View.VISIBLE);
        iv_add.setVisibility(View.VISIBLE);
        iv_seach.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        tv_common_work.setOnClickListener(this);
        tv_danger_work.setOnClickListener(this);
        tv_mine.setOnClickListener(this);
        WorkTypeAdapter adapter=new WorkTypeAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_add:
                int item = viewPager.getCurrentItem();
                Intent intent=new Intent(getActivity(),NewWorkActivity.class);
                intent.putExtra("isDangerWork",item);
                startActivity(intent);
                break;
            case R.id.iv_seach:
                startActivity(SeacherActivity.getLauncher(getContext(),"1"));
                break;
            case R.id.tv_danger_work:
                iv_add.setVisibility(View.VISIBLE);
//                ToastUtils.showtoast(getActivity(),"onclick1");
                tv_danger_work.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_work_normal));
                tv_mine.setBackgroundDrawable(getResources().getDrawable(R.drawable.work_mine));
                tv_common_work.setBackgroundDrawable(getResources().getDrawable(R.drawable.work_mine));
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_common_work:
                iv_add.setVisibility(View.VISIBLE);
//                ToastUtils.showtoast(getActivity(),"onclick2");
                tv_common_work.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_work_gradient_bg));
                tv_mine.setBackgroundDrawable(getResources().getDrawable(R.drawable.work_mine));
                tv_danger_work.setBackgroundDrawable(getResources().getDrawable(R.drawable.work_mine));
                viewPager.setCurrentItem(1);
                break;
            case R.id.tv_mine:
                iv_add.setVisibility(View.GONE);
                tv_mine.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_security_mine));
                tv_danger_work.setBackgroundDrawable(getResources().getDrawable(R.drawable.work_mine));
                tv_common_work.setBackgroundDrawable(getResources().getDrawable(R.drawable.work_mine));
                viewPager.setCurrentItem(2);
//                ToastUtils.showtoast(getActivity(),"onclick3");
                break;
        }
    }
    // 具体方法（通过反射的方式）
    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }


}
