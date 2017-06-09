package cc.sayaki.music;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;

/**
 * Author: sayaki
 * Date: 2017/6/8
 */
public class RxBus {

    private static final String TAG = "RxBus";

    private static volatile RxBus sInstance;

    private static final class HolderClass {
        private static final RxBus INSTANCE = new RxBus();
    }

    public static RxBus getInstance() {
        return HolderClass.INSTANCE;
    }

    private PublishSubject<Object> eventBus = PublishSubject.create();

    public void post(Object event) {
        eventBus.onNext(event);
    }

    public Observable<Object> toObservable() {
        return eventBus;
    }

    public static Subscriber<Object> defaultSubscriber() {
        return new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "RxBus onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "RxBus onError");
            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "RxBus onNext");
            }
        };
    }
}
