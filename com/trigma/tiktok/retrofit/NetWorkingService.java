package com.trigma.tiktok.retrofit;

import android.app.Application;
import com.trigma.tiktok.C1020R;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetWorkingService {
    public static final String GEO_BASE_URL = "https://maps.googleapis.com/";
    private ApiService networkAPI;
    private ApiService networkAPI_2;
    private ApiService networkAPI_3;

    public NetWorkingService(Application app) {
        Builder client = new Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(Level.BODY);
        client.addInterceptor(loggingInterceptor);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(app.getResources().getString(C1020R.string.base_url)).client(client.build()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        Retrofit retrofit2 = new Retrofit.Builder().baseUrl(GEO_BASE_URL).client(client.build()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        this.networkAPI = (ApiService) retrofit.create(ApiService.class);
        this.networkAPI_2 = (ApiService) retrofit2.create(ApiService.class);
    }

    public NetWorkingService(String url) {
        Builder client = new Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(Level.BODY);
        client.addInterceptor(loggingInterceptor);
        this.networkAPI_3 = (ApiService) new Retrofit.Builder().baseUrl(url).client(client.build()).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build().create(ApiService.class);
    }

    public ApiService getAPI() {
        return this.networkAPI;
    }

    public ApiService getAPI2() {
        return this.networkAPI_2;
    }

    public ApiService getAPI3() {
        return this.networkAPI_3;
    }
}
