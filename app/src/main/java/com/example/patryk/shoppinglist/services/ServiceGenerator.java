package com.example.patryk.shoppinglist.services;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    public static final String API_BASE_URL = "https://shoppinglis.herokuapp.com/api/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit retrofit;
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit.Builder builderWithoutExpose =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()));

    public static <S> S createService(Class<S> serviceClass, boolean withoutExpose) {
        return createService(serviceClass, withoutExpose, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, boolean withoutExpose, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                createInstance(withoutExpose);
            }
        }else{
            createInstance(withoutExpose);
        }
        return retrofit.create(serviceClass);
    }

    private static void createInstance(boolean withoutExpose) {
        if (withoutExpose) {
            builderWithoutExpose.client(httpClient.build());
            retrofit = builderWithoutExpose.build();
        } else {
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
    }
}
