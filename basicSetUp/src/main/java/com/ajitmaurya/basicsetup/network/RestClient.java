package com.ajitmaurya.basicsetup.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
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


    public static <S> S createServiceSSLOnly(Class<S> serviceClass, String baseUrl, String key) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        String credentials;

        credentials = Credentials.basic(key, key);

        OkHttpClient.Builder builder2 = new OkHttpClient.Builder();
        builder2.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("key", key)
                    //.header("User-Agent", UA)
                    .header("Authorization", credentials)
                    .header("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });

        OkHttpClient okHttpClient = builder2
                .connectTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(150, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor).build();



        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());


        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
