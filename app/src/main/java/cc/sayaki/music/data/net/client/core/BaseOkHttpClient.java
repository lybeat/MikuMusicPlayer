package cc.sayaki.music.data.net.client.core;

import java.util.concurrent.TimeUnit;

import cc.sayaki.music.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Author: lybeat
 * Date: 2016/8/17
 */
public abstract class BaseOkHttpClient {

    private static final int TIMEOUT_CONNECT = 30; // 30s
    private static final int TIMEOUT_READ = 10;
    private static final int TIMEOUT_WRITE = 10;

    public OkHttpClient create() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ?
                        HttpLoggingInterceptor.Level.BODY :
                        HttpLoggingInterceptor.Level.NONE));
        builder = customize(builder);

        return builder.build();
    }

    public abstract OkHttpClient.Builder customize(OkHttpClient.Builder builder);
}
