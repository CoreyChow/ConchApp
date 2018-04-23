package com.isoftston.issuser.conchapp.presenter;

import android.util.Log;

import com.corelibs.api.ApiFactory;
import com.corelibs.api.ResponseTransformer;
import com.corelibs.base.BasePresenter;
import com.corelibs.subscriber.ResponseSubscriber;
import com.isoftston.issuser.conchapp.model.apis.SecurityApi;
import com.isoftston.issuser.conchapp.model.bean.AddYHBean;
import com.isoftston.issuser.conchapp.model.bean.AddWZMessageRequestBean;
import com.isoftston.issuser.conchapp.model.bean.BaseData;
import com.isoftston.issuser.conchapp.model.bean.SafeListBean;
import com.isoftston.issuser.conchapp.model.bean.SafeRequestBean;
import com.isoftston.issuser.conchapp.utils.SharePrefsUtils;
import com.isoftston.issuser.conchapp.views.interfaces.SecuryView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by issuser on 2018/4/20.
 */

public class SecurityPresenter extends BasePresenter<SecuryView> {
    private SecurityApi api;
    @Override
    public void onStart() {

    }

    @Override
    protected void onViewAttach() {
        super.onViewAttach();
        api= ApiFactory.getFactory().create(SecurityApi.class);
    }
    public void addWZMessage(AddYHBean bean){
        String token= SharePrefsUtils.getValue(getContext(),"token",null);
        Log.i("token",token);
        String token1=token.replaceAll("\"","");
        api.addWZMessage(token1,bean)
                .compose(new ResponseTransformer<>(this.<BaseData>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData>() {

                    @Override
                    public void success(BaseData messageDetailBeanBaseData) {
                        view.addSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.addFailed();
                    }
                });
    }
    public void addYHMessage(AddYHBean bean){
        String token= SharePrefsUtils.getValue(getContext(),"token",null);
        Log.i("token",token);
        String token1=token.replaceAll("\"","");
        api.addYHMessage(token1,bean)
                .compose(new ResponseTransformer<>(this.<BaseData>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData>() {

                    @Override
                    public void success(BaseData messageDetailBeanBaseData) {
                        view.addSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.addFailed();
                    }
                });
    }
    public void getSafeMessageList(String type,String item,String lastId){
        SafeRequestBean bean=new SafeRequestBean();
        bean.item=item;
        bean.type=type;
        bean.lastId=lastId;
        String token= SharePrefsUtils.getValue(getContext(),"token",null);
        Log.i("token",token);
        String token1=token.replaceAll("\"","");
        api.getSefeMessageList(token1,bean)
                .compose(new ResponseTransformer<>(this.<BaseData<SafeListBean>>bindToLifeCycle()))
                .subscribe(new ResponseSubscriber<BaseData<SafeListBean>>() {

                    @Override
                    public void success(BaseData<SafeListBean> messageBeanBaseData) {
                        view.getSafeListSuccess(messageBeanBaseData.data);

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }
    public void uploadImg(List<File> listFiles){
        for (int i=0;i<listFiles.size();i++){
            File file=listFiles.get(i);
            String token= SharePrefsUtils.getValue(getContext(),"token",null);
            Log.i("token",token);
            String token1=token.replaceAll("\"","");
            api.uploadImage(token1,file)
                    .compose(new ResponseTransformer<>(this.<BaseData>bindToLifeCycle()))
                    .subscribe(new ResponseSubscriber<BaseData>() {

                        @Override
                        public void success(BaseData messageBeanBaseData) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }
                    });
        }
    }
}
