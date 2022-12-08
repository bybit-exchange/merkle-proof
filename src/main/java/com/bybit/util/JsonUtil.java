package com.bybit.util;

import com.google.gson.Gson;

public class JsonUtil {

    static Gson gson = new Gson();

    public static <T> T parseObject(String json, Class<T> tClass){
        return gson.fromJson(json, tClass);
    }
}
