package com.ajitmaurya.basicsetup.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class EncryptionInterceptor implements Interceptor {

    private static final String TAG = EncryptionInterceptor.class.getSimpleName();
    private static final boolean DEBUG = true;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody oldBody = request.body();
        Buffer buffer = new Buffer();
        oldBody.writeTo(buffer);
        String strOldBody = buffer.readUtf8();
        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
        String strNewBody = encrypt(strOldBody);
        RequestBody body = RequestBody.create(mediaType, strNewBody);
        request = request.newBuilder().header("Content-Type", body.contentType().toString()).header("Content-Length", String.valueOf(body.contentLength())).method(request.method(), body).build();
        return chain.proceed(request);
    }

    private static String encrypt(String text) {
        //your code

        return "encrypted data";
    }
}