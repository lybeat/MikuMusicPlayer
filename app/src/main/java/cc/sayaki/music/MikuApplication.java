package cc.sayaki.music;

import android.app.Application;

/**
 * Author: sayaki
 * Date: 2017/6/9
 */
public class MikuApplication extends Application {

    private static MikuApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    public static MikuApplication getInstance() {
        return sInstance;
    }
}
