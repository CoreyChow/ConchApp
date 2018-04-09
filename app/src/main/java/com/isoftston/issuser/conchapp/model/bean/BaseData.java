package com.isoftston.issuser.conchapp.model.bean;

import com.corelibs.subscriber.ResponseHandler;
import com.isoftston.issuser.conchapp.model.bean.Page;

import java.io.Serializable;

/**
 * Created by issuser on 2018/4/9.
 */

public class BaseData<T> implements Serializable,ResponseHandler.IBaseData {

    public int code;
    public String msg;
    public T data;
    public Page page;

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public int status() {
        return 0;
    }

    @Override
    public Object data() {
        return null;
    }

    @Override
    public String msg() {
        return null;
    }
}
