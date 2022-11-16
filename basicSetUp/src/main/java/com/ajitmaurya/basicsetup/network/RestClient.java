package com.ajitmaurya.basicsetup.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestClient {

    public static <S> S createService(Class<S> serviceClass, String baseUrl, String key) {


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient(key, key, key))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());


        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }


    public static <S> S createService(Class<S> serviceClass, String baseUrl, String key, String baseAuthUserName, String baseAuthPassword) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient(key, baseAuthUserName, baseAuthPassword))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceBody(Class<S> serviceClass, String baseUrl, String key, String baseAuthUserName, String baseAuthPassword) {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(UnsafeOkHttpClient.getUnsafeOkHttpClient(key, baseAuthUserName, baseAuthPassword))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
