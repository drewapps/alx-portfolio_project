package com.drewapps.ai.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.drewapps.ai.Config;
import com.drewapps.ai.api.common.LiveDataCallAdapterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static ApiService getApiService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        List<Protocol> protocols = new ArrayList<>();
        protocols.add(Protocol.HTTP_1_1);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(Config.IS_DEVELOPING ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .pingInterval(1, TimeUnit.SECONDS)
                .protocols(protocols)
                .addInterceptor(interceptor)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl(Config.APP_API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(ApiService.class);
    }

}
