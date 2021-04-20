package com.qc.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonUtils {

    public static <T> T parser(HttpServletRequest request, Class<T> classOfT) throws IOException {
        Gson gson = new GsonBuilder().create();
        InputStreamReader reader = new InputStreamReader(request.getInputStream());
        T ret = gson.fromJson(reader, classOfT);
        return ret;
    }

    public static <T> T parser(String msg, Class<T> classOfT) {
        Gson gson = new GsonBuilder().create();
        T ret = gson.fromJson(msg, classOfT);
        return ret;
    }

    public static <T> String toJson(T obj) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(obj);
    }

    public static <T> String toJson(JsonObject obj) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(obj);
    }
}
