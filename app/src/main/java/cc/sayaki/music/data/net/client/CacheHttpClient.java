package cc.sayaki.music.data.net.client;

import java.io.File;
import java.io.IOException;

import cc.sayaki.music.MikuApplication;
import cc.sayaki.music.data.net.client.core.BaseOkHttpClient;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: sayaki
 * Date: 2017/6/21
 */
public class CacheHttpClient extends BaseOkHttpClient {

    private static final long CACHE_SIZE = 1024 * 1024 * 10; // 10M

    @Override
    public OkHttpClient.Builder customize(OkHttpClient.Builder builder) {
        File cacheFile = new File(MikuApplication.getInstance().getCacheDir(), "mikuMusic");
        Cache cache = new Cache(cacheFile, CACHE_SIZE);
        builder.cache(cache);
        builder.addNetworkInterceptor(cacheControlInterceptor);

        return builder;
    }

    private final Interceptor cacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response originalResponse = chain.proceed(request);

            return originalResponse.newBuilder()
                    .build();
        }
    };
}
