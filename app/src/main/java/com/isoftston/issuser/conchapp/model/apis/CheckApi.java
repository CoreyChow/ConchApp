package com.isoftston.issuser.conchapp.model.apis;

import com.isoftston.issuser.conchapp.constants.Urls;
import com.isoftston.issuser.conchapp.model.bean.BaseData;
import com.isoftston.issuser.conchapp.model.bean.DeviceBean;
import com.isoftston.issuser.conchapp.model.bean.DeviceListBean;

import java.util.List;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by issuser on 2018/4/16.
 */

public interface CheckApi {

    @POST(Urls.GET_DEVICES)
    Observable<BaseData<DeviceListBean>> getDevices(@Query("userId") String userID,@Query("pageNo") String pageNo);

    @POST(Urls.CHECK_DEVICE)
    Observable<BaseData<DeviceBean>> checkDevices(@Query("equipId") String equipId,
                                                  @Query("userId") String userId,
                                                  @Query("createTime") String createTime,
                                                  @Query("location") String location);
}