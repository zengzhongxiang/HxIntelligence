package com.hx.hxintelligence.utils;

public  interface ResultCallback<T> {

    void success(T t);
    void fail(String str);

}
