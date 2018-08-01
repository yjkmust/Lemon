package com.yjkmust.fourleafclover.util;

import android.arch.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yj on 2018/8/1.
 */

public final class LiveDataBus {
    private final Map<String, MutableLiveData<Object>> bus;

    public LiveDataBus() {
        bus = new HashMap<>();
    }
    private static class SingletonHolder{
        private static final LiveDataBus DATA_BUS = new LiveDataBus();
    }
    public static LiveDataBus getDefault(){
        return SingletonHolder.DATA_BUS;
    }
    public <T> MutableLiveData<T> getMessage(String target,Class<T> type){
        if (!bus.containsKey(target)){
            bus.put(target, new MutableLiveData<>());
        }
        return (MutableLiveData<T>) bus.get(target);
    }
    public MutableLiveData<Object> post(String target){
        return getMessage(target, Object.class);
    }
}
