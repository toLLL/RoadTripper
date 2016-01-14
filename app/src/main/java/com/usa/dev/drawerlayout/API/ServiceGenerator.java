package com.usa.dev.drawerlayout.API;

import com.squareup.okhttp.OkHttpClient;
import com.usa.dev.drawerlayout.R;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Author: Philippe
 * Date : 12/01/2016
 * Purpose : Basic REST adapter
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = "http://192.168.0.250:3000";

    private static OkHttpClient httpClient = new OkHttpClient();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}
