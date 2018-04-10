package com.isoftston.issuser.conchapp.views.message;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.corelibs.base.BaseFragment;
import com.corelibs.base.BasePresenter;
import com.isoftston.issuser.conchapp.R;
import com.isoftston.issuser.conchapp.views.message.adpter.MessageListviewAdapter;
import com.isoftston.issuser.conchapp.views.message.adpter.VpAdapter;
import com.isoftston.issuser.conchapp.weight.NavBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by issuser on 2018/4/9.
 */

public class MessageFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.nav)
    NavBar nav;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.ll_main)
    LinearLayout ll_main;
    @Bind(R.id.yh_detail)
    LinearLayout ll_yh_detail;
    @Bind(R.id.wz_detail)
    LinearLayout ll_wz_detail;
    @Bind(R.id.aq_detail)
    LinearLayout ll_aq_detail;
    @Bind(R.id.bt_yh)
    Button bt_yh;
    @Bind(R.id.bt_aq)
    Button bt_aq;
    @Bind(R.id.bt_wz)
    Button bt_wz;
    @Bind(R.id.listview)
    ListView listView;
    private List<View> list=null;
    private List<String> datas=new ArrayList<>();
    public Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            int currentItem = viewPager.getCurrentItem();
            //切换至下一个页面
            viewPager.setCurrentItem(++currentItem);
            //再次调用
//            handler.sendEmptyMessageDelayed(1, 2000);
        };
    };
    @Override
    protected int getLayoutId() {
        initDate();
        return R.layout.fragment_message;

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        nav.setColorRes(R.color.colorPrimary);
        nav.setNavTitle(getString(R.string.home_message));
        nav.hideBack();
        ll_main.setOnClickListener(this) ;
        VpAdapter adapter = new VpAdapter(list,handler);
        addData();
        MessageListviewAdapter messageListviewAdapter=new MessageListviewAdapter(getActivity(),datas);
        listView.setAdapter(messageListviewAdapter);
        listView.setCacheColorHint(Color.TRANSPARENT);
        bt_aq.setOnClickListener(this);
        bt_wz.setOnClickListener(this);
        bt_yh.setOnClickListener(this);
        viewPager.setPageMargin(100);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getActivity(),"当前是第"+position+"个条目",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(getActivity(),ItemDtailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addData() {
        for (int i=0;i<10;i++){
            datas.add("我这是假数据假数据假数据"+i+"itme");
        }
    }

    private void initDate() {
        list=new ArrayList<>();
        for (int i=0;i<3;i++){
            View view=LayoutInflater.from(getActivity()).inflate(
                        R.layout.viewpager_yh_item, null);
            list.add(view);
        }

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_yh:
            case R.id.bt_wz:
            case R.id.bt_aq:
                bt_yh.setVisibility(View.GONE);
                bt_wz.setVisibility(View.GONE);
                bt_aq.setVisibility(View.GONE);
                ll_aq_detail.setVisibility(View.VISIBLE);
                ll_wz_detail.setVisibility(View.VISIBLE);
                ll_yh_detail.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_main:
                viewPager.setVisibility(View.VISIBLE);
                ll_main.setVisibility(View.GONE);
                break;
        }
    }
}
