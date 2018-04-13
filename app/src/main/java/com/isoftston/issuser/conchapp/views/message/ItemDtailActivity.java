package com.isoftston.issuser.conchapp.views.message;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.views.ptr.loadmore.adapter.GridViewAdapter;
import com.corelibs.views.roundedimageview.RoundedTransformationBuilder;
import com.isoftston.issuser.conchapp.R;
import com.isoftston.issuser.conchapp.adapters.mGridViewAdapter;
import com.isoftston.issuser.conchapp.views.message.adpter.VpAdapter;
import com.isoftston.issuser.conchapp.weight.CircleImageView;
import com.isoftston.issuser.conchapp.weight.NavBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by issuser on 2018/4/10.
 */

public class ItemDtailActivity extends BaseActivity {
    @Bind(R.id.nav)
    NavBar nav;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.vp)
    ViewPager vp;
    @Bind(R.id.ll)
    LinearLayout ll;
    @Bind(R.id.mGridView)
    GridView mGridView;
    private List<View> imageList;
    private ArrayList<View> dotsList;
    private int[] images = {R.drawable.aaa,R.drawable.bbb,R.drawable.ccc,R.drawable.ddd};
    private mGridViewAdapter gridViewAdapter;
    private List<Map<String, Object>> data_list;
    public Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            int currentItem = vp.getCurrentItem();
            //切换至下一个页面
            vp.setCurrentItem(++currentItem);
            //再次调用
//            handler.sendEmptyMessageDelayed(1, 2000);
        };
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_itemdetail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        nav.setNavTitle(getString(R.string.yh_project_check));
        tv_title.setTextColor(getResources().getColor(R.color.text_color));
        //初始化数据
        initImages();
        //初始化小圆点
        initDots();
        //适配器

        VpAdapter adapter = new VpAdapter(imageList,handler);
        vp.setPageMargin(10);
        vp.setAdapter(adapter);
        //初始化vp的位置
        vp.setCurrentItem(1);
        data_list = initList();
        gridViewAdapter = new mGridViewAdapter(this, data_list);
        mGridView.setAdapter(gridViewAdapter);
        //页面改变监听
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //遍历小圆点集合
                for(int i=0;i<dotsList.size();i++){
                    if(position%dotsList.size() == i){//设置当前小圆点
                        dotsList.get(i).setBackgroundResource(R.drawable.dots_focus);
                    }else{//设置其他小圆点
                        dotsList.get(i).setBackgroundResource(R.drawable.dots_normal);
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    public List<Map<String, Object>> initList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", images[i]);
            list.add(map);
        }
        return list;
    }
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private void initDots() {
        //实例化一个集合存放小圆点
        dotsList = new ArrayList<View>();
        for(int i=0;i<images.length;i++){
            //把第一个小圆点设置为选中状态
            View v = new View(this);
            if(i == 0){
                v.setBackgroundResource(R.drawable.dots_focus);
            }else{
                v.setBackgroundResource(R.drawable.dots_normal);
            }
            //指定其大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            if (i != 0){
                params.leftMargin = 20;
            }
            v.setLayoutParams(params);
            ll.addView(v);
            dotsList.add(v);
        }
    }
    private void initImages() {
        //实例化一个集合，用于存放图片
        imageList = new ArrayList<View>();
        for (int i = 0; i < images.length; i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.viewpager_item, null);
//            TextView title = (TextView) view.findViewById(R.id.view_title);
//            title.setText("头像");
            ImageView iv = view.findViewById(R.id.view_image);
//            Glide.with(this).load("http://pic29.photophoto.cn/20131204/0034034499213463_b.jpg")
//                    .centerCrop()
//                    .override(320,160)
//                    .transform(new CenterCrop(this), new RoundedTransformationBuilder().cornerRadius(20).build(this))
//                    .into(iv);
            iv.setImageResource(images[i]);
            imageList.add(view);
        }
    }
}
