package cc.sayaki.music.data.net.client.core;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: sayaki
 * Date: 2016/8/17
 */
public abstract class BaseRetrofit {

    public Retrofit create() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(getEndPoint().getBaseUrl())
                .client(getHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        return builder.build();
    }

    public abstract ApiEndPoint getEndPoint();
    public abstract OkHttpClient getHttpClient();
}
