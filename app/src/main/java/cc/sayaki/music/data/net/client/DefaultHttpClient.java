package cc.sayaki.music.data.net.client;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: sayaki
 * Date: 2017/6/21
 */
public class DefaultHttpClient extends CacheHttpClient{

    private String getAcceptHeader() {
        return "application/json";
    }

    @Override
    public OkHttpClient.Builder customize(OkHttpClient.Builder builder) {
        builder = super.customize(builder);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", getAcceptHeader())
                        .header("User-Agent", "Miaopass");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return super.customize(builder);
    }
}
