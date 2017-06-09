package cc.sayaki.music.data.source;

import android.content.Context;

import java.util.Date;
import java.util.List;

import cc.sayaki.music.data.model.Folder;
import cc.sayaki.music.data.model.PlayList;
import cc.sayaki.music.data.model.Song;
import rx.Observable;
import rx.Subscriber;

/**
 * Author: sayaki
 * Date: 2017/6/9
 */
public class MusicLocalDataSource implements MusicContract {

    private static final String TAG = "MusicLocalDataSource";

    private Context context;

    public MusicLocalDataSource(Context context) {
        this.context = context;
    }

    @Override
    public Observable<List<PlayList>> playLists() {
        return Observable.create(new Observable.OnSubscribe<List<PlayList>>() {
            @Override
            public void call(Subscriber<? super List<PlayList>> subscriber) {
            }
        });
    }

    @Override
    public List<PlayList> cachedPlayLists() {
        return null;
    }

    @Override
    public Observable<PlayList> create(final PlayList playList) {
        return Observable.create(new Observable.OnSubscribe<PlayList>() {
            @Override
            public void call(Subscriber<? super PlayList> subscriber) {
                Date now = new Date();
                playList.setCreatedAt(now);
                playList.setUpdatedAt(now);

                subscriber.onNext(playList);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<PlayList> update(PlayList playList) {
        return Observable.create(new Observable.OnSubscribe<PlayList>() {
            @Override
            public void call(Subscriber<? super PlayList> subscriber) {

            }
        });
    }

    @Override
    public Observable<PlayList> delete(PlayList playList) {
        return Observable.create(new Observable.OnSubscribe<PlayList>() {
            @Override
            public void call(Subscriber<? super PlayList> subscriber) {

            }
        });
    }

    @Override
    public Observable<List<Folder>> folders() {
        return Observable.create(new Observable.OnSubscribe<List<Folder>>() {
            @Override
            public void call(Subscriber<? super List<Folder>> subscriber) {

            }
        });
    }

    @Override
    public Observable<Folder> create(Folder folder) {
        return Observable.create(new Observable.OnSubscribe<Folder>() {
            @Override
            public void call(Subscriber<? super Folder> subscriber) {

            }
        });
    }

    @Override
    public Observable<List<Folder>> create(List<Folder> folders) {
        return Observable.create(new Observable.OnSubscribe<List<Folder>>() {
            @Override
            public void call(Subscriber<? super List<Folder>> subscriber) {

            }
        });
    }

    @Override
    public Observable<Folder> update(Folder folder) {
        return Observable.create(new Observable.OnSubscribe<Folder>() {
            @Override
            public void call(Subscriber<? super Folder> subscriber) {

            }
        });
    }

    @Override
    public Observable<Folder> delete(Folder folder) {
        return Observable.create(new Observable.OnSubscribe<Folder>() {
            @Override
            public void call(Subscriber<? super Folder> subscriber) {

            }
        });
    }

    @Override
    public Observable<List<Song>> insert(List<Song> songs) {
        return Observable.create(new Observable.OnSubscribe<List<Song>>() {
            @Override
            public void call(Subscriber<? super List<Song>> subscriber) {

            }
        });
    }

    @Override
    public Observable<Song> update(Song song) {
        return Observable.create(new Observable.OnSubscribe<Song>() {
            @Override
            public void call(Subscriber<? super Song> subscriber) {

            }
        });
    }

    @Override
    public Observable<Song> setSongAsFavorite(Song song, boolean favorite) {
        return Observable.create(new Observable.OnSubscribe<Song>() {
            @Override
            public void call(Subscriber<? super Song> subscriber) {

            }
        });
    }
}
