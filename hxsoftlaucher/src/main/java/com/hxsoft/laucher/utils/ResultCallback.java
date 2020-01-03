package com.hxsoft.laucher.utils;

public  interface ResultCallback<T> {

    void success(T t);
    void fail(String str);

}
