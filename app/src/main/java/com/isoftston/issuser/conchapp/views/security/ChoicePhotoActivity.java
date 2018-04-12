package com.isoftston.issuser.conchapp.views.security;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.isoftston.issuser.conchapp.R;
import com.isoftston.issuser.conchapp.weight.NavBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by issuser on 2018/4/11.
 */

public class ChoicePhotoActivity extends BaseActivity {

    @Bind(R.id.nav)
    NavBar navBar;

    private Context context=ChoicePhotoActivity.this;

    public static Intent getLauncher(Context context){
        Intent intent =new Intent(context,ChoicePhotoActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choice_photo;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        navBar.setColorRes(R.color.white);
        navBar.setTitleColor(getResources().getColor(R.color.black));
        setBarColor(getResources().getColor(R.color.transparent_black));
        navBar.setNavTitle(getString(R.string.choice_photo));
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
