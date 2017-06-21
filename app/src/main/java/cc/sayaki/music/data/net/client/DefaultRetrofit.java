package cc.sayaki.music.data.net.client;

import cc.sayaki.music.data.net.client.core.ApiEndPoint;
import cc.sayaki.music.data.net.client.core.BaseRetrofit;
import okhttp3.OkHttpClient;

/**
 * Author: sayaki
 * Date: 2017/6/21
 */
public class DefaultRetrofit extends BaseRetrofit {

    private static final String END_POINT = "http://115.28.247.58/";

    private DefaultHttpClient httpClient;

    public DefaultRetrofit() {
        this.httpClient = new DefaultHttpClient();
    }

    @Override
    public ApiEndPoint getEndPoint() {
        return new ApiEndPoint() {
            @Override
            public String getBaseUrl() {
                return END_POINT;
            }
        };
    }

    @Override
    public OkHttpClient getHttpClient() {
        return httpClient.create();
    }
}
