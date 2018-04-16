package com.isoftston.issuser.conchapp.views.work;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.corelibs.base.BaseFragment;
import com.corelibs.base.BasePresenter;
import com.corelibs.views.ptr.layout.PtrAutoLoadMoreLayout;
import com.corelibs.views.ptr.loadmore.widget.AutoLoadMoreListView;
import com.isoftston.issuser.conchapp.R;
import com.isoftston.issuser.conchapp.model.bean.MessageBean;
import com.isoftston.issuser.conchapp.views.work.adpter.WorkMessageItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by issuser on 2018/4/9.
 */

public class ItemFragment extends BaseFragment {

    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.lv_message)
    AutoLoadMoreListView lv_message;
    @Bind(R.id.ptrLayout)
    PtrAutoLoadMoreLayout<AutoLoadMoreListView> ptrLayout;

    public WorkMessageItemAdapter adapter;
    public List<MessageBean> listMessage;
    private String type;
    public static Fragment newInstance(String type) {
        ItemFragment fragment =new ItemFragment();
        Bundle b =new Bundle();
        b.putString("type",type);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_item;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tv.setText(type);

        adapter=new WorkMessageItemAdapter(getContext());
        listMessage=new ArrayList<>();
        for(int i=0;i<10;i++){
            listMessage.add(new MessageBean());
        }
        adapter.addAll(listMessage);
        lv_message.setAdapter(adapter);
        ptrLayout.disableLoading();
        ptrLayout.setCanRefresh(false);

        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入消息详情界面
                startActivity(ScanCodeActivity.getLauncher(getContext()));

            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

}
